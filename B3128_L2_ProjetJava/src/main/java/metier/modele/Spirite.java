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
public class Spirite extends Medium {
    private String support;

    public Spirite() {
    }

    @Override
    public String toString() {
        return "Spirite{" + super.toString() + "support=" + support + '}';
    }
    
    
    public Spirite(String support, String denominiation, String genre, String presentation) {
        super(denominiation, genre, presentation);
        this.support = support;
    }
    
    

    public String getSupport() {
        return support;
    }

    public void setSupport(String support) {
        this.support = support;
    }

}
