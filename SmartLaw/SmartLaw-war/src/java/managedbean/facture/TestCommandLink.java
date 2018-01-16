/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package managedbean.facture;

import java.io.Serializable;
import java.util.List;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import modeles.evenement.EvtDossierLibelle;

/**
 *
 * @author misa
 */
@Named(value = "testCommandLink")
@ViewScoped
public class TestCommandLink implements Serializable {

    /**
     * Creates a new instance of TestCommandLink
     */
    public TestCommandLink() {
    }

    public void antso(List<EvtDossierLibelle> evtDossier) {
        System.out.println("antso ");
        System.out.println("size " + evtDossier.size());
    }

}
