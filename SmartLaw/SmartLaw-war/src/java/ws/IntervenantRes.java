/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ws;

import ejb.GeneriqueBean;
import javax.annotation.ManagedBean;
import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import modeles.intervenants.Intervenant;

/**
 *
 * @author misa
 */
@ManagedBean
@Path("/intervenants")
public class IntervenantRes {
    @EJB
    private GeneriqueBean bean;
    
    @GET
    @Path("/authentification/{login}/{mdp}")
    @Produces(MediaType.APPLICATION_JSON)
    public Intervenant login(@PathParam("login") String login, @PathParam("mdp") String mdp) {
        try {
            Intervenant i = new Intervenant();
            i.setLogin(login);
            i.setMdp(mdp);
            return (Intervenant) bean.getService().find(i).get(0);
        } catch (Exception ex) {
            ex.printStackTrace();
            Intervenant i = new Intervenant();
            i.setId(0);
            return i;
        }
    }
}
