package com.example.demo.controller.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.annotation.Id;

public record CarDto(@Id Integer id, @NotEmpty String brand, @NotNull Integer kilowatt) {
}
