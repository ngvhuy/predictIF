package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import metier.modele.Client;
import metier.service.Service;

public class DemanderConsultationAction extends Action {
    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            request.setAttribute("succes", false);
            request.setAttribute("erreur", "Non connecté");
            return;
        }

        Client client = (Client) session.getAttribute("clientConnecte");
        if (client == null) {
            request.setAttribute("succes", false);
            request.setAttribute("erreur", "Non connecté");
            return;
        }

        String idMediumParam = request.getParameter("idMedium");
        if (idMediumParam == null) {
            request.setAttribute("succes", false);
            request.setAttribute("erreur", "Médium non spécifié");
            return;
        }

        try {
            Long idMedium = Long.parseLong(idMediumParam);
            Service service = new Service();
            Boolean succes = service.demanderConsultation(client.getId(), idMedium);
            request.setAttribute("succes", succes);
            if (!succes) {
                request.setAttribute("erreur", "Aucun employé disponible pour ce type de médium.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("succes", false);
            request.setAttribute("erreur", "Identifiant médium invalide");
        }
    }
}
