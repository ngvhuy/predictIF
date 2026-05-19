package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import metier.modele.Client;
import metier.modele.Employe;
import metier.service.Service;

public class Connexion extends Action {

    private final Service service;

    public Connexion() {
        this.service = new Service();
    }

    public Connexion(Service service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request) {
        String mail = request.getParameter("mail");
        String password = request.getParameter("password");

        if (mail == null || mail.isBlank() || password == null || password.isBlank()) {
            request.setAttribute("succes", false);
            request.setAttribute("erreur", "Identifiants manquants");
            return;
        }

        HttpSession session = request.getSession();

        Client client = service.authentifierClient(mail, password);
        if (client != null) {
            session.setAttribute("clientConnecte", client);
            session.setAttribute("profil", "client");
            request.setAttribute("succes", true);
            request.setAttribute("redirection", "espaceClient.html");
            return;
        }

        Employe employe = service.authentifierEmploye(mail, password);
        if (employe != null) {
            session.setAttribute("employeConnecte", employe);
            session.setAttribute("profil", "employe");
            request.setAttribute("succes", true);
            request.setAttribute("redirection", "espaceEmploye.html");
            return;
        }

        request.setAttribute("succes", false);
        request.setAttribute("erreur", "Identifiants incorrects");
    }
}
