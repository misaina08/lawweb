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
import modeles.parametres.Juridiction;
import modeles.parametres.Titre;
import services.ObjetStatiqueService;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "titresMB")
@ViewScoped
public class TitresMB implements Serializable {

    @EJB
    private GeneriqueBean bean;

    private List<Titre> titres;
    private Titre newType = new Titre();
    private Titre typeToUpdate;

    private List<Juridiction> modes;
    private Juridiction newMode = new Juridiction();
    private Juridiction modeToUpdate;

    /**
     * Creates a new instance of ProcedureMB
     */
    public TitresMB() {
    }

    public void initListe() {
        try {
            titres = (List<Titre>) (List<?>) bean.getService().find(new Titre());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ajouter() {
        try {
            bean.getService().save(newType);
            ObjetStatiqueService o = new ObjetStatiqueService();
            o.setBs(bean.getService());
            o.loadAll();
            initListe();
            newType = new Titre();
            MessageUtil.messageInfo("Ajout effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public void supprimer(Integer id) {
        try {
            Titre t = new Titre();
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
            Titre t = new Titre();
            t.setId(id);
            typeToUpdate = (Titre) bean.getService().findById(t);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void modifier() {
        try {
            bean.getService().update(typeToUpdate);
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

    // mode facturation
    public void initListeMode() {
        try {
            modes = (List<Juridiction>) (List<?>) bean.getService().find(new Juridiction());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void ajouterMode() {
        try {
            bean.getService().save(newMode);
            ObjetStatiqueService o = new ObjetStatiqueService();
            o.setBs(bean.getService());
            o.loadAll();
            initListeMode();
            newMode = new Juridiction();
            MessageUtil.messageInfo("Ajout effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public void supprimerMode(Integer id) {
        try {
            Juridiction t = new Juridiction();
            t.setId(id);
            bean.getService().delete(t);
            ObjetStatiqueService o = new ObjetStatiqueService();
            o.setBs(bean.getService());
            o.loadAll();
            initListeMode();
            MessageUtil.messageInfo("Supprimée");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de suppression");
        }
    }

    public void loadMode(Integer id) {
        try {
            Juridiction t = new Juridiction();
            t.setId(id);
            modeToUpdate = (Juridiction) bean.getService().findById(t);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void modifierMode() {
        try {
            bean.getService().update(modeToUpdate);
            ObjetStatiqueService o = new ObjetStatiqueService();
            o.setBs(bean.getService());
            o.loadAll();
            initListeMode();
            MessageUtil.messageInfo("Modification effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de modification");
        }
    }

    public List<Titre> getTitres() {
        if (titres == null) {
            initListe();
        }
        return titres;
    }

    public void setTitres(List<Titre> titres) {
        this.titres = titres;
    }

    public Titre getNewType() {
        return newType;
    }

    public void setNewType(Titre newType) {
        this.newType = newType;
    }

    public Titre getTypeToUpdate() {
        return typeToUpdate;
    }

    public void setTypeToUpdate(Titre typeToUpdate) {
        this.typeToUpdate = typeToUpdate;
    }

    public List<Juridiction> getModes() {
        if (modes == null) {
            initListeMode();
        }
        return modes;
    }

    public void setModes(List<Juridiction> modes) {
        this.modes = modes;
    }

    public Juridiction getNewMode() {
        return newMode;
    }

    public void setNewMode(Juridiction newMode) {
        this.newMode = newMode;
    }

    public Juridiction getModeToUpdate() {
        return modeToUpdate;
    }

    public void setModeToUpdate(Juridiction modeToUpdate) {
        this.modeToUpdate = modeToUpdate;
    }

}
