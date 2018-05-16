package lectures.oop


/**
  * BSTImpl - это бинарное дерево поиска, содержащее только значения типа Int
  *
  * * Оно обладает следующими свойствами:
  * * * * * левое поддерево содержит значения, меньшие значения родителя
  * * * * * правое поддерево содержит значения, большие значения родителя
  * * * * * значения, уже присутствующие в дереве, в него не добавляются
  * * * * * пустые значения (null) не допускаются
  *
  * * Завершите реализацию методов кейс класс BSTImpl:
  * * * * * Трейт BST и BSTImpl разрешается расширять любым образом
  * * * * * Изменять сигнатуры классов и методов, данные в условии, нельзя
  * * * * * Постарайтесь не использовать var и мутабильные коллекции
  * * * * * В задаче про распечатку дерева, нужно раскомментировать и реализовать метод toString()
  *
  * * Для этой структуры нужно реализовать генератор узлов.
  * * Генератор:
  * * * * * должен создавать дерево, содержащее nodesCount узлов.
  * * * * * не должен использовать переменные или мутабильные структуры.
  *
  */
trait BST {
  val value: Int
  val left: Option[BST]
  val right: Option[BST]

  def add(newValue: Int): BST

  def find(value: Int): Option[BST]
}

case class BSTImpl(value: Int,
                   left: Option[BSTImpl] = None,
                   right: Option[BSTImpl] = None) extends BST {

  def tempAdd(newValue: Int): BSTImpl = {
    def newleft:Option[BSTImpl] =  this.left match {
      case Some(x) if newValue < this.value => Option(x.tempAdd(newValue))
      case None if newValue < this.value => Option(BSTImpl(newValue, None, None))
      case x => x
    }
    def newright:Option[BSTImpl] = this.right match {
      case Some(x) if newValue > this.value => Option(x.tempAdd(newValue))
      case None if newValue > this.value => Option(BSTImpl(newValue, None, None))
      case  x => x
    }
    BSTImpl(value, newleft, newright)
  }
  def add(newValue: Int): BST =
    this.tempAdd(newValue)

  def find(value: Int): Option[BST] =
    if( value < this.value) this.left.flatMap( _.find(value))
    else if(value > this.value) this.right.flatMap( _.find(value))
    else Option(this)


  def pow(x: Int, y: Int): Int = Math.pow(x,y).toInt

  def depth: Int = {
    val l = this.left.map(_.depth).getOrElse(0)
    val r = this.right.map(_.depth).getOrElse(0)
    Math.max(l,r) + 1
  }
    def ArrList(d:Int): IndexedSeq[List[Option[Int]]] = {
      val leftA: IndexedSeq[List[Option[Int]]] = this.left match {
        case Some(x) => x.ArrList(d - 1)
        case None => (0 until d - 1).map(i => (1 to pow(2, i)).map(j => Option.empty[Int]).toList)
      }
      val rightA: IndexedSeq[List[Option[Int]]] = this.right match {
        case Some(x) => x.ArrList(d - 1)
        case None => (0 until d - 1).map(i => (1 to pow(2, i)).map(j => Option.empty[Int]).toList)
      }
      (0 until d).map(i => if(i == 0) List(Option(this.value)) else leftA(i - 1) ::: rightA(i - 1))
    }

   override def toString() = {
        val d = depth
        val output = ArrList(d)
      def width(i:Int):String = (1 until pow(2, i)).foldLeft("":String)( (str, j) => str.concat("\t"))

     (for(i <- 0 until d) yield {
       val w = width(d - i - 1)
        output(i).foldLeft("\n":String)( (str,x) => x match {
          case None => str + w + "\t" + w + "\t"
          case Some(p) => str + w + p.toString + w + "\t"
        })
      }).foldLeft("": String)( (s1:String,s2:String) => s1.concat(s2))

   }

}

object TreeTest extends App {

  val sc = new java.util.Scanner(System.in)
  val maxValue = 110000
  val nodesCount = sc.nextInt()

  val markerItem = (Math.random() * maxValue).toInt
  val markerItem2 = (Math.random() * maxValue).toInt
  val markerItem3 = (Math.random() * maxValue).toInt

  // Generate huge tree
  val root: BST = BSTImpl(maxValue / 2)
  val tree: BST =
    (1 until nodesCount).map(_ => (Math.random()*maxValue).toInt)
      .foldLeft(root)((tree, elem) => tree.add(elem)) // generator goes here

  // add marker items
  val testTree = tree.add(markerItem).add(markerItem2).add(markerItem3)

  // check that search is correct
  require(testTree.find(markerItem).isDefined)
  require(testTree.find(markerItem).isDefined)
  require(testTree.find(markerItem).isDefined)

  println(testTree)
}