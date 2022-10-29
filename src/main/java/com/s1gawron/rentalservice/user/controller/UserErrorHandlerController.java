package com.s1gawron.rentalservice.user.controller;

import com.s1gawron.rentalservice.address.exception.AddressRegisterEmptyPropertyException;
import com.s1gawron.rentalservice.shared.ErrorResponse;
import com.s1gawron.rentalservice.user.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

public abstract class UserErrorHandlerController {

    @ExceptionHandler(UserEmailExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse userEmailExistsExceptionHandler(final UserEmailExistsException userEmailExistsException,
        final HttpServletRequest httpServletRequest) {
        return new ErrorResponse(Instant.now().toString(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
            userEmailExistsException.getMessage(), httpServletRequest.getRequestURI());
    }

    @ExceptionHandler(UserNameExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse userNameExistsExceptionHandler(final UserNameExistsException userNameExistsException,
        final HttpServletRequest httpServletRequest) {
        return new ErrorResponse(Instant.now().toString(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase(),
            userNameExistsException.getMessage(), httpServletRequest.getRequestURI());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse userNotFoundExceptionHandler(final UserNotFoundException userNotFoundException,
        final HttpServletRequest httpServletRequest) {
        return new ErrorResponse(Instant.now().toString(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase(),
            userNotFoundException.getMessage(), httpServletRequest.getRequestURI());
    }

    @ExceptionHandler(UserRegisterEmptyPropertiesException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse userEmptyRegisterPropertiesExceptionHandler(final UserRegisterEmptyPropertiesException userRegisterEmptyPropertiesException,
        final HttpServletRequest httpServletRequest) {
        return new ErrorResponse(Instant.now().toString(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
            userRegisterEmptyPropertiesException.getMessage(), httpServletRequest.getRequestURI());
    }

    @ExceptionHandler(UserEmailPatternViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse userEmailPatternViolationExceptionHandler(final UserEmailPatternViolationException userEmailPatternViolationException,
        final HttpServletRequest httpServletRequest) {
        return new ErrorResponse(Instant.now().toString(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
            userEmailPatternViolationException.getMessage(), httpServletRequest.getRequestURI());
    }

    @ExceptionHandler(UserPasswordTooWeakException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse userPasswordTooWeakExceptionHandler(final UserPasswordTooWeakException userPasswordTooWeakException,
        final HttpServletRequest httpServletRequest) {
        return new ErrorResponse(Instant.now().toString(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
            userPasswordTooWeakException.getMessage(), httpServletRequest.getRequestURI());
    }

    @ExceptionHandler(AddressRegisterEmptyPropertyException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse addressEmptyRegisterPropertiesExceptionHandler(final AddressRegisterEmptyPropertyException addressRegisterEmptyPropertyException,
        final HttpServletRequest httpServletRequest) {
        return new ErrorResponse(Instant.now().toString(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(),
            addressRegisterEmptyPropertyException.getMessage(), httpServletRequest.getRequestURI());
    }

}
