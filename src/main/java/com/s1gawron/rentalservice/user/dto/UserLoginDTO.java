package com.s1gawron.rentalservice.user.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
@JsonDeserialize(builder = UserLoginDTO.UserLoginDTOBuilder.class)
public class UserLoginDTO {

    private final String email;

    private final String password;

    @JsonPOJOBuilder(withPrefix = "")
    public static class UserLoginDTOBuilder {

    }

}
