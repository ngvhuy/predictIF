package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import metier.modele.Client;
import metier.modele.Employe;
import metier.service.Service;

public class Connexion extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        String mail = request.getParameter("mail");
        String password = request.getParameter("password");
        String profil = request.getParameter("profil");

        Service service = new Service();
        HttpSession session = request.getSession();

        if ("client".equals(profil)) {
            Client client = service.authentifierClient(mail, password);
            if (client != null) {
                session.setAttribute("clientConnecte", client);
                session.setAttribute("profil", "client");
                request.setAttribute("succes", true);
                request.setAttribute("redirection", "espaceClient.html");
            } else {
                request.setAttribute("succes", false);
            }
        } else if ("employe".equals(profil)) {
            Employe employe = service.authentifierEmploye(mail, password);
            if (employe != null) {
                session.setAttribute("employeConnecte", employe);
                session.setAttribute("profil", "employe");
                request.setAttribute("succes", true);
                request.setAttribute("redirection", "espaceEmploye.html");
            } else {
                request.setAttribute("succes", false);
            }
        } else {
            request.setAttribute("succes", false);
        }
    }
}
