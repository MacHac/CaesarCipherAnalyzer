package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
        public Boolean call() {
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
                    .filter(f -> !f.isCancelled())
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
        threadPool = Executors.newFixedThreadPool(16); //Build thread pool
    }

    @Override
    protected int score(Cipher cipher) {
        String[] words = cipher.getWords();
        CipherAnalysis cas = new CipherAnalysis(words.length);
        for (String word : words) {
            WordAnalyzer wa = new WordAnalyzer(this.dictionaryWords, word);
            cas.push(threadPool.submit(wa));
        }

        return cas.score();
    }
}
