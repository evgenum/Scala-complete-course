package lectures.collections

/**
  * Постарайтесь не использовать мутабильные коллекции и var
  * Подробнее о сортировке можно подсмотреть здесь - https://en.wikipedia.org/wiki/Merge_sort
  *
  */
object MergeSortImpl extends App {

  def mergeSort(data: Seq[Int]): Seq[Int] = {
    if(data.isEmpty) data
    else if(data.tail.isEmpty) data
    else {
      val a = data.splitAt(data.length / 2)
      merge(mergeSort(a._1),mergeSort(a._2))
    }
  }

  def merge(a: (Seq[Int],Seq[Int])): Seq[Int] = a match {
    case (Seq(),right) => right
    case (left,Seq()) => left
    case (left,right) if left.head > right.head => Seq(right.head) ++ merge(left, right.tail)
    case (left,right) if left.head <= right.head => Seq(left.head) ++ merge(left.tail, right)
  }
}
