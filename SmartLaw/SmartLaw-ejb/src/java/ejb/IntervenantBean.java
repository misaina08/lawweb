/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import modeles.intervenants.Intervenant;
import services.BaseService;

/**
 *
 * @author misa
 */
@Stateless
@LocalBean
public class IntervenantBean {

    @PersistenceContext(unitName = "LawWeb-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    public Intervenant getByIdentification(String login, String mdp) throws Exception {
        List<Intervenant> result = null;
        try {
            BaseService service = new BaseService();
            Intervenant intervenant = new Intervenant();
            intervenant.setLogin(login);
            intervenant.setMdp(mdp);

            result = (List<Intervenant>) (List<?>) service.find(intervenant);
            if (result != null) {
                return result.get(0);
            } else {
                return null;
            }

        } catch (Exception ex) {
            throw ex;
        }

    }
}
