/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.client;

import ejb.ClientBean;
import ejb.GeneriqueBean;
import java.util.List;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import modeles.clients.Client;
import modeles.parametres.Titre;
import modeles.parametres.TypeFacturationClient;
import modeles.parametres.TypeFacture;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "ajoutClientMB")
@RequestScoped
public class AjoutClientMB {

    @EJB
    private GeneriqueBean generiqueBean;


    @EJB
    private ClientBean clientBean;

    private Titre titreSelected = new Titre();
    private Client client = new Client();
    private List<Titre> titres;

    /**
     * Creates a new instance of AjoutClientMB
     */
    public AjoutClientMB() {
    }

    public String ajouter() {
        try {

            TypeFacturationClient tf = new TypeFacturationClient();

            tf.setIdMode(1);
            tf.setCategorieComptable("");
            tf.setCompteComptable("70");
            tf.setLangue("Francais");
            tf.setEcheance(30);
            tf.setPeriodicite("");

            tf.setCompteTiers("");
            tf.setTypeFacture("");
            tf.setPeriodicite("Mensuelle");
            tf.setTauxTva(new Float(20.0));
            tf.setTauxMode(new Float(100.0));
            tf.setMtForfait(new Float(0.0));

            List<TypeFacture> typeFactures = (List<TypeFacture>) (List<?>) generiqueBean.getService().find(new TypeFacture());
            tf.setIdTypeFacture(typeFactures.get(0).getId());

            client.setIdTitre(titreSelected.getId());
            clientBean.save(client, tf);
            MessageUtil.addFlashInfoMessage("Client ajouté");
            return "/pages/client/liste.xhtml?faces-redirect=true;";

        } catch (Exception ex) {
            MessageUtil.addFlashErrorMessage("Erreur");
            return "";
        }
    }

    private Converter titreConverter = new Converter() {
        @Override
        public Object getAsObject(FacesContext context, UIComponent component, String value) {
            Titre t = null;
            try {
                t = new Titre();
                t.setId(new Integer(value));
                t = (Titre) generiqueBean.getService().findById(t);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return t;
        }

        @Override
        public String getAsString(FacesContext context, UIComponent component, Object value) {
            Titre t = (Titre) value;
            return t.getId() + "";
        }
    };

    public Converter getTitreConverter() {
        return titreConverter;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Titre> getTitres(){
        if (titres == null) {
            try {
                titres = (List<Titre>)(List<?>)generiqueBean.getService().find(new Titre());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return titres;
    }

    public void setTitres(List<Titre> titres) {
        this.titres = titres;
    }

    public Titre getTitreSelected() {
        return titreSelected;
    }

    public void setTitreSelected(Titre titreSelected) {
        this.titreSelected = titreSelected;
    }

    
}
