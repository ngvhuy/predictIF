package web.vue;

import jakarta.json.Json;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import metier.modele.ProfilAstral;

public class ProfilAstralSerialisation extends Serialisation {

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

        ProfilAstral p = (ProfilAstral) request.getAttribute("profil");
        pw.write(Json.createObjectBuilder()
            .add("zodiac",       p != null && p.getZodiac()      != null ? p.getZodiac()      : "")
            .add("signeChinois", p != null && p.getSigneChinois() != null ? p.getSigneChinois() : "")
            .add("couleur",      p != null && p.getCouleur()      != null ? p.getCouleur()      : "")
            .add("animal",       p != null && p.getAnimal()       != null ? p.getAnimal()       : "")
            .build().toString());
        pw.close();
    }
}
