package it.michalnowakowski.domain.numberreceiver;

enum ValidationResult {
    NOT_SIX_NUMBERS("YOU SHOULD INPUT 6 NUMBERS"),
    NOT_IN_RANGE("YOU SHOULD INPUT NUMBERS IN RANGE 1-99"),
    INPUT_SUCCESS("SUCCESS");

    final String info;

    ValidationResult(String info) {
        this.info = info;
    }
}
