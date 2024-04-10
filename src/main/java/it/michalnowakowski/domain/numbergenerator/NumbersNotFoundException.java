package it.michalnowakowski.domain.numbergenerator;

public class NumbersNotFoundException extends RuntimeException {
    NumbersNotFoundException(String msg) {
        super(msg);
    }
}
