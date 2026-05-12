/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import web.test.DemandeTest;
import web.test.ServiceTest;

/**
 *
 * @author rguteville
 */
public class ConsulterListeDemandesAction extends Action {
    @Override
    public void execute(HttpServletRequest request) {
        ServiceTest service = new ServiceTest();
        List<DemandeTest> demandes = service.listerDemandes();
        
        request.setAttribute("demandes", demandes);
        
        for (DemandeTest d : demandes) {
            System.out.println(d);
        }
    }
}
