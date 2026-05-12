package web.vue;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class pageConnexion extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Boolean succes = (Boolean) request.getAttribute("succes");
        String redirection = (String) request.getAttribute("redirection");

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        JsonObject json;
        if (Boolean.TRUE.equals(succes)) {
            json = Json.createObjectBuilder()
                    .add("succes", true)
                    .add("redirection", redirection)
                    .build();
        } else {
            json = Json.createObjectBuilder()
                    .add("succes", false)
                    .build();
        }

        PrintWriter writer = response.getWriter();
        writer.write(json.toString());
        writer.close();
    }
}
