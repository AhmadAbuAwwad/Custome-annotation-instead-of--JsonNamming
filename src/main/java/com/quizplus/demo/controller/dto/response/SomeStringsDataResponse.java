package com.quizplus.demo.controller.dto.response;

import com.quizplus.demo.annotation.JsonController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonController
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class SomeStringsDataResponse {
    private String dataInSnakeCase;
    private String dataInCamelCase;

    private String dataCase;

    public String getDataCase() {
        return dataCase;
    }
}
