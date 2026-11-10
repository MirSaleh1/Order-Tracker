package com.example.ordertracker.exception;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.core.PropertyReferenceException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice(basePackages = "com.example.ordertracker.controller")
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ExceptionResponse handleBusinessException(BusinessException ex, HttpServletResponse response) {
        response.setStatus(ex.getExceptionConstants().getStatus().value());
        return new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ExceptionResponse handleValidationException(MethodArgumentNotValidException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return new ExceptionResponse(errors);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ExceptionResponse handleBadCredentialsException(BadCredentialsException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return new ExceptionResponse("Email və ya şifrə yanlışdır.");
    }

    @ExceptionHandler(PropertyReferenceException.class)
    public ExceptionResponse handlePropertyReferenceException(PropertyReferenceException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return new ExceptionResponse("Mövcud olmayan çeşidləmə sütunu: " + ex.getPropertyName());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ExceptionResponse handleAccessDeniedException(AccessDeniedException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return new ExceptionResponse("Sizin bu əməliyyatı yerinə yetirmək üçün icazəniz yoxdur! (Access Denied)");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ExceptionResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return new ExceptionResponse("Məlumat bazası əlaqə xətası (Foreign Key və ya təkrarlanan məlumat).");
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ExceptionResponse handleHttpMessageNotReadableException(HttpMessageNotReadableException ex, HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return new ExceptionResponse("Sorğu mətni (JSON formatı) yanlışdır və ya oxuna bilmir.");
    }

    @ExceptionHandler(Exception.class)
    public ExceptionResponse handleGeneralException(Exception ex, HttpServletResponse response) {
        ex.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        return new ExceptionResponse("Daxili server xətası baş verdi: " + ex.getMessage());
    }
}
