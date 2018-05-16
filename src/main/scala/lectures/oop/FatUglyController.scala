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
class FatUglyController {

  sealed case class Response(code: Int, message: String)
  object EmptyFile extends Response(400, "Can not upload empty file")
  object ForbiddenFileSize extends Response(400, "File size should not be more than 8 MB")
  object ForbiddenExtension extends Response(400, "Request contains forbidden extension")
  case class Success(messageBody: String) extends Response(200, "Response:\n" + messageBody)
  object RouteNotFound extends Response(404, "Route not found")

  def processRoute(route: String, requestBody: Option[Array[Byte]]): Response = {
    val responseBuf = new StringBuilder()
    val databaseConnectionId = connectToPostgresDatabase()
    val mqConnectionId = connectToIbmMq()
    initializeLocalMailer()
    if (route == "/api/v1/uploadFile") {
      if (requestBody.isEmpty) {
        EmptyFile
      } else if (requestBody.get.length > 8388608) {
        ForbiddenFileSize
      } else {
        val stringBody = new String(requestBody.get.filter(_ != '\r'))
        val delimiter = stringBody.takeWhile(_ != '\n')
        val files = stringBody.split(delimiter).drop(1)
        files.foreach { file =>
          val (name, body) = file.trim.splitAt(file.trim.indexOf('\n'))
          val trimmedBody = body.trim
          val extension = name.reverse.takeWhile(_ != '.').reverse
          val id = hash(file.trim)
          if (Seq("exe", "bat", "com", "sh").contains(extension)) {
            ForbiddenExtension
          }
          // Emulate file saving to disk
          responseBuf.append(s"- saved file $name to " + id + "." + extension + s" (file size: ${trimmedBody.length})\n")

          executePostgresQuery(databaseConnectionId, s"insert into files (id, name, created_on) values ('$id', '$name', current_timestamp)")
          sendMessageToIbmMq(mqConnectionId, s"""<Event name="FileUpload"><Origin>SCALA_FTK_TASK</Origin><FileName>${name}</FileName></Event>""")
          send("admin@admin.tinkoff.ru", "File has been uploaded", s"Hey, we have got new file: $name")
        }

        Success(responseBuf.dropRight(1).toString)
      }
    } else {
      RouteNotFound
    }
  }

  def connectToPostgresDatabase(): Int = {
    // DO NOT TOUCH
    println("Connected to PostgerSQL database")
    42 // pretty unique connection id
  }

  def executePostgresQuery(connectionId: Int, sql: String): String = {
    // DO NOT TOUCH
    println(s"Executed SQL statement on connection $connectionId: $sql")
    s"Result of $sql"
  }

  def connectToIbmMq(): Int = {
    // DO NOT TOUCH
    println("Connected to IBM WebSphere super-duper MQ Manager")
    13 // chosen by fair dice roll
  }

  def sendMessageToIbmMq(connectionId: Int, message: String): String = {
    // DO NOT TOUCH
    println(s"Sent MQ message via $connectionId: $message")
    s"Message sending result for $message"
  }

  def initializeLocalMailer(): Unit = {
    // DO NOT TOUCH
    println("Initialized local mailer")
  }

  def send(email: String, subject: String, body: String): Unit = {
    // DO NOT TOUCH
    println(s"Sent email to $email with subject '$subject'")
  }

  def hash(s: String): String = {
    MessageDigest.getInstance("SHA-1").digest(s.getBytes("UTF-8")).map("%02x".format(_)).mkString
  }
}
