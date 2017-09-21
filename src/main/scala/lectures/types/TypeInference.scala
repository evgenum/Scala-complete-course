package lectures.types

/**
  * Не запуская приложения, предположите, чему будет равен 'result'.
  * Почему?
  *
  * Ответ: result будет равен "2 плюс 3 - это 32"
  * Это происходит из-за автоматического привидения типов к одному общему предку - строковому типу.
  */
object TypeInference extends App {

  def printSomething() = "2 плюс 3 - это "

  def calculateSomething() = 1 + 1

  def result = printSomething + 3 + calculateSomething

  print(result)

}

