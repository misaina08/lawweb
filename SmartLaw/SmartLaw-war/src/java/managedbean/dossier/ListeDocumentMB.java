/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.dossier;

import ejb.DossierBean;
import ejb.GeneriqueBean;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.dossiers.DocumentModele;
import modeles.dossiers.DossierLibelle;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import utilitaire.ConstanteDirectory;
import utilitaire.MessageUtil;

/**
 *
 * @author misa
 */
@Named(value = "listeDocumentMB")
@ViewScoped
public class ListeDocumentMB implements Serializable {

    @EJB
    private DossierBean dossierBean;
    @EJB
    private GeneriqueBean generiqueBean;

    private Integer idDossier;
    private DossierLibelle dossier;
    private String pathDossier;
    private String[] tiroirs = null;
    private String newDossier;
    List<DocumentModele> fichiers;
    private String fileSelected;

    /**
     * Creates a new instance of ListeDocumentMB
     */
    public ListeDocumentMB() {
    }

    public void loadDossier() {
        try {
            DossierLibelle d = new DossierLibelle();
            d.setId(idDossier);
            dossier = (DossierLibelle) generiqueBean.getService().findById(d);
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.addFlashErrorMessage("Erreur de chargement");
        }
    }

    public void ajouterTiroir() {
        try {
            dossierBean.ajoutTiroir(dossier, newDossier);
            tiroirs = dossierBean.getTiroirsDossier(dossier);
            newDossier = "";
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void supprimerTiroir(String nomTiroir) {
        try {
            dossierBean.supprimerTiroir(dossier, nomTiroir);
            tiroirs = dossierBean.getTiroirsDossier(dossier);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

//    liste des fichiers dans un tiroir
    public void voirFichiers(String nomTiroir) {
        try {
            setPathDossier(ConstanteDirectory.getDefaultDirectoryServer() + dossier.getNumeroDossier() + "/" + nomTiroir);
            fichiers = dossierBean.filesTiroir(getPathDossier());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public Integer getIdDossier() {
        return idDossier;
    }

    public void setIdDossier(Integer idDossier) {
        this.idDossier = idDossier;
    }

    public DossierLibelle getDossier() {
        return dossier;
    }

    public void setDossier(DossierLibelle dossier) {
        this.dossier = dossier;
    }

    public String getPathDossier() {
        return pathDossier;
    }

    public void setPathDossier(String pathDossier) {
        this.pathDossier = pathDossier;
    }

    public String[] getTiroirs() {
        if (tiroirs == null) {
            try {
                tiroirs = dossierBean.getTiroirsDossier(dossier);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return tiroirs;
    }

    public void setTiroirs(String[] tiroirs) {
        this.tiroirs = tiroirs;
    }

    public String getNewDossier() {
        return newDossier;
    }

    public void setNewDossier(String newDossier) {
        this.newDossier = newDossier;
    }

    public List<DocumentModele> getFichiers() {
        return fichiers;
    }

    public void setFichiers(List<DocumentModele> fichiers) {
        this.fichiers = fichiers;
    }

    public StreamedContent download() {
        System.out.println("aaaaaaaaaaaaaaaa");
        InputStream input = null;
        try {
            StreamedContent download = new DefaultStreamedContent();

            String pa = this.getPathDossier() + "/" + fileSelected;
            System.out.println(""+pa);
            String p = "D:\\work\\smrhr\\lawapp\\docs\\dossiers\\180007\\Honoraires\\20171001_215124.jpg";
            File file = new File(pa);
            input = new FileInputStream(file);
            ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
            download = new DefaultStreamedContent(input, externalContext.getMimeType(file.getName()), file.getName());
            return download;
        } catch (FileNotFoundException ex) {
            System.out.println("file not found");
            return null;
        }
    }

    public String getFileSelected() {
        return fileSelected;
    }

    public void setFileSelected(String fileSelected) {
        this.fileSelected = fileSelected;
    }

}
