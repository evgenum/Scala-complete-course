package lectures.oop.types

import lectures.matching.SortingStuff.{Stuff, Watches}

import scala.util.Random

/**
  * Модифицируйте реализацию BSTImpl из предыдущего задания.
  * Используя тайп параметры и паттерн Type Class, реализуйте GeneralBSTImpl таким образом,
  * чтобы дерево могло работать с произвольным типом данных.
  *
  * Наследников GeneralBSTImpl определять нельзя.
  *
  * Создайте генератор для деревьев 3-х типов данных:
  * * * * float
  * * * * String
  * * * * Watches из задачи SortStuff. Большими считаются часы с большей стоимостью
  */
trait GeneralBST[T] {
  val value: T
  val left: Option[GeneralBST[T]]
  val right: Option[GeneralBST[T]]

  def add(newValue: T): GeneralBST[T]

  def find(value: T): Option[GeneralBST[T]]
}

case class GeneralBSTImpl[T: Ordering](value: T,
                                       left: Option[GeneralBSTImpl[T]] = None,
                                       right: Option[GeneralBSTImpl[T]] = None)
    extends GeneralBST[T] {

  override def find(value: T): Option[GeneralBSTImpl[T]] =
    if (value == this.value) Some(this)
    else if (value < this.value) left.flatMap(_.find(value))
    else right.flatMap(_.find(value))

  override def add(newValue: T): GeneralBSTImpl[T] =
    if (newValue == this.value) this
    else if (newValue < this.value)
      this.copy(
        left = this.left.map(_.add(newValue)) orElse Some(
          GeneralBSTImpl[T](newValue)))
    else
      this.copy(
        right = this.right.map(_.add(newValue)) orElse Some(
          GeneralBSTImpl[T](newValue)))
}

object Application extends App {
  def createIntBST(n: Int): GeneralBST[Int] = {
    val r = new Random()
    var tree = GeneralBSTImpl[Int](r.nextInt())
    for (_ <- 1 to n) tree = tree.add(r.nextInt)
    tree
  }
  def createStringBST(n: Int): GeneralBST[String] = {
    val r = new Random()
    def aString = r.alphanumeric.take(r.nextInt(10)).mkString
    var tree = GeneralBSTImpl[String](aString)
    for (_ <- 1 to n) tree = tree.add(aString)
    tree
  }

  def createStuffBST(n: Int): GeneralBST[Watches] = {
    val r = new Random()
    def aString = r.alphanumeric.take(r.nextInt(10)).mkString
    def aWatches = Watches(aString, r.nextFloat() * 1000)
    var tree = GeneralBSTImpl[Watches](aWatches)
    for (_ <- 1 to n) tree = tree.add(aWatches)
    tree
  }

  val intTree = createIntBST(20)
  println(intTree.toString)
  val stringTree = createStringBST(20)
  println(stringTree.toString)
  val watchesTree = createStuffBST(20)
  println(watchesTree.toString)
}
