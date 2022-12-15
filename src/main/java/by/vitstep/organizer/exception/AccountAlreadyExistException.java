package by.vitstep.organizer.exception;

public class AccountAlreadyExistException extends RuntimeException{
    public AccountAlreadyExistException(String name){
        super(String.format("Аккаунт с именем %s уже существует",name));
    }
}
