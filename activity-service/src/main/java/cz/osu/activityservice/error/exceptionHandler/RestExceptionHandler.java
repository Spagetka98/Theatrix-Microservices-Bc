package cz.osu.activityservice.error.exceptionHandler;

import cz.osu.activityservice.error.exception.EActionNotFoundException;
import cz.osu.activityservice.error.exception.RetryFallException;
import cz.osu.activityservice.error.exception.TheatreActivityActionException;
import cz.osu.activityservice.error.exception.TheatreActivityNotFoundException;
import cz.osu.activityservice.model.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.time.Instant;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public MessageResponse badRequestException(Exception exception, WebRequest request) {
        log.error(String.format("Invoked exception type: BAD_REQUEST, full message: %s ", exception.getMessage()));

        return new MessageResponse(HttpStatus.BAD_REQUEST, exception.getMessage(), Instant.now().toString());
    }

    @ExceptionHandler({RetryFallException.class,TheatreActivityActionException.class, TheatreActivityNotFoundException.class, IllegalStateException.class, ConstraintViolationException.class,
            EActionNotFoundException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public MessageResponse internalServerErrorException(Exception exception, WebRequest request) {
        log.error(String.format("Invoked exception type: BAD_REQUEST, full message: %s ", exception.getMessage()));

        return new MessageResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage(), Instant.now().toString());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    public MessageResponse unauthorizedException(AccessDeniedException exception, WebRequest request) {
        log.error(String.format("Invoked exception type: BAD_REQUEST, full message: %s ", exception.getMessage()));

        return new MessageResponse(HttpStatus.UNAUTHORIZED, exception.getMessage(), Instant.now().toString());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        String errorResult = ex.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        log.error(String.format("Invoked exception type: BAD_REQUEST, full message: %s ", errorResult));

        return new ResponseEntity<>(new MessageResponse(HttpStatus.BAD_REQUEST, errorResult, Instant.now().toString()), HttpStatus.BAD_REQUEST);
    }

}
