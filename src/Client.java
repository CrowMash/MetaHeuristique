public class Client {

    private int id;
    private double x;
    private double y;
    private double quantite;
    private double tMin;
    private double tMax;
    private double duree;
    private boolean livre;

    Client(int id, double x, double y, double quantite, double tMin, double tMax, double duree) {
        setId(id);
        setX(x);
        setY(y);
        setQuantite(quantite);
        setTMin(tMin);
        setTMax(tMax);
        setDuree(duree);
        setLivre(false);
    }

    public boolean isLivre() {
        return livre;
    }

    public void setLivre(boolean livre) {
        this.livre = livre;
    }

    @Override
    public String toString() {
        return "Client n°" + id +
                ", X : " + x +
                ", Y : " + y +
                ", Quantite : " + quantite +
                ", tMin : " + tMin +
                ", tMax : " + tMax +
                ", duree : " + duree;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getQuantite() {
        return quantite;
    }

    public void setQuantite(double quantite) {
        this.quantite = quantite;
    }

    public double getTMin() {
        return tMin;
    }

    public void setTMin(double tMin) {
        this.tMin = tMin;
    }

    public double getTMax() {
        return tMax;
    }

    public void setTMax(double tMax) {
        this.tMax = tMax;
    }

    public double getDuree() {
        return duree;
    }

    public void setDuree(double duree) {
        this.duree = duree;
    }
}
