/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package metier.service;

import dao.JpaUtil;
import metier.modele.Client;
import dao.ClientDao;
import dao.ConsultationDao;
import dao.EmployeDao;
import dao.MediumDao;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import metier.modele.Astrologue;
import metier.modele.Cartomancien;
import metier.modele.Consultation;
import metier.modele.Medium;
import metier.modele.ProfilAstral;
import metier.modele.Spirite;
import metier.modele.Employe;
import util.Message;

/**
 *
 * @author dsteferra
 */
public class Service {
    
    private ProfilAstral calculProfilAstral(Client client) {
        JsonObject result = null;
        ProfilAstral profil = new ProfilAstral();
        
        try {
            // TODO: adapter l'URL de l'API et la liste des paramètres
            URI requestUri = URI.create(
                    "https://servif.insa-lyon.fr/WebDataGenerator/Astro"
                    + "?service=" + URLEncoder.encode("profil", StandardCharsets.UTF_8)
                    + "&key=" + URLEncoder.encode("ASTRO-01-M0lGLURBU0ktQVNUUk8tQjAx", StandardCharsets.UTF_8)
                    + "&prenom=" + URLEncoder.encode(client.getPrenom(), StandardCharsets.UTF_8)
                    + "&date-naissance=" + URLEncoder.encode(client.getDateDeNaissance().toString(), StandardCharsets.UTF_8)
            );

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder(requestUri).GET().build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                String body = httpResponse.body();
                //System.out.println(body);

                result = Json.createReader(new StringReader(body)).readObject();
                
                //System.out.println(result.toString());
                //{"profil":{"signe-zodiaque":"Capricorne","signe-chinois":"Chèvre","couleur":"Abricot","animal":"Kiwi"}}
                JsonObject obj = result.getJsonObject("profil");
                profil.setAnimal(obj.getString("animal"));
                profil.setCouleur(obj.getString("couleur"));
                profil.setSigneChinois(obj.getString("signe-chinois"));
                profil.setZodiac(obj.getString("signe-zodiaque"));
                
            } else {
                throw new IOException("HTTP Error Status Code " + httpResponse.statusCode());
            }

        } catch (Exception ex) {
            ex.printStackTrace(System.err);
            profil = null;
        }
        
