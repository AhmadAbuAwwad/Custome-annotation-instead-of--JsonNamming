package com.quizplus.demo.configurations;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizplus.demo.annotation.JsonController;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

//  *****************************   REQUEST    *****************************
@RestControllerAdvice
public class RequestResponseBodyAdvices  implements ResponseBodyAdvice<Object>, RequestBodyAdvice{

    /**
     * Allow to enter beforeBodyRead method
     * @param targetType the return type
     * @param converterType the selected converter type
     * @return
     */
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * The mapping operation is done here when the target object class is Annotated
     * @param inputMessage the request
     * @param parameter the target method parameter
     * @param targetType the target type, not necessarily the same as the method
     * parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the converter used to deserialize the body
     * @return
     * @throws IOException
     */
    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType,
                                           Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        if(((Class) targetType).isAnnotationPresent(JsonController.class)){
            String body = new String(inputMessage.getBody().readAllBytes(), StandardCharsets.UTF_8);

            InputStream inputStream = new ByteArrayInputStream(camelCaseBody(body).getBytes());

            //  Create the new httpInputMessage with the new body
            HttpInputMessage httpInputMessagess = new HttpInputMessage() {
                @Override
                public InputStream getBody() {
                    return inputStream;
                }

                @Override
                public HttpHeaders getHeaders() {
                    return inputMessage.getHeaders();
                }
            };

            return httpInputMessagess;
        }
    return inputMessage;
    }

    /**
     * @param body set to the converter Object before the first advice is called
     * @param inputMessage the request
     * @param parameter the target method parameter
     * @param targetType the target type, not necessarily the same as the method
     * parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the converter used to deserialize the body
     * @return
     */
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter,
                                Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    //  *************************************************************************
    //  *************************************************************************
    //  *****************************   RESPONSE    *****************************
    //  *************************************************************************
    //  *************************************************************************
    /**
     * Allow to enter beforeBodyWrite method
     * @param returnType the return type
     * @param converterType the selected converter type
     * @return
     */
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    /**
     * @param body usually set to {@code null} before the first advice is called
     * @param inputMessage the request
     * @param parameter the method parameter
     * @param targetType the target type, not necessarily the same as the method
     * parameter type, e.g. for {@code HttpEntity<String>}.
     * @param converterType the selected converter type
     * @return
     */
    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

    /**
     * @param body the body to be written
     * @param returnType the return type of the controller method
     * @param selectedContentType the content type selected through content negotiation
     * @param selectedConverterType the converter type selected to write to the response
     * @param request the current request
     * @param response the current response
     * @return the new Body to be written in Json Format
     */
    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
         //  Check Annotated Class
        if(body.getClass().isAnnotationPresent(JsonController.class)){
            Map<String, Object> bodyMap = new HashMap<>();
            try {
                bodyMap = getJsonFromMap(body);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return bodyMap;
        }
        return body;
    }

    /**
     * Object to Json as Map converter
     * @param object
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private Map<String, Object> getJsonFromMap(Object object) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = object.getClass();
        Map<String, Object> jsonElementsMap = new HashMap<>();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if(field.get(object) != null)
                if(!field.get(object).getClass().isAnnotationPresent(JsonController.class))
                    jsonElementsMap.put(toSnakeCaseMethod(field.getName()), field.get(object));
                else
                    jsonElementsMap.put(toSnakeCaseMethod(field.getName()), getJsonFromMap(field.get(object)));
            else
                jsonElementsMap.put(toSnakeCaseMethod(field.getName()), field.get(object));

        }
        return jsonElementsMap;
    }


    /**
     * String Snake Case converter
     * @param str
     * @return
     */
    private String toSnakeCaseMethod(String str) {
        //  Camel To Snake Case
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        str = str.replaceAll(
                        regex, replacement)
                .toLowerCase();
        return str;
    }

    private String camelCaseBody(String body) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> bodyMap = new ObjectMapper().readValue(body, HashMap.class);
        String newBody = body;

        for (Map.Entry<String, Object> set : bodyMap.entrySet()) {
            newBody = newBody.replace(set.getKey(), toCamelCaseMethod(set.getKey()));
            if (set.getValue().getClass().equals(LinkedHashMap.class)) {
                newBody  = nestedObjectParser((LinkedHashMap) set.getValue(),newBody);
            }
        }
        return newBody;
    }

    private String nestedObjectParser(LinkedHashMap object, String body) throws JsonProcessingException {

        String newBody = body;
        Set<String> keys = object.keySet();
        for (String key : keys) {
            newBody = newBody.replace(key, toCamelCaseMethod(key));
            if (object.get(key).getClass().equals(LinkedHashMap.class)) {
                newBody  = nestedObjectParser((LinkedHashMap) object.get(key),newBody);
            }
        }
        return newBody;
    }
    private String toCamelCaseMethod(String snakeCaseStr) {
        String toCamelCaseStr = snakeCaseStr;

        while(toCamelCaseStr.contains("_") && toCamelCaseStr.indexOf('_') != toCamelCaseStr.length()-1)
            toCamelCaseStr = toCamelCaseStr.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(
                    toCamelCaseStr.charAt(toCamelCaseStr.indexOf("_") + 1))));
        return  toCamelCaseStr;
    }
}
