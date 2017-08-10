package com.jarhan.timwi.santaTour;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

/**
 * Entry point
 * Resolution du pb via l'algorithme du plus proche voisin
 *
 */
public class App {
    public static void main(String[] args) {
        
        if(args.length != 1){
            //TODO ajout log
            System.exit(-1);
        }
        Map<String, Ville> villesParNom = recuperationVillesDuFichier(args[0]);
        
        
        List<String> villesVisitees = new ArrayList<>();

        Integer distanceMinimum = -1;
        List<String> cheminMinimum = null;
        
        // application de l'algorithme du plus proche voisin à chaque ville.
        for (Ville villeDepart : villesParNom.values()) {
            villesVisitees = new ArrayList<>();
            villesVisitees.add(villeDepart.getNom());
            Integer distance = chercherChemin(villesParNom, villesVisitees, villeDepart.getNom());
            
            if(distanceMinimum < 0 || distance < distanceMinimum){
                distanceMinimum = distance;
                cheminMinimum = villesVisitees;
            }
        }
        
        System.out.println("Chemin le plus court : ");
        cheminMinimum.stream().forEach(ville -> System.out.print(ville + " -> "));
        System.out.print(distanceMinimum);

    }
    
    /**
     * Lecture du fichier pour récupérer les villes
     * @param cheminFichier le chemin vers le fichier
     * @return une map avec en clé le nom de la ville et en valeur la ville correspondante
     */
    private static Map<String, Ville> recuperationVillesDuFichier(String cheminFichier){
        File fichierDistances = new File(cheminFichier);
        
        Map<String, Ville> villesParNom = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(fichierDistances))){
            String ligne = null;
            while ((ligne = reader.readLine()) != null) {
                // récupération des villes et des distances entre elles
                String villeA = StringUtils.substringBefore(ligne, " to ");
                String villeB = StringUtils.substringBetween(ligne, " to ", " = ");
                Integer distance = Integer.valueOf(StringUtils.substringAfter(ligne, " = "));
                
                // ajout des villes dans la map si elles n'y sont pas
                if(!villesParNom.containsKey(villeA)){
                    Ville ville = new Ville();
                    ville.setDistances(new HashMap<>());
                    ville.setNom(villeA);
                    villesParNom.put(villeA, ville);
                }
                
                if(!villesParNom.containsKey(villeB)){
                    Ville ville = new Ville();
                    ville.setDistances(new HashMap<>());
                    ville.setNom(villeB);
                    villesParNom.put(villeB, ville);
                }
                
                // ajout des distances entre villes
                villesParNom.get(villeA).getDistances().put(villeB, distance);
                villesParNom.get(villeB).getDistances().put(villeA, distance);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return villesParNom;
    }
    
    /**
     * Recherche récursive du plus proche voisin puis de son plus proche voisin, ...<br>
     * Ramène la distance totale du chemin
     * @param villesParNom la map des villes par leur nom
     * @param villesVisitees la liste des villes déjà visitées (est alimentée au fur et à mesure)
     * @param nomVilleActuelle le nom de la ville actuelle
     * @return la distance du chemin
     */
    private static Integer chercherChemin(Map<String, Ville> villesParNom, List<String> villesVisitees, String nomVilleActuelle){
        
        Integer distance = 0;
        
        String plusProcheVoisin = recuperationVillePlusProche(villesVisitees, villesParNom.get(nomVilleActuelle));
        
        if(plusProcheVoisin != null){
            distance += villesParNom.get(nomVilleActuelle).getDistances().get(plusProcheVoisin);
            villesVisitees.add(plusProcheVoisin);
            // si on trouve un plus proche voisin, on va chercher ses plus proches voisins
            distance += chercherChemin(villesParNom, villesVisitees, plusProcheVoisin);
        }
        
        // sinon on a fini le chemin
        return distance;
    }

    /**
     * Récupération du plus proche voisin qui n'a pas été déjà visité
     * @param villesVisitees les villes déjà visitées
     * @param villeActuelle ville actuelle
     * @return le nom de la ville la plus proche
     */
    private static String recuperationVillePlusProche(List<String> villesVisitees, Ville villeActuelle) {

        String villeLaPlusProche = null;

        Entry<String, Integer> plusProcheVoisin = villeActuelle.getDistances().entrySet().stream()
                .filter(entry -> !villesVisitees.contains(entry.getKey())).min((entry1, entry2) -> Integer.compare(entry1.getValue(), entry2.getValue())).orElse(null);
        if (plusProcheVoisin != null) {
            villeLaPlusProche = plusProcheVoisin.getKey();
        }

        return villeLaPlusProche;
    }
}
