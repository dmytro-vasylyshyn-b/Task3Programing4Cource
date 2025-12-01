package com.example.demo.tools;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

@Command(name = "tail", description = "Output the last part of files")
public class TailCommand implements Callable<Integer> {

    @Option(names = {"-n", "--lines"}, description = "Output the last K lines, instead of the last 10")
    private int linesToPrint = 10;

    @Parameters(description = "Files to read")
    private List<String> files;

    @Override
    public Integer call() throws Exception {
        if (files == null || files.isEmpty()) {
            processStream(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)), "stdin");
        } else {
            boolean printHeader = files.size() > 1;
            for (String file : files) {
                if (printHeader) {
                    System.out.println("==> " + file + " <==");
                }

                if ("-".equals(file)) {
                    processStream(new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8)), "stdin");
                } else {
                    try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
                        processStream(br, file);
                    } catch (IOException e) {
                        System.err.println("tail: cannot open '" + file + "' for reading: " + e.getMessage());
                    }
                }

                if (printHeader) System.out.println();
            }
        }
        return 0;
    }

    private void processStream(BufferedReader br, String sourceName) throws IOException {
        // Використовуємо LinkedList як чергу, щоб зберігати тільки останні N рядків
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