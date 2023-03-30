package com.quizplus.demo.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;
import java.io.IOException;

@Component
public class RequestMappingFilter extends OncePerRequestFilter {
        @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//          (!request.getParameterMap().isEmpty()) ? filterChain.doFilter(mutableRequest(request), response) :
//                                                      filterChain.doFilter(request, response);
          if ((!request.getParameterMap().isEmpty())) {
              filterChain.doFilter(mutableRequest(request), response);
          } else {
              filterChain.doFilter(request, response);
          }
    }

    //  Creating the new Mutable Request and map the values in it
    private MutableHttpRequest mutableRequest(HttpServletRequest request) {
        MutableHttpRequest mutableHttpRequest = new MutableHttpRequest(request);

        for(String oldParameter : request.getParameterMap().keySet()) {
            mutableHttpRequest.addParameter(toCamelCaseMethod(oldParameter), request.getParameterMap().get(oldParameter)[0]);
        }
        return mutableHttpRequest;
    }

    //  Snake to Camel Case Method
    private String toCamelCaseMethod(String snakeCaseStr) {
        String toCamelCaseStr = snakeCaseStr;

        while(toCamelCaseStr.contains("_"))
            toCamelCaseStr = toCamelCaseStr.replaceFirst("_[a-z]", String.valueOf(Character.toUpperCase(
                    toCamelCaseStr.charAt(toCamelCaseStr.indexOf("_") + 1))));
        return  toCamelCaseStr;
    }

}