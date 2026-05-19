package web.vue;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import metier.modele.Consultation;

public class MaConsultationSerialisation extends Serialisation {
    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Consultation consultation = (Consultation) request.getAttribute("consultation");

        JsonObjectBuilder builder = Json.createObjectBuilder();

        if (consultation != null) {
            builder.add("id", consultation.getId());
            builder.add("date", consultation.getDate() != null ? consultation.getDate().toString() : "");
            builder.add("heureDebut", consultation.getHeureDebut() != null ? consultation.getHeureDebut().toString() : "");
            builder.add("commentaire", consultation.getCommentaire() != null ? consultation.getCommentaire() : "");
            // heureDebut != null signifie que l'employé a cliqué "Je me sens prêt"
            builder.add("estPret", consultation.getHeureDebut() != null);

            // Client
            if (consultation.getClient() != null) {
                builder.add("clientNom", consultation.getClient().getNom());
                builder.add("clientPrenom", consultation.getClient().getPrenom());
                builder.add("clientTelephone", consultation.getClient().getNumeroTelephone() != null ? consultation.getClient().getNumeroTelephone() : "");
                builder.add("clientMail", consultation.getClient().getMail() != null ? consultation.getClient().getMail() : "");
                builder.add("clientDateNaissance", consultation.getClient().getDateDeNaissance() != null ? consultation.getClient().getDateDeNaissance().toString() : "");

                // Profil Astral du client
                if (consultation.getClient().getProfil() != null) {
                    builder.add("profilZodiac", consultation.getClient().getProfil().getZodiac() != null ? consultation.getClient().getProfil().getZodiac() : "");
                    builder.add("profilSigneChinois", consultation.getClient().getProfil().getSigneChinois() != null ? consultation.getClient().getProfil().getSigneChinois() : "");
                    builder.add("profilCouleur", consultation.getClient().getProfil().getCouleur() != null ? consultation.getClient().getProfil().getCouleur() : "");
                    builder.add("profilAnimal", consultation.getClient().getProfil().getAnimal() != null ? consultation.getClient().getProfil().getAnimal() : "");
                }
            }

            // Medium
            if (consultation.getMedium() != null) {
                builder.add("mediumDenomination", consultation.getMedium().getDenomination());
                builder.add("mediumGenre", consultation.getMedium().getGenre() != null ? consultation.getMedium().getGenre() : "");
                builder.add("mediumPresentation", consultation.getMedium().getPresentation() != null ? consultation.getMedium().getPresentation() : "");
            }

            // Historique des consultations passées entre ce client et ce médium
            JsonArrayBuilder historiqueBuilder = Json.createArrayBuilder();
            if (consultation.getClient() != null && consultation.getMedium() != null) {
                for (Consultation c : consultation.getClient().getListeConsultations()) {
                    if (c.getMedium() != null
                            && c.getMedium().getId().equals(consultation.getMedium().getId())
                            && !c.getId().equals(consultation.getId())
                            && c.getHeureFin() != null) {
                        JsonObjectBuilder histObj = Json.createObjectBuilder();
                        histObj.add("date", c.getDate() != null ? c.getDate().toString() : "");
                        histObj.add("commentaire", c.getCommentaire() != null ? c.getCommentaire() : "-");
                        historiqueBuilder.add(histObj);
                    }
                }
            }
            builder.add("historique", historiqueBuilder);

        } else {
            builder.add("id", 0);
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write(builder.build().toString());
        writer.close();
    }
}
