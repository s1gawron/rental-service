package com.s1gawron.rentalservice.tool.helper;

import com.s1gawron.rentalservice.tool.dto.ToolDTO;
import com.s1gawron.rentalservice.tool.dto.ToolStateDTO;
import com.s1gawron.rentalservice.tool.model.Tool;
import com.s1gawron.rentalservice.tool.model.ToolCategory;
import com.s1gawron.rentalservice.tool.model.ToolState;
import com.s1gawron.rentalservice.tool.model.ToolStateType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public enum ToolCreatorHelper {

    I;

    public List<ToolDTO> createToolDTOList() {
        final ToolStateDTO newState = new ToolStateDTO(ToolStateType.NEW, "New and shiny tool");
        final ToolDTO newToolDTO = new ToolDTO("Hammer", "It's just a hammer :)", ToolCategory.LIGHT, BigDecimal.valueOf(10.99), newState);
        final ToolStateDTO usedState = new ToolStateDTO(ToolStateType.MINIMAL_WEAR, "No signs of usage");
        final ToolDTO usedToolDTO = new ToolDTO("Loader", "4 wheeled loader :)", ToolCategory.HEAVY, BigDecimal.valueOf(1000.99), usedState);
        final ToolStateDTO wornState = new ToolStateDTO(ToolStateType.WELL_WORN, "Rusty");
        final ToolDTO wornToolDTO = new ToolDTO("Crane", "Mechanical giraffe", ToolCategory.HEAVY, BigDecimal.valueOf(19999.99), wornState);

        return List.of(newToolDTO, usedToolDTO, wornToolDTO);
    }

    public List<Tool> createToolList() {
        return createToolDTOList().stream()
            .map(toolDTO -> {
                final ToolState toolState = ToolState.from(toolDTO.getToolState());
                return Tool.from(toolDTO, toolState);
            })
            .collect(Collectors.toList());
    }

    public List<Tool> createHeavyTools() {
        final ToolStateDTO newState = new ToolStateDTO(ToolStateType.NEW, "New and shiny tool");
        final ToolDTO newToolDTO = new ToolDTO("Hammer", "It's just a hammer :)", ToolCategory.HEAVY, BigDecimal.valueOf(10.99), newState);
        final Tool newTool = Tool.from(newToolDTO, ToolState.from(newState));

        final ToolStateDTO usedState = new ToolStateDTO(ToolStateType.MINIMAL_WEAR, "No signs of usage");
        final ToolDTO usedToolDTO = new ToolDTO("Loader", "4 wheeled loader :)", ToolCategory.HEAVY, BigDecimal.valueOf(1000.99), usedState);
        final Tool usedTool = Tool.from(usedToolDTO, ToolState.from(usedState));

        final ToolStateDTO wornState = new ToolStateDTO(ToolStateType.WELL_WORN, "Rusty");
        final ToolDTO wornToolDTO = new ToolDTO("Crane", "Mechanical giraffe", ToolCategory.HEAVY, BigDecimal.valueOf(19999.99), wornState);
        final Tool wornTool = Tool.from(wornToolDTO, ToolState.from(wornState));

        return List.of(newTool, usedTool, wornTool);
    }

    public Tool createTool() {
        final ToolStateDTO newState = new ToolStateDTO(ToolStateType.NEW, "New and shiny tool");
        final ToolDTO newToolDTO = new ToolDTO("Hammer", "It's just a hammer :)", ToolCategory.HEAVY, BigDecimal.valueOf(10.99), newState);
        return Tool.from(newToolDTO, ToolState.from(newState));
    }

    public ToolDTO createToolDTO() {
        final ToolStateDTO newState = new ToolStateDTO(ToolStateType.NEW, "New and shiny tool");
        return new ToolDTO("Hammer", "It's just a hammer :)", ToolCategory.HEAVY, BigDecimal.valueOf(10.99), newState);
    }

    public ToolDTO createEditedToolDTO() {
        final ToolStateDTO newState = new ToolStateDTO(ToolStateType.NEW, "New");
        return new ToolDTO("Hammer#2", "It's a second hammer", ToolCategory.LIGHT, BigDecimal.valueOf(5.99), newState);
    }

}
