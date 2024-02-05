package it.michalnowakowski.domain.numberreceiver;

import it.michalnowakowski.domain.numberreceiver.dto.InputNumberResultDto;
import it.michalnowakowski.domain.numberreceiver.dto.TicketDto;
import lombok.AllArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;
    private final NumberReceiverRepository repository;
    private final HashGenerable hashGenerator;
    private final Clock clock;

    public InputNumberResultDto inputNumbers(Set<Integer> numbersFromUser) {

        boolean areNumbersValidated = validator.areAllNumbersInRange(numbersFromUser);
        if(!areNumbersValidated) {
            return InputNumberResultDto.builder().message("Fail").build();
        }
        String hash = hashGenerator.getHash();
        LocalDateTime drawDate = LocalDateTime.now(clock);
        Ticket savedTicket = repository.save(new Ticket(hash, drawDate, numbersFromUser));
        return InputNumberResultDto.builder()
                .drawDate(savedTicket.drawDate())
                .hash(savedTicket.hash())
                .numbersFromUser(savedTicket.numbersFromUser())
                .message("Success")
                .build();
    }

    public List<TicketDto> userNumbers(LocalDateTime date) {
        List<Ticket> allTicketsByDrawDate = repository.findAllTicketsByDrawDate(date);
        return allTicketsByDrawDate.stream()
                .map(TicketMapper::mapFromTicket)
                .toList();
    }
}
