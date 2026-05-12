/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import javax.persistence.TypedQuery;
import metier.modele.Medium;

/**
 *
 * @author dsteferra
 */
public class MediumDao {
    public static void create(Medium medium)
    {
        JpaUtil.obtenirContextePersistance().persist(medium);
    }
    
    
    public static List<Medium> findAll()
    {
        String req = "select m from Medium m";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(req, Medium.class);
        
        List<Medium> mediums = query.getResultList();
        return mediums;
    }
    
    public static Medium findById(Long id)
    {
        String req = "select m from Medium m where m.id = :unId";
        TypedQuery<Medium> query = JpaUtil.obtenirContextePersistance().createQuery(req, Medium.class);
        query.setParameter("unId", id);
        
        Medium medium = query.getSingleResult();
        return medium;
    }
}
