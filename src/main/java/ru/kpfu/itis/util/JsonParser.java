package ru.kpfu.itis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@UtilityClass
public class JsonParser {

    private final ObjectMapper mapper = new ObjectMapper();

    public static <T> T readRequestBody(HttpServletRequest request, Class<T> clazz) {
        try {
            return mapper.readValue(request.getReader(), clazz);
        } catch (IOException e) {
            //TODO: обработать
            throw new RuntimeException("Error reading request body", e);
        }
    }

    public static <T> void writeResponseBody(T object, HttpServletResponse resp) {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            mapper.writeValue(resp.getWriter(), object);
        } catch (IOException e) {
            //TODO: Обработать
            throw new RuntimeException("Error writing response", e);
        }
    }

}
