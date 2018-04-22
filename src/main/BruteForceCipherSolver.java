package main;

import java.util.Arrays;

/**
 * The simplest possible implementation of a Caesar Cipher solver, this class simply assembles the 25 possible
 * solutions (with their respective shifts) into a single string and returns it.  The impetus is then on the user
 * to locate the correct translation.
 *
 * @see CipherSolver
 */

public class BruteForceCipherSolver implements Solver {
    static class CipherAggregate extends Cipher {
        CipherAggregate(String value) {
            super(value, 0);
        }

        @Override
        public String toString() {
            return this.value;
        }
    }

    private String original;
    BruteForceCipherSolver(String original) {
        this.original = original;
    }

    private char shiftLetter(char in, int amt) {
        final int BASE = Character.isUpperCase(in) ? 'A' : 'a';
        return (char)((((in - BASE) + amt) % 26) + BASE);
    }

    private String shifted(int amt) {
        return Arrays.stream(original.split(""))
                .map(s -> Character.isAlphabetic(s.charAt(0)) ? shiftLetter(s.charAt(0), amt) : s) //Ignore non-alphabetic characters
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    @Override
    public Cipher solve() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 26; i++) {
            sb.append(new Cipher(this.shifted(i), i).toString());
            sb.append("\n");
        }

        return new CipherAggregate(sb.toString());
    }
}
