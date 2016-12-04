import java.util.ArrayList;

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
        double pastTime = 0;
        for (int i = 1; i < clientsALivrer.size(); i++) {
            Client prev = clientsALivrer.get(i - 1);
            Client next = clientsALivrer.get(i);
            if ((pastTime + Utils.getDistance(prev, next)) < next.getTMin()) {
                pastTime = next.getTMin() + next.getDuree();
            } else {
                pastTime += Utils.getDistance(prev, next) + next.getDuree();
            }
        }
        return pastTime;
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
