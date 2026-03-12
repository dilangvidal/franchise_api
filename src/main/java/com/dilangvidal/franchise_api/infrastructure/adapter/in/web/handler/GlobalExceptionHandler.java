package com.dilangvidal.franchise_api.infrastructure.adapter.in.web.handler;

import com.dilangvidal.franchise_api.domain.exception.BranchNotFoundException;
import com.dilangvidal.franchise_api.domain.exception.DuplicateNameException;
import com.dilangvidal.franchise_api.domain.exception.FranchiseNotFoundException;
import com.dilangvidal.franchise_api.domain.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FranchiseNotFoundException.class)
    public Mono<ProblemDetail> handleFranchiseNotFound(FranchiseNotFoundException ex) {
        return Mono.just(buildProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(BranchNotFoundException.class)
    public Mono<ProblemDetail> handleBranchNotFound(BranchNotFoundException ex) {
        return Mono.just(buildProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public Mono<ProblemDetail> handleProductNotFound(ProductNotFoundException ex) {
        return Mono.just(buildProblemDetail(HttpStatus.NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler(DuplicateNameException.class)
    public Mono<ProblemDetail> handleDuplicateName(DuplicateNameException ex) {
        return Mono.just(buildProblemDetail(HttpStatus.CONFLICT, ex.getMessage()));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ProblemDetail> handleValidationErrors(WebExchangeBindException ex) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .reduce("", (a, b) -> a.isEmpty() ? b : a + " | " + b);
        return Mono.just(buildProblemDetail(HttpStatus.BAD_REQUEST, errors));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ProblemDetail> handleGeneral(Exception ex) {
        return Mono.just(buildProblemDetail(
                HttpStatus.INTERNAL_SERVER_ERROR, "Se produjo un error inesperado"));
    }

    private ProblemDetail buildProblemDetail(HttpStatus status, String detail) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
        problem.setType(URI.create("about:blank"));
        problem.setProperty("timestamp", Instant.now().toString());
        return problem;
    }
}