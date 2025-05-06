package cs157a.webdev;

import org.eclipse.jetty.http.*;
import org.eclipse.jetty.io.*;
import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.*;

import java.io.*;
import java.nio.charset.*;

public abstract class BaseHttpHandler extends Handler.Abstract {
    // TODO is a single Handler only invoked on a single thread?
    // In any case, we don't care since demo project and like 0 (literally) concurrency

    protected static final int HTTP_OK = 0;
    protected static final int HTTP_REDIRECT = 1;
    protected int responseType;

    @Override
    public boolean handle(Request req, Response resp, Callback callback) throws Exception {
        responseType = HTTP_OK;
        var body = switch (req.getMethod()) {
            case "POST" -> handlePost(req);
            case "GET" -> handleGet(req);
            case "DELETE" -> handleDelete(req);
            default -> null;
        };

        if (body == null) {
            resp.setStatus(400);
            Content.Sink.write(resp, true, "Not supported", callback);
            return true;
        }

        switch (responseType) {
            case HTTP_OK -> {
                resp.setStatus(200);
                resp.getHeaders().put(HttpHeader.CONTENT_TYPE, "text/html; charset=UTF-8");
                Content.Sink.write(resp, true, body, callback);
            }
            case HTTP_REDIRECT -> {
                Response.sendRedirect(req, resp, callback, body);
            }
        }
        return true;
    }

    protected String handleGet(Request req) throws Exception {
        return null;
    }

    protected String handlePost(Request req) throws Exception {
        return null;
    }

    protected String handleDelete(Request req) throws Exception {
        return null;
    }

    // NOTE: if the same params exists in both path part and body, it will appear twice
    // for example, in our code, /book?bookId=12 has is a form for updating the book details; the form has an input for
    // bookId, so this value is returned both in path part and body.
    public static MultiMap<String> getHtmlFormParams(Request req) throws IOException {
        var res = UrlEncoded.decodeQuery(req.getHttpURI().getQuery());
        UrlEncoded.decodeTo(Content.Source.asString(req), res, StandardCharsets.UTF_8);
        return res;
    }
}
