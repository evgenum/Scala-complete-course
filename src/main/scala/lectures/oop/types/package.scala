package lectures.oop

import lectures.matching.SortingStuff.Watches

package object types {
  implicit class OrderingSyntax[T](t: T)(implicit ordering: Ordering[T]) {
    def >(other: T) = ordering.gt(t, other)
    def <(other: T) = ordering.lt(t, other)
    def ==(other: T) = ordering.compare(t, other)
  }
}
