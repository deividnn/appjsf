/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.File;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import static util.Util.zipFolder;

/**
 *
 * @author DeividNn
 */
@WebListener
public class AppContext implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            String fotos = sce.getServletContext().getRealPath("/resources/fotos/");
            zipFolder(fotos,
                    System.getProperty("user.home") + File.separator + "fotosappjsf" + File.separator + "fotos_" + new Date().getTime() + ".zip");
        } catch (Exception ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        try {
            String fotos = sce.getServletContext().getRealPath("/resources/fotos/");
            zipFolder(fotos,
                    System.getProperty("user.home") + File.separator + "fotosappjsf" + File.separator + "fotos_" + new Date().getTime() + ".zip");
        } catch (Exception ex) {
            Logger.getLogger(Util.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
