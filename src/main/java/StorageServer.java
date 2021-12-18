import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.server.Route;
import org.apache.zookeeper.*;

import static akka.http.javadsl.server.Directives.route;

public class StorageServer implements Watcher {

    private static final String PATH_SERVERS = "localhost:";

    private final Http http;
    private final ActorRef actorConfig;
    private final ZooKeeper zoo;
    private final String path;

    public StorageServer (Http http, ActorRef actorConfig, ZooKeeper zoo, String port) throws Exception {
        this.http = http;
        this.actorConfig = actorConfig;
        this.zoo = zoo;
        path = PATH_SERVERS + port;
        zoo.create("/servers/" + path,
                path.getBytes(),
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL);
    }

    public Route createRoute() {
        return route(
                
        )
    }
}
