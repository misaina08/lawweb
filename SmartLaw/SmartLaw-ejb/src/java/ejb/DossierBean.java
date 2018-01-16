/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.io.File;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import modeles.BaseModele;
import modeles.contact.ContactClient;
import modeles.dossiers.ContactDossier;
import modeles.dossiers.Dossier;
import modeles.parametres.DefaultDir;
import modeles.parametres.TypeFacturationClient;
import modeles.parametres.TypeFacturationDossier;
import org.hibernate.Session;
import org.hibernate.Transaction;
import statiques.ObjetStatique;
import utilitaire.ConstanteDirectory;
import utilitaire.DefaultsDirectory;
import utilitaire.Util;

/**
 *
 * @author misa
 */
@Stateless
@LocalBean
public class DossierBean {

    @EJB
    private GeneriqueBean generiqueBean;

    public void save(Dossier dossier) throws Exception {
        
        Session sess = null;
        Transaction tr = null;
        TypeFacturationDossier td = null;
        TypeFacturationClient tf = null;
        TypeFacturationClient tfc = null;
        List<BaseModele> bm = null;

        try {
            

            tf = new TypeFacturationClient();

            td = new TypeFacturationDossier();
            sess = generiqueBean.getService().getDao().getSessionFact().openSession();
            tr = sess.beginTransaction();
            
//            save dossier
            generiqueBean.getService().save(dossier);
            
            //find premier contact du client
            ContactClient cc = new ContactClient();
            cc.setIdClient(dossier.getIdClient());
            cc = ((List<ContactClient>) (List<?>) generiqueBean.getService().find(cc)).get(0);
//            save apporteur et adresse de facturation par défaut du dossier
            ContactDossier cd = new ContactDossier();
            cd.setIdContact(cc.getIdContact());
            cd.setIdDossier(dossier.getId());
            cd.setTypeContact("APP");
            generiqueBean.getService().save(cd, sess);
            ContactDossier cdf = new ContactDossier();
            cdf.setIdContact(cc.getIdContact());
            cdf.setIdDossier(dossier.getId());
            cdf.setTypeContact("FACT");
            generiqueBean.getService().save(cdf, sess);

            //find type facturation client pour le dossier
            tf.setIdClient(dossier.getIdClient());
            bm = generiqueBean.getService().find(tf);
            tfc = (TypeFacturationClient) bm.get(0);

            //save type facturation dossier
            td.setIdDossier(dossier.getId());
            td.setCategorieComptable(tfc.getCategorieComptable());
            td.setCompteComptable(tfc.getCompteComptable());
            td.setCompteTiers(tfc.getCompteTiers());
            td.setEcheance(tfc.getEcheance());
            td.setIdMode(tfc.getIdMode());
            td.setIdTypeFacture(tfc.getIdTypeFacture());
            td.setLangue(tfc.getLangue());
            td.setMtForfait(tfc.getMtForfait());
            td.setPeriodicite(tfc.getPeriodicite());
            td.setTauxMode(tfc.getTauxMode());
            td.setTauxTva(tfc.getTauxTva());
            td.setTypeFacture(tfc.getTypeFacture());

            generiqueBean.getService().save(td, sess);

//            création des répértoires du dossier
            String nomDossierCreated = dossier.getNumeroDossier();
            File dirDossier = new File(ConstanteDirectory.getDefaultDirectoryServer() + nomDossierCreated);
            dirDossier.mkdir();
            for (DefaultDir defDir : ObjetStatique.getDefaultDir()) {
                File dir = new File(dirDossier.getAbsolutePath() + "/" + defDir.getNom() + "/");
                dir.mkdir();
            }
            // crétaion d'un dossier dénomé "Factures"
            File dirFacture = new File(dirDossier.getAbsolutePath() + "/"+DefaultsDirectory.FACTURES.toString()+"/");
            dirFacture.mkdir();

            tr.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }
    
    
}
