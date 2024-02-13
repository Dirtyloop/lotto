package it.michalnowakowski.domain.numberreceiver.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record TicketDto(LocalDateTime drawDate, String hash, Set<Integer> numbersFromUser) {
}