        return profil;
    }
    
    private void mettreCoordonneeClient(Client client) {
        // transformation de l'adresse en coordonnées géographiques
        List<List<Float>> coordonnes = new ArrayList<>();
        JsonObject result = null;
        
        JsonArray cordJson;
        try {
            // TODO: adapter l'URL de l'API et la liste des paramètres
            URI requestUri = URI.create(
                    "https://data.geopf.fr/geocodage/search"
                    + "?autocomplete=" + URLEncoder.encode("0", StandardCharsets.UTF_8)
                    + "&index=" + URLEncoder.encode("address", StandardCharsets.UTF_8)
                    + "&limit=" + URLEncoder.encode("1", StandardCharsets.UTF_8)
                    + "&returntruegeometry=" + URLEncoder.encode("false", StandardCharsets.UTF_8)
                    + "&q=" + URLEncoder.encode(client.getAdressePostal(), StandardCharsets.UTF_8)
            );

            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest httpRequest = HttpRequest.newBuilder(requestUri).GET().build();
            HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() == 200) {
                String body = httpResponse.body();
                //System.out.println(body);

                result = Json.createReader(new StringReader(body)).readObject();

                //System.out.println(result.toString());
                //{"type":"FeatureCollection","features":[{"type":"Feature","geometry":{"type":"Point","coordinates":[1.37987,43.579724]},"properties":{"label":"13 Rue Albert Einstein 31100 Toulouse","score":0.9761863636363635,"housenumber":"13","id":"31555_0110_00013","banId":"51cef5c0-41ff-491d-96d9-a08eac41e0ff","name":"13 Rue Albert Einstein","postcode":"31100","citycode":"31555","x":569106.47,"y":6276972.18,"city":"Toulouse","context":"31, Haute-Garonne, Occitanie","type":"housenumber","importance":0.73805,"depcode":"31","street":"Rue Albert Einstein","_type":"address"}}],"query":"13 rue Albert Einstein"}                    //String pred = result.getString("prediction-amour");
                //{"type":"FeatureCollection","features":[],"query":"hvgv"}
                if (!result.getJsonArray("features").isEmpty()) {
                    cordJson = result.getJsonArray("features").getJsonObject(0).getJsonObject("geometry").getJsonArray("coordinates");
                    client.setAdresseLongitude((float) cordJson.getJsonNumber(0).doubleValue());
                    client.setAdresseLatitude((float) cordJson.getJsonNumber(1).doubleValue());

                }

            } else {
                throw new IOException("HTTP Error Status Code " + httpResponse.statusCode());
            }

        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }
    
    public Boolean inscrireClient(String mail, String nom, String prenom, LocalDate dateDeNaissance, String adressePostal, String numeroTelephone, String motDePasse) {
        System.out.println("metier.service.Service.inscrireClient()");
        Boolean clientInscrit = false; 
        
        Client client = new Client(mail, nom, prenom, dateDeNaissance, adressePostal, numeroTelephone, motDePasse);
        
        try {
            
            mettreCoordonneeClient(client);
            ProfilAstral profil = new ProfilAstral();
            profil = calculProfilAstral(client);
            
            if (profil != null) {
                client.setProfil(profil);
            }
            else {
                throw new Exception("Le profil n'a pas pu être créé.");
            }
            
            
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            
            // si le client n'exite pas déja dans la base
            try {
                Client clientExistant = ClientDao.findByMailAndPassword( client.getMail(), client.getMotDePasse());
                clientInscrit = false;
                System.out.println("Le client est déjà inscrit.");
            }
            catch (Exception e) {
                
                ClientDao.create(client);
                JpaUtil.validerTransaction();
                clientInscrit = true;
            }
          
            //System.out.println(profil.toString());
            
            
        } catch (Exception e) {
            e.printStackTrace();
            JpaUtil.annulerTransaction();
        }
        finally {
            JpaUtil.fermerContextePersistance();
        }
        
        String objet;
        String corps;
        if (clientInscrit) {
            objet = "Binevenue chez PREDICT'IF";
            corps = "Bonjour " + prenom + ", nous vous confirmons votre iscription au service PREDICT'IF. Rendez-vous vitesur notre site pour consulter votre profile astrologique et profiter des dons incroyables de nos mediums.";
        }
        else {
            objet = "Echec de l'inscription chez PREDICT'IF";
            corps = "Bonjour " + prenom + ", votre inscription au service PREDICT'IF a malencontreusement échoué... Merci de recommencer ultérieurement ou de vérifier si vous avez pas déjà un compte.";
        }
        Message.envoyerMail("contact@predictif.if", mail, objet, corps);
        return clientInscrit;
    }
    
    public Client authentifierClient(String mail, String password) {
        Client client = null;
        try {
            System.out.println("metier.service.Service.authentifierClient()");
            
            JpaUtil.creerContextePersistance();

            client = ClientDao.findByMailAndPassword(mail, password);

            //System.out.println(profil.toString());
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        finally {
            JpaUtil.fermerContextePersistance();
        }
        return client;
    }
    
    public Employe authentifierEmploye(String mail, String password) {
        Employe employe = null;
        try {
            System.out.println("metier.service.Service.authentifierEmploye()");
            
            JpaUtil.creerContextePersistance();

            employe = EmployeDao.findByMailAndPassword(mail, password);

            //System.out.println(profil.toString());
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        finally {
            JpaUtil.fermerContextePersistance();
        }
        return employe;
    }
    
    public void initialiserMedium() {
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            //Gwenaelle
            Spirite med1 = new Spirite("Boule de cristal", "Gwenaelle", "F", "Spécialiste des grandes conversations au-delà de TOUTES les frontières");
            MediumDao.create(med1);
            
            //Professeur Tran
            Spirite med2 = new Spirite("Marc de café, boule de cristal, oreilles de lapin", "Professeur Tran", "H", "Votre avenir est devant vous : regardonds-le ensemble !");
            MediumDao.create(med2);
            
            //Mme Irma
            Cartomancien med3 = new Cartomancien("Mme Irma", "F", "Comprenez votre entourage grâce à mes cartes ! Résultats rapides");
            MediumDao.create(med3);
            
            //Endora
            Cartomancien med4 = new Cartomancien("Endora", "F", "Mes cartes répondront à toutes vos questions personnelles.");
            MediumDao.create(med4);
            
            //Serena
            Astrologue med5 = new Astrologue("Ecole Normale Supérieure d'Astrologie (ENS-Astro)", "2006", "Serena", "F", "Basée à  Champigny-sur-Marne, Serena vous révèlera votre avenir pour éclairer votre passé.");
            MediumDao.create(med5);
            
            //Mr M
            Astrologue med6 = new Astrologue("Institut des Nouveaux Savoirs Astrologiques (INSA)", "2010", "Mr M", "H", "Avenir, avenir, que nous réserves-tu ? N'attendez plus, demander à me consulter !");
            MediumDao.create(med6);

            JpaUtil.validerTransaction();
          
        } catch (Exception e) {
            e.printStackTrace(System.err);
            JpaUtil.annulerTransaction();
        }
        finally {
            JpaUtil.fermerContextePersistance();
        }
    }
    
    
    
    public void initialiserEmploye() {
        try {
            JpaUtil.creerContextePersistance();
            JpaUtil.ouvrirTransaction();
            
            
            Employe e1 = new Employe("Guerin", "Yoan", "H", "0123456789", "yoan.guerin@insa-lyon.fr", "mdpyoan");
            EmployeDao.create(e1);
            
            Employe e2 = new Employe("Retourné", "Paul", "H", "0987654321", "paul.retourne@insa-lyon.fr", "mdppaul");
            EmployeDao.create(e2);

            
            JpaUtil.validerTransaction();
          
        } catch (Exception e) {
            e.printStackTrace(System.err);
            JpaUtil.annulerTransaction();
        }
        finally {
            JpaUtil.fermerContextePersistance();
        }
    }
    
    public Medium getMediumById(Long id) {
        Medium medium = null;
        try {
            System.out.println("metier.service.Service.getMediumById()");
            
            JpaUtil.creerContextePersistance();

            medium = MediumDao.findById(id);

            //System.out.println(profil.toString());
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        finally {
            JpaUtil.fermerContextePersistance();
        }
        return medium;
    }
    
    public List<Medium> getAllMedium() {
        List<Medium> mediums = new ArrayList<>();
        try {
            System.out.println("metier.service.Service.getAllMedium()");
            
            JpaUtil.creerContextePersistance();

            mediums = MediumDao.findAll();

            //System.out.println(profil.toString());
            
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
        finally {
            JpaUtil.fermerContextePersistance();
        }
        return mediums;
    }
    
    public Consultation getConsultationActuelle(Long idEmploye) {
        Employe employe = null;
        Consultation consultation = null;
        try {
            JpaUtil.creerContextePersistance();
            // Récupération des objets via les ID
            employe = EmployeDao.findById(idEmploye);
        } catch (Exception e) {
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        for (Consultation c : employe.getListeConsultations()) {
            if (c.getHeureFin() == null){
                consultation = c;
            }
        }
        return consultation;
    }
    
    public Boolean demanderConsultation(Long idClient, Long idMedium) {
        Boolean employeAffecte = false;
        try {
            System.out.println("metier.service.Service.demanderConsultation()");

            JpaUtil.creerContextePersistance();

            // Récupération des objets via les ID
            Client client = ClientDao.findById(idClient);
            Medium medium = MediumDao.findById(idMedium);

            Consultation consultation = new Consultation(client, medium);
            Employe employeLibre = EmployeDao.findEmployeLibre(medium.getGenre());
            consultation.setEmploye(employeLibre);

            employeLibre.setEstEnConsultation(Boolean.TRUE);
            employeLibre.appendListeConsultations(consultation);

            client.appendListeConsultations(consultation);

            JpaUtil.ouvrirTransaction();

            ConsultationDao.create(consultation);

            EmployeDao.update(employeLibre);
            ClientDao.update(client);

            employeAffecte = true;
            System.out.println("l'employe affecte est : " + employeLibre);
            JpaUtil.validerTransaction();
            String message = "Bonjour " + employeLibre.getPrenom() + ". Consultation requise pour " + client.getPrenom() + " " + client.getNom() + ". Médium à incarner : " + medium.getDenomination();
            Message.envoyerNotification(employeLibre.getNumeroTelephone(), message);
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace(System.err);
            System.out.println("pas d'employe affecté");
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return employeAffecte;
    }
    
    public List<Consultation> getConsultationsClientMedium(Long idClient, Long idMedium) {
        System.out.println("metier.service.Service.demanderConsultation()");
        List<Consultation> consultationsMedium = new ArrayList<>();
        Client client = null;
        Medium medium = null;
        try {
            JpaUtil.creerContextePersistance();
            // Récupération des objets via les ID
            client = ClientDao.findById(idClient);
            medium = MediumDao.findById(idMedium);
        } catch (Exception e) {
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        for (Consultation c : client.getListeConsultations()) {
            if (c.getMedium().equals(medium)) {
                consultationsMedium.add(c);
            }
        }
        return consultationsMedium;
    }
    
    public List<Consultation> getConsultationsClient(Long idClient) {
        List<Consultation> consultations = new ArrayList<>();
        try {
            JpaUtil.creerContextePersistance();
            Client client = ClientDao.findById(idClient);
            if (client != null) {
                consultations = client.getListeConsultations();
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        return consultations;
    }

    public List<Client> getAllClients() {
        List<Client> clients = new ArrayList<>();
        System.out.println("metier.service.Service.getAllClients()");
        
        try {
            JpaUtil.creerContextePersistance();
            clients = ClientDao.findAll();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        return clients;
    }
    
    public Boolean seMettrePret(Long idEmploye, Consultation consultation) {
        System.out.println("metier.service.Service.seMettrePret()");
        Boolean aPuSeMettrePret = false;
        LocalTime deb_heure = LocalTime.now();

        try {
            JpaUtil.creerContextePersistance();
            Employe employe = EmployeDao.findById(idEmploye);

            if (employe.getEstEnConsultation()) {
                consultation.setHeureDebut(deb_heure);

                String message = "Bonjour " + consultation.getClient().getPrenom() + ". J'ai bien reçu votre demande de consultation du " + consultation.getDate() + "à " + consultation.getHeureDebut() + ". Vous pouvez dès à présent me contacter au " + employe.getNumeroTelephone() + ". A tout de suite ! Médiumiquement vötre, " + consultation.getMedium().getDenomination();
                String telClient = consultation.getClient().getNumeroTelephone();
                Message.envoyerNotification(telClient, message);

                JpaUtil.ouvrirTransaction();
                ConsultationDao.update(consultation);
                JpaUtil.validerTransaction();
                aPuSeMettrePret = true;
            } else {
                System.out.println("L'employe que vous essayez de mettre pret n'est pas en consultation");
            }
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return aPuSeMettrePret;
    }
    
    public String getPredictionEnCasPanneInspiration(Long idClient, int amour, int sante, int travail) {
    JsonObject result = null;
    String prediction = "";
    
    try {
        JpaUtil.creerContextePersistance();
        Client client = ClientDao.findById(idClient);
        
        String couleur = client.getProfil().getCouleur();
        String animal = client.getProfil().getAnimal();
        
        URI requestUri = URI.create(
                "https://servif.insa-lyon.fr/WebDataGenerator/Astro"
                + "?service=" + URLEncoder.encode("predictions", StandardCharsets.UTF_8)
                + "&key=" + URLEncoder.encode("ASTRO-01-M0lGLURBU0ktQVNUUk8tQjAx", StandardCharsets.UTF_8)
                + "&couleur=" + URLEncoder.encode(couleur, StandardCharsets.UTF_8)
                + "&animal=" + URLEncoder.encode(animal, StandardCharsets.UTF_8)
                + "&niveau-amour=" + URLEncoder.encode(Integer.toString(amour), StandardCharsets.UTF_8)
                + "&niveau-sante=" + URLEncoder.encode(Integer.toString(sante), StandardCharsets.UTF_8)
                + "&niveau-travail=" + URLEncoder.encode(Integer.toString(travail), StandardCharsets.UTF_8)
        );

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder(requestUri).GET().build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (httpResponse.statusCode() == 200) {
            String body = httpResponse.body();
            result = Json.createReader(new StringReader(body)).readObject();
            
            String pred = result.getString("prediction-amour");
            prediction += "Prediction amour : \n" + pred + "\n";
            pred = result.getString("prediction-sante");
            prediction += "Prediction sante : \n" + pred + "\n";
            pred = result.getString("prediction-travail");
            prediction += "Prediction travail : \n" + pred + "\n";
            
        } else {
            throw new IOException("HTTP Error Status Code " + httpResponse.statusCode());
        }

    } catch (Exception e) {
        e.printStackTrace(System.err);
        prediction = null;
    } finally {
        JpaUtil.fermerContextePersistance();
    }
    
    return prediction;
}
    
    public Boolean finirConsultation(Long idEmploye, Consultation consultation, String commentaire) {
        System.out.println("metier.service.Service.finirConsultation()");
        Boolean aPuFinirConsultation = false;
        LocalTime fin_heure = LocalTime.now();

        try {
            JpaUtil.creerContextePersistance();
            Employe employe = EmployeDao.findById(idEmploye);

            if (employe.getEstEnConsultation()) {
                consultation.setHeureFin(fin_heure);
                consultation.setCommentaire(commentaire);

                employe.setEstEnConsultation(false);

                JpaUtil.ouvrirTransaction();
                ConsultationDao.update(consultation);
                EmployeDao.update(employe);
                JpaUtil.validerTransaction();

                aPuFinirConsultation = true;
            } else {
                System.out.println("L'employe que vous essayez de mettre pret n'est pas en consultation");
            }
        } catch (Exception e) {
            JpaUtil.annulerTransaction();
            e.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return aPuFinirConsultation;
    }

    public List<Medium> getTop5Medium() {
        List<Medium> topMedium = new ArrayList<>();
        
        // get toutes les consultations
        List<Consultation> listConsultations = new ArrayList<>();
        try {
            JpaUtil.creerContextePersistance();
            listConsultations = ConsultationDao.findAll();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        // hashmap avec medium : nbConslt
        // pour pouvoir compter le nombre de consultations
        Map<Medium, Integer> nbConsultationParMedium = new HashMap<>();
        for (Consultation c : listConsultations) {
            if (nbConsultationParMedium.get(c.getMedium()) != null) {
                nbConsultationParMedium.put(c.getMedium(), nbConsultationParMedium.get(c.getMedium()) + 1);
            }
            else {
                nbConsultationParMedium.put(c.getMedium(), 1);
            }
        }
        List<Map.Entry<Medium, Integer>> sortedMediumConsultationNb = new ArrayList<>(nbConsultationParMedium.entrySet());
        sortedMediumConsultationNb.sort(Map.Entry.comparingByValue());
        
        //System.out.println("listeConsultations : " + listConsultations);
        //System.out.println("sortedMediumConsultationNb" + sortedMediumConsultationNb);
        for (int i = sortedMediumConsultationNb.size()-1; (i >= 0) && (i >= sortedMediumConsultationNb.size()-5); i--) {
            topMedium.add(sortedMediumConsultationNb.get(i).getKey());
        }
        
        return topMedium;
    }
    
    public Double getNbConsultationMoyenneParMedium() {
        // get toutes les consultations
        List<Consultation> listConsultations = new ArrayList<>();
        try {
            JpaUtil.creerContextePersistance();
            listConsultations = ConsultationDao.findAll();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        // hashmap avec medium : nbConslt
        // pour pouvoir compter le nombre de consultations
        Map<Medium, Integer> nbConsultationParMedium = new HashMap<>();
        for (Consultation c : listConsultations) {
            if (nbConsultationParMedium.get(c.getMedium()) != null) {
                nbConsultationParMedium.put(c.getMedium(), nbConsultationParMedium.get(c.getMedium()) + 1);
            }
            else {
                nbConsultationParMedium.put(c.getMedium(), 1);
            }
        }
        List<Map.Entry<Medium, Integer>> listMediumConsultationNb = new ArrayList<>(nbConsultationParMedium.entrySet());
        
        
        Double somme = 0.0;
        for (Map.Entry<Medium, Integer> nbConsltMedium : listMediumConsultationNb) {
            somme += nbConsltMedium.getValue();
        }
        
        return somme/listMediumConsultationNb.size();
    }
    
    public List<Client> getTop5Client() {
        List<Client> topClient = new ArrayList<>();
        
        // get touts les clients
        List<Client> listClients = new ArrayList<>();
        try {
            JpaUtil.creerContextePersistance();
            listClients = ClientDao.findAll();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }
        
        Client temp; 
        for (int i = 0; i<listClients.size()-1; i++) {
            for (int j = 0; j<listClients.size() - i - 1; j++) {
                if (listClients.get(j).getListeConsultations().size() < listClients.get(j+1).getListeConsultations().size()) {
                    temp = listClients.get(j);
                    listClients.set(j, listClients.get(j+1));
                    listClients.set(j+1, temp);
                }
            }
        }
        for (int i = 0; (i < 5) && (i < listClients.size()); i++) {
            topClient.add(listClients.get(i));
        }
        
        return topClient;
    }
    
    public Map<Employe, Integer> getNbConsultationParEmploye() {
        List<Employe> listEmploye;
        Map<Employe, Integer> repartition = new HashMap<>();

        try {
            JpaUtil.creerContextePersistance();
            listEmploye = EmployeDao.findAll();

            // On remplit la Map
            for (Employe emp : listEmploye) {
                // .size() donne le nombre de consultations dans la liste
                repartition.put(emp, emp.getListeConsultations().size());
            }
        } catch (Exception e) {
            e.printStackTrace(System.err);
        } finally {
            JpaUtil.fermerContextePersistance();
        }

        return repartition;
    }
}
    
