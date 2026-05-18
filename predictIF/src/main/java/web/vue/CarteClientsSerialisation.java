package web.vue;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import metier.modele.Client;

public class CarteClientsSerialisation extends Serialisation {

    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        List<Client> clients = (List<Client>) request.getAttribute("clients");
        JsonArrayBuilder array = Json.createArrayBuilder();

        for (Client c : clients) {
            if (c.getAdresseLongitude() != null && c.getAdresseLatitude() != null) {
                array.add(Json.createObjectBuilder()
                    .add("nom", c.getNom() + " " + c.getPrenom())
                    .add("adresse", c.getAdressePostal() != null ? c.getAdressePostal() : "")
                    .add("lat", c.getAdresseLatitude())
                    .add("lng", c.getAdresseLongitude())
                );
            }
        }

        PrintWriter pw = response.getWriter();
        pw.write(array.build().toString());
        pw.close();
    }
}
