import akka.actor.ActorRef;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.ArrayList;
import java.util.List;

public class ZooWatcher implements Watcher {

    private ZooKeeper zoo;
    private ActorRef storage;

    private void sendServers() throws KeeperException, InterruptedException {
        List<String> servers = new ArrayList<>();
        for (String s : zoo.getChildren(SERVERS, this)) {
            servers.add(new String(zoo.getData(SERVERS + "/" + s, false, null)));
        }
        storage.tell(
                new MessageServersList(servers),
                ActorRef.noSender()
        );
    }


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
