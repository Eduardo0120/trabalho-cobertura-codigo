package br.com.avaliacao.backend.service;

public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }
}
