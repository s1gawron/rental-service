package com.s1gawron.rentalservice.tool.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.s1gawron.rentalservice.address.dto.AddressDTO;
import com.s1gawron.rentalservice.tool.repository.ToolRepository;
import com.s1gawron.rentalservice.tool.service.ToolService;
import com.s1gawron.rentalservice.user.dto.UserLoginDTO;
import com.s1gawron.rentalservice.user.dto.UserRegisterDTO;
import com.s1gawron.rentalservice.user.model.UserRole;
import com.s1gawron.rentalservice.user.repository.UserRepository;
import com.s1gawron.rentalservice.user.service.UserService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public abstract class AbstractToolControllerIntegrationTest {

    private static final String CUSTOMER_EMAIL = "customer@test.pl";

    private static final String WORKER_EMAIL = "worker@test.pl";

    private static final String PASSWORD = "Start00!";

    private static final String NO_USER_IN_DB_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0MkB0ZXN0LnBsIiwiYXV0aG9yaXRpZXMiOlt7ImF1dGhvcml0eSI6IkNVU1RPTUVSIn1dLCJpYXQiOjE2NzE0NjcwODgsImV4cCI6MTY3MTQ5MDgwMH0.m49W6B0f6zyLQhs-79Yj640q_TnJzcQLBGmLbs-jZm4";

    @Autowired
    protected MockMvc mockMvc;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    protected ToolService toolService;

    @Autowired
    private ToolRepository toolRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        final AddressDTO addressDTO = new AddressDTO("Poland", "Warsaw", "Test", "01-000");
        final UserRegisterDTO customerRegisterDTO = new UserRegisterDTO(CUSTOMER_EMAIL, PASSWORD, "John", "Kowalski", UserRole.CUSTOMER.getName(), addressDTO);
        final UserRegisterDTO workerRegisterDTO = new UserRegisterDTO(WORKER_EMAIL, PASSWORD, "John", "Kowalski", UserRole.WORKER.getName(), null);

        userService.validateAndRegisterUser(customerRegisterDTO);
        userService.validateAndRegisterUser(workerRegisterDTO);
    }

    @AfterEach
    @Transactional
    void cleanUp() {
        toolRepository.deleteAll();
        userRepository.deleteAll();
    }

    @SneakyThrows
    protected String getCustomerAuthorizationToken() {
        final UserLoginDTO userLoginDTO = new UserLoginDTO(CUSTOMER_EMAIL, PASSWORD);
        final String userLoginJson = objectMapper.writeValueAsString(userLoginDTO);
        final RequestBuilder request = MockMvcRequestBuilders.post("/api/user/login").content(userLoginJson);
        final MvcResult loginResult = mockMvc.perform(request).andReturn();

        return loginResult.getResponse().getHeader("Authorization");
    }

    @SneakyThrows
    protected String getWorkerAuthorizationToken() {
        final UserLoginDTO userLoginDTO = new UserLoginDTO(WORKER_EMAIL, PASSWORD);
        final String userLoginJson = objectMapper.writeValueAsString(userLoginDTO);
        final RequestBuilder request = MockMvcRequestBuilders.post("/api/user/login").content(userLoginJson);
        final MvcResult loginResult = mockMvc.perform(request).andReturn();

        return loginResult.getResponse().getHeader("Authorization");
    }

    protected String getUnregisteredUserAuthorizationToken() {
        return NO_USER_IN_DB_TOKEN;
    }

}
