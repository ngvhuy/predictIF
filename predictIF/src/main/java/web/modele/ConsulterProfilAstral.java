package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import metier.modele.Client;
import metier.modele.ProfilAstral;

public class ConsulterProfilAstral extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("clientConnecte") == null) {
            request.setAttribute("erreur", "non connecté");
            return;
        }
        Client client = (Client) session.getAttribute("clientConnecte");
        ProfilAstral profil = client.getProfil();
        request.setAttribute("profil", profil);
    }
}
