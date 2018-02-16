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
import modeles.parametres.TypeProcedure;
import services.ObjetStatiqueService;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "procedureMB")
@ViewScoped
public class ProcedureMB implements Serializable {

    @EJB
    private GeneriqueBean bean;

    private List<TypeProcedure> procedures;
    private TypeProcedure newProcedure = new TypeProcedure();
    private TypeProcedure toUpdate;

    /**
     * Creates a new instance of ProcedureMB
     */
    public ProcedureMB() {
    }

    public void initListe() {
        try {
            procedures = (List<TypeProcedure>) (List<?>) bean.getService().find(new TypeProcedure());
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
            newProcedure = new TypeProcedure();
            MessageUtil.messageInfo("Ajout effectué");
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur d'ajout");
        }
    }

    public void supprimer(Integer id) {
        try {
            TypeProcedure t = new TypeProcedure();
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
            TypeProcedure t = new TypeProcedure();
            t.setId(id);
            toUpdate = (TypeProcedure) bean.getService().findById(t);
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

    public List<TypeProcedure> getProcedures() {
        if (procedures == null) {
            initListe();
        }
        return procedures;
    }

    public void setProcedures(List<TypeProcedure> procedures) {
        this.procedures = procedures;
    }

    public TypeProcedure getNewProcedure() {
        return newProcedure;
    }

    public void setNewProcedure(TypeProcedure newProcedure) {
        this.newProcedure = newProcedure;
    }

    public TypeProcedure getToUpdate() {
        return toUpdate;
    }

    public void setToUpdate(TypeProcedure toUpdate) {
        this.toUpdate = toUpdate;
    }
    

}
