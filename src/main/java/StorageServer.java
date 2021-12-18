import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.server.Route;
import akka.pattern.Patterns;
import org.apache.zookeeper.*;

import static akka.http.javadsl.server.Directives.*;
import static io.netty.handler.codec.http.cookie.CookieHeaderNames.PATH;

public class StorageServer implements Watcher {

    private static final String PATH_SERVERS = "localhost:";
    private static final String URL_QUERY = "url";
    private static final String COUNT_QUERY = "count";
    private static final String ZERO_COUNT= "0";

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
                path(PATH, () ->
                        route(
                                get( () ->
                                        parameter(URL_QUERY, (url) ->
                                                parameter(COUNT_QUERY, (count) -> {
                                                    App.print("Count = " + count + " on " + path);
                                                    if (count.equals(ZERO_COUNT)) {
                                                        return completeWithFuture(http.singleRequest(HttpRequest.create(url)));
                                                    }
                                                    return completeWithFuture(
                                                            Patterns
                                                                    .ask(
                                                                            
                                                                    )
                                                    )

                                                }
                        )
        )
    }
}
