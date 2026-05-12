/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package metier.modele;

import javax.persistence.Entity;

/**
 *
 * @author dsteferra
 */
@Entity
public class Astrologue extends Medium {
    private String formation;
    private String promotion;

    public Astrologue() {
    }
    
    
    public Astrologue(String formation, String promotion, String denominiation, String genre, String presentation) {
        super(denominiation, genre, presentation);
        this.formation = formation;
        this.promotion = promotion;
    }

    @Override
    public String toString() {
        return "Astrologue{" + super.toString() + "formation=" + formation + ", promotion=" + promotion + '}';
    }
    
    
    
    public String getFormation() {
        return formation;
    }

    public String getPromotion() {
        return promotion;
    }

    public void setFormation(String formation) {
        this.formation = formation;
    }

    public void setPromotion(String promotion) {
        this.promotion = promotion;
    }
    
    
}
