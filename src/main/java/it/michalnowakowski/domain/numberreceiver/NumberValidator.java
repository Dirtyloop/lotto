package it.michalnowakowski.domain.numberreceiver;

import java.util.Set;

class NumberValidator {

    public static final int NUMBERS_COUNT = 6;
    public static final int MIN_RANGE = 1;
    public static final int MAX_RANGE = 99;


    boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .filter(num -> num >= MIN_RANGE)
                .filter(num -> num <= MAX_RANGE)
                .count() == NUMBERS_COUNT;
    }
}

