package web.vue;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import metier.modele.Client;
import metier.modele.Employe;
import metier.modele.Medium;

public class DashboardSerialisation extends Serialisation {

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

        Map<Medium, Integer> nbParMedium = (Map<Medium, Integer>) request.getAttribute("nbParMedium");
        Double moyenne = (Double) request.getAttribute("moyenne");
        List<Client> topClients = (List<Client>) request.getAttribute("topClients");
        Map<Employe, Integer> repartition = (Map<Employe, Integer>) request.getAttribute("repartition");

        JsonArrayBuilder topMediumsJson = Json.createArrayBuilder();
        if (nbParMedium != null) {
            List<Map.Entry<Medium, Integer>> sorted = new ArrayList<>(nbParMedium.entrySet());
            sorted.sort(Comparator.comparingInt((Map.Entry<Medium, Integer> e) -> e.getValue()).reversed());
            int limite = Math.min(5, sorted.size());
            for (int i = 0; i < limite; i++) {
                Medium m = sorted.get(i).getKey();
                Integer nb = sorted.get(i).getValue();
                topMediumsJson.add(Json.createObjectBuilder()
                        .add("denomination", m.getDenomination() != null ? m.getDenomination() : "")
                        .add("nbConsultations", nb != null ? nb : 0)
                );
            }
        }

        JsonArrayBuilder topClientsJson = Json.createArrayBuilder();
        if (topClients != null) {
            for (Client c : topClients) {
                int nb = c.getListeConsultations() != null ? c.getListeConsultations().size() : 0;
                topClientsJson.add(Json.createObjectBuilder()
                        .add("nom", c.getNom() != null ? c.getNom() : "")
                        .add("prenom", c.getPrenom() != null ? c.getPrenom() : "")
                        .add("nbConsultations", nb)
                );
            }
        }

        JsonArrayBuilder repartitionJson = Json.createArrayBuilder();
        if (repartition != null) {
            for (Map.Entry<Employe, Integer> e : repartition.entrySet()) {
                Employe emp = e.getKey();
                String label = (emp.getPrenom() != null ? emp.getPrenom() : "")
                        + " " + (emp.getNom() != null ? emp.getNom() : "");
                repartitionJson.add(Json.createObjectBuilder()
                        .add("label", label.trim())
                        .add("nb", e.getValue() != null ? e.getValue() : 0)
                );
            }
        }

        JsonObjectBuilder result = Json.createObjectBuilder()
                .add("topMediums", topMediumsJson)
                .add("moyenneConsultParMedium", (moyenne != null && !moyenne.isNaN()) ? moyenne : 0.0)
                .add("topClients", topClientsJson)
                .add("repartitionEmployes", repartitionJson);

        pw.write(result.build().toString());
        pw.close();
    }
}
