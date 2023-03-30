package com.quizplus.demo.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;
public class MutableHttpRequest extends HttpServletRequestWrapper {

    private final Map<String, String[]> mutableParams = new HashMap<>();

    public MutableHttpRequest(final HttpServletRequest request) {
        super(request);
    }

    public MutableHttpRequest addParameter(String name, String value) {
        if (value != null)
            mutableParams.put(name, new String[] { value });

        return this;
    }

    @Override
    public String getParameter(final String name) {
        String[] values = getParameterMap().get(name);

        if(values == null){
            return super.getParameter(name);
        }

        return Arrays.stream(values)
                .findFirst()
                .orElse(super.getParameter(name));
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> allParameters = new HashMap<>();
        allParameters.putAll(super.getParameterMap());
        allParameters.putAll(mutableParams);

        return Collections.unmodifiableMap(allParameters);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return Collections.enumeration(getParameterMap().keySet());
    }

    @Override
    public String[] getParameterValues(final String name) {
        return getParameterMap().get(name);
    }
}
