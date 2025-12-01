Linux Tools (Java Edition)

Це Java-реалізація класичних Unix-утиліт sort та tail

Функціонал

sort: Сортування рядків з файлів або stdin.

-r, --reverse: Зворотний порядок.

-n, --numeric: Числове сортування (числа йдуть першими).

tail: Вивід останніх N рядків файлу.

-n <count>: Вказати кількість рядків (за замовчуванням 10).

Вимоги

Java 17 або вище

Maven

Як зібрати проект

mvn clean package


Ця команда створить файл target

Як запустити

Загальний синтаксис:

- java -jar demo-0.0.1-SNAPSHOT.jar  <command> [options]


Приклади використання:

Sort:

# Звичайне сортування
- java -jar demo-0.0.1-SNAPSHOT.jar sort myfile.txt

# Числове сортування у зворотному порядку
- java -jar demo-0.0.1-SNAPSHOT.jar  sort -n -r numbers.txt

# Через pipe (stdin)
- cat myfile.txt | java -jar demo-0.0.1-SNAPSHOT.jar  sort


Tail:

# Останні 10 рядків (default)
- java -jar demo-0.0.1-SNAPSHOT.jar tail log.txt

# Останні 20 рядків
- java -jar demo-0.0.1-SNAPSHOT.jar tail -n 20 log.txt

