package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

public class ConcurrentDictionaryAnalysisCipherSolver extends DictionaryAnalysisCipherSolver {
    protected ExecutorService threadPool;

    protected class WordAnalyzer implements Callable<Boolean> {
        private final String testWord;
        private List<String> dict;

        WordAnalyzer(List<String> t_dict, String t_word) {
            this.testWord = t_word;
            this.dict = t_dict;
        }

        @Override
        public Boolean call() throws Exception {
            return dict.contains(testWord);
        }
    }

    protected class CipherAnalysis {
        List<Future<Boolean>> values;

        CipherAnalysis(int length) {
            values = new ArrayList<>(length);
        }

        public void push(Future<Boolean> f) {
            values.add(f);
        }

        public int score() {
            return values.stream()
                    .flatMap(f -> f.isCancelled() ? Stream.empty() : Stream.of(f))
                    .map(f -> {
                        try {
                            return f.get();
                        } catch (Exception e) {
                            e.printStackTrace();
                            return false;
                        }
                    }).mapToInt(b -> b ? 1 : 0)
                    .sum();
        }
    }

    public ConcurrentDictionaryAnalysisCipherSolver(String original) {
        super(original);
        dictionaryWords = Collections.unmodifiableList(dictionaryWords); //Prevent threads from modifying the loaded dictionary.
        threadPool = Executors.newFixedThreadPool(256); //Build thread pool
    }

    @Override
    public Cipher solve() {
        Cipher[] ciphers = new Cipher[26];
        for (int i = 0; i < ciphers.length; i++) {
            ciphers[i] = new Cipher(this.shifted(i), i);
        }

        int wordCount = ciphers[0].getWords().length; //All ciphers have the same value.

        //Queue tasks
        CipherAnalysis[] cas = new CipherAnalysis[26];
        for (int i = 0; i < 26; i++) {
            String[] words = ciphers[i].getWords();
            cas[i] = new CipherAnalysis(words.length);
            for (String word : words) {
                WordAnalyzer wa = new WordAnalyzer(this.dictionaryWords, word);
                cas[i].push(threadPool.submit(wa));
            }
        }

        //Evaluate scores
        int highestScore = cas[0].score();
        int highestIndex = 0;

        for (int i = 1; i < 26; i++) {
            int newScore = cas[i].score();
            if (newScore > highestScore) {
                highestIndex = i;
                highestScore = newScore;
            }
        }

        return ciphers[highestIndex];
    }
}
