/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;
import java.util.List;
import javax.persistence.TypedQuery;
import metier.modele.Client;
import metier.modele.Consultation;

/**
 *
 * @author dsteferra
 */
public class ClientDao {
    public static void create(Client client)
    {
        JpaUtil.obtenirContextePersistance().persist(client);
    }
    
    public static Client update(Client client)
    {
        return JpaUtil.obtenirContextePersistance().merge(client);
    }
    
    public static Client findByMailAndPassword(String mail, String password)
    {
        String req = "select c from Client c where c.mail = :unMail and c.motDePasse = :unMdp ";
        TypedQuery<Client> query = JpaUtil.obtenirContextePersistance().createQuery(req, Client.class);
        query.setParameter("unMail", mail);
        query.setParameter("unMdp", password);
        
        Client client = query.getSingleResult();
        
        return client;
    }
    
    public static List<Client> findAll()
    {
        String req = "select c from Client c";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(req, Client.class);
        
        List<Client> clients = query.getResultList();
        
        return clients;
    }
    
    public static Client findById(Long id)
    {
        String req = "select c from Client c where c.id = :unId";
        TypedQuery<Client> query = JpaUtil.obtenirContextePersistance().createQuery(req, Client.class);
        query.setParameter("unId", id);
        
        Client client = query.getSingleResult();
        return client;
    }
    
    public static List<Consultation> findConsultationsById(Long id)
    {
        String req = "select c from Client c where c.id = :unId";
        TypedQuery query = JpaUtil.obtenirContextePersistance().createQuery(req, Client.class);
        query.setParameter("unId", id);
        
        List<Consultation> consultations = ((Client) query.getSingleResult()).getListeConsultations();
        
        return consultations;
    }
    
}
