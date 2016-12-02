import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

        Client depot = clients.get(0);
        clients.remove(depot);

        int nbCamionMax = clients.size();

        //while temps

        Solution solutionDepart = new Solution();
        int nbCamion = (int) (Math.random() * (nbCamionMax-1) + 1);
        System.out.println(nbCamion);
        int clientParCamion = clients.size() / nbCamion;
        // pour chaque camions, on prend une partie de la liste des clients
        for(int i = 1; i <= nbCamion; i ++){
            Camion camionActuel = new Camion(capacite);
            List<Client> clientsActuels;
            if(i == nbCamion){
                clientsActuels = clients.subList((i-1)*clientParCamion,clients.size()-1);
            }else{
                clientsActuels = clients.subList((i-1)*clientParCamion,i*clientParCamion);
            }
            Collections.shuffle(clientsActuels);
            // solution depot
            camionActuel.addClient(depot);
            for(Client c : clientsActuels){
                camionActuel.addClient(c);
            }
            // arrivee depot
            camionActuel.addClient(depot);
            // ajout du camion dans la solution
            solutionDepart.addCamions(camionActuel);
        }

        generateDiag(solutionDepart);
    }

    private static void generateDiag(Solution solution) {
        File logFile = new File(FICHIER_RES);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("digraph camion {\n");
            ArrayList<Camion> camions = solution.getCamions();
            for (Camion camion : camions) {
                ArrayList<Client> clients = camion.getClientsALivrer();
                for(int i = 0; i < clients.size(); i ++){
                    if(i == clients.size() - 1){
                        writer.write(clients.get(i).getId() + ";\n");
                    }else{
                        writer.write(clients.get(i).getId() + " -> ");
                    }

                }
            }
            writer.write("}");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
