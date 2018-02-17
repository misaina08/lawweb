/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.parametres;

import ejb.GeneriqueBean;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.parametres.Fonction;
import services.ObjetStatiqueService;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "fonctionsMB")
@ViewScoped
public class FonctionsMB implements Serializable {

    @EJB
    private GeneriqueBean bean;

    private List<Fonction> procedures;
    private Fonction newProcedure = new Fonction();
    private Fonction toUpdate;

    /**
     * Creates a new instance of ProcedureMB
     */
    public FonctionsMB() {
    }

    public void initListe() {
        try {
            procedures = (List<Fonction>) (List<?>) bean.getService().find(new Fonction());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ajouter() {
        try {
            bean.getService().save(newProcedure);
            ObjetStatiqueService o = new ObjetStatiqueService();
            o.setBs(bean.getService());
            o.loadAll();
            initListe();
            newProcedure = new Fonction();
            MessageUtil.messageInfo("Ajout effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public void supprimer(Integer id) {
        try {
            Fonction t = new Fonction();
            t.setId(id);
            bean.getService().delete(t);
            ObjetStatiqueService o = new ObjetStatiqueService();
            o.setBs(bean.getService());
            o.loadAll();
            initListe();
            MessageUtil.messageInfo("Supprimée");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de suppression");
        }
    }

    public void load(Integer id) {
        try {
            Fonction t = new Fonction();
            t.setId(id);
            toUpdate = (Fonction) bean.getService().findById(t);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void modifier() {
        try {
            bean.getService().update(toUpdate);
            ObjetStatiqueService o = new ObjetStatiqueService();
            o.setBs(bean.getService());
            o.loadAll();
            initListe();
            MessageUtil.messageInfo("Modification effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modification");
        }
    }

    public List<Fonction> getProcedures() {
        if (procedures == null) {
            initListe();
        }
        return procedures;
    }

    public void setProcedures(List<Fonction> procedures) {
        this.procedures = procedures;
    }

    public Fonction getNewProcedure() {
        return newProcedure;
    }

    public void setNewProcedure(Fonction newProcedure) {
        this.newProcedure = newProcedure;
    }

    public Fonction getToUpdate() {
        return toUpdate;
    }

    public void setToUpdate(Fonction toUpdate) {
        this.toUpdate = toUpdate;
    }
    

}
