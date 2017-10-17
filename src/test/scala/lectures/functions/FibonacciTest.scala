package lectures.functions

import org.scalatest.{FlatSpec, Matchers}
import lectures.functions.Fibonacci._
import lectures.functions.Fibonacci2._

class FibonacciTest extends FlatSpec with Matchers {
  "Fibonacci function" should "calculate the right result" in {
    fibs(1) should be (1)
    fibs(2) should be (1)
    fibs(4) should be (3)
    fibs(7) should be (13)
  }
  it should "throw a StackOverflowError when argument is < 1" in {
    an[StackOverflowError] should be thrownBy fibs(0)
    an[StackOverflowError] should be thrownBy fibs(-10)
  }

  "Second Fibonacci function" should "calculate the right result" in {
    fibs2(1) should be (1)
    fibs2(2) should be (1)
    fibs2(4) should be (3)
    fibs2(7) should be (13)
  }
  it should "throw an exception" in {
    an[IndexOutOfBoundsException] should be thrownBy fibs2(0)
  }
}