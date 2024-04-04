package it.michalnowakowski.domain.numbergenerator;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class LuckyNumbersRepositoryTestImpl implements LuckyNumbersRepository {

    private final Map<LocalDateTime, LuckyNumbers> luckyNumbersMap = new ConcurrentHashMap<>();
    @Override
    public Optional<LuckyNumbers> findNumbersByDate(LocalDateTime date) {
        return Optional.empty();
    }

    @Override
    public boolean existsByDate(LocalDateTime nextDrawDate) {
        return false;
    }

    @Override
    public LuckyNumbers save(LuckyNumbers luckyNumbers) {
        return null;
    }
}
