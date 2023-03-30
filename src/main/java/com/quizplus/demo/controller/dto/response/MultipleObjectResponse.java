package com.quizplus.demo.controller.dto.response;

import com.quizplus.demo.annotation.JsonController;
import com.quizplus.demo.controller.dto.request.DifferentDataTypesRequest;
import com.quizplus.demo.controller.dto.request.SomeStringsDataRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonController
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MultipleObjectResponse {
    private String stringA;
    private SomeStringsDataRequest someStringsDataRequest;

    private DifferentDataTypesRequest differentDataTypesRequest;
}
