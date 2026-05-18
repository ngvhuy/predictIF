package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import metier.modele.Client;
import metier.modele.Consultation;
import metier.service.Service;

public class ConsulterHistoriqueClient extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("clientConnecte") == null) {
            request.setAttribute("erreur", "non connecté");
            return;
        }

        Client client = (Client) session.getAttribute("clientConnecte");
        Service service = new Service();
        List<Consultation> consultations = service.getConsultationsClient(client.getId());
        request.setAttribute("consultations", consultations);
    }
}
