import akka.actor.ActorRef;
import akka.http.javadsl.Http;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class StorageServer implements Watcher {

    public StorageServer (Http http, ActorRef actorConfig, ZooKeeper zoo, String port) throws InterruptedException, KeeperException {
        this.http = http;
        this.actorConfig = actorConfig;
        this.zoo = zoo;
    }
}
