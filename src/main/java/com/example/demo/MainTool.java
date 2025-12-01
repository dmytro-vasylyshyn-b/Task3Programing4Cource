package com.example.demo;

import com.example.demo.tools.SortCommand;
import com.example.demo.tools.TailCommand;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "jtools",
        mixinStandardHelpOptions = true,
        version = "jtools 1.0",
        description = "Java implomentation of Linux tools",
        subcommands = {SortCommand.class, TailCommand.class})
public class MainTool {

    public static void main(String[] args) {
        int exitCode = new CommandLine(new MainTool()).execute(args);
        System.exit(exitCode);
    }
}
