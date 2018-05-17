package lectures.eval

import java.time.{Clock, Instant}

import scala.collection.SeqView

/**
  * В этом задании ваша задча реализовать своеобразный View с таймером.
  *
  * Он должен представлять из себя стандартный SeqView c ограничением по времени.
  * Т.е. этот view ведет себя как обычно, пока не истечет таймаут, предеданный при создании.
  * Как только таймаут истекает, view должен начать вести себя так, как будто коллекция пуста.
  *
  * Для решения задачи подставьте на место вопросительных знаков реализацию view.
  * Раскомментируйте и выполните тесты в lectures.eval.LazySchedulerTest
  */
object LazySchedulerView {

  implicit class SeqViewConverter[A](f: Seq[A]) {
    val c = Clock.systemDefaultZone()

    class ViewWithExpiration(val e: Instant, val s: Seq[A])
      extends SeqView[A, Seq[_]] {
      def isValuable: Boolean = c.instant().isBefore(e)
      val v: SeqView[A, Seq[_]] = s.view
      override def iterator: Iterator[A] =
        if (isValuable) v.iterator else Iterator.empty

      override def underlying: Seq[A] =
        if (isValuable) s else Seq.empty
      override def apply(n: Int) =
        if (isValuable) v.apply(n)
        else v.apply(-1) // Обращение по несуществующему индексу, т.к. теперь коллекция пуста

      override def length: Int = if (isValuable) s.length else 0
    }
    /**
      *
      * @param expirationTimeout - таймаут, после которого view становится пустым, в миллисекундах
      * @return - view
      */
    def lazySchedule(expirationTimeout: Long): SeqView[A, Seq[_]] = {
      val i = c.instant().plusMillis(expirationTimeout)
      new ViewWithExpiration(i, f)
    }
  }
}

object LazySchedulerViewExample extends App {

  import LazySchedulerView._

  val v = List(1, 2, 3, 56)
  val d = v.lazySchedule(1300)

  println(d.length)
  Thread.sleep(1500)
  println(d.length)
}
