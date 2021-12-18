import akka.actor.ActorRef;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZooWatcher implements Watcher {

    private ZooKeeper zoo;
    private ActorRef storage;

    
}
