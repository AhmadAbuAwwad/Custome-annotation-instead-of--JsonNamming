package com.quizplus.demo.controller.dto.request;

import com.quizplus.demo.annotation.JsonController;
import lombok.*;
import java.util.List;
import java.util.Map;

@JsonController
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder


public class DifferentDataTypesRequest {
    private String string;

    private int number;
    private boolean bool;
    private List<String> stringsList;
    private List<Integer> intList;
    private Map<String, Integer> map;


}
