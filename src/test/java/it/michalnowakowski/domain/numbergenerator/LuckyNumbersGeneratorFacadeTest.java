package it.michalnowakowski.domain.numbergenerator;

import java.util.Set;
import it.michalnowakowski.domain.numbergenerator.dto.LuckyNumbersDto;
import it.michalnowakowski.domain.numberreceiver.NumberReceiverFacade;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LuckyNumbersGeneratorFacadeTest {

    private final LuckyNumbersRepository luckyNumbersRepository = new LuckyNumbersRepositoryTestImpl();
    NumberReceiverFacade numberReceiverFacade = mock(NumberReceiverFacade.class);

    @Test
    public void should_return_set_of_6_numbers() {
        RandomNumbersGenerable generator = new RandomGenerator();
        when(numberReceiverFacade.retriveNextDrawDate()).thenReturn(LocalDateTime.now());
        LuckyNumberGeneratorFacade numbersGenerator = new NumbersGeneratorConfiguration().createForTest(numberReceiverFacade, generator, luckyNumbersRepository);

        LuckyNumbersDto generatedNumbers = numbersGenerator.generateLuckyNumbers();

        assertThat(generatedNumbers.getLuckyNumbers().size()).isEqualTo(6);
    }

    @Test
    public void should_return_set_of_numbers_in_required_range() {
        RandomNumbersGenerable generator = new RandomGenerator();
        when(numberReceiverFacade.retriveNextDrawDate()).thenReturn(LocalDateTime.now());
        LuckyNumberGeneratorFacade numbersGenerator = new NumbersGeneratorConfiguration().createForTest(numberReceiverFacade, generator, luckyNumbersRepository);

        LuckyNumbersDto generatedNumbers = numbersGenerator.generateLuckyNumbers();

        int min_range = 1;
        int max_range = 99;
        Set<Integer> luckyNmbers = generatedNumbers.getLuckyNumbers();
        boolean numbersInRange = luckyNmbers.stream().allMatch(number -> number >= min_range && number <= max_range);
        assertThat(numbersInRange).isTrue();
    }

}