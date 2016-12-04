import java.util.ArrayList;

public class Solution {

    private ArrayList<Camion> camions;

    public Solution() {
        camions = new ArrayList<>();
    }

    public Solution(Solution clone) {
        camions = new ArrayList<>();
        for (Camion c : clone.getCamions()) {
            camions.add(new Camion(c));
        }
    }

    public double getTemps() {
        double total = 0;
        for (Camion c : camions) {
            total += c.getTemps();
        }
        return total;
    }

    public ArrayList<Camion> getCamions() {
        return camions;
    }

    void addCamions(Camion c){
        camions.add(c);
    }

    void addCamions(int index, Camion c) {
        camions.add(index, c);
    }

    void removeCamion(Camion camion) {
        camions.remove(camion);
    }

    boolean isValid() {
        boolean valid = true;

        for (Camion c : camions) {
            if (c.getPoids() > c.getCapacite()) {
                return false;
            }
        }

        for (Camion ca : camions) {
            double pastTime = 0;
            ArrayList<Client> clients = ca.getClientsALivrer();
            for (int i = 1; i < clients.size(); i++) {
                Client prev = clients.get(i - 1);
                Client next = clients.get(i);
                pastTime += Utils.getDistance(prev, next);
                if (pastTime > (next.getTMax() - next.getDuree())) {
                    return false;
                } else {
                    pastTime = Math.max(pastTime, next.getTMin()) + next.getDuree();
                }
            }
        }

        return valid;
    }
}
