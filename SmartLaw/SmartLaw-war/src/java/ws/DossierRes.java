/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import ejb.GeneriqueBean;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import modeles.dossiers.DossierLibelle;

/**
 *
 * @author misa
 */
@ManagedBean
@Path("/dossiers")
public class DossierRes {

    @EJB
    private GeneriqueBean bean;

    @GET
    @Path("")
    @Produces("application/json")
    public List<DossierLibelle> getAll() {
        try {
            return (List<DossierLibelle>) (List<?>) bean.getService().find(new DossierLibelle());
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<DossierLibelle>();
        }
    }

    @GET
    @Path("/{idDossier}")
    @Produces("application/json")
    public DossierLibelle findById(@PathParam("idDossier") Integer idDossier) {
        try {
            DossierLibelle d = new DossierLibelle();
            d.setId(idDossier);
            return (DossierLibelle) bean.getService().findById(d);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
