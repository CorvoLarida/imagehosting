package kz.am.imagehosting.controllers.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiErrorHandlerController extends ResponseEntityExceptionHandler {
    
    @ExceptionHandler({
        UsernameNotFoundException.class,
        UserAlreadyRegisteredException.class,
    })
    public ProblemDetail handleResourceNotFound(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        return problemDetail;
    }

    @ExceptionHandler({
        UnauthorizedAccessException.class,
    })
    public ProblemDetail handleUnauthorized(Exception ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return problemDetail;
    }
    
}
