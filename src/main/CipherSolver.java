package main;

import java.util.Arrays;

public abstract class CipherSolver implements Solver {
    private String original;

    CipherSolver(String original) {
        this.original = original;
    }

    public String getOriginal() {
        return original;
    }

    void setOriginal(String original) {
        this.original = original;
    }

    /**
     * This function 'rotates' a letter around the alphabet.
     * @param in The letter to rotate
     * @param amt The amount to rotate it by
     * @return The rotated letter.
     */

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

    private String shifted(int amt) {
        return Arrays.stream(original.split(""))
                .map(s -> Character.isAlphabetic(s.charAt(0)) ? shiftLetter(s.charAt(0), amt) : s) //Ignore non-alphabetic characters
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
    }

    /**
     * This function produces an array of Cipher objects, one for each possible key.
     * @return The produced array.
     */

    private Cipher[] permutations() {
        Cipher[] ciphers = new Cipher[26];
        for (int i = 0; i < ciphers.length; i++) {
            ciphers[i] = new Cipher(this.shifted(i), i);
        }

        return ciphers;
    }

    /**
     * This function rates each of the possible ciphers from permutations() using the object's
     * implementation of the score() function, then returns the one that scored the highest.
     * @return The highest-scoring Cipher object.
     */

    public Cipher solve() {
        Cipher[] ciphers = this.permutations();

        int max = 0;
        int maxScore = score(ciphers[0]);

        for (int i = 1; i < 26; i++) {
            int newScore = score(ciphers[i]);
            if (newScore > maxScore) {
                max = i;
                maxScore = newScore;
            }
        }

        return ciphers[max];
    }

    protected abstract int score(Cipher cipher);
}
