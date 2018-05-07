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

    /**
      *
      * @param expirationTimeout - таймаут, после которого view становится пустым, в миллисекундах
      * @return - view
      */
    def lazySchedule(expirationTimeout: Long): SeqView[A, Seq[_]]  = {
      val i = c.instant().plusMillis(expirationTimeout)
      class ViewWithExpiration(val e: Instant, val s: Seq[A]) extends SeqView[A, Seq[_]] {

        val v: SeqView[A, Seq[_]] = s.view
        def iterator: Iterator[A] = if (c.instant().isBefore(i)) v.iterator else Iterator.empty

        override def underlying: Seq[A] = if (c.instant().isBefore(i)) s else Seq.empty
        def apply(n: Int) = if( c.instant().isBefore(i)) v.apply(n) else throw new Exception("View is expired.")

        override def length: Int = if( c.instant().isBefore(i)) s.length else 0
      }

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
