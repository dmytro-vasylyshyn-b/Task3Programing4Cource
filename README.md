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


Ця команда створить файл target/nastya-linux-tools-1.0.0.jar.

Як запустити

Загальний синтаксис:

java -jar target/nastya-linux-tools-1.0.0.jar <command> [options]


Приклади використання:

Sort:

# Звичайне сортування
java -jar target/nastya-linux-tools-1.0.0.jar sort myfile.txt

# Числове сортування у зворотному порядку
java -jar target/nastya-linux-tools-1.0.0.jar sort -n -r numbers.txt

# Через pipe (stdin)
cat myfile.txt | java -jar target/nastya-linux-tools-1.0.0.jar sort


Tail:

# Останні 10 рядків (default)
java -jar target/nastya-linux-tools-1.0.0.jar tail log.txt

# Останні 20 рядків
java -jar target/nastya-linux-tools-1.0.0.jar tail -n 20 log.txt


Автори

Nastya
