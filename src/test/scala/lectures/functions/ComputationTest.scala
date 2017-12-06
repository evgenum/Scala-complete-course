package lectures.functions

import org.scalatest._
import lectures.functions.Computation._
import lectures.functions.CurriedComputation._
import lectures.functions.FunctionalComputation._

class ComputationTest extends FlatSpec with Matchers {
  val testFilter = "test string that contains words for testing"
  val testProducer = Array("test","testing")

  "Computation" should "compute result" in {
    computation(testFilter,testProducer).toSet should be (Set("test","testing"))
  }

  "CurriedComputation" should "compute result" in {
    curriedComputation(testFilter)(testProducer).toSet should be (Set("test","testing"))
  }

  "FunctionalComputation" should "compute result" in {
    val testFilterFunc = functionalComputation(testFilter.split(" "))
    testFilterFunc(testProducer).toSet should be (Set("test","testing"))
  }
}
