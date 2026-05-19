package web.vue;

import jakarta.json.Json;
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
            builder.add("heureFin", consultation.getHeureFin() != null ? consultation.getHeureFin().toString() : "");
            builder.add("commentaire", consultation.getCommentaire() != null ? consultation.getCommentaire() : "");
            builder.add("prediction", consultation.getPrediction() != null ? consultation.getPrediction() : "");
            builder.add("niveauAmour", consultation.getNiveauAmour() != null ? consultation.getNiveauAmour() : 0);
            builder.add("niveauSante", consultation.getNiveauSante() != null ? consultation.getNiveauSante() : 0);
            builder.add("niveauTravail", consultation.getNiveauTravail() != null ? consultation.getNiveauTravail() : 0);
            builder.add("statut", consultation.getStatut() != null ? consultation.getStatut().toString() : "RESERVEE");
            
            // Client
            if (consultation.getClient() != null) {
                builder.add("clientNom", consultation.getClient().getNom());
                builder.add("clientPrenom", consultation.getClient().getPrenom());
                builder.add("clientTelephone", consultation.getClient().getNumeroTelephone());
                builder.add("clientMail", consultation.getClient().getMail());
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
                builder.add("mediumId", consultation.getMedium().getId());
                builder.add("mediumDenomination", consultation.getMedium().getDenomination());
                builder.add("mediumGenre", consultation.getMedium().getGenre());
                builder.add("mediumPresentation", consultation.getMedium().getPresentation());
            }
        } else {
            builder.add("id", 0);
            builder.add("message", "Aucune consultation en cours");
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter writer = response.getWriter();
        writer.write(builder.build().toString());
        writer.close();
    }
}
