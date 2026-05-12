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
public class Cartomancien extends Medium {

    public Cartomancien() {
    }
    
    public Cartomancien(String denominiation, String genre, String presentation) {
        super(denominiation, genre, presentation);
    }

    @Override
    public String toString() {
        return "Cartomancien{" + super.toString() +'}';
    }
    
    
}
