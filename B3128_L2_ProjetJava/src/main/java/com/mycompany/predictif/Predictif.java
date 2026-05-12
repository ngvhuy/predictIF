/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.predictif;

import dao.JpaUtil;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import metier.modele.Client;
import metier.modele.Consultation;
import metier.modele.Employe;
import metier.modele.Medium;
import metier.service.Service;
import util.Saisie;

/**
 *
 * @author dsteferra
 */
public class Predictif {

    public static void main(String[] args) {
        JpaUtil.creerFabriquePersistance();
        Service service = new Service();

        
        service.initialiserMedium();
        service.initialiserEmploye();
        
//        scenarioTest();
        
        JpaUtil.fermerFabriquePersistance();
    }

    
    private static void scenarioTest () {
        Service service = new Service();
        System.out.println("\n-------------------------------------------------");
        
        //Inscription et authentification d'un client
        System.out.println("Inscription et authentification\n-------------------------------------------------");
        LocalDate date = LocalDate.parse("1802-02-26");
        //Client c1 = new Client("victor.hugo@insa-lyon.fr", "Hugo", "Victor", date, "13 rue Albert Einstein 69100 Villeurbanne", "01 23 45 67 89", "m0tdepasse1");
        Boolean resultat = service.inscrireClient("victor.hugo@insa-lyon.fr", "Hugo", "Victor", date, "13 rue Albert Einstein 69100 Villeurbanne", "01 23 45 67 89", "m0tdepasse1");
        assert resultat;
        Client c12 = service.authentifierClient("victor.hugo@insa-lyon.fr", "m0tdepasse1");
        System.out.println("\n-------------------------------------------------");
        
        // Inscription et authentification du deuxième client
        System.out.println("Inscription et authentification\n-------------------------------------------------");
        LocalDate date2 = LocalDate.parse("1802-02-26");
        //Client c2 = new Client("tableau.hugo@insa-lyon.fr", "Hugo", "Tableau", date2, "12 rue Albert Einstein 69100 Villeurbanne", "01 33 55 77 99", "m0tdepasse1");
        Boolean resultat2 = service.inscrireClient("tableau.hugo@insa-lyon.fr", "Hugo", "Tableau", date2, "12 rue Albert Einstein 69100 Villeurbanne", "01 33 55 77 99", "m0tdepasse1");
        assert resultat2;
        Client c22 = service.authentifierClient("tableau.hugo@insa-lyon.fr", "m0tdepasse1");
        System.out.println("\n-------------------------------------------------");
        
        System.out.println("liste des mediums\n-------------------------------------------------");
        //On récupère la liste des mediums
        List<Medium> mediums = service.getAllMedium();
        System.out.println(mediums);
        System.out.println("\n-------------------------------------------------");
        
        System.out.println("Choix du medium et demande de la consultation\n-------------------------------------------------");
        //Il choisit un medium et demande une consultation
        long id_medium = 2;
        Medium medium = service.getMediumById(id_medium);
        System.out.println(medium);
        service.demanderConsultation(c12.getId(), medium.getId());
        
        service.demanderConsultation(c22.getId(), medium.getId());
        System.out.println("\n-------------------------------------------------");
        
        
        System.out.println("Adresses clients\n-------------------------------------------------");
        // on devra peut-être l'effacer vu que c'est juste récupérer les adresses disctinctes
        System.out.println(service.getAllClients());
        System.out.println("\n-------------------------------------------------");
        
        // l'employe en consultation pour les deux prochains services
        Employe employe = service.authentifierEmploye("paul.retourne@insa-lyon.fr", "mdppaul");
        System.out.println("se mettre pret\n-------------------------------------------------");
        Consultation consultation_act = service.getConsultationActuelle(employe.getId());
        service.seMettrePret(employe.getId(), consultation_act);
        System.out.println("\n-------------------------------------------------");
        
        System.out.println("finir la consultation\n-------------------------------------------------");
        String commentaire = "J'ai prédit ça... elle a réagit comme ça...";
        Consultation consultation = service.getConsultationActuelle(employe.getId());
        service.finirConsultation(employe.getId(), consultation, commentaire);
        System.out.println("\n-------------------------------------------------");
        
        System.out.println("get une prediction\n-------------------------------------------------");
        String pred = service.getPredictionEnCasPanneInspiration(c22.getId(), 1, 2, 4);
        System.out.println(pred);
        System.out.println("\n-------------------------------------------------");
        
        System.out.println("get top 5 medium\n-------------------------------------------------");
        long id_medium2 = 6;
        Medium medium2 = service.getMediumById(id_medium2);
        System.out.println(medium2);
        service.demanderConsultation(c22.getId(), medium2.getId());
        System.out.println(service.getTop5Medium());
        System.out.println("\n-------------------------------------------------");
        
        System.out.println("get moy nb consultation par medium\n-------------------------------------------------");
        System.out.println(service.getNbConsultationMoyenneParMedium());
        System.out.println("\n-------------------------------------------------");
        
        System.out.println("get top 5 clients\n-------------------------------------------------");
        System.out.println(service.getTop5Client());
        System.out.println("\n-------------------------------------------------");
        
        
        System.out.println("get consults par employe");
        System.out.println(service.getNbConsultationParEmploye());
        System.out.println("\n-------------------------------------------------");
        
        System.out.println("get consults d'un client pour un medium donnée");
        System.out.println(service.getConsultationsClientMedium(c22.getId(), id_medium));
        System.out.println("\n-------------------------------------------------");
        /*String invite = "";
        Saisie.lireChaine(invite);*/
    }
    
//    private static void testerHistoConsultation() {
//        
//        service.getHistoriqueConsultationsById(c12);
//    }
}
