package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import metier.service.Service;

public class Inscription extends Action {

    private final Service service;

    public Inscription() {
        this.service = new Service();
    }

    public Inscription(Service service) {
        this.service = service;
    }

    @Override
    public void execute(HttpServletRequest request) {
        String mail = request.getParameter("mail");
        String nom = request.getParameter("nom");
        String prenom = request.getParameter("prenom");
        String dateStr = request.getParameter("dateDeNaissance");
        String adresse = request.getParameter("adresse");
        String telephone = request.getParameter("telephone");
        String password = request.getParameter("password");

        if (mail == null || mail.isBlank()
                || nom == null || nom.isBlank()
                || prenom == null || prenom.isBlank()
                || dateStr == null || dateStr.isBlank()
                || adresse == null || adresse.isBlank()
                || telephone == null || telephone.isBlank()
                || password == null || password.isBlank()) {
            request.setAttribute("succes", false);
            request.setAttribute("erreur", "Champs obligatoires manquants");
            return;
        }

        LocalDate dateDeNaissance;
        try {
            dateDeNaissance = LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            request.setAttribute("succes", false);
            request.setAttribute("erreur", "Date de naissance invalide");
            return;
        }

        Boolean inscrit = service.inscrireClient(mail, nom, prenom, dateDeNaissance, adresse, telephone, password);

        if (Boolean.TRUE.equals(inscrit)) {
            request.setAttribute("succes", true);
            request.setAttribute("redirection", "connexion.html");
        } else {
            request.setAttribute("succes", false);
            request.setAttribute("erreur", "Inscription échouée (compte déjà existant ou erreur technique)");
        }
    }
}
