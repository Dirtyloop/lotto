package it.michalnowakowski.domain.numberreceiver;

import it.michalnowakowski.domain.numberreceiver.dto.InputNumberDto;
import lombok.AllArgsConstructor;

import java.util.Set;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;

    public InputNumberDto inputNumbers(Set<Integer> numbersFromUser) {

        boolean areNumbersValidated = validator.areAllNumbersInRange(numbersFromUser);
        if(!areNumbersValidated) {
            return InputNumberDto.builder().message("Fail").build();
        }
        return InputNumberDto.builder().message("Success").build();
    }
}
