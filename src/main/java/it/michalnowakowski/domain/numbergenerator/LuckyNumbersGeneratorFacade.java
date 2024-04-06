package it.michalnowakowski.domain.numbergenerator;

import it.michalnowakowski.domain.numbergenerator.dto.LuckyNumbersDto;
import it.michalnowakowski.domain.numberreceiver.NumberReceiverFacade;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
public class LuckyNumbersGeneratorFacade {

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

    public LuckyNumbersDto retriveLuckyNumbersByDate(LocalDateTime date) {
        LuckyNumbers numbersByDate = luckyNumbersRepository.findNumbersByDate(date)
                .orElseThrow(() -> new RuntimeException("Lucky Numbers Not Found."));
        return LuckyNumbersDto.builder()
                .luckyNumbers(numbersByDate.luckyNumbers())
                .date(numbersByDate.date())
                .build();

    }
}
