package ru.kpfu.itis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@UtilityClass
public class JsonParser {

    private ObjectMapper mapper = new ObjectMapper();

    public static <T> T readRequestBody(HttpServletRequest request, Class<T> clazz) {
        try {
            return mapper.convertValue(request.getReader(), clazz);
        } catch (IOException e) {
            //TODO: обработать
            throw new RuntimeException(e);
        }
    }

    public static <T> void writeResponseBody(T object, HttpServletResponse resp) throws JsonProcessingException {
        String json = mapper.writeValueAsString(object);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            resp.getWriter().println(json);
            resp.getWriter().flush();
        } catch (IOException e) {
            //TODO: Обработать
            throw new RuntimeException(e);
        }
    }

}
