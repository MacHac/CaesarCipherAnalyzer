package main;

/**
 * This implementation evaluates the 'englishness' of ciphertexts based on their use of the most common one, two, and
 * three letter english words.  The more valid words found in a ciphertext, the higher its score; The text with the
 * highest score is submitted as the program's guess.  Obviously, this won't work for ciphertexts with only 4+ letter
 * words.
 *
 * This strikes a good balance between speed and reliability; Virtually all written english has at least one of these
 * words in it, and the odds of a false positive are low.
 *
 * @see CipherSolver
 */

public class ShortWordAnalysisCipherSolver extends CipherSolver {
    ShortWordAnalysisCipherSolver(String original) {
        super(original);
    }

    /**
     * My source for these lists is
     * https://www3.nd.edu/~busiforc/handouts/cryptography/cryptography%20hints.html
     */

    private static final String[] oneLetterWords = new String[] {
            "i",
            "a"
    }; //There are exactly two valid single-letter words in english.

    private static final String[] twoLetterWords = new String[]{
            "of", "to", "in", "it", "is",
            "be", "as", "at", "so", "we",
            "he", "by", "or", "on", "do",
            "if", "me", "my", "up", "an",
            "go", "no", "us", "am"
    }; //There are more of these, so incorrect ciphers are more likely to form one.

    private static final String[] threeLetterWords = new String[]{
            "the", "and", "for", "are", "but",
            "not", "you", "all", "any", "can",
            "had", "her", "was", "one", "our",
            "out", "day", "get", "has", "him",
            "his", "how", "man", "new", "now",
            "old", "see", "two", "way", "who",
            "boy", "did", "its", "let", "put",
            "say", "she", "too", "use"
    }; //Not an exhaustive list


    protected int score(Cipher cipher) {
        String[] words = cipher.getWords();

        int score = 0;
        for (String operand : words) {
            for (String one : oneLetterWords) if (operand.equals(one)) score++;
            for (String two : twoLetterWords) if (operand.equals(two)) score++;
            for (String three : threeLetterWords) if (operand.equals(three)) score++;
        }

        return score;
    }
}
