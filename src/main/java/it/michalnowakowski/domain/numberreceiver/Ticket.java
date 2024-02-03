package it.michalnowakowski.domain.numberreceiver;

import java.time.LocalDateTime;
import java.util.Set;

public record Ticket(String ticketId, LocalDateTime drawDate, Set<Integer> numbersFromUser) {
}
