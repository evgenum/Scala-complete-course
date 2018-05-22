package lectures.oop

import java.security.MessageDigest

/**
  * Данный класс содержит код, наспех написанный одним джуниор-разработчиком,
  * который плохо слушал лекции по эффективному программированию.
  *
  * Вам необходимо:
  * - отрефакторить данный класс, выделив уровни ответственности, необходимые
  *   интерфейсы и абстракции
  * - дописать тесты в FatUglyControllerTest и реализовать в них проверку на
  *   сохранение в БД, отправку сообщения в очередь и отправку email-а
  * - исправить очевидные костыли в коде
  *
  * Код внутри методов, помеченный как DO NOT TOUCH, трогать нельзя (сами методы
  * при этом можно выносить куда и как угодно)
  *
  * Интерфейс метода processRoute менять можно и нужно!
  * Передаваемые данные при этом должны оставаться неизменными.
  *
  * Удачи!
  */
class FatUglyController(mq: Mq, database: DataBase, localMailer: LocalMailer) {

  val uploadRoute = "/api/v1/uploadFile"
  val maxRequestLength = 8388608
  def proceedFile(file: String)(implicit responseBuf: StringBuilder,
                                databaseConnectionId: Int,
                                mqConnectionId: Int): Unit = {
    val (name, body) = file.trim.splitAt(file.trim.indexOf('\n'))
    val trimmedBody = body.trim
    val extension = name.reverse.takeWhile(_ != '.').reverse
    val id = hash(file.trim)
    if (Seq("exe", "bat", "com", "sh").contains(extension)) {
      ForbiddenExtension
    }
    // Emulate file saving to disk
    responseBuf.append(
      s"- saved file $name to " + id + "." + extension + s" (file size: ${trimmedBody.length})\n")

    database.executePostgresQuery(
      databaseConnectionId,
      s"insert into files (id, name, created_on) values ('$id', '$name', current_timestamp)")
    mq.sendMessageToIbmMq(
      mqConnectionId,
      s"""<Event name="FileUpload"><Origin>SCALA_FTK_TASK</Origin><FileName>${name}</FileName></Event>""")
    localMailer.send("admin@admin.tinkoff.ru",
                     "File has been uploaded",
                     s"Hey, we have got new file: $name")
  }
  def deserializeRequest(r: Array[Byte])(
      implicit responseBuf: StringBuilder): Response = {
    val stringBody = new String(r.filter(_ != '\r'))
    val delimiter = stringBody.takeWhile(_ != '\n')
    val files = stringBody.split(delimiter).drop(1)
    files.foreach(proceedFile)

    Success(responseBuf.dropRight(1).toString)
  }
  def proceedRequest(request: Option[Array[Byte]]): Response = request match {
    case None                                   => EmptyFile
    case Some(r) if r.length > maxRequestLength => ForbiddenFileSize
    case Some(r)                                => deserializeRequest(r)
  }
  def processRoute(route: String,
                   requestBody: Option[Array[Byte]]): Response = {
    implicit val responseBuf: StringBuilder = new StringBuilder()
    implicit val databaseConnectionId: Int =
      database.connectToPostgresDatabase()
    implicit val mqConnectionId: Int = mq.connectToIbmMq()
    localMailer.initializeLocalMailer()

    route match {
      case r if r == uploadRoute => proceedRequest(requestBody)
      case _                     => RouteNotFound
    }
  }

  def hash(s: String): String = {
    MessageDigest
      .getInstance("SHA-1")
      .digest(s.getBytes("UTF-8"))
      .map("%02x".format(_))
      .mkString
  }
}

trait Mq {
  def connectToIbmMq(): Int
  def sendMessageToIbmMq(connectionId: Int, message: String): String
}

trait DataBase {
  def connectToPostgresDatabase(): Int
  def executePostgresQuery(connectionId: Int, sql: String): String
}
trait LocalMailer {
  def initializeLocalMailer(): Unit
  def send(email: String, subject: String, body: String): Unit
}

object MqImpl extends Mq {
  override def connectToIbmMq(): Int = {
    // DO NOT TOUCH
    println("Connected to IBM WebSphere super-duper MQ Manager")
    13 // chosen by fair dice roll
  }
  override def sendMessageToIbmMq(connectionId: Int,
                                  message: String): String = {
    // DO NOT TOUCH
    println(s"Sent MQ message via $connectionId: $message")
    s"Message sending result for $message"
  }
}
object DatabaseImpl extends DataBase {
  override def connectToPostgresDatabase(): Int = {
    // DO NOT TOUCH
    println("Connected to PostgerSQL database")
    42 // pretty unique connection id
  }
  override def executePostgresQuery(connectionId: Int, sql: String): String = {
    // DO NOT TOUCH
    println(s"Executed SQL statement on connection $connectionId: $sql")
    s"Result of $sql"
  }
}

object LocalMailerImpl extends LocalMailer {
  def initializeLocalMailer(): Unit = {
    // DO NOT TOUCH
    println("Initialized local mailer")
  }
  def send(email: String, subject: String, body: String): Unit = {
    // DO NOT TOUCH
    println(s"Sent email to $email with subject '$subject'")
  }
}

sealed case class Response(code: Int, message: String)
object EmptyFile extends Response(400, "Can not upload empty file")
object ForbiddenFileSize
    extends Response(400, "File size should not be more than 8 MB")
object ForbiddenExtension
    extends Response(400, "Request contains forbidden extension")
case class Success(messageBody: String)
    extends Response(200, "Response:\n" + messageBody)
object RouteNotFound extends Response(404, "Route not found")
