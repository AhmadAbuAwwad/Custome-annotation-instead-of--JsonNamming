package com.quizplus.demo.controller.dto.response;

import com.quizplus.demo.annotation.JsonController;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@JsonController
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder


public class DifferentDataTypesResponse {
    private String string;

    private int number;
    private boolean bool;
    private List<String> stringsList;
    private List<Integer> intList;
    private Map<String, Integer> map;


}
