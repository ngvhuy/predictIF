package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import metier.modele.Consultation;
import metier.service.Service;

public class GenererPredictionAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        try {
            HttpSession session = request.getSession(false);
            
            // Assurez-vous que l'attribut correspond à celui défini à la connexion (ex: idEmploye ou idUtilisateur)
            if (session != null && session.getAttribute("idEmploye") != null) {
                Long idEmploye = (Long) session.getAttribute("idEmploye");
                
                int amour = Integer.parseInt(request.getParameter("amour"));
                int sante = Integer.parseInt(request.getParameter("sante"));
                int travail = Integer.parseInt(request.getParameter("travail"));
                
                Service service = new Service();
                Consultation consultation = service.getConsultationActuelle(idEmploye);
                
                if (consultation != null && consultation.getClient() != null) {
                    String prediction = service.getPredictionEnCasPanneInspiration(
                            consultation.getClient().getId(), amour, sante, travail);
                    request.setAttribute("prediction", prediction);
                } else {
                    request.setAttribute("erreur", "Aucune consultation en cours.");
                }
            } else {
                request.setAttribute("erreur", "Utilisateur non connecté ou non employé.");
            }
        } catch (Exception e) {
            request.setAttribute("erreur", "Paramètres invalides ou erreur interne.");
        }
    }
}