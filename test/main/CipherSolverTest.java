package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

class CipherSolverTest {
    static CipherSolver a, b, c, d;
    
    private static class CipherTestCase {
        String solution;
        String ciphertext;

        public CipherTestCase(String solution, String ciphertext) {
            this.solution = solution;
            this.ciphertext = ciphertext;
        }

        public boolean check(String out) {
            return out.equals(solution);
        }
    }

    public static void main(String[] args) {
        System.out.println("Loading solvers...");
        long start = System.currentTimeMillis();
        a = new FrequencyAnalysisCipherSolver("");
        b = new ShortWordAnalysisCipherSolver("");
        c = new DictionaryAnalysisCipherSolver("");
        d = new ConcurrentDictionaryAnalysisCipherSolver("");
        long end = System.currentTimeMillis();
        System.out.printf("Finished loading in %d ms.\n", end - start);
        System.out.println("Loading test cases...");
        List<CipherTestCase> testCases = loadTestCases("sample.txt");
        System.out.printf("%d test cases loaded.\n", testCases.size());

        int aCorrect = 0;
        int bCorrect = 0;
        int cCorrect = 0;
        int dCorrect = 0;

        Cipher aSol, bSol, cSol, dSol;
        long aTime = 0, bTime = 0, cTime = 0, dTime = 0;
        for (CipherTestCase tCase : testCases) {
            a.setOriginal(tCase.ciphertext);
            b.setOriginal(tCase.ciphertext);
            c.setOriginal(tCase.ciphertext);
            d.setOriginal(tCase.ciphertext);

            start = System.currentTimeMillis();
            aSol = a.solve();
            end = System.currentTimeMillis();
            aTime += end - start;
            System.out.printf("Frequency Analysis:\nProduced:\n%s;\nExpected:\n%s.\n\n", aSol, tCase.solution);
            if (tCase.check(aSol.getValue())) aCorrect++;

            start = System.currentTimeMillis();
            bSol = b.solve();
            end = System.currentTimeMillis();
            bTime += end - start;
            System.out.printf("Short Word Analysis:\nProduced:\n%s;\nExpected:\n%s.\n\n", bSol, tCase.solution);
            if (tCase.check(bSol.getValue())) bCorrect++;

            start = System.currentTimeMillis();
            cSol = c.solve();
            end = System.currentTimeMillis();
            cTime += end - start;
            System.out.printf("Dictionary Analysis:\nProduced:\n%s;\nExpected:\n%s.\n\n", cSol, tCase.solution);
            if (tCase.check(cSol.getValue())) cCorrect++;

            start = System.currentTimeMillis();
            dSol = d.solve();
            end = System.currentTimeMillis();
            dTime += end - start;
            System.out.printf("Concurrent Dictionary Analysis:\nProduced:\n%s;\nExpected:\n%s.\n\n", dSol, tCase.solution);
            if (tCase.check(dSol.getValue())) dCorrect++;
        }

        System.out.printf("Frequency Analysis Accuracy: %d/%d; Time taken: %d ms.\n", aCorrect, testCases.size(), aTime);
        System.out.printf("Short Word Analysis Accuracy: %d/%d; Time taken: %d ms.\n", bCorrect, testCases.size(), bTime);
        System.out.printf("Dictionary Analysis Accuracy: %d/%d; Time taken: %d ms.\n", cCorrect, testCases.size(), cTime);
        System.out.printf("Concurrent Dictionary Analysis Accuracy: %d/%d; Time taken: %d ms.\n", dCorrect, testCases.size(), dTime);
    }


    public static List<CipherTestCase> loadTestCases(String fileName) {
        List<String> lines = new ArrayList<>();
        String line = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(fileName));

            while ((line = br.readLine()) != null) lines.add(line);

            br.close();
        } catch (FileNotFoundException ex) {
            System.err.printf("Could not load sample from file %s.\n", fileName);
        } catch (IOException ex) {
            System.err.printf("An error occurred loading file %s.\n", fileName);
        }

        List<CipherTestCase> testCases = new ArrayList<>();
        for (String t_line : lines) {
            int shift = ThreadLocalRandom.current().nextInt(0, 26);
            testCases.add(new CipherTestCase(t_line, shifted(t_line, shift)));
        }

        return testCases;
    }

    protected static char shiftLetter(char in, int amt) {
        final int BASE = Character.isUpperCase(in) ? 'A' : 'a';
        return (char)((((in - BASE) + amt) % 26) + BASE);
    }

    protected static String shifted(String original, int amt) {
        String ciphertext = Arrays.stream(original.split(""))
                .map(s -> Character.isAlphabetic(s.charAt(0)) ? shiftLetter(s.charAt(0), amt) : s) //Ignore non-alphabetic characters
                .collect(StringBuilder::new, (a, b) -> a.append(b), (a, b) -> a.append(b))
                .toString();
        return ciphertext;
    }
}