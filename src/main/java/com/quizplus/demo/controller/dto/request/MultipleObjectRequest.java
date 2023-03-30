package com.quizplus.demo.controller.dto.request;

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
public class MultipleObjectRequest {
    private String stringA;
    private SomeStringsDataRequest someStringsDataRequest;

    private DifferentDataTypesRequest differentDataTypesRequest;
}
