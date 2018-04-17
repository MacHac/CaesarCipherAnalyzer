package main;

public class Main {
    public static void main(String[] args) {
        CipherSolver cf = new BruteForceCipherSolver("AOL SLNPVU THYJOLZ");
        System.out.print(cf.solve());
    }
}
