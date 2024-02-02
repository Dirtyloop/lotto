package it.michalnowakowski.domain.numberreceiver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

class NumberReceiverFacadeTest {

    @Test
    public void should_succed_when_six_numbers_in_range_received() {
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();

        String result = numberReceiverFacade.inpitNumbers(Set.of(1, 2, 3, 4, 5, 6));

        Assertions.assertThat("Succes").isEqualTo(result);
    }

    @Test
    public void should_failed_when_five_numbers_in_range_received() {
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade();

        String result = numberReceiverFacade.inpitNumbers(Set.of(1, 2, 3, 4, 5));

        Assertions.assertThat("Fail").isEqualTo(result);
    }

}