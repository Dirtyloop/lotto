package it.michalnowakowski.domain.numbergenerator;

import java.time.LocalDateTime;
import java.util.Optional;

public interface LuckyNumbersRepository {

    Optional<LuckyNumbers> findNumbersByDate(LocalDateTime date);
    boolean existsByDate(LocalDateTime nextDrawDate);
    LuckyNumbers save(LuckyNumbers luckyNumbers);
}
