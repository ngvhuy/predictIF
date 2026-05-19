package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalTime;
import metier.modele.Consultation;
import metier.service.Service;

public class DemarrerConsultationAction extends Action {
    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long idEmploye = (Long) session.getAttribute("idEmploye");
        
        if (idEmploye != null) {
            Service service = new Service();
            Consultation consultation = service.getConsultationActuelle(idEmploye);
            
            if (consultation != null) {
                consultation.setStatut(Consultation.StatutConsultation.ACTIVE);
                consultation.setHeureDebut(LocalTime.now());
                service.updateConsultation(consultation);
                request.setAttribute("success", true);
            }
        }
    }
}
