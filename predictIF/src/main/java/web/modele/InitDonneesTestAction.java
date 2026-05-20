package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import metier.modele.Client;
import metier.modele.Consultation;
import metier.modele.Employe;
import metier.modele.Medium;
import metier.service.Service;

/**
 * Endpoint d'initialisation des données de test.
 * Appeler une fois via GET /predictIF/ActionServlet?todo=init-donnees-test
 *
 * Employés connus: yoan.guerin@insa-lyon.fr/mdpyoan et paul.retourne@insa-lyon.fr/mdppaul (sexe H)
 * Seuls les médiums genre H peuvent être consultés (findEmployeLibre filtre par sexe).
 */
public class InitDonneesTestAction extends Action {

    private static final String[][] CLIENTS_TEST = {
        // mail, nom, prenom, dateNaissance, adresse, tel, mdp
        {"alice.pascal@predict.fr",  "Pascal",  "Alice",  "1990-03-15", "13 rue Albert Einstein 69100 Villeurbanne", "0611223344", "password123"},
        {"bob.martin@predict.fr",    "Martin",  "Bob",    "1985-07-22", "12 rue de la Paix 75002 Paris",             "0655667788", "password123"},
        {"claire.dubois@predict.fr", "Dubois",  "Claire", "1978-11-30", "5 place Bellecour 69002 Lyon",             "0699887766", "password123"},
        {"david.leroy@predict.fr",   "Leroy",   "David",  "1995-05-10", "3 cours Mirabeau 13100 Aix-en-Provence",   "0677889900", "password123"},
        {"emma.simon@predict.fr",    "Simon",   "Emma",   "2000-09-25", "10 cours Victor Hugo 33000 Bordeaux",      "0633445566", "password123"}
    };

    @Override
    public void execute(HttpServletRequest request) {
        Service service = new Service();
        StringBuilder rapport = new StringBuilder();

        // --- 1. Créer les clients de test ---
        rapport.append("=== CLIENTS ===\n");
        for (String[] c : CLIENTS_TEST) {
            Boolean ok = service.inscrireClient(
                c[0], c[1], c[2],
                LocalDate.parse(c[3]),
                c[4], c[5], c[6]
            );
            rapport.append(ok
                ? "✓ Créé : " + c[2] + " " + c[1] + "\n"
                : "~ Existant : " + c[2] + " " + c[1] + "\n");
        }

        // --- 2. Récupérer clients et médiums H ---
        Client alice  = service.authentifierClient("alice.pascal@predict.fr",  "password123");
        Client bob    = service.authentifierClient("bob.martin@predict.fr",     "password123");
        Client claire = service.authentifierClient("claire.dubois@predict.fr",  "password123");
        Client david  = service.authentifierClient("david.leroy@predict.fr",    "password123");
        Client emma   = service.authentifierClient("emma.simon@predict.fr",     "password123");

        List<Medium> mediumsH = service.getAllMedium().stream()
                .filter(m -> "H".equals(m.getGenre()))
                .collect(Collectors.toList());

        if (mediumsH.isEmpty()) {
            rapport.append("\n⚠ Aucun médium de genre H trouvé. Lancez d'abord initialiserMedium().\n");
            request.setAttribute("rapport", rapport.toString());
            return;
        }

        Employe yoan = service.authentifierEmploye("yoan.guerin@insa-lyon.fr", "mdpyoan");
        Employe paul = service.authentifierEmploye("paul.retourne@insa-lyon.fr", "mdppaul");

        if (yoan == null || paul == null) {
            rapport.append("\n⚠ Employés de test introuvables. Lancez d'abord initialiserEmploye().\n");
            request.setAttribute("rapport", rapport.toString());
            return;
        }

        Medium m1 = mediumsH.get(0);
        Medium m2 = mediumsH.size() > 1 ? mediumsH.get(1) : m1;

        rapport.append("\n=== CONSULTATIONS ===\n");

        // Consultation 1 : Alice + m1 → terminée
        creerConsultationTerminee(service, alice, m1, yoan, "Client très réceptif, bonne séance.", rapport);

        // Consultation 2 : Bob + m2 → terminée
        creerConsultationTerminee(service, bob, m2, paul, "Client attentif, bonnes vibrations.", rapport);

        // Consultation 3 : Claire + m1 → terminée
        creerConsultationTerminee(service, claire, m1, yoan, "Excellente énergie, messages clairs.", rapport);

        // Consultation 4 : Emma + m2 → terminée
        creerConsultationTerminee(service, emma, m2, paul, "Séance productive, client satisfait.", rapport);

        // Consultation 5 : Alice + m2 → terminée (2ème pour Alice, historique)
        creerConsultationTerminee(service, alice, m2, paul, "Deuxième consultation d'Alice, toujours réceptive.", rapport);

        // Consultation 6 : Bob + m1 → terminée (2ème pour Bob)
        creerConsultationTerminee(service, bob, m1, yoan, "Bob progresse bien.", rapport);

        // Consultation 7 : David + m1 → ACTIVE (pour tester maConsultation)
        rapport.append("\n--- Consultation active pour David ---\n");
        boolean ok = Boolean.TRUE.equals(service.demanderConsultation(david.getId(), m1.getId()));
        if (ok) {
            Consultation actuelle = service.getConsultationActuelle(yoan.getId());
            if (actuelle != null) {
                service.seMettrePret(yoan.getId(), actuelle);
                rapport.append("✓ Consultation ACTIVE créée pour David (employé: Yoan)\n");
                rapport.append("  → Connectez-vous avec yoan.guerin@insa-lyon.fr / mdpyoan pour la tester\n");
            }
        } else {
            rapport.append("~ Yoan occupé ou médium indisponible pour David\n");
        }

        request.setAttribute("rapport", rapport.toString());
    }

    private void creerConsultationTerminee(Service service, Client client, Medium medium,
                                            Employe employe, String commentaire, StringBuilder rapport) {
        if (client == null) return;
        try {
            Boolean ok = service.demanderConsultation(client.getId(), medium.getId());
            if (!ok) {
                rapport.append("~ Pas d'employé libre pour ").append(client.getPrenom()).append(" + ").append(medium.getDenomination()).append("\n");
                return;
            }
            Consultation c = service.getConsultationActuelle(employe.getId());
            if (c == null) {
                rapport.append("~ Consultation introuvable pour ").append(client.getPrenom()).append("\n");
                return;
            }
            service.seMettrePret(employe.getId(), c);
            c = service.getConsultationActuelle(employe.getId());
            if (c != null) {
                service.finirConsultation(employe.getId(), c, commentaire);
                rapport.append("✓ Terminée : ").append(client.getPrenom()).append(" + ").append(medium.getDenomination()).append("\n");
            }
        } catch (Exception e) {
            rapport.append("✗ Erreur : ").append(client.getPrenom()).append(" — ").append(e.getMessage()).append("\n");
        }
    }
}
