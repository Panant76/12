package by.vitstep.organizer.exception;

public class TransactionNotFoundException extends RuntimeException {
    public TransactionNotFoundException(Long id) {
        super(String.format("Операция не найдена. Id: %d", id));
    }
}
