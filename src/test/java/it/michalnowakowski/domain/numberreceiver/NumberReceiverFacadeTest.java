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

    AdjustableClock clock = new AdjustableClock(LocalDateTime.of(2024, 04, 06, 11, 0, 0)
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
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        InputNumberResultDto expected = new InputNumberResultDto(null, ValidationResult.NOT_SIX_NUMBERS.info);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_fail_when_more_than_six_numbers_in_range_received() {
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);

        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6, 7);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        InputNumberResultDto expected = new InputNumberResultDto(null, ValidationResult.NOT_SIX_NUMBERS.info);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_fail_when_at_least_one_number_is_out_of_range() {
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);

        Set<Integer> numbersFromUser = Set.of(100, 2, 3, 4, 5, 6);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        InputNumberResultDto expected = new InputNumberResultDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_fail_when_at_least_one_number_is_out_of_range_and_negative() {
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);

        Set<Integer> numbersFromUser = Set.of(-1, 2, 3, 4, 5, 6);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        InputNumberResultDto expected = new InputNumberResultDto(null, ValidationResult.NOT_IN_RANGE.info);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    public void should_throw_illegalArgumentException_when_2_is_duplicated() {
        HashGenerable hashGenerator = new HashGenerator();
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
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        LocalDateTime drawDate = LocalDateTime.of(2024, 04, 06, 12, 0, 0);

        List<TicketDto> ticketDtos = numberReceiverFacade.retriveAllTicketsByNextDrawDate(drawDate);

        assertThat(ticketDtos).isEmpty();
    }

    @Test
    public void should_return_empty_list_when_given_date_is_after_next_draw_date() {
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);
        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);
        LocalDateTime drawDate = result.ticketDto().drawDate();

        List<TicketDto> ticketDtos = numberReceiverFacade.retriveAllTicketsByNextDrawDate(drawDate.plusWeeks(1l));

        assertThat(ticketDtos).isEmpty();
    }

    @Test
    public void should_generate_test_hash_when_six_numbers_in_range_received() {
        HashGenerable hashGenerator = new HashGeneratorTestImpl();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        InputNumberResultDto result = numberReceiverFacade.inputNumbers(numbersFromUser);

        assertThat(result.ticketDto().hash()).isEqualTo("hash123");
    }

    @Test
    public void should_return_correct_draw_date() {
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        LocalDateTime testDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        LocalDateTime expectedDrawDate = LocalDateTime.of(2024,04, 06, 12, 0, 0);
        assertThat(testDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_noon() {
        HashGenerable hashGenerator = new HashGenerator();
        clock.plusMinutes(60);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        LocalDateTime testDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        LocalDateTime expectedDrawDate = LocalDateTime.of(2024,04, 13, 12, 0, 0);
        assertThat(testDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_next_Saturday_draw_date_when_date_is_Saturday_afternoon() {
        HashGenerable hashGenerator = new HashGenerator();
        clock.plusMinutes(120);
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        Set<Integer> numbersFromUser = Set.of(1, 2, 3, 4, 5, 6);

        LocalDateTime testDrawDate = numberReceiverFacade.inputNumbers(numbersFromUser).ticketDto().drawDate();

        LocalDateTime expectedDrawDate = LocalDateTime.of(2024,04, 13, 12, 0, 0);
        assertThat(testDrawDate).isEqualTo(expectedDrawDate);
    }

    @Test
    public void should_return_ticket_with_correct_draw_date() {
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);
        Set<Integer> numberFromUser = Set.of(1, 2, 3, 4, 5, 6);
        InputNumberResultDto result1 = numberReceiverFacade.inputNumbers(numberFromUser);
        clock.plusMinutes(59);
        InputNumberResultDto result2 = numberReceiverFacade.inputNumbers(numberFromUser);
        clock.plusDays(1);
        InputNumberResultDto result3 = numberReceiverFacade.inputNumbers(numberFromUser);
        clock.plusDays(1);
        InputNumberResultDto result4 = numberReceiverFacade.inputNumbers(numberFromUser);
        TicketDto ticketDto1 = result1.ticketDto();
        TicketDto ticketDto2 = result2.ticketDto();
        LocalDateTime drawDate = LocalDateTime.of(2024, 04, 06, 12, 0, 0);

        List<TicketDto> ticketsDtos = numberReceiverFacade.retriveAllTicketsByNextDrawDate(drawDate);

        assertThat(ticketsDtos).containsOnly(ticketDto1, ticketDto2);
    }

    @Test
    public void should_retrive_correct_next_draw_date() {
        HashGenerable hashGenerator = new HashGenerator();
        NumberReceiverFacade numberReceiverFacade = new NumberReceiverConfiguration().createForTest(hashGenerator, clock, repository);

        LocalDateTime testDrawDate = numberReceiverFacade.retriveNextDrawDate();

        LocalDateTime expectedDrawDate = LocalDateTime.of(2024,04, 06, 12, 0, 0);
        assertThat(testDrawDate).isEqualTo(expectedDrawDate);
    }
}