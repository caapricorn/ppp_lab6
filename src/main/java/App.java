import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;
import org.apache.log4j.BasicConfigurator;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class App {

    private static final int INDEX_OF_ZOOKEEPER_ADDRESS = 0;
    private static final int ZOOKEEPER_TIMEOUT = 3000;
    final private static String LOCAL_HOST = "localhost";
    private static final int NO_SERVERS_RUNNING = 0;
    private static final String ERROR = "NO SERVERS ARE RUNNING";
    private static final String NEW_LINE = "\n";

    public static void main(String[] args) throws Exception {
        BasicConfigurator.configure();
        ActorSystem system = ActorSystem.create("routes");
        ActorRef storage = system.actorOf(Props.create(StorageActor.class));
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Http http = Http.get(system);
        ZooKeeper zk = null;

        try {
            zk = new ZooKeeper(args[INDEX_OF_ZOOKEEPER_ADDRESS], ZOOKEEPER_TIMEOUT, null);
            new ZooWatcher(zk, storage);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        List<CompletionStage<ServerBinding>> bindings = new ArrayList<>();

        StringBuilder serversInfo = new StringBuilder("Servers online at" + NEW_LINE);

        try {
            StorageServer server = new StorageServer(http, storage, zk, args[i]);
            final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = server.createRoute().flow(system, materializer);
            bindings.add(http.bindAndHandle(
                    routeFlow,
                    ConnectHttp.toHost(LOCAL_HOST, Integer.parseInt(args[i])),
                    materializer
            ));
            serversInfo.append("http://localhost:").append(args[i]).append(NEW_LINE);
        } catch (InterruptedException | KeeperException e) {
            e.printStackTrace();
        }

        if(bindings.size() == NO_SERVERS_RUNNING) {
            System.err.println(ERROR);
        }

        print(serversInfo + NEW_LINE + "Press RETURN to stop...");

        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        for (CompletionStage<ServerBinding> binding : bindings) {
            binding
                    .thenCompose(ServerBinding::unbind)
                    .thenAccept(unbound -> system.terminate());
        }
    }

    public static void print(String s) {
        System.out.println(s);
    }
}
