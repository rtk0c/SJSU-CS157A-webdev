package cs157a.webdev;

import cs157a.webdev.Controller.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.server.handler.*;

public class Main {
    public static void main(String[] args) throws Exception {
        System.out.println("here1");
        var server = new Server();

        var connector = new ServerConnector(server);
        connector.setPort(9999);
        server.addConnector(connector);

        var context = new ContextHandlerCollection();
        context.addHandler(new ContextHandler(new HttpLandingPage(), "/"));
        context.addHandler(new ContextHandler(new HttpMembers(), "/members"));
//        context.addHandler(new ContextHandler(new HttpBooks(), "/books"));

        //context.addHandler(new ContextHandler(new Borrow_ReturnsController(), "/borrow_returns"));

        server.setHandler(context);

        server.start();
        server.join();
    }
}