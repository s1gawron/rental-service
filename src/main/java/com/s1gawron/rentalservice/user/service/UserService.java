package com.s1gawron.rentalservice.user.service;

import com.s1gawron.rentalservice.address.service.AddressService;
import com.s1gawron.rentalservice.shared.UserNotFoundException;
import com.s1gawron.rentalservice.user.dto.UserDTO;
import com.s1gawron.rentalservice.user.dto.UserRegisterDTO;
import com.s1gawron.rentalservice.user.dto.validator.UserDTOValidator;
import com.s1gawron.rentalservice.user.exception.UserEmailExistsException;
import com.s1gawron.rentalservice.user.model.User;
import com.s1gawron.rentalservice.user.model.UserRole;
import com.s1gawron.rentalservice.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final AddressService addressService;

    @Transactional
    public UserDTO validateAndRegisterUser(final UserRegisterDTO userRegisterDTO) {
        UserDTOValidator.I.validate(userRegisterDTO);

        final Optional<User> userEmailExistOptional = getUserByEmail(userRegisterDTO.getEmail());

        if (userEmailExistOptional.isPresent()) {
            throw UserEmailExistsException.create();
        }

        final String encryptedPassword = new BCryptPasswordEncoder().encode(userRegisterDTO.getPassword());
        final UserRole userRole = UserRole.findByValue(userRegisterDTO.getUserRole());
        final User user = User.createUser(userRegisterDTO, userRole, encryptedPassword);

        addressService.validateAndSaveAddress(userRegisterDTO.getAddress(), userRole).ifPresent(user::setCustomerAddress);
        userRepository.save(user);

        return user.toUserDTO();
    }

    @Transactional(readOnly = true)
    public Optional<User> getUserByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public UserDTO getUserDetails() {
        final String authenticatedUserEmail = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final User user = getUserByEmail(authenticatedUserEmail).orElseThrow(() -> UserNotFoundException.create(authenticatedUserEmail));

        return user.toUserDTO();
    }

    @Transactional
    public void saveCustomerWithReservation(final User customer) {
        userRepository.save(customer);
    }
}
