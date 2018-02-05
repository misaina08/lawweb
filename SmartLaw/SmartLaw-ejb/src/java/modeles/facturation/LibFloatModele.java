/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeles.facturation;

/**
 *
 * @author misa
 */
public class LibFloatModele {

    private String libelle;
    private Float valeur;

    public LibFloatModele() {

    }

    public LibFloatModele(String l, Float val) {
        setLibelle(l);
        setValeur(val);
        
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Float getValeur() {
        return valeur;
    }

    public void setValeur(Float valeur) {
        this.valeur = valeur;
    }

   

}
