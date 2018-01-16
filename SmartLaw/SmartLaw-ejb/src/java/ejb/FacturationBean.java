/*
 * To change generiqueBean.getService() license header, choose License Headers in Project Properties.
 * To change generiqueBean.getService() template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import modeles.dossiers.ContactDossier;
import modeles.evenement.EvenementDossier;
import modeles.evenement.EvtDossierLibelle;
import modeles.facturation.Facture;
import modeles.facturation.FactureEvt;
import modeles.facturation.TarifFactInterv;
import modeles.facturation.TarifFactIntervttar;
import modeles.facturation.TarifFactIntervttarLibelle;
import modeles.facturation.TarifFacture;
import modeles.parametres.TypeFacturationDossier;
import modeles.parametres.TypeTarifEvt;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.EvenementService;
import statiques.ObjetStatique;

/**
 *
 * @author misa
 */
@Stateless
@LocalBean
public class FacturationBean {

    @EJB
    private EvenementBean evenementBean;

    @EJB
    private GeneriqueBean generiqueBean;

    public void genererFacture(List<EvtDossierLibelle> liste, Float tva) throws Exception {
        System.out.println("generation facture");
        Session sess = null;
        Transaction tr = null;
        EvenementService service = null;
        try {
            service = new EvenementService();
            sess = generiqueBean.getService().getDao().getSessionFact().openSession();
            tr = sess.beginTransaction();
//            udpate evenementdossier :  afacturer=>false
            for (EvtDossierLibelle e : liste) {

                EvenementDossier evt = new EvenementDossier();
                evt.setId(e.getId());
                evt = (EvenementDossier) generiqueBean.getService().findById(evt);
                evt.setAfacturer(false);
                generiqueBean.getService().update(evt, sess);
            }
//            ajout facture
//            recherche mode de facturation par défaut pour le dossier
            TypeFacturationDossier typeFactDossier = new TypeFacturationDossier();
            typeFactDossier.setIdDossier(liste.get(0).getIdDossier());
            typeFactDossier = ((List<TypeFacturationDossier>) (List<?>) generiqueBean.getService().find(typeFactDossier, sess)).get(0);

            Facture fact = new Facture();

//            a faire : trouver adresse de facturation pour dossier : OK
            ContactDossier cd = new ContactDossier();
            cd.setIdDossier(liste.get(0).getIdDossier());
            cd.setTypeContact("FACT");
            cd = ((List<ContactDossier>) (List<?>) generiqueBean.getService().find(cd)).get(0);
            fact.setIdContact(cd.getIdContact());
            fact.setIdDossier(liste.get(0).getIdDossier());
            fact.setIdTypeFacture(typeFactDossier.getIdTypeFacture());
            fact.setDateFacture(new Date());
            fact.setEditee(false);
            fact.setReglee(false);
            fact.setTva(tva);
            fact.setEditeePar("");
//            a faire : date ech = date.now + echeance dossier
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, typeFactDossier.getEcheance());
            Date toDate = cal.getTime();
            fact.setDateEcheance(toDate);
            fact.setSansSuite(false);
            fact.setIdIntervenantEdit(0);
            List<TarifFactIntervttarLibelle> listeTarifFactInterv = service.groupTarifIntervFactParTypeTarif(liste);
            Float mtfact = new Float(0);
            for (TarifFactIntervttarLibelle tfi : listeTarifFactInterv) {
                mtfact += tfi.getMt();
            }
            fact.setMontant(mtfact);
            generiqueBean.getService().save(fact, sess);

            //id intervenant rehetra
            List<Integer> idRecu = new ArrayList<Integer>();

            int taille = 0;
            idRecu.add(liste.get(0).getIdIntervenant());
            for (EvtDossierLibelle e : liste) {
                int mtov = 0;
                for (Integer id : idRecu) {
                    if (e.getIdIntervenant().equals(id)) {
                        mtov++;
                        break;
                    }
                }
                if (mtov == 0) {
                    idRecu.add(e.getIdIntervenant());
                }
            }

//            ajout tariffactinterv
            for (Integer it : idRecu) {
                TarifFactInterv tarfi = new TarifFactInterv();
                tarfi.setIdFacture(fact.getId());
                tarfi.setTotalHt(new Float(0));
                tarfi.setIdIntervenant(it);
                for (TarifFactIntervttarLibelle tfi : listeTarifFactInterv) {
                    if (tfi.getIdIntervenant().equals(it)) {
                        tarfi.setTotalHt(tarfi.getTotalHt() + tfi.getMt());
                    }
                }
                generiqueBean.getService().save(tarfi, sess);

                //            ajout tariffactintervttar
                for (TarifFactIntervttarLibelle tfi : listeTarifFactInterv) {
                    if (it.equals(tfi.getIdIntervenant())) {
                        TarifFactIntervttar newtfi = new TarifFactIntervttar();
                        newtfi.setIdTarifFactInterv(tarfi.getId());
                        newtfi.setIdTypeTarif(tfi.getIdTypeTarif());
                        newtfi.setMt(tfi.getMt());

                        generiqueBean.getService().save(newtfi, sess);
                    }

                }

            }
//            ajout dans tariffacture
            for (TypeTarifEvt tte : ObjetStatique.getTypeTarifEvt()) {
                TarifFacture tf = new TarifFacture();
                tf.setIdTypeTarif(tte.getId());
                tf.setIdFacture(fact.getId());
                tf.setMt(new Float(0));
                for (TarifFactIntervttarLibelle tfi : listeTarifFactInterv) {
                    if (tte.getId().equals(tfi.getIdTypeTarif())) {
                        tf.setMt(tf.getMt() + tfi.getMt());
                    }
                }
                generiqueBean.getService().save(tf, sess);
            }

//            ajout dans factureevt
            for (EvtDossierLibelle l : liste) {
                FactureEvt fe = new FactureEvt();
                fe.setIdFacture(fact.getId());
                fe.setIdEvtDossier(l.getId());
                generiqueBean.getService().save(fe, sess);
            }
            tr.commit();
        } catch (Exception ex) {
            tr.rollback();
            throw ex;
        } finally {
            if (sess != null) {
                sess.close();
            }
        }
    }

}
