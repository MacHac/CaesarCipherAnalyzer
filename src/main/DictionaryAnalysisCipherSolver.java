package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The logical conclusion of any word-matching solver schemes, this solver loads a dictionary of about 500,000 words
 * and counts the matches.  The false-positive rate is virtually zero, but the solve time can reach into the seconds
 * for longer ciphers.  The bump in accuracy is only noticable at very short lengths (<= 6-7 words), so an alternative
 * like ShortWordAnalysisCipherFactor should be used for longer texts.
 */

public class DictionaryAnalysisCipherSolver extends CipherSolver {
    protected List<String> dictionaryWords;

    public DictionaryAnalysisCipherSolver(String original) {
        super(original);
        dictionaryWords = loadDictionary();
    }

    protected List<String> loadDictionary() {
        List<String> out = new ArrayList<>();
        String line = null;
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("wordlist.txt"));

            while ((line = br.readLine()) != null) out.add(line);

            br.close();
        } catch (FileNotFoundException ex) {
            System.err.println("Could not load wordlist from file wordlist.txt.");
        } catch (IOException ex) {
            System.err.println("An error occurred loading file wordlist.txt.");
        }

        return out;
    }

    @Override
    protected int score(Cipher cipher) {
        String[] words = cipher.getWords();
        int score = 0;
        for (String word : words) {
            for (String dWord : dictionaryWords) {
                if (word.equals(dWord)) score++;
            }
        }

        return score;
    }
}
