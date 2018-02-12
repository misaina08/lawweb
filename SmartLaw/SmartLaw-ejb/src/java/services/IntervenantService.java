/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.util.ArrayList;
import java.util.List;
import modeles.BaseModele;
import modeles.evenement.EvtTarif;
import modeles.intervenants.Intervenant;
import modeles.intervenants.IntervenantFonctionLibelle;
import modeles.intervenants.TarifIntervLibelle;
import modeles.intervenants.TarifIntervenant;
import modeles.intervenants.TarifNormaux;
import modeles.intervenants.TarifSpecial;
import modeles.intervenants.TarifSpeciaux;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author Misaina
 */
public class IntervenantService extends BaseService {

    public void saveTarifSpecial(EvtTarif evtTarif, Integer idIntervenant) throws Exception {
        Session sess = null;
        Transaction tr = null;
        try {
            sess = this.getDao().getSessionFact().openSession();
            tr = sess.beginTransaction();
            evtTarif.setType("ts");
            this.save(evtTarif, sess);
            TarifIntervenant tarifIntervenant = new TarifIntervenant();
            tarifIntervenant.setIdIntervenant(idIntervenant);
            tarifIntervenant.setIdEvtTarif(evtTarif.getId());
            tarifIntervenant.setTaux((float) 100);
            tarifIntervenant.setMt((float) 0);
            this.save(tarifIntervenant, sess);
            tr.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
        } finally {
            if (tr != null) {
                tr = null;
            }
            if (sess != null) {
                sess.close();
            }
        }
    }

    public List<Intervenant> find(Intervenant intr) throws Exception {
        List<Intervenant> res = null;
        try {
            res = new ArrayList<Intervenant>();
            List<BaseModele> bm = this.getDao().find(intr);
            for (BaseModele b : bm) {
                res.add((Intervenant) b);
            }
            return res;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<IntervenantFonctionLibelle> findIntervenantFonctionLibelle(IntervenantFonctionLibelle intr) throws Exception {
        List<IntervenantFonctionLibelle> res = null;
        try {
            res = new ArrayList<IntervenantFonctionLibelle>();
            List<BaseModele> bm = this.getDao().find(intr);
            for (BaseModele b : bm) {
                res.add((IntervenantFonctionLibelle) b);
            }
            return res;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<Intervenant> findWhere(Intervenant intr) throws Exception {
        List<Intervenant> res = null;
        try {
            res = new ArrayList<Intervenant>();
            List<BaseModele> bm = this.getDao().findWhere(intr);
            for (BaseModele b : bm) {
                res.add((Intervenant) b);
            }
            return res;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<TarifIntervLibelle> findTarifIntervlibelle(TarifIntervLibelle tarifIntervlib) throws Exception {
        List<TarifIntervLibelle> res = new ArrayList<TarifIntervLibelle>();
        for (BaseModele b : this.getDao().find(tarifIntervlib)) {
            res.add((TarifIntervLibelle) b);
        }
        return res;
    }

    public List<TarifNormaux> findTarifNormaux(TarifNormaux tn) throws Exception {
        List<TarifNormaux> res = new ArrayList<TarifNormaux>();
        for (BaseModele b : this.getDao().find(tn)) {
            res.add((TarifNormaux) b);
        }
        return res;
    }

    public List<TarifSpeciaux> findTarifSpeciaux(TarifSpeciaux tn) throws Exception {
        List<TarifSpeciaux> res = new ArrayList<TarifSpeciaux>();
        for (BaseModele b : this.getDao().find(tn)) {
            res.add((TarifSpeciaux) b);
        }
        return res;
    }

    public List<TarifSpecial> findTarifSpecial(TarifSpecial tn) throws Exception {
        List<TarifSpecial> res = new ArrayList<TarifSpecial>();
        for (BaseModele b : this.getDao().find(tn)) {
            res.add((TarifSpecial) b);
        }
        return res;
    }

    public void saveTarifSpecial(TarifSpecial tarifSpecial) throws Exception {

        super.save(tarifSpecial);

    }

    public void saveIntervenant(Intervenant intervenant) throws Exception {
        Session sess = null;
        Transaction tr = null;
        try {
            intervenant.setIdTypeUser(2);
            sess = this.getDao().getSessionFact().openSession();
            tr = sess.beginTransaction();
            this.save(intervenant, sess);
            int dernieriD = intervenant.getId();
            TarifIntervenant tarifIntervenant;
            EvtTarif evt = new EvtTarif();
            evt.setType("tn");
            EvenementService evenementService = new EvenementService();
            List<EvtTarif> evtTarif = (List<EvtTarif>) (List<?>) this.find(evt, sess);
            for (int compteur = 0; compteur < evtTarif.size(); compteur++) {
                tarifIntervenant = new TarifIntervenant();
                tarifIntervenant.setIdIntervenant(dernieriD);
                tarifIntervenant.setIdEvtTarif(evtTarif.get(compteur).getId());
                tarifIntervenant.setTaux((float) 100);
                tarifIntervenant.setMt((float) 0);

                this.save(tarifIntervenant, sess);
            }
            tr.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            tr.rollback();
            throw ex;
        }

    }

    public void saveTarifIntervenant(TarifIntervenant tarifIntervenant) throws Exception {

        super.save(tarifIntervenant);

    }

    public void updateTarifIntervenant(TarifIntervenant tarifIntervenant) throws Exception {

        super.update(tarifIntervenant);

    }

    public void updateIntervenant(Intervenant intervenant) throws Exception {

        super.update(intervenant);

    }

//    public List<TarifIntervLibelle> findTarifIntervLibelle(TarifIntervLibelle intr) throws Exception
//    {
//        List<Intervenant> res=null;
//        try
//        {
//            res=new ArrayList<Intervenant>();
//            List<BaseModele> bm=this.getDao().find(intr);
//            for(BaseModele b:bm)
//            {
//                res.add((Intervenant)b);
//            }
//            return res;
//        }
//        catch(Exception ex)
//        {
//            throw ex;
//        }
//    }
}
