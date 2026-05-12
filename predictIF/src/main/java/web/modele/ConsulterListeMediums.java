/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import metier.modele.Medium;
import metier.service.Service;

/**
 *
 * @author rguteville
 */
public class ConsulterListeMediums extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        Service service = new Service();
        List<Medium> mediums = service.getAllMedium();
        for (Medium m : mediums) {
            System.out.println("\n medium : " + m);
        }

        request.setAttribute("mediums", mediums);

    }
}
