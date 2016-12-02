import java.util.ArrayList;

/**
 * Created by Julien on 09/11/2016.
 */
public class Camion {

    private double capacite;
    private double poids;

    private ArrayList<Client> clientsALivrer;

    Camion(double capacite) {
        setCapacite(capacite);
        clientsALivrer = new ArrayList<>();
    }

    public double getCapacite() {
        return capacite;
    }

    public void setCapacite(double capacite) {
        this.capacite = capacite;
    }

    public double getPoids() {
        return poids;
    }

    public void setPoids(double poids) {
        this.poids = poids;
    }

    public boolean addClient(Client client) {
        if (client.getQuantite() + poids < capacite) {
            poids += client.getQuantite();
            clientsALivrer.add(client);
            return true;
        } else {
            return false;
        }
    }
}
