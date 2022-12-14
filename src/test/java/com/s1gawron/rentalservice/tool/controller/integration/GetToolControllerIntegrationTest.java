package com.s1gawron.rentalservice.tool.controller.integration;

import com.fasterxml.jackson.core.type.TypeReference;
import com.s1gawron.rentalservice.tool.dto.ToolDetailsDTO;
import com.s1gawron.rentalservice.tool.dto.ToolListingDTO;
import com.s1gawron.rentalservice.tool.helper.ToolCreatorHelper;
import com.s1gawron.rentalservice.tool.model.Tool;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GetToolControllerIntegrationTest extends AbstractToolControllerIntegrationTest {

    private static final String TOOL_GET_ENDPOINT = "/api/public/tool/get/";

    @Test
    @SneakyThrows
    void shouldGetToolsByCategory() {
        toolRepository.saveAll(ToolCreatorHelper.I.createHeavyTools());
        toolRepository.saveAll(ToolCreatorHelper.I.createLightTools());

        final RequestBuilder request = MockMvcRequestBuilders.get(TOOL_GET_ENDPOINT + "category/heavy");

        final MvcResult result = mockMvc.perform(request).andReturn();
        final String resultJson = result.getResponse().getContentAsString();
        final ToolListingDTO resultObject = objectMapper.readValue(resultJson, ToolListingDTO.class);

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(3, resultObject.getCount());
        assertEquals(3, resultObject.getTools().size());
    }

    @Test
    @SneakyThrows
    void shouldReturnBadRequestResponseWhenToolCategoryDoesNotExist() {
        toolRepository.saveAll(ToolCreatorHelper.I.createHeavyTools());
        toolRepository.saveAll(ToolCreatorHelper.I.createLightTools());

        final RequestBuilder request = MockMvcRequestBuilders.get(TOOL_GET_ENDPOINT + "category/unknown");

        final MvcResult result = mockMvc.perform(request).andReturn();

        assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
    }

    @Test
    @SneakyThrows
    void shouldGetNewTools() {
        toolRepository.saveAll(ToolCreatorHelper.I.createHeavyToolsWithDate());
        toolRepository.saveAll(ToolCreatorHelper.I.createLightToolsWithDate());

        final RequestBuilder request = MockMvcRequestBuilders.get(TOOL_GET_ENDPOINT + "new");

        final MvcResult result = mockMvc.perform(request).andReturn();
        final String resultJson = result.getResponse().getContentAsString();
        final List<ToolDetailsDTO> resultList = objectMapper.readValue(resultJson, new TypeReference<>() {

        });

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(3, resultList.size());

        final long areProperToolsInResultListCount = resultList.stream().filter(tool -> {
                final String toolName = tool.getName();

                return toolName.equals("Loader") || toolName.equals("Crane") || toolName.equals("Big hammer");
            })
            .count();

        assertEquals(3, areProperToolsInResultListCount);
    }

    @Test
    @SneakyThrows
    void shouldGetToolById() {
        toolRepository.saveAll(ToolCreatorHelper.I.createHeavyTools());
        toolRepository.saveAll(ToolCreatorHelper.I.createLightTools());
        final Tool chainsaw = ToolCreatorHelper.I.createChainsaw();
        toolRepository.save(chainsaw);

        final RequestBuilder request = MockMvcRequestBuilders.get(TOOL_GET_ENDPOINT + "id/" + chainsaw.getToolId());

        final MvcResult result = mockMvc.perform(request).andReturn();
        final String resultJson = result.getResponse().getContentAsString();
        final ToolDetailsDTO resultObject = objectMapper.readValue(resultJson, ToolDetailsDTO.class);

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(chainsaw.getToolId(), resultObject.getToolId());
        assertEquals(chainsaw.getName(), resultObject.getName());
    }

    @Test
    @SneakyThrows
    void shouldReturnNotFoundResponseWhenToolIsNotFoundById() {
        toolRepository.saveAll(ToolCreatorHelper.I.createHeavyTools());
        toolRepository.saveAll(ToolCreatorHelper.I.createLightTools());

        final Optional<Tool> toolById = toolRepository.findById(99L);

        if (toolById.isPresent()) {
            throw new IllegalStateException("Tool cannot be in database, because it was not added!");
        }

        final RequestBuilder request = MockMvcRequestBuilders.get(TOOL_GET_ENDPOINT + "id/99");

        final MvcResult result = mockMvc.perform(request).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

    @Test
    @SneakyThrows
    void shouldGetToolsByName() {
        toolRepository.saveAll(ToolCreatorHelper.I.createCommonNameToolList());
        toolRepository.save(ToolCreatorHelper.I.createChainsaw());

        final String json = "{\n"
            + "  \"toolName\": \"hammer\"\n"
            + "}";

        final RequestBuilder request = MockMvcRequestBuilders.post(TOOL_GET_ENDPOINT + "name").contentType(MediaType.APPLICATION_JSON).content(json);

        final MvcResult result = mockMvc.perform(request).andReturn();
        final String resultJson = result.getResponse().getContentAsString();
        final List<ToolDetailsDTO> resultList = objectMapper.readValue(resultJson, new TypeReference<>() {

        });

        assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
        assertEquals(2, resultList.size());

        for (final ToolDetailsDTO details : resultList) {
            assertTrue(details.getName().toLowerCase().contains("hammer"));
        }
    }

    @Test
    @SneakyThrows
    void shouldReturnNotFoundResponseWhenToolsAreNotFoundByName() {
        toolRepository.saveAll(ToolCreatorHelper.I.createCommonNameToolList());

        final String json = "{\n"
            + "  \"toolName\": \"chainsaw\"\n"
            + "}";

        final RequestBuilder request = MockMvcRequestBuilders.post(TOOL_GET_ENDPOINT + "name").contentType(MediaType.APPLICATION_JSON).content(json);
        final MvcResult result = mockMvc.perform(request).andReturn();

        assertEquals(HttpStatus.NOT_FOUND.value(), result.getResponse().getStatus());
    }

}
