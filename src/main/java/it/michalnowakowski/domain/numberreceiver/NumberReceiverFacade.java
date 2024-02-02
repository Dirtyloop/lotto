package it.michalnowakowski.domain.numberreceiver;

import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;

    public String inputNumbers(Set<Integer> numbersFromUser) {

        boolean areNumbersValidated = validator.areAllNumbersInRange(numbersFromUser);
        if(!areNumbersValidated) {
            return "Fail";
        }
        return "Success";
    }
}
