package cs157a.webdev;

import org.eclipse.jetty.server.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("here1");
        var server = new Server();

        Db.init();

        var connector = new ServerConnector(server);
        connector.setPort(9999);
        server.addConnector(connector);
        server.setHandler(new MainHandler());

        server.start();
        server.join();
    }
}