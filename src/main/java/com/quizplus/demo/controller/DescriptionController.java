package com.quizplus.demo.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.quizplus.demo.controller.dto.request.*;
import com.quizplus.demo.controller.dto.response.DifferentDataTypesResponse;
import com.quizplus.demo.controller.dto.response.MultipleObjectResponse;
import com.quizplus.demo.controller.dto.response.SomeStringsDataResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Controller
@RequestMapping("/")
public class DescriptionController {

    //@Autowired
    //DescriptionService descriptionService;

    /**
     * Get Some Data
     * @RequestParam
     http://localhost:8080/?data_case=good_Bye&data_in_snake_case=good_bye&data_in_camel_case=goodBye
     * @param changeDescriptionRequest
     * @return
     */
    @GetMapping("/")
    public ResponseEntity<SomeStringsDataResponse>  getSomeStringData (SomeStringsDataRequest changeDescriptionRequest)
    {
        return new ResponseEntity<>(new SomeStringsDataResponse(changeDescriptionRequest.getDataInSnakeCase(),
            changeDescriptionRequest.getDataInCamelCase(), changeDescriptionRequest.getDataCase()), HttpStatus.OK);
    }

    /**
     * Post Some Data
     http://localhost:8080/?data_case=good_Bye&data_in_snake_case=good_bye&data_in_camel_case=goodBye
     * @param changeDescriptionRequest
     * @return
     */
    @PostMapping("/")
    public ResponseEntity<SomeStringsDataResponse>  postSomeStringData (SomeStringsDataRequest changeDescriptionRequest)
    {
        return new ResponseEntity<>(new SomeStringsDataResponse(changeDescriptionRequest.getDataInSnakeCase(),
                changeDescriptionRequest.getDataInCamelCase(), changeDescriptionRequest.getDataCase()), HttpStatus.OK);
    }

    /**
     * Get and Post throw Body
     * @param myObject
     * @return
     * @throws JsonProcessingException
     */
    @GetMapping("/multiple")
    public ResponseEntity<MultipleObjectResponse> multiple(@RequestBody MultipleObjectRequest myObject) throws JsonProcessingException {
        return new ResponseEntity<>(new MultipleObjectResponse(myObject.getStringA(), myObject.getSomeStringsDataRequest()
        , myObject.getDifferentDataTypesRequest()), HttpStatus.OK);
    }
    @PostMapping("/multiplePost")
    public ResponseEntity<MultipleObjectResponse> multiplePost(@RequestBody MultipleObjectRequest myObject) throws JsonProcessingException {
        return new ResponseEntity<>(new MultipleObjectResponse(myObject.getStringA(), myObject.getSomeStringsDataRequest()
                , myObject.getDifferentDataTypesRequest()), HttpStatus.OK);
    }


    /**
     * Get and Post throw Body
     * different data type
     * @param myObject
     * @return
     */
    @GetMapping("/different")
    public ResponseEntity<DifferentDataTypesResponse> different(@RequestBody DifferentDataTypesRequest myObject){
        return new ResponseEntity<>(new DifferentDataTypesResponse(myObject.getString(), myObject.getNumber(),
                myObject.isBool(), myObject.getStringsList(), myObject.getIntList(), myObject.getMap()), HttpStatus.OK);
    }
    @PostMapping("/differentPost")
    public ResponseEntity<DifferentDataTypesResponse> differentPost(@RequestBody DifferentDataTypesRequest myObject){
        return new ResponseEntity<>(new DifferentDataTypesResponse(myObject.getString(), myObject.getNumber(),
                myObject.isBool(), myObject.getStringsList(), myObject.getIntList(), myObject.getMap()), HttpStatus.OK);
    }
}