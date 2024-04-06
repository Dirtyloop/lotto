package it.michalnowakowski.domain.numbergenerator;

import it.michalnowakowski.domain.numberreceiver.NumberReceiverFacade;

public class NumbersGeneratorConfiguration {

    LuckyNumbersGeneratorFacade createForTest(NumberReceiverFacade numberReceiverFacade, RandomNumbersGenerable generator, LuckyNumbersRepository luckyNumbersRepository) {
        LuckyNumbersValidator luckyNumbersValidator = new LuckyNumbersValidator();
        return new LuckyNumbersGeneratorFacade(numberReceiverFacade, generator, luckyNumbersValidator, luckyNumbersRepository);
    }
}
