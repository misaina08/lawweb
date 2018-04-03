/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import ejb.GeneriqueBean;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import modeles.planning.PlanningLibelle;

/**
 *
 * @author misa
 */
@ManagedBean
@Path("/planning")
public class PlanningRes {

    @EJB
    private GeneriqueBean bean;

    /**
     * Liste de tous les plannings
     * @return 
     */
    @GET
    @Path("")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlanningLibelle> liste() {
        try {
            return (List<PlanningLibelle>) (List<?>) bean.getService().find(new PlanningLibelle());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Liste des plannings par intervenants
     * @param idIntervenant
     * @return 
     */
    @GET
    @Path("/par-intervenant/{idIntervenant}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlanningLibelle> getByIntervenant(@PathParam("idIntervenant") Integer idIntervenant) {
        try {
            PlanningLibelle p = new PlanningLibelle();
            p.setIdIntervenantpl(idIntervenant);
            return (List<PlanningLibelle>) (List<?>) bean.getService().find(p);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * liste des plannings par dossier
     * @param idDossier
     * @return 
     */
    @GET
    @Path("/par-dossier/{idDossier}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<PlanningLibelle> getByDossier(@PathParam("idDossier") Integer idDossier) {
        try {
            PlanningLibelle p = new PlanningLibelle();
            p.setIdDossier(idDossier);
            return (List<PlanningLibelle>) (List<?>) bean.getService().find(p);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
