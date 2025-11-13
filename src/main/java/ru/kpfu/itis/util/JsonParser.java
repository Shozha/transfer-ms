package ru.kpfu.itis.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.experimental.UtilityClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@UtilityClass
public class JsonParser {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public static <T> T readRequestBody(HttpServletRequest request, Class<T> clazz) {
        try {
            return mapper.readValue(request.getReader(), clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeResponseBody(Object object, HttpServletResponse resp) throws JsonProcessingException {
        String json = mapper.writeValueAsString(object);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        try {
            resp.getWriter().println(json);
            resp.getWriter().flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
