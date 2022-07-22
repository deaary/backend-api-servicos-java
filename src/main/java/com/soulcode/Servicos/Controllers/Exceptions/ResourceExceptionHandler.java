package com.soulcode.Servicos.Controllers.Exceptions;

import com.soulcode.Servicos.Services.Exceptions.DataIntegrityViolationException;
import com.soulcode.Servicos.Services.Exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<StandardError> entityNotFound(EntityNotFoundException e, HttpServletRequest request){
        StandardError erro = new StandardError();
        erro.setTimestamp(Instant.now());
        erro.setStatus(HttpStatus.NOT_FOUND.value());
        erro.setError("Registro nao encontrado");
        erro.setMessage(e.getMessage());
        erro.setPath(request.getRequestURI());
        erro.setTrace("EntityNotFoundException");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    public ResponseEntity<StandardError> DataIntegrityViolationException(DataIntegrityViolationException e,
                                                                         HttpServletRequest request){
        StandardError erro = new StandardError();
        erro.setTimestamp(Instant.now());
        erro.setStatus(HttpStatus.CONFLICT.value());
        erro.setError("Atributo nao pode ser duplicado");
        erro.setMessage(e.getMessage());
        erro.setPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(erro);
    }
}
