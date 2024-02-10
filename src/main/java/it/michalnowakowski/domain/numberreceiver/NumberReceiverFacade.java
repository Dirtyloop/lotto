package it.michalnowakowski.domain.numberreceiver;

import it.michalnowakowski.domain.numberreceiver.dto.InputNumberResultDto;
import it.michalnowakowski.domain.numberreceiver.dto.TicketDto;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
public class NumberReceiverFacade {

    private final NumberValidator validator;
    private final NumberReceiverRepository repository;
    private final DrawDateGenerator drawDateGenerator;
    private final HashGenerable hashGenerator;

    public InputNumberResultDto inputNumbers(Set<Integer> numbersFromUser) {

        boolean areNumbersValidated = validator.areAllNumbersInRange(numbersFromUser);
        if(!areNumbersValidated) {
            return InputNumberResultDto.builder().message("Fail").build();
        }
        String hash = hashGenerator.getHash();
        LocalDateTime drawDate = drawDateGenerator.getNextDrawDate();

        Ticket savedTicket = Ticket.builder()
                .hash(hash)
                .drawDate(drawDate)
                .numbersFromUser(numbersFromUser)
                .build();

        repository.save(savedTicket);

        return InputNumberResultDto.builder()
                .ticketDto(TicketDto.builder()
                        .drawDate(savedTicket.drawDate())
                        .hash(savedTicket.hash())
                        .numbersFromUser(savedTicket.numbersFromUser())
                        .build())
                .message("Success")
                .build();
    }

    public List<TicketDto> retriveAllTicketsByNextDrawDate() {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        return retriveAllTicketsByNextDrawDate(nextDrawDate);
    }

    public List<TicketDto> retriveAllTicketsByNextDrawDate(LocalDateTime date) {
        LocalDateTime nextDrawDate = drawDateGenerator.getNextDrawDate();
        if(date.isAfter(nextDrawDate)) {
            return Collections.emptyList();
        }
        return repository.findAllTicketsByDrawDate(date)
                .stream()
                .filter(ticket -> ticket.drawDate().equals(date))
                .map(TicketMapper::mapFromTicket)
                .toList();
    }

    public LocalDateTime retriveNextDrawDate() {
        return drawDateGenerator.getNextDrawDate();
    }

    public TicketDto findByHash(String hash) {
        Ticket ticket = repository.findByHash(hash);
        return TicketMapper.mapFromTicket(ticket);
    }

//    public List<TicketDto> userNumbers(LocalDateTime date) {
//        List<Ticket> allTicketsByDrawDate = repository.findAllTicketsByDrawDate(date);
//        return allTicketsByDrawDate.stream()
//                .map(TicketMapper::mapFromTicket)
//                .toList();
//    }
}
