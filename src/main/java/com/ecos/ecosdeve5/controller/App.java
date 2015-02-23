package com.ecos.ecosdeve5.controller;

import com.ecos.ecosdeve5.model.CalcularSimpsonRule;
import com.ecos.ecosdeve5.view.MainView;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Hello world!
 *
 */
public class App extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            List<CalcularSimpsonRule> ejercicios =new ArrayList<CalcularSimpsonRule>();
            CalcularSimpsonRule ejercicio=new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(1.1).setScale(1, RoundingMode.HALF_UP), new BigDecimal(10), new BigDecimal(9));
            ejercicio.calcular();
            ejercicios.add(ejercicio);
            ejercicio=new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(1.1812).setScale(4, RoundingMode.HALF_UP), new BigDecimal(10), new BigDecimal(10));
            ejercicio.calcular();
            ejercicios.add(ejercicio);
            ejercicio=new CalcularSimpsonRule(new BigDecimal(0.00001).setScale(5, RoundingMode.HALF_UP), new BigDecimal(2.750).setScale(3, RoundingMode.HALF_UP), new BigDecimal(10), new BigDecimal(30));
            ejercicio.calcular();
            ejercicios.add(ejercicio);
            MainView.showHome(req, resp,ejercicios);
        } catch (Exception ex) {
            MainView.error(req, resp, ex);
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            MainView.showHome(req, resp,new ArrayList<CalcularSimpsonRule>());
        } catch (Exception ex) {
            MainView.error(req, resp, ex);
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) throws Exception {
        Server server = new Server(Integer.valueOf(System.getenv("PORT")));
        //Server server = new Server(80);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        context.addServlet(new ServletHolder(new App()), "/*");
        try {
            server.start();
            server.join();
        } catch (Exception ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
            throw new Exception();
        }
    }
}

