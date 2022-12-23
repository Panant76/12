package by.vitstep.organizer.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super(String.format("Пользователь с id = %d не найден",id));
    }
    public UserNotFoundException(String msg){
        super(msg);
    }
}
