/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import modeles.affichage.EvtDossierLibGroupBy;
import modeles.dossiers.DossierLibelle;
import modeles.evenement.EvtDossierLibelle;
import modeles.evenement.EvtDossierLibellePDF;
import modeles.facturation.TarifFactIntervttarLibelle;
import modeles.parametres.TypeTarifEvt;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import statiques.ObjetStatique;
import utilitaire.Util;

/**
 *
 * @author misa
 */
public class EvenementService extends BaseService {

    public List<TarifFactIntervttarLibelle> groupTarifIntervFactParTypeTarif(List<EvtDossierLibelle> liste) throws Exception {
        List<TarifFactIntervttarLibelle> res = null;
        try {
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
            taille = idRecu.size();
            res = new ArrayList<TarifFactIntervttarLibelle>(taille * ObjetStatique.getTypeTarifEvt().size());
            for (Integer idre : idRecu) {
                for (TypeTarifEvt tte : ObjetStatique.getTypeTarifEvt()) {
                    TarifFactIntervttarLibelle newtfi = new TarifFactIntervttarLibelle();
                    newtfi.setMt(new Float(0));
                    newtfi.setIdTypeTarif(tte.getId());

                    newtfi.setIdIntervenant(idre);
                    for (EvtDossierLibelle edl : liste) {
                        if (edl.getIdIntervenant().equals(idre) && edl.getIdTypeTarif().equals(tte.getId())) {
                            newtfi.setMt(newtfi.getMt() + edl.getMt());
                        }
                    }
                    if (idRecu.size() != 0) {
                        res.add(newtfi);
                    }
                }
            }
            return res;
        } catch (Exception ex) {
            throw ex;
        }

    }

    public Map produceMapEvtDossier(List<EvtDossierLibelle> liste, DossierLibelle dLib) throws Exception {
        Map<String, Object> map = null;
        Util util = null;
        try {
            util = new Util();
            map = new HashMap<String, Object>();

//            evt group by typetarif
            List<EvtDossierLibellePDF> listeEvtGroupe = new ArrayList<EvtDossierLibellePDF>(ObjetStatique.getTypeTarifEvt().size());

            for (TypeTarifEvt tarif : ObjetStatique.getTypeTarifEvt()) {
                EvtDossierLibellePDF modeleEvtTarif = new EvtDossierLibellePDF();
                modeleEvtTarif.setIdTypeTarif(tarif.getId());
                modeleEvtTarif.setTypeTarif(tarif.getLibelle());
                modeleEvtTarif.setListeEvt(new ArrayList<EvtDossierLibelle>());
                Float totalMt = new Float(0);
                Time totalDuree = new Time(0, 0, 0);
                for (EvtDossierLibelle evt : liste) {
                    if (tarif.getId().equals(evt.getIdTypeTarif())) {
                        totalMt += evt.getMt();
                        System.out.println("date " + evt.getDuree());
                        totalDuree.setMinutes(totalDuree.getMinutes() + util.dateToMinute(evt.getDuree()));

                        modeleEvtTarif.getListeEvt().add(evt);
                    }
                }
                modeleEvtTarif.setTotHt(totalMt);
                modeleEvtTarif.setTotDuree(totalDuree);
                listeEvtGroupe.add(modeleEvtTarif);
            }
            JRBeanCollectionDataSource jrCollTar = new JRBeanCollectionDataSource(listeEvtGroupe);
            map.put("LIST_EVT", jrCollTar);
            map.put("DOSSIER", dLib);

            return map;
        } catch (Exception ex) {
            throw ex;
        }
    }

    public List<EvtDossierLibGroupBy> findEventByIntervGroupByDossier(Integer iDIntervenant, String type) throws Exception {
        Util u = new Util();
        Session sess = null;
        List<EvtDossierLibGroupBy> res = null;

        try {
            sess = this.getDao().getSessionFact().openSession();
            String sql = null;
            if (type.equalsIgnoreCase("intervenant")) {
                sql = "SELECT e.iddossier,dl.numerodossier,dl.vnomdossier as nomDossier,dl.nomclient   FROM evenementdossier e\n"
                        + "JOIN intervenant i ON i.idintervenant = e.idintervenant  JOIN evttarif evt ON evt.idevttarif = e.idevttarif  JOIN typetarifevt tt ON tt.idtypetarif = evt.idtypetarif JOIN dossierlibelle dl ON dl.iddossier = e.iddossier\n"
                        + "where e.idIntervenant = " + iDIntervenant + " group by  e.iddossier,dl.numerodossier,dl.vnomdossier,dl.nomclient";
            } else {
                sql = "SELECT e.iddossier,dl.numerodossier,dl.vnomdossier as nomDossier,dl.nomclient   FROM evenementdossier e\n"
                        + "JOIN intervenant i ON i.idintervenant = e.demandeur  JOIN evttarif evt ON evt.idevttarif = e.idevttarif  JOIN typetarifevt tt ON tt.idtypetarif = evt.idtypetarif JOIN dossierlibelle dl ON dl.iddossier = e.iddossier\n"
                        + "where e.demandeur = " + iDIntervenant + " group by  e.iddossier,dl.numerodossier,dl.vnomdossier,dl.nomclient";
            }
            res = new ArrayList<EvtDossierLibGroupBy>();

            SQLQuery query = sess.createSQLQuery(sql);
            List list = query.list();
            Iterator iter = list.iterator();
            EvtDossierLibGroupBy evt;
            while (iter.hasNext()) {
                Object[] objArray = (Object[]) iter.next();
                evt = new EvtDossierLibGroupBy();
                evt.setIdDossier((Integer) objArray[0]);
                evt.setNumDossier(objArray[1].toString());
                evt.setNomAdversaire(objArray[2].toString());
                evt.setNomClient(objArray[3].toString());
                evt.setNomDossier(objArray[2].toString());
                res.add(evt);

            }
        } catch (Exception e) {

            e.printStackTrace();
        } finally {
            sess.close();
        }
        return res;

    }
}
