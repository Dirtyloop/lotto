package it.michalnowakowski.domain.numbergenerator.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class LuckyNumbersDto {

    private Set<Integer> luckyNumbers;
    private LocalDateTime date;
}
