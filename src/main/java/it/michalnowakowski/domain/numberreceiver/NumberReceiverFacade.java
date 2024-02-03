package it.michalnowakowski.domain.numberreceiver;

import it.michalnowakowski.domain.numberreceiver.dto.InputNumberResultDto;
import it.michalnowakowski.domain.numberreceiver.dto.TicketDto;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;
    private NumberReceiverRepository repository;

    public InputNumberResultDto inputNumbers(Set<Integer> numbersFromUser) {

        boolean areNumbersValidated = validator.areAllNumbersInRange(numbersFromUser);
        if(!areNumbersValidated) {
            return InputNumberResultDto.builder().message("Fail").build();
        }
        String ticketId = UUID.randomUUID().toString();
        LocalDateTime drawDate = LocalDateTime.now();
        Ticket savedTicket = repository.save(new Ticket(ticketId, drawDate, numbersFromUser));
        return InputNumberResultDto.builder()
                .drawDate(savedTicket.drawDate())
                .ticketId(savedTicket.ticketId())
                .message("Success")
                .build();
    }

    public List<TicketDto> userNumbers(LocalDateTime date) {
        return List.of(
                TicketDto.builder()
                        .ticketId("1")
                        .drawDate(LocalDateTime.now())
                        .numbersFromUser(Set.of(1,2,3,4,5,6))
                        .build()
        );
    }
}
