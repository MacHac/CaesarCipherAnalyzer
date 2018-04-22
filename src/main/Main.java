package main;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        String inString = Arrays.stream(args).flatMap(s -> Stream.of(s, " ")).reduce("", (a, b)-> a + b);
        Solver cf = parseArg(inString);
        if (cf != null)
            System.out.print(cf.solve());
    }

    private static Solver parseArg(String argument) {
        String regex = "-{1,2}([a-zA-Z_]+)\\s?(\\w*)";

        Matcher argMatcher = Pattern.compile(regex).matcher(argument);

        if (!argMatcher.find()) {
            System.err.println("Illegal argument.  Use --help for help.%n");
            return null;
        }

        String command = argMatcher.group(1);
        String arg = argMatcher.group(2);

        switch (command) {
            case "dict":
                return new DictionaryAnalysisCipherSolver(arg);
            case "concurrent_dict":
                return new ConcurrentDictionaryAnalysisCipherSolver(arg);
            case "frequency":
                return new FrequencyAnalysisCipherSolver(arg);
            case "short_word":
                return new ShortWordAnalysisCipherSolver(arg);
            case "manual":
                return new BruteForceCipherSolver(arg);
            default:
                System.out.println("To run, run the program with an argument formatted like '--[technique] ciphertext'.\n");
                System.out.println("Available techniques: dict concurrent_dict frequency short_word manual\n");
                return null;
        }
    }
}
