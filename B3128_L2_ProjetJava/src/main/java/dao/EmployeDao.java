/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.util.List;
import javax.persistence.TypedQuery;
import metier.modele.Employe;

/**
 *
 * @author dsteferra
 */
public class EmployeDao {
    public static void create(Employe employe)
    {
        JpaUtil.obtenirContextePersistance().persist(employe);
    }
    
    public static Employe update(Employe employe)
    {
        return JpaUtil.obtenirContextePersistance().merge(employe);
    }
    
    public static Employe findById(Long id)
    {
        String req = "select e from Employe e where e.id = :unId";
        TypedQuery<Employe> query = JpaUtil.obtenirContextePersistance().createQuery(req, Employe.class);
        query.setParameter("unId", id);
        
        Employe employe = query.getSingleResult();
        return employe;
    }
    
    public static List<Employe> findAll()
    {
        String req = "select e from Employe e";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(req, Employe.class);
        
        List<Employe> employes = query.getResultList();
        return employes;
    }
    
     public static Employe findByMailAndPassword(String mail, String password)
    {
        String req = "select e from Employe e where e.mail = :unMail and e.password = :unMdp ";
        TypedQuery<Employe> query = JpaUtil.obtenirContextePersistance().createQuery(req, Employe.class);
        query.setParameter("unMail", mail);
        query.setParameter("unMdp", password);
        
        Employe employe = query.getSingleResult();
        
        return employe;
    }
    
    public static Employe findEmployeLibre(String genre)
    {
        String req = "select e "
           + "from Employe e "
           + "where e.estEnConsultation = :status and e.sexe = :unGenre "
           + "order by SIZE(e.listeConsultations)";
        TypedQuery<Employe> query = JpaUtil.obtenirContextePersistance().createQuery(req, Employe.class);
        query.setParameter("status", false);
        query.setParameter("unGenre", genre);
        
        List<Employe> employes = query.getResultList();
         
        Employe employe = null;
        try {
            employe = employes.getFirst();
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
        }
        return employe;
    }
}
