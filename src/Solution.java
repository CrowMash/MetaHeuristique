import java.util.ArrayList;

public class Solution {

    private double temps;

    public ArrayList<Camion> getCamions() {
        return camions;
    }

    private ArrayList<Camion> camions;

    public Solution() {
        camions = new ArrayList<>();
        temps = 0;
    }

    void addCamions(Camion c){
        camions.add(c);
        double tempsCamion = c.getTemps();
        if(tempsCamion > temps){
            temps = tempsCamion;
        }
    }
}
