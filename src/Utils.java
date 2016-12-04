import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by Julien on 04/12/2016.
 */
public class Utils {

    public static double getDistance(Client c1, Client c2) {
        return sqrt(pow((c1.getX() - c2.getX()), 2) + pow((c1.getY() - c2.getY()), 2));
    }

}
