package cs157a.webdev;

import org.eclipse.jetty.io.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.*;

import java.util.*;

public class MainHandler extends Handler.Abstract {
    Map<String, Handler> routes = new HashMap<>();

    public MainHandler() {
        // (root is the exception)
        routes.put("/", new HttpLandingPage());
        // Routes should never have a trailing slash
        routes.put("/members", new HttpMembers());
        routes.put("/member", new HttpMember());
        routes.put("/books", new HttpBookList());
        routes.put("/book", new HttpBook());
        routes.put("/borrows", new HttpBorrows());
        routes.put("/borrows/checkout", new HttpCheckout());
    }

    @Override
    public boolean handle(Request req, Response resp, Callback callback) throws Exception {
        var path = req.getHttpURI().getPath();
        if (!"/".equals(path) && path.charAt(path.length() - 1) == '/') {
            var target = path.substring(0, path.length() - 1);
            Response.sendRedirect(req, resp, callback, target);
            return true;
        }

        var route = routes.get(path);
        if (route != null) {
            return route.handle(req, resp, callback);
        }

        resp.setStatus(404);
        Content.Sink.write(resp, true, "Not found", callback);
        return true;
    }
}
