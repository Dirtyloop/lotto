package it.michalnowakowski.domain.numberreceiver;

import java.time.Clock;

public class NumberReceiverConfiguration {

    NumberReceiverFacade createForTest(HashGenerable hashGenerator, Clock clock, NumberReceiverRepository repository) {
        NumberValidator numberValidator = new NumberValidator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        return new NumberReceiverFacade(numberValidator, repository, drawDateGenerator, hashGenerator);
    }

}
