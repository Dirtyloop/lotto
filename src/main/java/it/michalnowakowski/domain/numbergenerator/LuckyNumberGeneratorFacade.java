package it.michalnowakowski.domain.numbergenerator;

import it.michalnowakowski.domain.numbergenerator.dto.LuckyNumbersDto;
import it.michalnowakowski.domain.numberreceiver.NumberReceiverFacade;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class LuckyNumberGeneratorFacade {

    private final NumberReceiverFacade numberReceiverFacade;
    private final RandomNumbersGenerable luckyNumberGenerator;
    private final LuckyNumbersValidator luckyNumbersValidator;
    private final LuckyNumbersRepository luckyNumbersRepository;

    public LuckyNumbersDto generateLuckyNumbers() {
        LocalDateTime nextDrawDate = numberReceiverFacade.retriveNextDrawDate();
        Set<Integer> luckyNumbers = luckyNumberGenerator.generateRandomNumbers();
        luckyNumbersValidator.validate(luckyNumbers);
        luckyNumbersRepository.save(LuckyNumbers.builder()
                .luckyNumbers(luckyNumbers)
                .date(nextDrawDate)
                .build());
        return LuckyNumbersDto.builder()
                .luckyNumbers(luckyNumbers)
                .build();
    }
}