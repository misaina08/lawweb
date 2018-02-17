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
import modeles.parametres.Nature;
import modeles.parametres.DefaultDir;
import services.ObjetStatiqueService;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "dossiersMB")
@ViewScoped
public class DossiersMB implements Serializable {

    @EJB
    private GeneriqueBean bean;

    private List<DefaultDir> defaultDirs;
    private DefaultDir newType = new DefaultDir();
    private DefaultDir typeToUpdate;

    private List<Nature> modes;
    private Nature newMode = new Nature();
    private Nature modeToUpdate;

    /**
     * Creates a new instance of ProcedureMB
     */
    public DossiersMB() {
    }

    public void initListe() {
        try {
            defaultDirs = (List<DefaultDir>) (List<?>) bean.getService().find(new DefaultDir());
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
            newType = new DefaultDir();
            MessageUtil.messageInfo("Ajout effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public void supprimer(Integer id) {
        try {
            DefaultDir t = new DefaultDir();
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
            DefaultDir t = new DefaultDir();
            t.setId(id);
            typeToUpdate = (DefaultDir) bean.getService().findById(t);
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
            modes = (List<Nature>) (List<?>) bean.getService().find(new Nature());
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
            newMode = new Nature();
            MessageUtil.messageInfo("Ajout effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public void supprimerMode(Integer id) {
        try {
            Nature t = new Nature();
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
            Nature t = new Nature();
            t.setId(id);
            modeToUpdate = (Nature) bean.getService().findById(t);
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

    public List<DefaultDir> getDefaultDirs() {
        if (defaultDirs == null) {
            initListe();
        }
        return defaultDirs;
    }

    public void setDefaultDirs(List<DefaultDir> titres) {
        this.defaultDirs = titres;
    }

    public DefaultDir getNewType() {
        return newType;
    }

    public void setNewType(DefaultDir newType) {
        this.newType = newType;
    }

    public DefaultDir getTypeToUpdate() {
        return typeToUpdate;
    }

    public void setTypeToUpdate(DefaultDir typeToUpdate) {
        this.typeToUpdate = typeToUpdate;
    }

    public List<Nature> getModes() {
        if (modes == null) {
            initListeMode();
        }
        return modes;
    }

    public void setModes(List<Nature> modes) {
        this.modes = modes;
    }

    public Nature getNewMode() {
        return newMode;
    }

    public void setNewMode(Nature newMode) {
        this.newMode = newMode;
    }

    public Nature getModeToUpdate() {
        return modeToUpdate;
    }

    public void setModeToUpdate(Nature modeToUpdate) {
        this.modeToUpdate = modeToUpdate;
    }

}
