package it.michalnowakowski.domain.numbergenerator;

import java.util.HashSet;
import java.util.Set;
import it.michalnowakowski.domain.numbergenerator.dto.LuckyNumbersDto;
import it.michalnowakowski.domain.numberreceiver.NumberReceiverFacade;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LuckyNumbersGeneratorFacadeTest {

    private final LuckyNumbersRepository luckyNumbersRepository = new LuckyNumbersRepositoryTestImpl();
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    @Test
    public void should_return_set_of_6_numbers() {
        RandomNumbersGenerable generator = new RandomGenerator();
        when(numberReceiverFacade.retriveNextDrawDate()).thenReturn(LocalDateTime.now());
        LuckyNumbersGeneratorFacade numbersGenerator = new NumbersGeneratorConfiguration().createForTest(numberReceiverFacade, generator, luckyNumbersRepository);

        LuckyNumbersDto generatedNumbers = numbersGenerator.generateLuckyNumbers();

        assertThat(generatedNumbers.getLuckyNumbers().size()).isEqualTo(6);
    }

    @Test
    public void should_return_set_of_numbers_in_required_range() {
        RandomNumbersGenerable generator = new RandomGenerator();
        when(numberReceiverFacade.retriveNextDrawDate()).thenReturn(LocalDateTime.now());
        LuckyNumbersGeneratorFacade numbersGenerator = new NumbersGeneratorConfiguration().createForTest(numberReceiverFacade, generator, luckyNumbersRepository);

        LuckyNumbersDto generatedNumbers = numbersGenerator.generateLuckyNumbers();

        int min_range = 1;
        int max_range = 99;
        Set<Integer> luckyNmbers = generatedNumbers.getLuckyNumbers();
        boolean numbersInRange = luckyNmbers.stream().allMatch(number -> number >= min_range && number <= max_range);
        assertThat(numbersInRange).isTrue();
    }

    @Test
    public void should_throw_an_exception_when_number_is_out_of_range() {
        Set<Integer> numbersOutOfRange = Set.of(0, 1, 2, 3, 4, 5);
        RandomNumbersGenerable generator = new LuckyNumbersGeneratorTestImpl(numbersOutOfRange);
        when(numberReceiverFacade.retriveNextDrawDate()).thenReturn(LocalDateTime.now());
        LuckyNumbersGeneratorFacade numbersGenerator = new NumbersGeneratorConfiguration().createForTest(numberReceiverFacade, generator, luckyNumbersRepository);

        assertThrows(IllegalStateException.class, numbersGenerator::generateLuckyNumbers, "One or more numbers is out of range.");
    }

    @Test
    public void should_return_unique_numbers() {
        RandomNumbersGenerable generator = new RandomGenerator();
        when(numberReceiverFacade.retriveNextDrawDate()).thenReturn(LocalDateTime.now());
        LuckyNumbersGeneratorFacade numbersGenerator = new NumbersGeneratorConfiguration().createForTest(numberReceiverFacade, generator, luckyNumbersRepository);

        LuckyNumbersDto generatedNumbers = numbersGenerator.generateLuckyNumbers();

        int generatedNumbersSize = new HashSet<>(generatedNumbers.getLuckyNumbers()).size();
        assertThat(generatedNumbersSize).isEqualTo(6);
    }

    @Test
    public void should_return_lucky_numbers_by_given_date() {
        LocalDateTime drawDate = LocalDateTime.of(2024, 4, 6, 12, 0, 0);
        Set<Integer> generatedLuckyNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        LuckyNumbers luckyNumbers = LuckyNumbers.builder()
                .id(id)
                .date(drawDate)
                .luckyNumbers(generatedLuckyNumbers)
                .build();
        luckyNumbersRepository.save(luckyNumbers);
        RandomNumbersGenerable generator = new LuckyNumbersGeneratorTestImpl();
        when(numberReceiverFacade.retriveNextDrawDate()).thenReturn(drawDate);
        LuckyNumbersGeneratorFacade numberGenerator = new NumbersGeneratorConfiguration().createForTest(numberReceiverFacade, generator, luckyNumbersRepository);

        LuckyNumbersDto luckyNumbersDto = numberGenerator.retriveLuckyNumbersByDate(drawDate);

        LuckyNumbersDto expectedLuckyNumbersDto = LuckyNumbersDto.builder()
                .date(drawDate)
                .luckyNumbers(generatedLuckyNumbers)
                .build();
        assertThat(expectedLuckyNumbersDto).isEqualTo(luckyNumbersDto);
    }

    @Test
    public void should_throw_an_exception_when_can_not_retrive_numbers_by_date() {
        LocalDateTime drawDate = LocalDateTime.of(2024, 4, 6, 12, 0, 0);
        RandomNumbersGenerable generator = new LuckyNumbersGeneratorTestImpl();
        when(numberReceiverFacade.retriveNextDrawDate()).thenReturn(drawDate);
        LuckyNumbersGeneratorFacade luckyNumbersGeneratorFacade = new NumbersGeneratorConfiguration().createForTest(numberReceiverFacade, generator, luckyNumbersRepository);

        assertThrows(RuntimeException.class, () -> luckyNumbersGeneratorFacade.retriveLuckyNumbersByDate(drawDate), "Lucky Numbers Not Found.");
    }

    @Test
    public void should_return_true_if_numbers_are_generated_by_given_date() {
        LocalDateTime drawDate = LocalDateTime.of(2024, 4, 6, 12, 0, 0);
        Set<Integer> generatedLuckyNumbers = Set.of(1, 2, 3, 4, 5, 6);
        String id = UUID.randomUUID().toString();
        LuckyNumbers luckyNumbers = LuckyNumbers.builder()
                .id(id)
                .date(drawDate)
                .luckyNumbers(generatedLuckyNumbers)
                .build();
        luckyNumbersRepository.save(luckyNumbers);
        RandomNumbersGenerable generator = new LuckyNumbersGeneratorTestImpl();
        when(numberReceiverFacade.retriveNextDrawDate()).thenReturn(drawDate);
        LuckyNumbersGeneratorFacade numberGenerator = new NumbersGeneratorConfiguration().createForTest(numberReceiverFacade, generator, luckyNumbersRepository);

        boolean areLuckyNumbersGeneratedByDate = numberGenerator.areWinningNumbersGeneratedByDate();

        assertTrue(areLuckyNumbersGeneratedByDate);
    }
}