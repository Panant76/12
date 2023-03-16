package by.vitstep.organizer.exception;

public class CityNotFoundException extends RuntimeException{

    public CityNotFoundException(String city) {
        super(String.format("Город %s не найден", city));
    }
}
