package com.example.demo.tools;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "sort", description = "Sort lines of text files")
public class SortCommand implements Callable<Integer> {

    @Option(names = {"-r", "--reverse"}, description = "Reverse sort order")
    private boolean reverse;

    @Option(names = {"-n", "--numeric"}, description = "Compare according to numeric value")
    private boolean numeric;

    @Parameters(description = "Files to read (empty for stdin)")
    private List<String> files;

    @Override
    public Integer call() throws Exception {
        List<String> lines = readLines();

        Comparator<String> comparator;

        if (numeric) {
            comparator = (s1, s2) -> {
                Double n1 = tryParseFloat(s1);
                Double n2 = tryParseFloat(s2);

                if (n1 != null && n2 != null) {
                    return Double.compare(n1, n2);
                } else if (n1 != null) {
                    return -1; // Числа йдуть перед текстом
                } else if (n2 != null) {
                    return 1;
                } else {
                    return s1.compareTo(s2); // Якщо обидва не числа, сортуємо як текст
                }
            };
        } else {
            comparator = String::compareTo;
        }

        if (reverse) {
            comparator = Collections.reverseOrder(comparator);
        }

        lines.sort(comparator);

        // Вивід результату
        for (String line : lines) {
            System.out.println(line);
        }

        return 0;
    }

    // Допоміжний метод для читання рядків з файлів або stdin
    private List<String> readLines() throws IOException {
        List<String> lines = new ArrayList<>();

        if (files == null || files.isEmpty()) {
            readStream(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)), lines);
        } else {
            for (String file : files) {
                if ("-".equals(file)) {
                    readStream(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)), lines);
                } else {
                    try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                        readStream(br, lines);
                    } catch (IOException e) {
                        System.err.println("Error reading file " + file + ": " + e.getMessage());
                    }
                }
            }
        }
        return lines;
    }

    private void readStream(BufferedReader br, List<String> list) throws IOException {
        String line;
        while ((line = br.readLine()) != null) {
            list.add(line);
        }
    }

    private Double tryParseFloat(String s) {
        try {
            return Double.parseDouble(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}