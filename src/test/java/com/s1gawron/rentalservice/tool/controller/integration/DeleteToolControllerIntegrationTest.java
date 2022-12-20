package com.s1gawron.rentalservice.tool.controller.integration;

import com.s1gawron.rentalservice.tool.helper.ToolCreatorHelper;
import com.s1gawron.rentalservice.tool.model.Tool;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DeleteToolControllerIntegrationTest extends AbstractToolControllerIntegrationTest {

    private static final String TOOL_DELETE_ENDPOINT = "/api/tool/delete/";

    private long currentToolId;

    @BeforeEach
    void setUp() {
        super.setUp();

        final Tool tool = ToolCreatorHelper.I.createTool();
        toolRepository.save(tool);
        currentToolId = tool.getToolId();
    }

    @AfterEach
    void cleanUp() {
        super.cleanUp();
        toolRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void shouldDeleteTool() {
        final RequestBuilder request = MockMvcRequestBuilders.delete(TOOL_DELETE_ENDPOINT + currentToolId)
            .header("Authorization", getWorkerAuthorizationToken());
        final MvcResult result = mockMvc.perform(request).andReturn();

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals("true", result.getResponse().getContentAsString());
        assertTrue(toolService.getToolById(1L).isEmpty());
    }

    @Test
    @SneakyThrows
    void shouldReturnForbiddenResponseWhenUserIsNotAllowedToDeleteTool() {
        final RequestBuilder request = MockMvcRequestBuilders.delete(TOOL_DELETE_ENDPOINT + currentToolId)
            .header("Authorization", getCustomerAuthorizationToken());
        final MvcResult result = mockMvc.perform(request).andReturn();

        assertEquals(HttpStatus.FORBIDDEN.value(), result.getResponse().getStatus());
        assertTrue(toolService.getToolById(currentToolId).isPresent());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotFoundResponseWhenToolIsNotFoundWhileDeletingTool() {
        final Optional<Tool> noToolInDb = toolService.getToolById(99L);

        if (noToolInDb.isPresent()) {
            throw new IllegalStateException("Tool cannot be in database, because it was not added!");
        }

        final RequestBuilder request = MockMvcRequestBuilders.delete(TOOL_DELETE_ENDPOINT + "99").header("Authorization", getWorkerAuthorizationToken());
        final MvcResult result = mockMvc.perform(request).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
        assertTrue(toolService.getToolById(99L).isEmpty());
    }

}