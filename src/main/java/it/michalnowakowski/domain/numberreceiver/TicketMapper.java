package it.michalnowakowski.domain.numberreceiver;

import it.michalnowakowski.domain.numberreceiver.dto.TicketDto;

public class TicketMapper {
    public static TicketDto mapFromTicket(Ticket ticket) {
        return TicketDto.builder()
                .ticketId(ticket.ticketId())
                .drawDate(ticket.drawDate())
                .numbersFromUser(ticket.numbersFromUser())
                .build();
    }
}
