package utilitaire;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.Flash;

/**
 * Classe utilitaire pour JSF. Pour les messages, avec ou sans redirection.
 *
 * @author richard
 */
public class MessageUtil {

    /**
     * Avec clientId. Pour retrouver un clientId, sur un exemple : si le a un id
     * égal à "input" et s'il est dans un formulaire dont le id est "form",
     * clientId est "form:input". On peut aussi le trouver en examinant le
     * source de la page HTML.
     *
     * @param messageDetail message détaillé.
     * @param messageResume message résumé.
     * @param severite sévérité du message.
     * @param clientId id du client qui est attaché au message.
     */
    public static void message(String messageDetail, String messageResume,
            FacesMessage.Severity severite, String clientId) {
        FacesMessage msg
                = new FacesMessage(severite, messageResume, messageDetail);
        FacesContext.getCurrentInstance().addMessage(clientId, msg);
    }

    /**
     * Message qui n'est pas attaché à un composant particulier.
     *
     * @param messageDetail
     * @param messageResume
     * @param severite
     */
    public static void message(String messageDetail, String messageResume,
            FacesMessage.Severity severite) {
        FacesMessage msg
                = new FacesMessage(severite, messageResume, messageDetail);
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    /**
     * Message de sévérité FacesMessage.SEVERITY_ERROR qui n'est pas attaché à
     * un composant particulier. On peut donner un message différente pour le
     * message détaillé et pour le message résumé.
     *
     * @param messageDetail
     * @param messageResume
     */
    public static void messageErreur(String messageDetail, String messageResume) {
        message(messageDetail, messageResume, FacesMessage.SEVERITY_ERROR);
    }

    public static void messageErreur(String message) {
        message(message, message, FacesMessage.SEVERITY_ERROR);
    }

    /**
     * Message d'erreur attaché à un composant particulier.
     *
     * @param messageDetail
     * @param messageResume
     * @param clientId
     */
    public static void messageErreur(String messageDetail, String messageResume, String clientId) {
        message(messageDetail, messageResume, FacesMessage.SEVERITY_ERROR, clientId);
    }

    /**
     * Message d'information (ou de succès) qui n'est pas attaché à un composant
     * particulier.
     *
     * @param messageDetail
     * @param messageResume
     */
    public static void messageInfo(String messageDetail, String messageResume) {
        message(messageDetail, messageResume, FacesMessage.SEVERITY_INFO);
    }

    public static void messageInfo(String message) {
        message(message, message, FacesMessage.SEVERITY_INFO);
    }

    /**
     * Messages avec redirection, attaché à un composant particulier.
     *
     * @param message à afficher
     */
    public static void addFlashMessage(FacesMessage message, String clientId) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Flash flash = facesContext.getExternalContext().getFlash();
        flash.setKeepMessages(true);
        facesContext.addMessage(clientId, message);
    }

    /**
     * Messages avec redirection, pas attaché à un composant particulier.
     *
     * @param message à afficher
     */
    public static void addFlashMessage(FacesMessage message) {
        addFlashMessage(message, null);
    }

    /**
     * Message d'erreur avec redirection, attaché à un composant particulier.
     *
     * @param message
     */
    public static void addFlashErrorMessage(String message, String clientId) {
        FacesMessage msg
                = new FacesMessage(FacesMessage.SEVERITY_ERROR, message, message);
        addFlashMessage(msg, clientId);
    }

    /**
     * Message d'erreur avec redirection, pas attaché à un composant
     * particulier.
     *
     * @param message
     */
    public static void addFlashErrorMessage(String message) {
        addFlashErrorMessage(message, null);
    }

    /**
     * Message d'information avec redirection, pas attaché à un composant
     * particulier.
     *
     * @param message
     */
    public static void addFlashInfoMessage(String message) {
        FacesMessage msg
                = new FacesMessage(FacesMessage.SEVERITY_INFO, message, message);
        addFlashMessage(msg);
    }

    /**
     * Message d'information avec redirection, pas attaché à un composant
     * particulier.
     *
     * @param message
     */
    public static void addFlashInfoMessage(String titre, String message) {
        FacesMessage msg
                = new FacesMessage(FacesMessage.SEVERITY_INFO, titre, message);
        addFlashMessage(msg);
    }

    /**
     * Message d'erreur avec redirection, attaché à un composant particulier.
     *
     * @param message
     */
    public static void addFlashInfoMessageComposant(String message, String clientId) {
        FacesMessage msg
                = new FacesMessage(FacesMessage.SEVERITY_INFO, message, message);
        addFlashMessage(msg);
    }
    
    
    /**
     * Message d'avertissement avec redirection
     * @param titre
     * @param message 
     */
    public static void addFlashWarnMessage(String titre, String message) {
        FacesMessage msg
                = new FacesMessage(FacesMessage.SEVERITY_WARN, titre, message);
        addFlashMessage(msg);
    }
    public static void addFlashWarnMessageComposant(String message, String clientId) {
        FacesMessage msg
                = new FacesMessage(FacesMessage.SEVERITY_WARN, message, message);
        addFlashMessage(msg);
    }
    public static void addFlashWarnMessage(String message) {
        FacesMessage msg
                = new FacesMessage(FacesMessage.SEVERITY_WARN, message, message);
        addFlashMessage(msg);
    }
    public static void messageWarn(String messageDetail, String messageResume) {
        message(messageDetail, messageResume, FacesMessage.SEVERITY_WARN);
    }

    public static void messageWarn(String message) {
        message(message, message, FacesMessage.SEVERITY_WARN);
    }

}
