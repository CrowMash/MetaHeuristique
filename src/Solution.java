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
}
