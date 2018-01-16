/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeles.intervenants;

import java.sql.Time;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import modeles.BaseModele;

/**
 *
 * @author Misaina
 */
public class TarifsNS extends BaseModele {

    private Integer idintervenant;
    private Integer idtarifinterv;
    private Integer idevttarif;
    private Float taux;
    private Float mt;
    private String libelle;
    private String code;
    
    @Temporal(TemporalType.TIME)
    private Date duree;
    private Float mtevt;
    private Integer idfonction;
    private String libelleTypeTarif;
    private String type;

    public Integer getIdintervenant() {
        return idintervenant;
    }

    public void setIdintervenant(Integer idintervenant) {
        this.idintervenant = idintervenant;
    }

    public Integer getIdtarifinterv() {
        return idtarifinterv;
    }

    public void setIdtarifinterv(Integer idtarifinterv) {
        this.idtarifinterv = idtarifinterv;
    }

    public Integer getIdevttarif() {
        return idevttarif;
    }

    public void setIdevttarif(Integer idevttarif) {
        this.idevttarif = idevttarif;
    }

    public Float getTaux() {
        return taux;
    }

    public void setTaux(Float taux) {
        this.taux = taux;
    }

    public Float getMt() {
        return mt;
    }

    public void setMt(Float mt) {
        this.mt = mt;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDuree() {
        return duree;
    }

    public void setDuree(Date duree) {
        this.duree = duree;
    }

    public Float getMtevt() {
        return mtevt;
    }

    public void setMtevt(Float mtevt) {
        this.mtevt = mtevt;
    }

    public Integer getIdfonction() {
        return idfonction;
    }

    public void setIdfonction(Integer idfonction) {
        this.idfonction = idfonction;
    }

    public String getLibelleTypeTarif() {
        return libelleTypeTarif;
    }

    public void setLibelleTypeTarif(String libelleTypeTarif) {
        this.libelleTypeTarif = libelleTypeTarif;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVal() {
        String t = "";
        if (type.compareToIgnoreCase("tn") == 0) {
            t = "Tarif normal";
        } else {
            t = "Tarif special";
        }
        return libelle + " (" + t + ")";
    }

    public void setVal(String v) {

    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (getId() != null ? getId().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the getId() fields are not set
        if (!(object instanceof TarifsNS)) {
            return false;
        }
        TarifsNS other = (TarifsNS) object;
        if ((this.getId() == null && other.getId() != null) || (this.getId() != null && !this.getId().equals(other.getId()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + getId();
    }

}
