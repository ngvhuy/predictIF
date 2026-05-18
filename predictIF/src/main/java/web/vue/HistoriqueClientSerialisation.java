package web.vue;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import metier.modele.Consultation;

public class HistoriqueClientSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter pw = response.getWriter();

        String erreur = (String) request.getAttribute("erreur");
        if (erreur != null) {
            pw.write(Json.createObjectBuilder().add("erreur", erreur).build().toString());
            pw.close();
            return;
        }

        List<Consultation> consultations = (List<Consultation>) request.getAttribute("consultations");
        JsonArrayBuilder array = Json.createArrayBuilder();

        for (Consultation c : consultations) {
            array.add(Json.createObjectBuilder()
                .add("medium", c.getMedium().getDenomination())
                .add("date", c.getDate() != null ? c.getDate().toString() : "")
                .add("commentaire", c.getCommentaire() != null ? c.getCommentaire() : "")
            );
        }

        pw.write(array.build().toString());
        pw.close();
    }
}
