/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web.vue;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import metier.modele.Medium;
import metier.service.Service;

/**
 *
 * @author rguteville
 */
public class ListeMediumsSerialisation extends Serialisation {
    @Override
    public void appliquer(HttpServletRequest request, HttpServletResponse response) throws IOException {
        
        Service service = new Service();
        List<Medium> mediums = service.getAllMedium();
        
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Medium m : mediums) {
            arrayBuilder.add(Json.createObjectBuilder()
                .add("id", m.getId())
                .add("denomination", m.getDenomination())
                .add("genre", m.getGenre())
                .add("presentation", m.getPresentation())
            );
        }
        JsonArray jsonArray = arrayBuilder.build();
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter writer = response.getWriter();
        writer.write(jsonArray.toString());
        writer.close();
    }
}
