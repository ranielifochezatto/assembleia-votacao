package com.example.assembleiavotacao.exceptions;

import com.example.assembleiavotacao.components.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class CommonsExceptionHandler extends ResponseEntityExceptionHandler {

    public static final int CODIGO_HTTP_BUSINESS_EXCEPTION = 460;

    @Autowired
    private Messages messages;

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(RuntimeException ex, WebRequest request) {
        ex.printStackTrace();
        Erro erro = new Erro();
        erro.getMensagens().add(messages.get("erro.ocorreu-erro"));
        return this.handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({ EmptyResultDataAccessException.class })
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        Erro erro = new Erro();
        erro.getMensagens().add(messages.get("recurso.nao-encontrado"));
        return handleExceptionInternal(ex, erro, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ BusinessException.class })
    public ResponseEntity<Object> handleBusinessException(BusinessException ex, WebRequest request) {
        Erro erro = new Erro();
        erro.getMensagens().add(ex.getMessage());
        return ResponseEntity.status(CODIGO_HTTP_BUSINESS_EXCEPTION).body(erro);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        Erro erro = new Erro();
        erro.getMensagens().add(messages.get("mensagem.invalida"));
        return this.handleExceptionInternal(ex, erro, headers, HttpStatus.BAD_REQUEST, request);
    }

    public static class Erro {
        private List<String> mensagens;

        public List<String> getMensagens() {
            if (this.mensagens == null) {
                this.mensagens = new ArrayList<>();
            }

            return this.mensagens;
        }

        public void setMensagens(List<String> mensagens) {
            this.mensagens = mensagens;
        }
    }

}
