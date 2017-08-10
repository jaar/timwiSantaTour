package com.jarhan.timwi.santaTour;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Ville {

    private String nom;

    private Map<String, Integer> distances;

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom
     *            the nom to set
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * @return the distances
     */
    public Map<String, Integer> getDistances() {
        return distances;
    }

    /**
     * @param distances
     *            the distances to set
     */
    public void setDistances(Map<String, Integer> distances) {
        this.distances = distances;
    }

    public boolean equals(Object obj) {
        return (obj instanceof Ville && StringUtils.equals(((Ville) obj).getNom(), nom));
    }
    
    public int hashCode(){
        return new HashCodeBuilder(25, 15).append(nom).toHashCode();
    }

}
