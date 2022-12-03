package com.s1gawron.rentalservice.user.dto.validator;

import com.s1gawron.rentalservice.address.dto.AddressDTO;
import com.s1gawron.rentalservice.user.dto.UserRegisterDTO;
import com.s1gawron.rentalservice.user.exception.UserEmailPatternViolationException;
import com.s1gawron.rentalservice.user.exception.UserPasswordTooWeakException;
import com.s1gawron.rentalservice.user.exception.UserRegisterEmptyPropertiesException;
import com.s1gawron.rentalservice.user.model.UserType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDTOValidatorTest {

    @Test
    void shouldValidate() {
        final AddressDTO addressDTO = new AddressDTO("Poland", "Warsaw", "Test", "01-000");
        final UserRegisterDTO userRegisterDTO = new UserRegisterDTO("test@test.pl", "Start00!", "John", "Kowalski", UserType.CUSTOMER, addressDTO);

        assertTrue(UserDTOValidator.I.validate(userRegisterDTO));
    }

    @Test
    void shouldThrowExceptionWhenEmailIsNull() {
        final AddressDTO addressDTO = new AddressDTO("Poland", "Warsaw", "Test", "01-000");
        final UserRegisterDTO userRegisterDTO = new UserRegisterDTO(null, "Start00!", "John", "Kowalski", UserType.CUSTOMER, addressDTO);

        assertThrows(UserRegisterEmptyPropertiesException.class, () -> UserDTOValidator.I.validate(userRegisterDTO), "User email cannot be empty!");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsNull() {
        final AddressDTO addressDTO = new AddressDTO("Poland", "Warsaw", "Test", "01-000");
        final UserRegisterDTO userRegisterDTO = new UserRegisterDTO("test@test.pl", null, "John", "Kowalski", UserType.CUSTOMER, addressDTO);

        assertThrows(UserRegisterEmptyPropertiesException.class, () -> UserDTOValidator.I.validate(userRegisterDTO), "User password cannot be empty!");
    }

    @Test
    void shouldThrowExceptionWhenFirstNameIsNull() {
        final AddressDTO addressDTO = new AddressDTO("Poland", "Warsaw", "Test", "01-000");
        final UserRegisterDTO userRegisterDTO = new UserRegisterDTO("test@test.pl", "Start00!", null, "Kowalski", UserType.CUSTOMER, addressDTO);

        assertThrows(UserRegisterEmptyPropertiesException.class, () -> UserDTOValidator.I.validate(userRegisterDTO), "User first name cannot be empty!");
    }

    @Test
    void shouldThrowExceptionWhenLastNameIsNull() {
        final AddressDTO addressDTO = new AddressDTO("Poland", "Warsaw", "Test", "01-000");
        final UserRegisterDTO userRegisterDTO = new UserRegisterDTO("test@test.pl", "Start00!", "John", null, UserType.CUSTOMER, addressDTO);

        assertThrows(UserRegisterEmptyPropertiesException.class, () -> UserDTOValidator.I.validate(userRegisterDTO), "User last name cannot be empty!");
    }

    @Test
    void shouldThrowExceptionWhenUserTypeIsNull() {
        final AddressDTO addressDTO = new AddressDTO("Poland", "Warsaw", "Test", "01-000");
        final UserRegisterDTO userRegisterDTO = new UserRegisterDTO("test@test.pl", "Start00!", "John", "Kowalski", null, addressDTO);

        assertThrows(UserRegisterEmptyPropertiesException.class, () -> UserDTOValidator.I.validate(userRegisterDTO), "User type cannot be empty!");
    }

    @Test
    void shouldThrowExceptionWhenEmailDoesNotMatchPattern() {
        final String email = "test-test.pl";
        final AddressDTO addressDTO = new AddressDTO("Poland", "Warsaw", "Test", "01-000");
        final UserRegisterDTO userRegisterDTO = new UserRegisterDTO(email, "Start00!", "John", "Kowalski", UserType.CUSTOMER, addressDTO);

        assertThrows(UserEmailPatternViolationException.class, () -> UserDTOValidator.I.validate(userRegisterDTO),
            "Email: " + email + ", does not match validation pattern. If this is proper email please contact me for a fix.");
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsTooWeak() {
        final AddressDTO addressDTO = new AddressDTO("Poland", "Warsaw", "Test", "01-000");
        final UserRegisterDTO userRegisterDTO = new UserRegisterDTO("test@test.pl", "password", "John", "Kowalski", UserType.CUSTOMER, addressDTO);

        assertThrows(UserPasswordTooWeakException.class, () -> UserDTOValidator.I.validate(userRegisterDTO),
            "Provided password is too weak! The password must be minimum 8 characters long and contain upper and lower case letters, a number and one of the characters !, @, #, $, *");
    }

}