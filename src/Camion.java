import java.util.ArrayList;

public class Camion {

    private double capacite;
    private double poids;

    private double temps;

    public ArrayList<Client> getClientsALivrer() {
        return clientsALivrer;
    }

    private ArrayList<Client> clientsALivrer;

    Camion(double capacite) {
        this.capacite = capacite;
        clientsALivrer = new ArrayList<>();
        temps = 0;
    }

    public double getTemps() {
        return temps;
    }

    public double getPoids() {
        return poids;
    }

    public boolean addClient(Client client) {
        poids += client.getQuantite();
        temps += client.getDuree();
        clientsALivrer.add(client);
        return true;
        /*
        if (client.getQuantite() + poids < capacite) {
            poids += client.getQuantite();
            temps += client.getDuree();
            clientsALivrer.add(client);
            return true;
        } else {
            return false;
        }
        */
    }
}
