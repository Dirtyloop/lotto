package it.michalnowakowski.domain.numberreceiver;

import java.util.Set;

public class NumberReceiverFacade {

    public String inpitNumbers(Set<Integer> numbers) {
        if(numbers.size() != 6) {
            return "Fail";
        }
        return "Succes";
    }

}
