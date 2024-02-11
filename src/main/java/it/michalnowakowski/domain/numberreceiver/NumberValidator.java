package it.michalnowakowski.domain.numberreceiver;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class NumberValidator {

    public static final int NUMBERS_COUNT = 6;
    public static final int MIN_RANGE = 1;
    public static final int MAX_RANGE = 99;

    List<ValidationResult> errors;

    List<ValidationResult> validate(Set<Integer> numbersFromUser) {
        errors = new LinkedList<>();
        if(!isNumbersSizeEqualSix(numbersFromUser)) {
            errors.add(ValidationResult.NOT_SIX_NUMBERS);
        }
        if(!areNumbersInRange(numbersFromUser)) {
            errors.add(ValidationResult.NOT_IN_RANGE);
        }
        return errors;
    }

    String createResultMessage() {
        return errors.stream()
                .map(validationResult -> validationResult.info)
                .collect(Collectors.joining(", "));
    }

    private boolean isNumbersSizeEqualSix(Set<Integer> numbersFromUser) {
        return numbersFromUser.size() == NUMBERS_COUNT;
    }

    private boolean areNumbersInRange(Set<Integer> numbersFromUser) {
        return numbersFromUser.stream()
                .allMatch(number -> number >= MIN_RANGE && number <= MAX_RANGE);
    }
}

