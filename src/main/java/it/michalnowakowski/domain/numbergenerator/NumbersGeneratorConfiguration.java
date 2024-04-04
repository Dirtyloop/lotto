package it.michalnowakowski.domain.numbergenerator;

import it.michalnowakowski.domain.numberreceiver.NumberReceiverFacade;

public class NumbersGeneratorConfiguration {

    LuckyNumberGeneratorFacade createForTest(NumberReceiverFacade numberReceiverFacade, RandomNumbersGenerable generator, LuckyNumbersRepository luckyNumbersRepository) {
        LuckyNumbersValidator luckyNumbersValidator = new LuckyNumbersValidator();
        return new LuckyNumberGeneratorFacade(numberReceiverFacade, generator, luckyNumbersValidator, luckyNumbersRepository);
    }
}
