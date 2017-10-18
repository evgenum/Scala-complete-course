package lectures.operators

import org.scalatest.{FlatSpec, Matchers}

class CompetitionTest extends FlatSpec with Matchers{
  "Competition" should "compute finalResult equal to 3" in {
    Competition.finalResult shouldBe 0
  }
}