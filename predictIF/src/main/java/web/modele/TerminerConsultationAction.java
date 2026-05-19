package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import metier.modele.Consultation;
import metier.service.Service;

public class TerminerConsultationAction extends Action {
    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return;
        Long idEmploye = (Long) session.getAttribute("idEmploye");
        if (idEmploye == null) return;

        String commentaire = request.getParameter("commentaire");

        Service service = new Service();
        Consultation consultation = service.getConsultationActuelle(idEmploye);
        if (consultation != null) {
            service.finirConsultation(idEmploye, consultation, commentaire != null ? commentaire : "");
            request.setAttribute("succes", true);
        }
    }
}
