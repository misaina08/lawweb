/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.facture;

import ejb.FacturationBean;
import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.dossiers.ContactDossierLibelle;
import modeles.dossiers.DossierLibelle;
import modeles.facturation.FactureLibelle;
import modeles.facturation.LibFloatModele;
import modeles.facturation.TarifFactIntervttarLibelle;
import modeles.parametres.TypeTarifEvt;
import statiques.ObjetStatique;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "ficheFactureMB")
@ViewScoped
public class FicheFactureMB implements Serializable {
    
    @EJB
    private GeneriqueBean generiqueBean;
    @EJB
    private FacturationBean facturationBean;
    
    private Integer idFacture;
    private FactureLibelle facture;
    private DossierLibelle dossier;
    private ContactDossierLibelle contDoss;
    private List<TarifFactIntervttarLibelle> dataIntervs;
    private List<TarifFactIntervttarLibelle> tarifsParInterv;
    private TarifFactIntervttarLibelle montantsInterv;
    private LibFloatModele[] tabTarifs;
    private Float tva = new Float(20);
    private Float totalHT = new Float(0);
    private Float mtTVA = new Float(0);
    private Float totalTTC = new Float(0);

    /**
     * Creates a new instance of FicheFactureMB
     */
    public FicheFactureMB() {
    }
    
    public void loadFacture() {
        try {
            FactureLibelle f = new FactureLibelle();
            f.setId(idFacture);
            facture = (FactureLibelle) generiqueBean.getService().findById(f);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void loadDossier() {
        try {
            DossierLibelle d = new DossierLibelle();
            d.setId(facture.getIdDossier());
            dossier = (DossierLibelle) generiqueBean.getService().findById(d);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void onIntervChange(Integer idIntervenant) {
        try {
            tabTarifs = new LibFloatModele[ObjetStatique.getTypeTarifEvt().size()];
            int indice = 0;
            Float tht = new Float(0);
            for (TypeTarifEvt tte : ObjetStatique.getTypeTarifEvt()) {
                
                Float m = new Float(0);
                
                for (TarifFactIntervttarLibelle tfi : tarifsParInterv) {
                    if (idIntervenant == 0) {
                        if (tte.getId().equals(tfi.getIdTypeTarif())) {
                            m += tfi.getMt();
                        }
                    } else {
                        if (tte.getId().equals(tfi.getIdTypeTarif()) && tfi.getIdIntervenant().equals(idIntervenant)) {
                            m += tfi.getMt();
                        }
                    }
                }
                tht += m;
                tabTarifs[indice] = new LibFloatModele(tte.getLibelle(), m);
                indice++;
            }
            
            totalHT = tht;
            mtTVA = tva * totalHT / 100;
            totalTTC = mtTVA + tht;
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void regler() {
        try {
            facturationBean.reglerFacture(idFacture);
            loadFacture();
            MessageUtil.messageInfo("Facture réglée");
        } catch(Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur");
        }
    }
    
    public Integer getIdFacture() {
        return idFacture;
    }
    
    public void setIdFacture(Integer idFacture) {
        this.idFacture = idFacture;
    }
    
    public FactureLibelle getFacture() {
        return facture;
    }
    
    public void setFacture(FactureLibelle facture) {
        this.facture = facture;
    }
    
    public DossierLibelle getDossier() {
        return dossier;
    }
    
    public void setDossier(DossierLibelle dossier) {
        this.dossier = dossier;
    }
    
    public ContactDossierLibelle getContDoss() {
        if (contDoss == null) {
            try {
                contDoss = new ContactDossierLibelle();
                contDoss.setIdDossier(facture.getIdDossier());
                contDoss.setTypeContact("FACT");
                contDoss = ((List<ContactDossierLibelle>) (List<?>) generiqueBean.getService().find(contDoss)).get(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return contDoss;
    }
    
    public void setContDoss(ContactDossierLibelle contDoss) {
        this.contDoss = contDoss;
    }
    
    public List<TarifFactIntervttarLibelle> getDataIntervs() {
        if (dataIntervs == null) {
            try {
                List<TarifFactIntervttarLibelle>[] temp = facturationBean.getDataIntervenantsByFacture(idFacture);
                dataIntervs = temp[1];
                tarifsParInterv = temp[0];
                onIntervChange(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return dataIntervs;
    }
    
    public void setDataIntervs(List<TarifFactIntervttarLibelle> dataIntervs) {
        this.dataIntervs = dataIntervs;
    }
    
    public TarifFactIntervttarLibelle getMontantsInterv() {
        return montantsInterv;
    }
    
    public Float getTva() {
        return tva;
    }
    
    public Float getTotalHT() {
        return totalHT;
    }
    
    public Float getTotalTTC() {
        return totalTTC;
    }
    
    public Float getMtTVA() {
        return mtTVA;
    }
    
    public LibFloatModele[] getTabTarifs() {
        return tabTarifs;
    }
    
    public void setTabTarifs(LibFloatModele[] tabTarifs) {
        this.tabTarifs = tabTarifs;
    }
    
}
