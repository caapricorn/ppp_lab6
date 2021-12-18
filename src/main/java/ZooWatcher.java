import akka.actor.ActorRef;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;

public class ZooWatcher implements Watcher {

    private ZooKeeper zoo;
    private ActorRef storage;

    static class MessageServersList {
        private final List<String> servers;

        MessageServersList(List<String> servers){
            this.servers = servers;
        }

        public List<String> getServers() {
            return servers;
        }
    }
}
