package it.michalnowakowski.domain.numbergenerator;

import java.util.Set;

public class LuckyNumbersGeneratorTestImpl implements RandomNumbersGenerable {

    private final Set<Integer> generatedNumbers;
    LuckyNumbersGeneratorTestImpl(Set<Integer> generatedNumbers) {
        this.generatedNumbers = generatedNumbers;
    }

    LuckyNumbersGeneratorTestImpl() {
        generatedNumbers = Set.of(1, 2, 3, 4, 5, 6);
    }

    @Override
    public Set<Integer> generateRandomNumbers() {
        return generatedNumbers;
    }
}
