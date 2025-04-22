import cs157a.webdev.PrimaryHttpHandler;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.Server;

void main() throws Exception {
    var server = new Server();

    var connector = new ServerConnector(server);
    connector.setPort(9999);
    server.addConnector(connector);

    server.setHandler(new PrimaryHttpHandler());

    server.start();
}
