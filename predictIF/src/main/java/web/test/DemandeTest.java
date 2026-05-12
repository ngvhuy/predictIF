/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package web.test;

import java.time.LocalDateTime;

/**
 * "Fausse" classe d'entité pour simulation
 */
public class DemandeTest {

    private Long id;
    private LocalDateTime dateCreation;
    private String description;

    public DemandeTest(Long id, LocalDateTime dateCreation, String description) {
        this.id = id;
        this.dateCreation = dateCreation;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getDateCreation() {
        return dateCreation;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "DemandeTest{" + "id=" + id + ", dateCreation=" + dateCreation + ", description=" + description + '}';
    }
    
}