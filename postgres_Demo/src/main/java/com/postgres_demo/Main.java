package com.postgres_demo;

import com.Controller.MembersController;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("here1");
        var server = new Server();

        var connector = new ServerConnector(server);
        connector.setPort(9999);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(new ServletHolder(new PrimaryHttpHandler()), "/");

        // Members directory for each function (add, list, update etc whatever we need)
        context.addServlet(new ServletHolder(new MembersController()), "/members/*");

        server.setHandler(context);

        server.start();
        server.join();
    }
}