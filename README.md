# ️ Linux Tools (Java Edition)

Java-реалізація класичних Unix-утиліт **sort** та **tail**, що працюють з файлами та стандартним вводом.

---

##  Функціонал

###  **sort**
Сортування рядків з файлу або `stdin`.

Опції:
- **-r**, **--reverse** — зворотний порядок
- **-n**, **--numeric** — числове сортування (числа йдуть першими)

---

###  **tail**
Вивід останніх `N` рядків файлу.

Опції:
- **-n <count>** — кількість рядків (default: 10)

---

##  Вимоги

- **Java 17+**
- **Maven**

---

##  Збірка

```bash
mvn clean package

```

Після збірки JAR буде доступний у директорії `target/`.

## Запуск
### Загальний синтаксис




```
java -jar demo-0.0.1-SNAPSHOT.jar <command> [options]
```


##  Приклади використання

##  SORT

### Звичайне сортування
```
java -jar demo-0.0.1-SNAPSHOT.jar sort myfile.txt
```

### Числове сортування у зворотному порядку
```
java -jar demo-0.0.1-SNAPSHOT.jar sort -n -r numbers.txt
```

### Через pipe (stdin)
```
cat myfile.txt | java -jar demo-0.0.1-SNAPSHOT.jar sort
```



##  TAIL

### Останні 10 рядків (default)
```
java -jar demo-0.0.1-SNAPSHOT.jar tail log.txt
```
### Останні 20 рядків
```
java -jar demo-0.0.1-SNAPSHOT.jar tail -n 20 log.txt
```

##  Ліцензія
Вільна для використання в навчальних цілях.