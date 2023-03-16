package by.vitstep.organizer.exception;

public class FriendGroupNotFoundException extends RuntimeException{

    public FriendGroupNotFoundException(Long id) {
        super(String.format("Группа %d не найдена", id));
    }
}
