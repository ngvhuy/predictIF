/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package web.controleur;

import dao.JpaUtil;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import web.modele.Action;
import web.modele.Connexion;
import web.modele.ConsulterListeDemandesAction;
import web.modele.ConsulterListeMediums;
import web.vue.ListeMediumsSerialisation;
import web.vue.pageConnexion;
import web.vue.Serialisation;

/**
 *
 * @author rguteville
 */
@WebServlet(name = "ActionServlet", urlPatterns = {"/ActionServlet"})
public class ActionServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
       
        String todo = request.getParameter("todo");
        System.out.println("\n todo = " + todo);
        switch(todo){
            case "consulter-liste-demandes":
                Action action = new ConsulterListeDemandesAction();
                action.execute(request);
                //Serialisation serialisation = new ListeDemandesSerialisation();
                //serialisation.appliquer(request, response);
                break;
            case "consulter-liste-mediums":
                Action actionMedium = new ConsulterListeMediums();
                actionMedium.execute(request);
                Serialisation serialisation = new ListeMediumsSerialisation();
                serialisation.appliquer(request, response);
                break;
            case "connexion":
                Action actionConnexion = new Connexion();
                actionConnexion.execute(request);
                Serialisation serialisationConnexion = new pageConnexion();
                serialisationConnexion.appliquer(request, response);
                break;
            default:
                
        }
     
    }
    
    @Override
    public void init() throws ServletException{
        super.init();
        JpaUtil.creerFabriquePersistance();
    }
    
    @Override
    public void destroy() {
        JpaUtil.fermerFabriquePersistance();
        super.destroy();
        
    }
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
