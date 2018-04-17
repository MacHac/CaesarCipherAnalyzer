package main;

import java.util.Arrays;

public abstract class CipherSolver {
    protected String original;

    public CipherSolver(String original) {
        this.original = original;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    private char shiftLetter(char in, int amt) {
        final int BASE = Character.isUpperCase(in) ? 'A' : 'a';
        return (char)((((in - BASE) + amt) % 26) + BASE);
    }

    /**
     * This function shifts the 'original' text by amt units clockwise and returns a new cipher object with the result.
     * This does not mutate the factory.
     * @param amt The amount of letters to shift the sentence (may be any number, including 0.)
     * @return A Cipher object with the shifted string and properly set amount.
     */

    protected String shifted(int amt) {
        String ciphertext = Arrays.stream(original.split(""))
                .map(s -> Character.isAlphabetic(s.charAt(0)) ? shiftLetter(s.charAt(0), amt) : s) //Ignore non-alphabetic characters
                .collect(StringBuilder::new, (a, b) -> a.append(b), (a, b) -> a.append(b))
                .toString();
        return ciphertext;
    }

    public abstract Cipher solve();
}
