/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package metier.modele;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

/**
 *
 * @author dsteferra
 */
@Entity
public class Client {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String mail;
    private String nom;
    private String prenom;
    private LocalDate dateDeNaissance;
    private String adressePostal;
    private Float adresseLongitude;
    private Float adresseLatitude;
    private String numeroTelephone;
    private String motDePasse;
    @Embedded
    private ProfilAstral profil;
    @OneToMany (mappedBy = "client")
    private List<Consultation> listeConsultations;

    public Client() {
    }
    
    
    public Client(String mail, String nom, String prenom, LocalDate dateDeNaissance, String adressePostal, String numeroTelephone, String motDePasse) {
        this.mail = mail;
        this.nom = nom;
        this.prenom = prenom;
        this.dateDeNaissance = dateDeNaissance;
        this.adressePostal = adressePostal;
        this.numeroTelephone = numeroTelephone;
        this.motDePasse = motDePasse;
        this.listeConsultations = new ArrayList<>();
    }
    
    public void appendListeConsultations(Consultation c) {
        this.listeConsultations.add(c);
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setDateDeNaissance(LocalDate dateDeNaissance) {
        this.dateDeNaissance = dateDeNaissance;
    }

    public void setAdressePostal(String adressePostal) {
        this.adressePostal = adressePostal;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public void setProfil(ProfilAstral profil) {
        this.profil = profil;
    }
    
    public void setAdresseLongitude(Float AdresseLongitude) {
        this.adresseLongitude = AdresseLongitude;
    }

    public void setAdresseLatitude(Float AdresseLatitude) {
        this.adresseLatitude = AdresseLatitude;
    }

    public ProfilAstral getProfil() {
        return profil;
    }

    public List<Consultation> getListeConsultations() {
        return listeConsultations;
    }
    
    
    
    
    
    public String getMail() {
        return mail;
    }
    
    public LocalDate getDateDeNaissance() {
        return dateDeNaissance;
    }

    public String getAdressePostal() {
        return adressePostal;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public Float  getAdresseLongitude() {
        return adresseLongitude;
    }

    public Float getAdresseLatitude() {
        return adresseLatitude;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", mail=" + mail + ", nom=" + nom + ", prenom=" + prenom + ", dateDeNaissance=" + dateDeNaissance + ", adressePostal=" + adressePostal + ", adresseLongitude=" + adresseLongitude+ ", adresseLatitude=" + adresseLatitude + ", numeroTelephone=" + numeroTelephone + ", motDePasse=" + motDePasse + ", profil=" + profil + '}';
    }
    
    public List<Consultation> getConsultationsByMedium(Medium m) {
        List<Consultation> lc = new ArrayList<>(this.listeConsultations);
        for (Consultation c : lc) {
            if (c.getMedium() != m) {
                lc.remove(c);
            }
        }
        return lc;
    }
    
    
}
