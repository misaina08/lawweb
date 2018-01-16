/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import services.BaseService;
import services.ObjetStatiqueService;

/**
 *
 * @author misa
 */
@Stateless
@LocalBean
public class GeneriqueBean {

    private BaseService service;
    
    public GeneriqueBean(){
        
    }
    
    @PostConstruct
    public void init() {
        try {
            System.out.println("injection du bean generiqueBean");
            service = new BaseService();
            ObjetStatiqueService objetStatiqueService = new ObjetStatiqueService();
            objetStatiqueService.setBs(service);
            objetStatiqueService.loadAll();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public BaseService getService() {
        return service;
    }

    public void setService(BaseService service) {
        this.service = service;
    }
    
}
