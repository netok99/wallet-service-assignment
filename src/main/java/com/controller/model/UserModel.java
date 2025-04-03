package com.controller.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UserModel(
    @JsonProperty("name") String name,
    @JsonProperty("email") String email) {
}

