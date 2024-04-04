package it.michalnowakowski.domain.numbergenerator;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
record LuckyNumbers(String id, Set<Integer> luckyNumbers, LocalDateTime date) {
}
