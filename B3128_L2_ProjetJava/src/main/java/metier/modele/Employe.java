/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package metier.modele;

import java.util.ArrayList;
import java.util.List;
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
public class Employe {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String nom;
    private String prenom;
    private String sexe;
    private String mail;
    private String numeroTelephone;
    private String password;
    @OneToMany (mappedBy = "employe")
    private List<Consultation> listeConsultations;
    private Boolean estEnConsultation;

    public Employe() {
    }

    public Employe(String nom, String prenom, String sexe, String numeroTelephone, String mail, String password) {
        this.nom = nom;
        this.prenom = prenom;
        this.sexe = sexe;
        this.numeroTelephone = numeroTelephone;
        this.mail = mail;
        this.password = password;
        this.listeConsultations = new ArrayList<>();
        this.estEnConsultation = false;
    }

    public void appendListeConsultations(Consultation c) {
        this.listeConsultations.add(c);
    }

    public void setEstEnConsultation(Boolean estEnConsultation) {
        this.estEnConsultation = estEnConsultation;
    }

    public List<Consultation> getListeConsultations() {
        return listeConsultations;
    }

    public Boolean getEstEnConsultation() {
        return estEnConsultation;
    }

    public void setListeConsultations(List<Consultation> listeConsultations) {
        this.listeConsultations = listeConsultations;
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

    public String getSexe() {
        return sexe;
    }

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    @Override
    public String toString() {
        return "Employe{" + "id=" + id + ", nom=" + nom + ", prenom=" + prenom + ", sexe=" + sexe + ", numeroTelephone=" + numeroTelephone + ", listeConsultations=" + listeConsultations + ", estEnConsultation=" + estEnConsultation + '}';
    }
    
    
    
}
