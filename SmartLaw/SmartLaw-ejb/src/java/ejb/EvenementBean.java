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
import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import modeles.affichage.EvtDossierLibGroupBy;
import modeles.dossiers.ContactDossier;
import modeles.dossiers.DossierLibelle;
import modeles.evenement.EvenementDossier;
import modeles.evenement.EvtDossierLibelle;
import modeles.evenement.MtTypeTarif;
import modeles.facturation.Facture;
import modeles.facturation.FactureEvt;
import modeles.facturation.TarifFactInterv;
import modeles.facturation.TarifFactIntervttar;
import modeles.facturation.TarifFactIntervttarLibelle;
import modeles.facturation.TarifFacture;
import modeles.intervenants.TarifsNS;
import modeles.parametres.TypeFacturationDossier;
import modeles.parametres.TypeTarifEvt;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.EvenementService;
import statiques.ObjetStatique;
import utilitaire.ReportUtil;
import utilitaire.Util;

/**
 *
 * @author misa
 */
@Stateless
@LocalBean
public class EvenementBean {

    @EJB
    private GeneriqueBean generiqueBean;

    public List<EvtDossierLibGroupBy> findDossierByRoleInterv(Integer iDIntervenant, String type) throws Exception{
        EvenementService es = new EvenementService();
        try {
            return es.findEventByIntervGroupByDossier(iDIntervenant, type);
        }catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    
    public List<MtTypeTarif> calculerTotaux(List<EvtDossierLibelle> taches) {
        List<MtTypeTarif> res = null;
        try {
            Util util = new Util();
            res = new ArrayList<MtTypeTarif>(ObjetStatique.getTypeTarifEvt().size());
            for (TypeTarifEvt te : ObjetStatique.getTypeTarifEvt()) {
                Float mt = new Float(0);
                Calendar cal = Calendar.getInstance();
                Date t = new Date(0);
                t.setSeconds(0);
                t.setMinutes(0);
                t.setHours(0);
                cal.setTime(t);
                for (EvtDossierLibelle ev : taches) {
                    System.out.println("evt " + ev.getLibTypeTarif());
                    System.out.println("idtypetarif " + ev.getIdTypeTarif());
                    System.out.println("idtypetarif statique " + te.getId());
                    System.out.println("duree " + ev.getDuree());

                    if (ev.getIdTypeTarif().equals(te.getId())) {
                        mt += ev.getMt();
                        cal.add(Calendar.HOUR, ev.getDuree().getHours());
                        cal.add(Calendar.MINUTE, ev.getDuree().getMinutes());
                        cal.add(Calendar.SECOND, ev.getDuree().getSeconds());

                    }
                    System.out.println("total mt " + mt);
                    System.out.println("total heure " + cal.getTime().getHours());
                    System.out.println("===");
                }
                Date dateRepere = new Date(0);
                Calendar calRepere = Calendar.getInstance();
                dateRepere.setSeconds(0);
                dateRepere.setMinutes(0);
                dateRepere.setHours(0);
                dateRepere.setDate(1);
                calRepere.setTime(dateRepere);
//
//                System.out.println("date repe " + dateRepere);
                MtTypeTarif tot = new MtTypeTarif();
                tot.setLibTypeTarif(te.getLibelle().toUpperCase());
                tot.setMtTotal(mt);
//                tot.setDureeTotal(util.dateDifference(calRepere.getTime(), cal.getTime()));
                tot.setDureeTotal(cal.getTime());
                res.add(tot);
            }
            return res;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void genererFacture(List<EvtDossierLibelle> liste, Float tva) throws Exception {
        EvenementService service = null;
        Session sess = null;
        Transaction tr = null;
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
            System.out.println("______________ idcontact " + cd.getIdContact());
            System.out.println("______________ iddossier " + cd.getIdDossier());
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

    public void printListeEvt(List<EvtDossierLibelle> liste, DossierLibelle dLib) throws Exception {
        ReportUtil reportUtil = null;
        try {
            EvenementService service = new EvenementService();
            for (EvtDossierLibelle e : liste) {
                System.out.println("id " + e.getId());
            }

            Map<String, Object> map = service.produceMapEvtDossier(liste, dLib);
            System.out.println("map " + map);
            reportUtil = new ReportUtil();
            String pathReport = ReportUtil.pathDocs + "/report/evt-dossier";

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.reset();
//            Nom du fichier
            Util util = new Util();

            reportUtil.download(map, response, ReportUtil.ReportType.PDF, pathReport, "Dossier N " + dLib.getNumeroDossier() + " : Taches non facturees" + util.dateToString(new Date()));

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Float calculMtInterv(Date duree, int idInterv, int idEvt) throws Exception {
        Util u = new Util();

        try {
            System.out.println("idinterv " + idInterv);
            System.out.println("idEvt " + idEvt);
            TarifsNS crit = new TarifsNS();
            crit.setIdevttarif(idEvt);
            crit.setIdintervenant(idInterv);
            List<TarifsNS> tarif = (List<TarifsNS>) (List<?>) generiqueBean.getService().find(crit);
            if (tarif == null) {
                throw new Exception("Cet intervenant n'a pas encore de tarifs pour les événements");
            }
            System.out.println("taille " + tarif.size());
            System.out.println("date " + tarif.get(0).getDuree());
            System.out.println("minute " + u.dateToMinute(duree));
            System.out.println("minute " + u.dateToMinute(tarif.get(0).getDuree()));
            return new Float((u.dateToMinute(duree) * tarif.get(0).getMt()) / u.dateToMinute(tarif.get(0).getDuree()));
        } catch (Exception ex) {
            throw ex;

        }
    }

}
