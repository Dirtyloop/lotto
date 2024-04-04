package it.michalnowakowski.domain.numbergenerator;

import java.util.Set;

class LuckyNumbersValidator {

    private final int MIN_RANGE = 1;
    private final int MAX_RANGE = 99;

    public  Set<Integer> validate(Set<Integer> luckyNumbers) {
        if (outOfRange(luckyNumbers)) {
            throw new IllegalStateException("One or more numbers is out of range.");
        }
        return luckyNumbers;
    }

    private boolean outOfRange(Set<Integer> luckyNumbers) {
        return luckyNumbers.stream()
                .anyMatch(number -> number <MIN_RANGE || number > MAX_RANGE);
    }
}
