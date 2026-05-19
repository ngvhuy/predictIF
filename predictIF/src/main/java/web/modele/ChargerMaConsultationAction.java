package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import metier.modele.Consultation;
import metier.service.Service;

public class ChargerMaConsultationAction extends Action {
    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long idEmploye = (Long) session.getAttribute("idEmploye");
        
        if (idEmploye != null) {
            Service service = new Service();
            Consultation consultation = service.getConsultationActuelle(idEmploye);
            request.setAttribute("consultation", consultation);
        }
    }
}
