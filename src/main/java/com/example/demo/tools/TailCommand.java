package com.example.demo.tools;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "tail", description = "Output the last part of files or string arguments")
public class TailCommand implements Callable<Integer> {

    @Option(names = {"-n", "--lines"}, description = "Output the last K lines, instead of the last 10")
    private int linesToPrint = 10;

    @Parameters(description = "Files to read or strings to process")
    private List<String> inputs;

    @Override
    public Integer call() throws Exception {
        // 1. Якщо аргументів немає — читаємо System.in (Pipe)
        if (inputs == null || inputs.isEmpty()) {
            processStream(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)));
            return 0;
        }

        List<String> validFiles = new ArrayList<>();
        List<String> textLines = new ArrayList<>();

        // 2. Розподіляємо аргументи: що є файлом, а що текстом
        for (String input : inputs) {
            File f = new File(input);
            if (f.exists() && f.isFile()) {
                validFiles.add(input);
            } else if ("-".equals(input)) {
                // "-" явно вказує на читання з Pipe
                processStream(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)));
            } else {
                // Якщо це не файл, додаємо у список рядків
                textLines.add(input);
            }
        }

        // Логіка заголовків: потрібні, якщо у нас більше одного джерела даних
        boolean multipleSources = (validFiles.size() + (textLines.isEmpty() ? 0 : 1)) > 1;
        boolean needNewLine = false;

        // 3. Обробляємо текстові аргументи як єдиний потік
        if (!textLines.isEmpty()) {
            if (multipleSources) {
                System.out.println("==> arguments <==");
            }

            // Беремо останні N елементів зі списку
            int start = Math.max(0, textLines.size() - linesToPrint);
            for (int i = start; i < textLines.size(); i++) {
                System.out.println(textLines.get(i));
            }
            needNewLine = true;
        }

        // 4. Обробляємо справжні файли
        for (String file : validFiles) {
            if (needNewLine) System.out.println();

            if (multipleSources) {
                System.out.println("==> " + file + " <==");
            }

            try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                processStream(br);
            } catch (IOException e) {
                System.err.println("tail: cannot open '" + file + "' for reading: " + e.getMessage());
            }
            needNewLine = true;
        }

        return 0;
    }

    // Допоміжний метод для читання потоків (файли або stdin)
    private void processStream(BufferedReader br) throws IOException {
        LinkedList<String> buffer = new LinkedList<>();
        String line;
        while ((line = br.readLine()) != null) {
            buffer.add(line);
            if (buffer.size() > linesToPrint) {
                buffer.removeFirst();
            }
        }
        for (String l : buffer) {
            System.out.println(l);
        }
    }
}