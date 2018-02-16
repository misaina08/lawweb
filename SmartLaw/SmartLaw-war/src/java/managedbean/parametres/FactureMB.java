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
import modeles.parametres.ModeFacturation;
import modeles.parametres.TypeTarifEvt;
import services.ObjetStatiqueService;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "factureMB")
@ViewScoped
public class FactureMB implements Serializable {

    @EJB
    private GeneriqueBean bean;

    private List<TypeTarifEvt> types;
    private TypeTarifEvt newType = new TypeTarifEvt();
    private TypeTarifEvt typeToUpdate;

    private List<ModeFacturation> modes;
    private ModeFacturation newMode = new ModeFacturation();
    private ModeFacturation modeToUpdate;

    /**
     * Creates a new instance of ProcedureMB
     */
    public FactureMB() {
    }

    public void initListe() {
        try {
            types = (List<TypeTarifEvt>) (List<?>) bean.getService().find(new TypeTarifEvt());
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
            newType = new TypeTarifEvt();
            MessageUtil.messageInfo("Ajout effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public void supprimer(Integer id) {
        try {
            TypeTarifEvt t = new TypeTarifEvt();
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
            TypeTarifEvt t = new TypeTarifEvt();
            t.setId(id);
            typeToUpdate = (TypeTarifEvt) bean.getService().findById(t);
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
            modes = (List<ModeFacturation>) (List<?>) bean.getService().find(new ModeFacturation());
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
            newMode = new ModeFacturation();
            MessageUtil.messageInfo("Ajout effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public void supprimerMode(Integer id) {
        try {
            ModeFacturation t = new ModeFacturation();
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
            ModeFacturation t = new ModeFacturation();
            t.setId(id);
            modeToUpdate = (ModeFacturation) bean.getService().findById(t);
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

    public List<TypeTarifEvt> getTypes() {
        if (types == null) {
            initListe();
        }
        return types;
    }

    public void setTypes(List<TypeTarifEvt> types) {
        this.types = types;
    }

    public TypeTarifEvt getNewType() {
        return newType;
    }

    public void setNewType(TypeTarifEvt newType) {
        this.newType = newType;
    }

    public TypeTarifEvt getTypeToUpdate() {
        return typeToUpdate;
    }

    public void setTypeToUpdate(TypeTarifEvt typeToUpdate) {
        this.typeToUpdate = typeToUpdate;
    }

    public List<ModeFacturation> getModes() {
        if (modes == null) {
            initListeMode();
        }
        return modes;
    }

    public void setModes(List<ModeFacturation> modes) {
        this.modes = modes;
    }

    public ModeFacturation getNewMode() {
        return newMode;
    }

    public void setNewMode(ModeFacturation newMode) {
        this.newMode = newMode;
    }

    public ModeFacturation getModeToUpdate() {
        return modeToUpdate;
    }

    public void setModeToUpdate(ModeFacturation modeToUpdate) {
        this.modeToUpdate = modeToUpdate;
    }

}
