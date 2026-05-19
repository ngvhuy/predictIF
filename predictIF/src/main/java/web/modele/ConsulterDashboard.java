package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import metier.modele.Client;
import metier.modele.Employe;
import metier.modele.Medium;
import metier.service.Service;

public class ConsulterDashboard extends Action {

    private final Service service;

    public ConsulterDashboard() {
        this.service = new Service();
    }

    public ConsulterDashboard(Service service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request) {
        try {
            Map<Medium, Integer> nbParMedium = service.getNbConsultationParMedium();
            Double moyenne = service.getNbConsultationMoyenneParMedium();
            List<Client> topClients = service.getTop5Client();
            Map<Employe, Integer> repartition = service.getNbConsultationParEmploye();

            request.setAttribute("nbParMedium", nbParMedium);
            request.setAttribute("moyenne", moyenne);
            request.setAttribute("topClients", topClients);
            request.setAttribute("repartition", repartition);
        } catch (Exception e) {
            request.setAttribute("erreur", "Erreur lors du chargement du dashboard");
            e.printStackTrace(System.err);
        }
    }
}
