package web.vue;

import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class GenererPredictionSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        JsonObjectBuilder builder = Json.createObjectBuilder();
        
        String erreur = (String) request.getAttribute("erreur");
        if (erreur != null) {
            builder.add("erreur", erreur);
        } else {
            String prediction = (String) request.getAttribute("prediction");
            if (prediction != null) {
                builder.add("prediction", prediction);
            } else {
                builder.add("prediction", "Les énergies sont momentanément instables...");
            }
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(builder.build().toString());
        writer.close();
    }
}