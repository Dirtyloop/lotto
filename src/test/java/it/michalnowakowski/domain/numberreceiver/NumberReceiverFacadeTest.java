package it.michalnowakowski.domain.numberreceiver;

import it.michalnowakowski.domain.AdjustableClock;
import it.michalnowakowski.domain.numberreceiver.dto.InputNumberResultDto;
import it.michalnowakowski.domain.numberreceiver.dto.TicketDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NumberReceiverFacadeTest {

    AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2024, 02, 05, 17, 20, 0)
            .toInstant(ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now())),
            ZoneId.systemDefault());

    InMemoryNumberReceiverRepositoryTestImpl repository = new InMemoryNumberReceiverRepositoryTestImpl();


    @Test
    public void should_succed_when_six_numbers_in_range_received() {
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();

        TicketDto generatedTicket = TicketDto.builder()
                .hash(hashGenerator.getHash())
                .numbersFromUser(numbersFromUser)
                .drawDate(nextDrawDate)
                .build();

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        InputNumberResultDto expected = new InputNumberResultDto(generatedTicket, ValidationResult.INPUT_SUCCESS.info);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_fail_when_less_than_six_numbers_in_range_received() {
        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        InputNumberResultDto expected = new InputNumberResultDto(null, ValidationResult.NOT_SIX_NUMBERS.info);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_fail_when_more_than_six_numbers_in_range_received() {
        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        InputNumberResultDto expected = new InputNumberResultDto(null, ValidationResult.NOT_SIX_NUMBERS.info);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_fail_when_at_least_one_number_is_out_of_range() {
        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);

        Set<Integer> numbersFromUser = Set.of(100, 2, 3, 4, 5, 6);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        InputNumberResultDto expected = new InputNumberResultDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_fail_when_at_least_one_number_is_out_of_range_and_negative() {
        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);

        Set<Integer> numbersFromUser = Set.of(-1, 2, 3, 4, 5, 6);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        InputNumberResultDto expected = new InputNumberResultDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_throw_illegalArgumentException_when_2_is_duplicated() {
        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> numberReceiverFacade.inputNumbers(Set.of(2, 2, 3, 4, 5, 6)));

        String actualMessage = exception.getMessage();
        String expectedMessage = "duplicate element: 2";

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void should_save_to_database_when_six_numbers_in_range_received() {
        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
//        LocalDateTime drawDate = LocalDateTime.of(2024, 02, 10, 17, 20, 0);
        LocalDateTime drawDate = drawDateGenerator.getNextDrawDate();
        List<TicketDto> ticketDtos = numberReceiverFacade.retriveAllTicketsByNextDrawDate(drawDate);

        assertThat(ticketDtos).contains(
                TicketDto.builder()
                        .hash(result.ticketDto().hash())
                        .drawDate(result.ticketDto().drawDate())
                        .numbersFromUser(result.ticketDto().numbersFromUser())
                        .build()
        );
    }

    @Test
    public void should_return_empty_list_when_database_is_empty() {
        HashGenerable hashGenerator = new HashGenerator();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);

        LocalDateTime drawDate = LocalDateTime.of(2024, 02, 05, 17, 20, 0);

        List<TicketDto> ticketDtos = numberReceiverFacade.retriveAllTicketsByNextDrawDate(drawDate);

        assertThat(ticketDtos).isEmpty();
    }

    @Test
    public void should_generate_test_hash_when_six_numbers_in_range_received() {
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        DrawDateGenerator drawDateGenerator = new DrawDateGenerator(clock);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        assertThat(result.ticketDto().hash()).isEqualTo("hash123");
    }
}