package cs157a.webdev;

import org.eclipse.jetty.io.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.*;

public class HttpLandingPage extends Handler.Abstract {
    @Override
    public boolean handle(Request request, Response response, Callback callback) {
        response.setStatus(200);

        Content.Sink.write(response, true, """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Jetty Hello World Servlet</title>
            </head>
            <body>
                <p>Hello World1 (Servlet)</p>
                <a href="/members">MEMBERS</a>
                <a href="/books">BOOKS</a>
            </body>
            </html>
            """, callback);
        return true;
    }
}
