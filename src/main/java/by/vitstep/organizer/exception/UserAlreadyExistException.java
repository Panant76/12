package by.vitstep.organizer.exception;

import lombok.experimental.SuperBuilder;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String message){
        super(message);
    }
}
