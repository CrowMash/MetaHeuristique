import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Camion {

    private double capacite;
    private ArrayList<Client> clientsALivrer;

    Camion(double capacite) {
        this.capacite = capacite;
        clientsALivrer = new ArrayList<>();
    }

    Camion(Camion clone) {
        this.capacite = clone.getCapacite();
        clientsALivrer = new ArrayList<>();
        clientsALivrer.addAll(clone.getClientsALivrer());
    }

    private static double getDistance(Client c1, Client c2) {
        return sqrt(pow((c1.getX() - c2.getX()), 2) + pow((c1.getY() - c2.getY()), 2));
    }

    public double getCapacite() {
        return capacite;
    }

    public ArrayList<Client> getClientsALivrer() {
        return clientsALivrer;
    }

    public void setClientsALivrer(ArrayList<Client> clientsALivrer) {
        this.clientsALivrer = clientsALivrer;
    }

    public double getTemps() {
        double total = 0;
        for (int i = 1; i < clientsALivrer.size() - 1; i++) {
            Client c1 = clientsALivrer.get(i);
            Client c2 = clientsALivrer.get(i + 1);
            total += c1.getDuree() + getDistance(c1, c2);
        }
        Client terminux = clientsALivrer.get(clientsALivrer.size() - 1);
        total += terminux.getDuree();
        return total;
    }

    public double getPoids() {
        double poids = 0;

        for (Client c : clientsALivrer) {
            poids += c.getQuantite();
        }

        return poids;
    }

    public boolean addClient(Client client) {
        clientsALivrer.add(client);
        return true;
    }

    public boolean addFirstClient(Client client) {
        clientsALivrer.add(0, client);
        return true;
    }
}
