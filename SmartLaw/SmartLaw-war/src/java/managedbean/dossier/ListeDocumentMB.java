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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.dossiers.DocumentModele;
import modeles.dossiers.DossierLibelle;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
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
    private String tiroirSelected;

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
            fichiers = null;
            fichiers = dossierBean.filesTiroir(getPathDossier());
            for (DocumentModele d : fichiers) {
                System.out.println("___ " + d.getNom());
            }
            tiroirSelected = nomTiroir;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void supprimerFichier() {
        try {
            String pa = this.getPathDossier() + "/" + fileSelected;
            System.out.println("tiroir " + tiroirSelected);
            dossierBean.supprimerFichier(pa);
            voirFichiers(tiroirSelected);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            UploadedFile file = event.getFile();

            File f = new File(file.getFileName());
            FileOutputStream fos = new FileOutputStream(f);
            InputStream is = file.getInputstream();
            int BUFFER_SIZE = 8192;
            byte[] buffer = new byte[BUFFER_SIZE];
            int a;
            while (true) {
                a = is.read(buffer);
                if (a < 0) {
                    break;
                }
                fos.write(buffer, 0, a);
                fos.flush();
            }
//            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMddHHmmssSSS");
//            String newname = fmt.format(new Date())
//                    + event.getFile().getFileName().substring(
//                            event.getFile().getFileName().lastIndexOf('.'));

            Path fileToCpy = Paths.get(f.getAbsolutePath());
            System.out.println("fichier " + this.getPathDossier() + "/" + event.getFile().getFileName());
            Path fileCopy = Paths.get(this.getPathDossier() + "/" + event.getFile().getFileName());
            Path newFile = Files.copy(fileToCpy, fileCopy);

            fos.close();
            is.close();
            
            voirFichiers(tiroirSelected);
            MessageUtil.messageInfo(event.getFile().getFileName() + " a été sauvegardé.");

        } catch (Exception ex) {
            ex.printStackTrace();
            MessageUtil.messageErreur("Erreur de chargement du fichier");
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

    public String getTiroirSelected() {
        return tiroirSelected;
    }

    public void setTiroirSelected(String tiroirSelected) {
        this.tiroirSelected = tiroirSelected;
    }

}
