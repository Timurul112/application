package util.utillity;

import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@UtilityClass
public class ServletUtils {

    public static void setJsonResponse(String jsonResponse, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter writer = response.getWriter()) {
            writer.write(jsonResponse);
        }
    }

    public static void setJsonResponse(Object response, HttpServletResponse httpServletResponse) throws IOException {
        String jsonResponse = ObjectMapperUtil.getObjectMapper().writeValueAsString(response);
        httpServletResponse.setContentType("application/json");
        httpServletResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());
        try (PrintWriter writer = httpServletResponse.getWriter()) {
            writer.write(jsonResponse);
        }
    }
}
