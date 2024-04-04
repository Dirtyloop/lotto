package it.michalnowakowski.domain.numbergenerator;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

class RandomGenerator implements RandomNumbersGenerable {

    private final int MIN_RANGE = 1;
    private final int MAX_RANGE = 99;
    private final int RANDOM_NUMBER_RANGE = (MAX_RANGE - MIN_RANGE) +1;

    @Override
    public Set<Integer> generateRandomNumbers() {
        Set<Integer> luckyNumbers = new HashSet<>();
        while (isAmountOfNumbersLowerThanSix(luckyNumbers)) {
            int randomNumber = generateRandom();
            luckyNumbers.add(randomNumber);
        }
        return luckyNumbers;
    }

    private int generateRandom() {
        Random random = new SecureRandom();
        return random.nextInt(RANDOM_NUMBER_RANGE);
    }

    private boolean isAmountOfNumbersLowerThanSix(Set<Integer> luckyNumbers) {
        return luckyNumbers.size() < 6;
    }


}
