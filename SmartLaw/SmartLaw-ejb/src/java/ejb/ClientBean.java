/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.Map;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;
import modeles.clients.Client;
import modeles.contact.Contact;
import modeles.contact.ContactClient;
import modeles.parametres.TypeFacturationClient;
import org.hibernate.Session;
import org.hibernate.Transaction;
import services.ClientService;
import utilitaire.ReportUtil;
import utilitaire.ReportUtil.ReportType;

/**
 *
 * @author misa
 */
@Stateless
@LocalBean
public class ClientBean {

    @EJB
    private GeneriqueBean generiqueBean;

    public void save(Client client, TypeFacturationClient tf) throws Exception {
        Session sess = null;
        Transaction tr = null;
        try {
            sess = generiqueBean.getService().getDao().getSessionFact().openSession();
            tr = sess.beginTransaction();
            generiqueBean.getService().save(client, sess);
            tf.setIdClient(client.getId());
            generiqueBean.getService().save(tf, sess);
            Contact contact = new Contact();
            contact.setNom(client.getNom());
            contact.setPrenom("");
            contact.setFonction("");
            contact.setVille("");
            contact.setCodePostal(101);
            contact.setPays("Madagascar");
            contact.setIdTitre(client.getIdTitre());
            contact.setEnSommeil(Boolean.FALSE);

            generiqueBean.getService().save(contact, sess);
            ContactClient cc = new ContactClient();
            cc.setIdClient(client.getId());
            cc.setIdContact(contact.getId());
            generiqueBean.getService().save(cc, sess);
            tr.commit();
        } catch (Exception ex) {
            tr.rollback();
            throw ex;
        }
    }


    public void printFiche(Integer id) throws Exception {
        ReportUtil reportUtil = null;
        try {
            ClientService service = new ClientService();

            Map<String, Object> map = service.produceMapFicheClient(id);
            reportUtil = new ReportUtil();
            String pathReport = ReportUtil.pathDocs + "/report/client-fiche";

            HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
            response.reset();
//            Nom du fichier
            reportUtil.download(map, response, ReportType.PDF, pathReport, "Client N°"+id);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
