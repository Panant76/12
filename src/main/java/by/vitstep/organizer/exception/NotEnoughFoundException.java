package by.vitstep.organizer.exception;

public class NotEnoughFoundException extends RuntimeException {
    public NotEnoughFoundException(String accountName) {
        super(String.format("Не достаточно средств на счете %s", accountName));
    }
}
