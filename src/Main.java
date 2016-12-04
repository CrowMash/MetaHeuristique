import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    private static final String FICHIER_RES = "res/diag.dot";
    private static final int ITERATION_PER_REBOOT = 100;

    private static ArrayList<Client> clients = new ArrayList<>();

    private static int nbCamion;
    private static Client depot;
    private static double capacite;

    public static void main(String[] args) {

        String nomFichier = "res/" + args[1];

        capacite = 0;
        try {
            capacite = getCapacite(nomFichier);
            clients = getClients(nomFichier);
        } catch (IOException e) {
            e.printStackTrace();
        }

        depot = clients.get(0);
        clients.remove(depot);

        nbCamion = clients.size();
        long tempsVoulu = System.currentTimeMillis() + (Integer.parseInt(args[0]) * 1000);
        long tempsDebut = System.currentTimeMillis();

        Solution bestOverAll = reboot();

        while (System.currentTimeMillis() < tempsVoulu) {
            Solution bestCurrent = reboot();

            int boucle = 0;
            boolean notSame = true;
            while (boucle < ITERATION_PER_REBOOT && notSame) {
                Solution temp = bestSolution(bestCurrent);
                double tempsTemp = temp.getTemps();
                double tempsBest = bestCurrent.getTemps();
                if (tempsTemp < tempsBest) {
                    bestCurrent = new Solution(temp);
                } else {
                    System.out.println("Same (" + boucle + "ème itération)");
                    notSame = false;
                }
                boucle++;
            }

            System.out.print("Score : " + (int) bestCurrent.getTemps() +
                    " avec " + bestCurrent.getCamions().size() + " camions.");

            if (bestCurrent.isValid()) {
                System.out.println(" Valide");
            } else {
                System.out.println(" Non valide");
            }

            System.out.println("Temps d'exécution : " + (System.currentTimeMillis() - tempsDebut) / 1000 + " secondes");

            if (bestCurrent.getTemps() < bestOverAll.getTemps()) {
                bestOverAll = new Solution(bestCurrent);
            }
        }

        System.out.print("\nScore final : " + (int) bestOverAll.getTemps() +
                " avec " + bestOverAll.getCamions().size() + " camions.");

        System.out.println(" Valide ? " + bestOverAll.isValid());

        generateDiag(bestOverAll);
    }

    private static Solution bestSolution(Solution actuelle) {

        Solution best;
        Solution permut = bestPermut(actuelle);
        Solution fusion = bestFusion(actuelle);
        Solution cut = bestCut(actuelle);
        double tempsPermut;
        double tempsFusion;
        double tempsCut;

        if (permut != null) {
            tempsPermut = permut.getTemps();
        } else {
            tempsPermut = 99999999;
        }

        if (fusion != null) {
            tempsFusion = fusion.getTemps();
        } else {
            tempsFusion = 99999999;
        }

        if (cut != null) {
            tempsCut = cut.getTemps();
        } else {
            tempsCut = 99999999;
        }

        if (tempsPermut < tempsFusion) {
            if (tempsPermut < tempsCut) {
                best = new Solution(permut);
            } else {
                if (tempsCut < tempsFusion) {
                    best = new Solution(cut);
                } else {
                    best = new Solution(fusion);
                }
            }
        } else {
            if (tempsFusion < tempsCut) {
                best = new Solution(fusion);
            } else {
                best = new Solution(cut);
            }
        }
        return best;
    }

    private static Solution bestCut(Solution actuelle) {
        if (actuelle.getCamions().size() == nbCamion) {
            return null;
        }
        Solution best = new Solution(actuelle);
        Solution save = new Solution(actuelle);

        ArrayList<Camion> camions = save.getCamions();

        int nbCamion = camions.size();
        for (int i = 0; i < nbCamion; i++) {
            camions = save.getCamions();
            Camion c1 = new Camion(camions.get(i));
            List<Client> listc1 = c1.getClientsALivrer();
            if (listc1.size() > 3) {
                int nbClient = listc1.size();
                for (int j = 1; j < nbClient - 1; j++) {
                    camions = save.getCamions();
                    c1 = new Camion(camions.get(i));
                    listc1 = c1.getClientsALivrer();
                    List<Client> listc2 = listc1.subList(j + 1, listc1.size());
                    listc1 = listc1.subList(0, j + 1);
                    c1.setClientsALivrer(new ArrayList<>(listc1));
                    c1.addClient(depot);
                    Camion aVirer = save.getCamions().get(i);
                    save.removeCamion(aVirer);
                    save.addCamions(i, c1);
                    Camion c2 = new Camion(capacite);
                    c2.setClientsALivrer(new ArrayList<>(listc2));
                    c2.addFirstClient(depot);
                    save.addCamions(c2);
                    double tempsSave = save.getTemps();
                    double tempsBest = best.getTemps();
                    if (best.isValid()) {
                        if (save.isValid() && (tempsSave < tempsBest)) {
                            best = new Solution(save);
                        }
                    } else {
                        if (save.isValid()) {
                            best = new Solution(save);
                        }
                    }
                    save = new Solution(actuelle);
                }
            }
        }

        return best;
    }

    private static Solution bestFusion(Solution actuelle) {
        if (actuelle.getCamions().size() == 1) {
            return null;
        }
        Solution best = new Solution(actuelle);
        Solution save = new Solution(actuelle);

        ArrayList<Camion> camions = save.getCamions();

        for (int i = 0; i < camions.size() - 1; i++) {
            for (int j = 0; j < camions.size() - 1; j++) {
                camions = save.getCamions();
                Camion c1 = new Camion(camions.get(i));
                Camion c2 = new Camion(camions.get(j));
                if (i != j) {
                    Camion c3 = new Camion(capacite);
                    List<Client> listc1 = c1.getClientsALivrer();
                    listc1.remove(listc1.size() - 1);
                    List<Client> listc2 = c2.getClientsALivrer();
                    listc2.remove(0);
                    ArrayList<Client> listc3 = new ArrayList<>();
                    listc3.addAll(listc1);
                    listc3.addAll(listc2);
                    c3.setClientsALivrer(listc3);
                    save.addCamions(c3);
                    Camion aVirer1 = save.getCamions().get(i);
                    Camion aVirer2 = save.getCamions().get(j);
                    save.removeCamion(aVirer1);
                    save.removeCamion(aVirer2);

                    double tempsSave = save.getTemps();
                    double tempsBest = best.getTemps();
                    if (best.isValid()) {
                        if (save.isValid() && (tempsSave < tempsBest)) {
                            best = new Solution(save);
                        }
                    } else {
                        if (save.isValid()) {
                            best = new Solution(save);
                        }
                    }
                    save = new Solution(actuelle);
                }
            }
        }

        return best;
    }

    private static Solution bestPermut(Solution actuelle) {
        Solution best = new Solution(actuelle);
        Solution save = new Solution(actuelle);

        ArrayList<Camion> camions = save.getCamions();

        for (int k = 0; k < camions.size(); k++) {
            camions = save.getCamions();
            Camion c = camions.get(k);
            ArrayList<Client> clientsCamion = c.getClientsALivrer();
            for (int i = 1; i < clientsCamion.size() - 1; i++) {
                for (int j = 1; j < clientsCamion.size() - 1; j++) {
                    camions = save.getCamions();
                    c = camions.get(k);
                    clientsCamion = c.getClientsALivrer();
                    if (i != j) {
                        Camion swap = new Camion(c);
                        Collections.swap(clientsCamion, i, j);
                        swap.setClientsALivrer(clientsCamion);
                        save.removeCamion(c);
                        save.addCamions(k, swap);
                        double tempsSave = save.getTemps();
                        double tempsBest = best.getTemps();
                        if (best.isValid()) {
                            if (save.isValid() && (tempsSave < tempsBest)) {
                                best = new Solution(save);
                            }
                        } else {
                            if (save.isValid()) {
                                best = new Solution(save);
                            }
                        }
                        save = new Solution(actuelle);
                    }
                }
            }
        }
        return best;
    }

    private static Solution reboot() {
        Solution solutionDepart = new Solution();
        Collections.shuffle(clients);
        int clientParCamion = clients.size() / nbCamion;
        for (int i = 1; i <= nbCamion; i++) {
            Camion camionActuel = new Camion(capacite);
            List<Client> clientsActuels;
            if (i == nbCamion) {
                clientsActuels = clients.subList((i - 1) * clientParCamion, clients.size() - 1);
            } else {
                clientsActuels = clients.subList((i - 1) * clientParCamion, i * clientParCamion);
            }
            camionActuel.addClient(depot);
            for (Client c : clientsActuels) {
                camionActuel.addClient(c);
            }
            camionActuel.addClient(depot);
            solutionDepart.addCamions(camionActuel);
        }
        return solutionDepart;
    }

    private static void generateDiag(Solution solution) {
        File logFile = new File(FICHIER_RES);
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
            writer.write("digraph camion {\n");
            ArrayList<Camion> camions = solution.getCamions();
            for (Camion camion : camions) {
                ArrayList<Client> clients = camion.getClientsALivrer();
                for (int i = 0; i < clients.size() - 1; i++) {
                    if (i == clients.size() - 2) {
                        writer.write(clients.get(i).getId() + ";\n");
                    } else {
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
        br.readLine();
        br.readLine();
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
        String ligne = br.readLine();
        String[] parts = ligne.split(" ");
        capacite = Double.parseDouble(parts[1]);
        br.close();
        return capacite;
    }
}
