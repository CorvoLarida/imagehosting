package kz.am.imagehosting.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import kz.am.imagehosting.dto.ErrorDTO;
import kz.am.imagehosting.exceptions.AppException;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(AppException.class)
    @ResponseBody
    public ResponseEntity<ErrorDTO> handleAppException(AppException ex) {
        return ResponseEntity.status(ex.getStatus()).body(new ErrorDTO(ex.getMessage()));
    }

}
