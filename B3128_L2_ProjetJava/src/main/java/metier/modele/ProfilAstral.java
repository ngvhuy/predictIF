/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package metier.modele;

import javax.persistence.Embeddable;
import javax.persistence.Entity;

/**
 *
 * @author dsteferra
 */
@Embeddable
public class ProfilAstral {
    private String zodiac;
    private String signeChinois;
    private String couleur;
    private String animal;
    
    public ProfilAstral() {
    }

    public void setZodiac(String zodiac) {
        this.zodiac = zodiac;
    }

    public void setSigneChinois(String signeChinois) {
        this.signeChinois = signeChinois;
    }

    public void setCouleur(String couleur) {
        this.couleur = couleur;
    }

    public void setAnimal(String animal) {
        this.animal = animal;
    }

    public String getZodiac() {
        return zodiac;
    }

    public String getSigneChinois() {
        return signeChinois;
    }

    public String getCouleur() {
        return couleur;
    }

    public String getAnimal() {
        return animal;
    }

    @Override
    public String toString() {
        return "ProfilAstral{" + "zodiac=" + zodiac + ", signeChinois=" + signeChinois + ", couleur=" + couleur + ", animal=" + animal + '}';
    }
    
    
    
}
