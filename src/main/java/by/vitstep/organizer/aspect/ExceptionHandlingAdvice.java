package by.vitstep.organizer.aspect;

import by.vitstep.organizer.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "by.vitstep.organizer.web")
public class ExceptionHandlingAdvice {
    @ExceptionHandler(value = {UserNotFoundException.class, AccountNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public CommonException handleNotFound(Exception ex){
        return CommonException
                .builder()
                .code(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
    }
    @ExceptionHandler(value={UserAlreadyExistException.class, AccountAlreadyExistException.class, TransactionException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonException handleBadRequest(Throwable ex){
        return CommonException
                .builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

    }
}
