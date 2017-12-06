package lectures.functions

import org.scalatest.{FlatSpec, Matchers}
import lectures.functions.Fibonacci._
import lectures.functions.Fibonacci2._

class FibonacciTest extends FlatSpec with Matchers {
  "Fibonacci function" should "calculate the right result 1" in {
    fibs(1) should be (1)
  }
  it should "calculate the right result 2" in {
    fibs(2) should be (1)
  }
  it should "calculate the right result 3" in {
    fibs(4) should be (3)
  }
  it should "calculate the right result 4" in {
    fibs(7) should be(13)
  }
  it should "throw a StackOverflowError when argument is < 1" in {
    an[StackOverflowError] should be thrownBy fibs(0)
  }
    it should "throw a StackOverflowError when argument is < 1 (2)" in {
      an[StackOverflowError] should be thrownBy fibs(-10)
    }

  "Second Fibonacci function" should "calculate the right result 1" in {
    fibs2(1) should be (1)
  }
  it should "calculate the right result 2" in {
    fibs(2) should be (1)
  }
  it should "calculate the right result 3" in {
    fibs(4) should be (3)
  }
  it should "calculate the right result 4" in {
    fibs(7) should be (13)
  }
  it should "throw an exception" in {
    an[IndexOutOfBoundsException] should be thrownBy fibs2(0)
  }
}