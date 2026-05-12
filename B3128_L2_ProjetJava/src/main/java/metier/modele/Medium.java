/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package metier.modele;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

/**
 *
 * @author dsteferra
 */
@Entity
@Inheritance (strategy = InheritanceType.JOINED) 
abstract public class Medium {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    private String denomination;
    private String genre;
    private String presentation;

    public Medium() {
    }
    
    public Medium(String denomination, String genre, String presentation) {
        this.denomination = denomination;
        this.genre = genre;
        this.presentation = presentation;
    }

    @Override
    public String toString() {
        return "id=" + id + ", denomination=" + denomination + ", genre=" + genre + ", presentation=" + presentation;
    }
    
    

    public Long getId() {
        return id;
    }

    public String getDenomination() {
        return denomination;
    }

    public String getGenre() {
        return genre;
    }

    public String getPresentation() {
        return presentation;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }
    
    
}
