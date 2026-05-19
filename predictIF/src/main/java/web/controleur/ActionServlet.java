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
import jakarta.json.Json;
import jakarta.servlet.http.HttpSession;
import web.modele.Action;
import web.modele.CarteClientsAction;
import web.modele.Connexion;
import web.modele.ConsulterDashboard;
import web.modele.ConsulterHistoriqueClient;
import web.modele.ConsulterListeDemandesAction;
import web.modele.ConsulterListeMediums;
import web.modele.ConsulterProfilAstral;
import web.modele.Inscription;
import web.modele.ChargerMaConsultationAction;
import web.modele.DemarrerConsultationAction;
import web.modele.TerminerConsultationAction;
import web.modele.GenererPredictionAction;
import web.vue.CarteClientsSerialisation;
import web.vue.DashboardSerialisation;
import web.vue.HistoriqueClientSerialisation;
import web.vue.ListeMediumsSerialisation;
import web.vue.pageConnexion;
import web.vue.pageInscription;
import web.vue.ProfilAstralSerialisation;
import web.vue.MaConsultationSerialisation;
import web.vue.Serialisation;
import web.vue.GenererPredictionSerialisation;

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
            case "profil-astral":
                Action actionProfil = new ConsulterProfilAstral();
                actionProfil.execute(request);
                Serialisation serialisationProfil = new ProfilAstralSerialisation();
                serialisationProfil.appliquer(request, response);
                break;
            case "carte-clients":
                Action actionCarte = new CarteClientsAction();
                actionCarte.execute(request);
                Serialisation serialisationCarte = new CarteClientsSerialisation();
                serialisationCarte.appliquer(request, response);
                break;
            case "historique-client":
                Action actionHistorique = new ConsulterHistoriqueClient();
                actionHistorique.execute(request);
                Serialisation serialisationHistorique = new HistoriqueClientSerialisation();
                serialisationHistorique.appliquer(request, response);
                break;
            case "connexion":
                Action actionConnexion = new Connexion();
                actionConnexion.execute(request);
                Serialisation serialisationConnexion = new pageConnexion();
                serialisationConnexion.appliquer(request, response);
                break;
            case "inscription":
                Action actionInscription = new Inscription();
                actionInscription.execute(request);
                Serialisation serialisationInscription = new pageInscription();
                serialisationInscription.appliquer(request, response);
                break;
            case "dashboard":
                Action actionDashboard = new ConsulterDashboard();
                actionDashboard.execute(request);
                Serialisation serialisationDashboard = new DashboardSerialisation();
                serialisationDashboard.appliquer(request, response);
                break;
            case "session":
                HttpSession sess = request.getSession(false);
                String profil = (sess != null) ? (String) sess.getAttribute("profil") : null;
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter pw = response.getWriter();
                pw.write(Json.createObjectBuilder()
                        .add("profil", profil != null ? profil : "")
                        .build().toString());
                pw.close();
                break;
            case "deconnexion":
                HttpSession sessLogout = request.getSession(false);
                if (sessLogout != null) sessLogout.invalidate();
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter pwLogout = response.getWriter();
                pwLogout.write("{\"succes\":true}");
                pwLogout.close();
                break;
            case "charger-ma-consultation":
                Action actionMaConsultation = new ChargerMaConsultationAction();
                actionMaConsultation.execute(request);
                Serialisation serialisationMaConsultation = new MaConsultationSerialisation();
                serialisationMaConsultation.appliquer(request, response);
                break;
            case "demarrer-consultation":
                Action actionDemarrer = new DemarrerConsultationAction();
                actionDemarrer.execute(request);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter pwDemarrer = response.getWriter();
                pwDemarrer.write("{\"succes\":true}");
                pwDemarrer.close();
                break;
            case "terminer-consultation":
                Action actionTerminer = new TerminerConsultationAction();
                actionTerminer.execute(request);
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                PrintWriter pwTerminer = response.getWriter();
                pwTerminer.write("{\"succes\":true}");
                pwTerminer.close();
                break;
            case "generer-prediction":
                Action actionPrediction = new GenererPredictionAction();
                actionPrediction.execute(request);
                Serialisation serialisationPrediction = new GenererPredictionSerialisation();
                serialisationPrediction.appliquer(request, response);
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
