package it.michalnowakowski.domain.numberreceiver;

import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;

    public String inputNumbers(Set<Integer> numbersFromUser) {
        if(!validator.areAllNumbersInRange(numbersFromUser)) {
            return "Fail";
        }
        return "Success";
    }
}
