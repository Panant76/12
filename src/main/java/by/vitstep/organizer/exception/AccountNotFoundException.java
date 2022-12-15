package by.vitstep.organizer.exception;

public class AccountNotFoundException extends RuntimeException{
    public AccountNotFoundException(Long id){
        super(String.format("Счет с id = %d не найден",id));
    }
}
