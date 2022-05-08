package org.ntu.apiconverter.merger;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Token {

    private String value;

    private int index;

    private int frequency;

    public Token(String value, int index) {
        this.value = value;
        this.index = index;
    }

    public Token(String value) {
        this.value = value;
    }

    public Token(String value, int index, int frequency) {
        this.value = value;
        this.index = index;
        this.frequency = frequency;
    }
}
