/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.intervenant;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.intervenants.Intervenant;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "utilisateursMB")
@ViewScoped
public class UtilisateursMB implements Serializable {

    @EJB
    private GeneriqueBean generiqueBean;
    private List<Intervenant> intervenants;
    private Integer idIntervSelected = 0;
    private Intervenant intervenant = null;

    private String amdp = "";
    private String nmdp = "";
    private String cmdp = "";

    /**
     * Creates a new instance of UtilisateursMB
     */
    public UtilisateursMB() {
    }

    public void onIntervChange(Integer id) {
        try {
            idIntervSelected = id;
            Intervenant i = new Intervenant();
            i.setId(id);
            intervenant = (Intervenant) generiqueBean.getService().findById(i);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void valider() {

        try {
         
            generiqueBean.getService().update(intervenant);

            MessageUtil.messageInfo("Accès modifié");

        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modification de l'accès");

        }
    }

    public void updateMdp() {
        try {
         
            Intervenant i = new Intervenant();
            i.setId(idIntervSelected);
            intervenant = (Intervenant) generiqueBean.getService().findById(i);
         

            if (amdp.compareTo(intervenant.getMdp()) == 0) {
                intervenant.setMdp(nmdp);
                generiqueBean.getService().update(intervenant);
                MessageUtil.messageInfo("Mot de passe modifié pour l'utilisateur sélectionné");
                
            } else {
                throw new Exception("Mot de passe erroné");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modification du mot de passe. ");
            if (ex.getMessage().compareToIgnoreCase("Mot de passe erroné") == 0) {
                MessageUtil.messageErreur(ex.getMessage());
            }
        }
    }

    public List<Intervenant> getIntervenants() {
        if (intervenants == null) {
            try {

                intervenants = (List<Intervenant>) (List<?>) generiqueBean.getService().find(new Intervenant());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return intervenants;
    }

    public Intervenant getIntervenant() {
        if (intervenant == null) {
            onIntervChange(getIntervenants().get(0).getId());
        }
        return intervenant;
    }

    public void setIntervenant(Intervenant intervenant) {
        this.intervenant = intervenant;
    }

    public Integer getIdIntervSelected() {
        return idIntervSelected;
    }

    public void setIdIntervSelected(Integer idIntervSelected) {
        this.idIntervSelected = idIntervSelected;
    }

    public String getAmdp() {
        return amdp;
    }

    public void setAmdp(String amdp) {
        this.amdp = amdp;
    }

    public String getNmdp() {
        return nmdp;
    }

    public void setNmdp(String nmdp) {
        this.nmdp = nmdp;
    }

    public String getCmdp() {
        return cmdp;
    }

    public void setCmdp(String cmdp) {
        this.cmdp = cmdp;
    }

}
