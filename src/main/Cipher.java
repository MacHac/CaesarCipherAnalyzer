package main;

import java.util.Arrays;

public class Cipher {
    String value;
    int shift;

    public Cipher(String value, int shift) {
        this.value = value;
        this.shift = shift;
    }

    public String getValue() {
        return value;
    }

    public String[] getWords() {
        return Arrays.stream(value.split(" ")) //Split into words
                .map(String::toLowerCase) //Convert to uppercase
                .map(s -> s.replaceAll("[^a-zA-Z]", "")) //Remove any punctuation/numbers
                .toArray(String[]::new); //Collapse into an array
    }

    public int getShift() {
        return shift;
    }

    @Override
    public String toString() {
        return String.format("%s (shift %d)", this.value, this.shift);
    }
}
