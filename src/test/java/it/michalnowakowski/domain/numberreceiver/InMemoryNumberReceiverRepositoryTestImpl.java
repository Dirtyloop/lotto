package it.michalnowakowski.domain.numberreceiver;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryNumberReceiverRepositoryTestImpl implements NumberReceiverRepository {

    Map<String, Ticket> inMemoryDatabase = new ConcurrentHashMap<>();

    @Override
    public Ticket save(Ticket ticket) {

        return inMemoryDatabase.put(ticket.ticketId(), ticket);
    }
}
