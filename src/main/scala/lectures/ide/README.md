Домашнее задание по лекции "Среда разработки и тестирования"

[Markdown syntax](https://github.com/adam-p/markdown-here/wiki/Markdown-Cheatsheet)

### 1. Составить перечень горячих клавиш (минимум 10):

* ``Ctrl + B`` - Перейти к реализации метода или класса
* ``Alt + Left`` - Навигация влево среди открытых файлов
* ``Alt + Right`` - Навигация вправо среди открытых файлов
* ``Alt + Up`` - Перемещение к заголовку метода/класса выше по файлу
* ``Alt + Down`` - Перемещение к заголовку метода/класса ниже по файлу
* ``Ctrl + Shift + L`` - Рефакторинг кода с помощью scalafmt
* ``Click + Alt + Move`` - растянуть курсор на несколько строк
* ``Ctrl + O`` - Override method template
* ``Ctrl + I`` - Implement method template
* ``Alt + =`` - Показать тип выделенного выражения


### 2. Завести себе минимум 3 шаблона быстрой подстановки (Live templates)

1. create `object` :

```
object $NAME$ {
    $END$
}
```

2. `Awai`:

```
Await.result( $VALUE$, Duration(10,'s'))$END$
```

3. `succ`:

```
Future.successful($NAME$)$END$
```
### 3. Настроить 2 таски на прогон всех тестов: через IDEA и через SBT
