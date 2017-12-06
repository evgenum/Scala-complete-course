package lectures.functions

import org.scalatest.{FlatSpec, Matchers}

import scala.collection.mutable.Queue

class SQLAPITest extends FlatSpec with Matchers{


  "SQLAPI" should "return success" in {

    new SQLAPI("TestDB").execute("TestRequest") shouldBe "SQL has been executed. Congrats!"

  }
  it should "open connection successfully" in {
    val connection = new SQLAPI("TestDB").connection("TestRequest").open()


      connection.opened shouldBe true

  }
}
