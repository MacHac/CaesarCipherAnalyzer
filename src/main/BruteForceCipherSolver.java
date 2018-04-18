package main;

/**
 * The simplest possible implementation of a Caesar Cipher solver, this class simply assembles the 25 possible
 * solutions (with their respective shifts) into a single string and returns it.  The impetus is then on the user
 * to locate the correct translation.
 *
 * @see CipherSolver
 */

public class BruteForceCipherSolver extends CipherSolver {
    public BruteForceCipherSolver(String original) {
        super(original);
    }

    @Override
    public Cipher solve() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 26; i++) {
            sb.append(new Cipher(this.shifted(i), i).toString());
            sb.append("\n");
        }

        return new Cipher(sb.toString(), -1);
    }

    @Override
    protected int score(Cipher cipher) {
        return 0;
    }
}
