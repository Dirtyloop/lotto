package it.michalnowakowski.domain.numberreceiver;

import it.michalnowakowski.domain.numberreceiver.dto.InputNumberResultDto;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumberReceiverFacadeTest {

    NumberReceiverFacade numberReceiverFacade = new NumberReceiverFacade(new NumberValidator());

    @Test
    public void should_succed_when_six_numbers_in_range_received() {
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        assertThat(result.message()).isEqualTo("Success");
    }

    @Test
    public void should_failed_when_less_than_six_numbers_in_range_received() {
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        assertThat(result.message()).isEqualTo("Fail");
    }

    @Test
    public void should_failed_when_more_than_six_numbers_in_range_received() {
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        assertThat(result.message()).isEqualTo("Fail");
    }

    @Test
    public void should_failed_when_at_least_one_number_is_out_of_range() {
        Set<Integer> numbersFromUser = Set.of(100, 2, 3, 4, 5, 6);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        assertThat(result.message()).isEqualTo("Fail");
    }

    @Test
    public void should_throw_illegalArgumentException_when_2_is_duplicated() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> numberReceiverFacade.inputNumbers(Set.of(2, 2, 3, 4, 5, 6)));

        String actualMessage = exception.getMessage();
        String expectedMessage = "duplicate element: 2";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}