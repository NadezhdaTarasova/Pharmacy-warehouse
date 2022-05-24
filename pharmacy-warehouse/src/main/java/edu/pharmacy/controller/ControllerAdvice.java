package edu.pharmacy.controller;

import edu.pharmacy.exception.BusinessLogicException;
import edu.pharmacy.model.dto.ErrorDTO;
import org.springframework.boot.json.JsonParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestControllerAdvice
public class ControllerAdvice {

    @ExceptionHandler(BusinessLogicException.class)
    public ErrorDTO conflictBetweenData(BusinessLogicException businessLogicException) {
        ResponseEntity.badRequest();
        return new ErrorDTO(businessLogicException.getMessage());
    }

    @ExceptionHandler(JsonParseException.class)
    public ErrorDTO jsonParseException() {
        ResponseEntity.badRequest();
        return new ErrorDTO("Invalid json request");
    }
}
