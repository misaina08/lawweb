/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.dossier;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.dossiers.ContactDossierLibelle;
import modeles.dossiers.DossierLibelle;
import modeles.evenement.EvtDossierLibelle;
import modeles.intervenants.IntervDossierLibelle;
import modeles.intervenants.Intervenant;
import modeles.intervenants.IntervenantDossier;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "ficheDossierMB")
@ViewScoped
public class FicheDossierMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idDossier;
    private DossierLibelle dossier;
    private List<IntervDossierLibelle> intervs;
    private ContactDossierLibelle contDoss;
    private List<EvtDossierLibelle> listeEvt;
    private List<Intervenant> intervenants;
    private Integer idIntervSelected;

    /**
     * Creates a new instance of FicheDossierMB
     */
    public FicheDossierMB() {
    }

    public void loadDossier() {
        try {
            DossierLibelle d = new DossierLibelle();
            d.setId(idDossier);
            dossier = (DossierLibelle) generiqueBean.getService().findById(d);
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur de chargement");
        }
    }

    public void supprimerIntervenant(Integer id) {
        try {
            IntervenantDossier intervDel = new IntervenantDossier();
            intervDel.setId(id);
            generiqueBean.getService().delete(intervDel);

            IntervDossierLibelle i = new IntervDossierLibelle();
            i.setIdDossier(idDossier);
            intervs = (List<IntervDossierLibelle>) (List<?>) generiqueBean.getService().find(i);

            MessageUtil.messageInfo("Intervenant supprimé");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de suppression");
        }
    }

    public void loadAddIntervenants() {
        try {
            System.out.println("debut");
            if (intervenants == null) {
                System.out.println("null");
                intervenants = (List<Intervenant>) (List<?>) generiqueBean.getService().find(new Intervenant());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ajouterIntervenant() {
        try {
            IntervenantDossier intervDoss = new IntervenantDossier();
            intervDoss.setIdDossier(idDossier);
            intervDoss.setIdIntervenant(idIntervSelected);
            int mtovy = 0;
            for (IntervDossierLibelle idl : getIntervs()) {
                if (idl.getIdIntervenant().equals(intervDoss.getIdIntervenant())) {
                    mtovy++;
                    break;
                }
            }
            if (mtovy == 0) {
                IntervenantDossier ido = new IntervenantDossier();
                ido.setIdDossier(idDossier);
                ido.setIdIntervenant(idIntervSelected);
                generiqueBean.getService().save(ido);
                IntervDossierLibelle i = new IntervDossierLibelle();
                i.setIdDossier(idDossier);
                intervs = (List<IntervDossierLibelle>) (List<?>) generiqueBean.getService().find(i);
            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }

    public Integer getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(Integer idDossier) {
        this.idDossier = idDossier;
    }

    public DossierLibelle getDossier() {
        return dossier;
    }

    public void setDossier(DossierLibelle dossier) {
        this.dossier = dossier;
    }

    public List<IntervDossierLibelle> getIntervs() {
        if (intervs == null) {
            try {
                IntervDossierLibelle i = new IntervDossierLibelle();
                i.setIdDossier(idDossier);
                intervs = (List<IntervDossierLibelle>) (List<?>) generiqueBean.getService().find(i);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return intervs;
    }

    public void setIntervs(List<IntervDossierLibelle> intervs) {
        this.intervs = intervs;
    }

    public ContactDossierLibelle getContDoss() {
        if (contDoss == null) {
            try {
                contDoss = new ContactDossierLibelle();
                contDoss.setIdDossier(idDossier);
                contDoss.setTypeContact("APP");
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

    public List<EvtDossierLibelle> getListeEvt() {
        if (listeEvt == null) {
            try {
                EvtDossierLibelle evl = new EvtDossierLibelle();
                evl.setIdDossier(idDossier);
                evl.setAfacturer(true);
                listeEvt = (List<EvtDossierLibelle>) (List<?>) generiqueBean.getService().find(evl);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return listeEvt;
    }

    public void setListeEvt(List<EvtDossierLibelle> listeEvt) {
        this.listeEvt = listeEvt;
    }

    public List<Intervenant> getIntervenants() {
        return intervenants;
    }

    public void setIntervenants(List<Intervenant> intervenant) {
        this.intervenants = intervenant;
    }

    public Integer getIdIntervSelected() {
        return idIntervSelected;
    }

    public void setIdIntervSelected(Integer idIntervSelected) {
        this.idIntervSelected = idIntervSelected;
    }

}
