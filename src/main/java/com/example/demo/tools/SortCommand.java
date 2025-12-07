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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "sort", description = "Sort lines of text files or direct string arguments")
public class SortCommand implements Callable<Integer> {

    @Option(names = {"-r", "--reverse"}, description = "Reverse sort order")
    private boolean reverse;

    @Option(names = {"-n", "--numeric"}, description = "Compare according to numeric value")
    private boolean numeric;

    @Parameters(description = "Files to read or strings to sort")
    private List<String> inputs; // Перейменував files на inputs для ясності

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
                    return -1;
                } else if (n2 != null) {
                    return 1;
                } else {
                    return s1.compareTo(s2);
                }
            };
        } else {
            comparator = String::compareTo;
        }

        if (reverse) {
            comparator = Collections.reverseOrder(comparator);
        }

        lines.sort(comparator);

        for (String line : lines) {
            System.out.println(line);
        }

        return 0;
    }

    private List<String> readLines() throws IOException {
        List<String> lines = new ArrayList<>();

        // Якщо аргументів немає, читаємо зі Standard Input (Pipe)
        if (inputs == null || inputs.isEmpty()) {
            readStream(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)), lines);
        } else {
            for (String input : inputs) {
                if ("-".equals(input)) {
                    readStream(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)), lines);
                    continue;
                }

                File file = new File(input);

                if (file.exists() && file.isFile()) {
                    try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                        readStream(br, lines);
                    } catch (IOException e) {
                        System.err.println("Error reading file " + input + ": " + e.getMessage());
                    }
                } else {
                    // Це просто рядок (наприклад, "apple")
                    lines.add(input);
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