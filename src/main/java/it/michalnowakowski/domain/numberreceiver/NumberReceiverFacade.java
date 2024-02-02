package it.michalnowakowski.domain.numberreceiver;

import java.util.Set;

public class NumberReceiverFacade {

    public String inputNumbers(Set<Integer> numbersFromUser) {
        if(!(filterNumbersInRangeAndCount(numbersFromUser) == 6)) {
            return "Fail";
        }
        return "Success";
    }

    private long filterNumbersInRangeAndCount(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .filter(num -> num >= 1)
                .filter(num -> num <= 99)
                .count();
    }

}
