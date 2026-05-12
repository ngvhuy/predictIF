/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import javax.persistence.TypedQuery;
import metier.modele.Consultation;
import metier.modele.Employe;

/**
 *
 * @author dsteferra
 */
public class ConsultationDao {
    public static void create(Consultation consultation)
    {
        JpaUtil.obtenirContextePersistance().persist(consultation);
    }
    
    public static Consultation update(Consultation consultation)
    {
        return JpaUtil.obtenirContextePersistance().merge(consultation);
    }
    
    public static Consultation findById(Long id)
    {
        String req = "select c from Consultation c where c.id = :unId";
        TypedQuery<Consultation> query = JpaUtil.obtenirContextePersistance().createQuery(req, Consultation.class);
        query.setParameter("unId", id);
        
        Consultation consultation = query.getSingleResult();
        return consultation;
    }
    
    
    public static List<Consultation> findAll()
    {
        String req = "select c from Consultation c";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(req, Consultation.class);
        
        List<Consultation> employes = query.getResultList();
        return employes;
    }
    
    public static Consultation findConsultationActuelleEmploye(Employe employe) {
        String req = "select c from Consultation c where c.employe = :unEmploye";
        TypedQuery<Consultation> query = JpaUtil.obtenirContextePersistance().createQuery(req, Consultation.class);
        query.setParameter("unEmploye", employe);
        
        Consultation consultation = query.getSingleResult();
        return consultation;
    }

}
