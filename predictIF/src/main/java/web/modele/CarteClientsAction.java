package web.modele;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import metier.modele.Client;
import metier.service.Service;

public class CarteClientsAction extends Action {

    @Override
    public void execute(HttpServletRequest request) {
        Service service = new Service();
        List<Client> clients = service.getAllClients();
        request.setAttribute("clients", clients);
    }
}
