import java.io.*;
import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Main {

    private static final String FICHIER = "res/routage_a1.txt";
    private static final String FICHIER_RES = "res/diag.dot";

    private static ArrayList<Client> clients = new ArrayList<>();

    public static void main(String[] args) {
        double capacite = 0;
        try {
            capacite = getCapacite(FICHIER);
            clients = getClients(FICHIER);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //DEPOT
        Client depot = clients.get(0);
        clients.remove(depot);
        depot.setLivre(true);

        boolean clientRestant = true;

        int nbCamion = 3;
        Camion[] camions = new Camion[nbCamion];
        String[] chemins = new String[nbCamion];
        for (int i = 0; i < nbCamion; i++) {
            Camion camionActuel = new Camion(capacite);
            StringBuilder build = new StringBuilder();

            boolean camionAvecPlace = true;
            double capaciteActuelle = 0;
            double temps = 0;

            Client clientActuel = depot;
            build.append("\t1");

            while (clientRestant && camionAvecPlace) {

                build.append(" -> ");

                Client prochainClient = plusProcheVoisinNonLivre(clientActuel);
                if (prochainClient != null) {
                    if (camionActuel.addClient(prochainClient)) {
                        capaciteActuelle += prochainClient.getQuantite();
                        temps += getDistance(clientActuel, prochainClient) + prochainClient.getDuree();
                        clients.remove(prochainClient);
                        prochainClient.setLivre(true);
                        clientActuel = prochainClient;
                        build.append(clientActuel.getId());
                    } else {
                        camionAvecPlace = false;
                    }
                } else {
                    clientRestant = false;
                }
            }

            //Retour au dépôt
            temps += getDistance(clientActuel, depot);
            build.append("1;\n");

            System.out.println("Coût total : " + temps);
            System.out.println("Capacité camion : " + capaciteActuelle);

            chemins[i] = build.toString();
            camions[i] = camionActuel;
        }


        generateDiag(chemins);
    }

    private static void generateDiag(String[] chemins) {
        File logFile = new File(FICHIER_RES);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("digraph camion {\n");
            for (String s : chemins) {
                if (s.contains(" -> ")) {
                    writer.write(s);
                }
            }
            writer.write("}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Client plusProcheVoisinNonLivre(Client clientActuel) {
        double distMinimale = 999;
        double distActuelle;
        Client lePlusProche = null;
        for (Client c : clients) {
            if (!c.isLivre()) {
                distActuelle = getDistance(clientActuel, c);
                if (distActuelle < distMinimale) {
                    distMinimale = distActuelle;
                    lePlusProche = c;
                }
            }
        }
        return lePlusProche;
    }

    private static ArrayList<Client> getClients(String nomFichier) throws IOException {
        ArrayList<Client> clients = new ArrayList<>();
        Client c;
        int id;
        double x, y, quantite, tMin, tMax, duree;
        InputStream ips = new FileInputStream(nomFichier);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String ligne;
        //Capacite
        br.readLine();
        //En-tête
        br.readLine();
        //Client
        while ((ligne = br.readLine()) != null) {
            String[] parts = ligne.split("\t");
            id = Integer.parseInt(parts[0]);
            x = Double.parseDouble(parts[1]);
            y = Double.parseDouble(parts[2]);
            quantite = Double.parseDouble(parts[3]);
            tMin = Double.parseDouble(parts[4]);
            tMax = Double.parseDouble(parts[5]);
            duree = Double.parseDouble(parts[6]);
            c = new Client(id, x, y, quantite, tMin, tMax, duree);
            //System.out.println(c.toString());
            clients.add(c);
        }
        br.close();

        return clients;
    }

    private static double getCapacite(String nomFichier) throws IOException {
        double capacite;
        InputStream ips = new FileInputStream(nomFichier);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        //Capacite
        String ligne = br.readLine();
        String[] parts = ligne.split(" ");
        capacite = Double.parseDouble(parts[1]);
        br.close();
        return capacite;
    }

    private static double getDistance(Client c1, Client c2) {
        return sqrt(pow((c1.getX() - c2.getX()), 2) + pow((c1.getY() - c2.getY()), 2));
    }
}
