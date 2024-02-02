package it.michalnowakowski.domain.numberreceiver;

import java.util.Set;

class NumberValidator {

    boolean areAllNumbersInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .filter(num -> num >= 1)
                .filter(num -> num <= 99)
                .count() == 6;
    }
}
