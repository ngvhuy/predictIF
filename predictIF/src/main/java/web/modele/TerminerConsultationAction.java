package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import java.time.LocalTime;
import metier.modele.Consultation;
import metier.service.Service;

public class TerminerConsultationAction extends Action {
    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession();
        Long idEmploye = (Long) session.getAttribute("idEmploye");
        String commentaire = request.getParameter("commentaire");
        String prediction = request.getParameter("prediction");
        String niveauAmour = request.getParameter("niveauAmour");
        String niveauSante = request.getParameter("niveauSante");
        String niveauTravail = request.getParameter("niveauTravail");
        
        if (idEmploye != null) {
            Service service = new Service();
            Consultation consultation = service.getConsultationActuelle(idEmploye);
            
            if (consultation != null) {
                consultation.setStatut(Consultation.StatutConsultation.CLOTUREE);
                consultation.setHeureFin(LocalTime.now());
                consultation.setCommentaire(commentaire);
                consultation.setPrediction(prediction);
                
                if (niveauAmour != null) {
                    consultation.setNiveauAmour(Integer.parseInt(niveauAmour));
                }
                if (niveauSante != null) {
                    consultation.setNiveauSante(Integer.parseInt(niveauSante));
                }
                if (niveauTravail != null) {
                    consultation.setNiveauTravail(Integer.parseInt(niveauTravail));
                }
                
                service.updateConsultation(consultation);
                request.setAttribute("success", true);
            }
        }
    }
}
