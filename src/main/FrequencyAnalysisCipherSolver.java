package main;

/**
 * A more complex solution that looks for high occurences of certain letters that are very common in the english
 * language.  Using this paradigm, the factory will make its best guess.
 *
 * @see CipherSolver
 */

public class FrequencyAnalysisCipherSolver extends CipherSolver {
    protected class FACipher extends Cipher {
        /**
         * See http://www.counton.org/explorer/codebreaking/frequency-analysis.php for more information.
         */
        private final char[] letters = new char[]{'e', 't', 'a', 'o', 'i', 'n', 's', 'h', 'r'};
        private final double[] expectedFrequencies = new double[]{
                0.127,
                0.091,
                0.082,
                0.075,
                0.070,
                0.067,
                0.063,
                0.061,
                0.060
        };

        private double error;
        public FACipher(String value, int shift) {
            super(value, shift);

            double[] actualFrequencies = new double[expectedFrequencies.length];
            for (int i = 0; i < expectedFrequencies.length; i++) {
                actualFrequencies[i] = frequency(letters[i]);
            }

            error = meanSquaredError(expectedFrequencies, actualFrequencies);
        }

        public double getError() {
            return error;
        }

        //Compute the frequency of a letter in the given string.
        protected double frequency(char letter) {
            double count = 0;
            for (int i = 0; i < this.value.length(); i++) {
                char compareTo = Character.toLowerCase(this.value.charAt(i));
                if (compareTo == letter) count++;
            }

            return count / this.value.length();
        }

        //Determine the squared error in the frequencies
        protected double squaredError(double expected, double actual) {
            return Math.pow(actual - expected, 2);
        }

        //Determine the mean squared error of the set being evaluated.
        protected double meanSquaredError(double[] expected, double[] actual) {
            double count = 0;
            for (int i = 0; i < actual.length; i++) {
                count += squaredError(expected[i], actual[i]);
            }

            return Math.sqrt(count / actual.length);
        }

        @Override
        public String toString() {
            return String.format("%s (shift %d, error %1.5f)", this.value, this.shift, this.error);
        }
    }

    public FrequencyAnalysisCipherSolver(String original) {
        super(original);
    }

    @Override
    protected int score(Cipher cipher) {
        FACipher sub = new FACipher(cipher.getValue(), cipher.getShift());
        return (int)(Integer.MAX_VALUE * sub.getError());
    }
}
