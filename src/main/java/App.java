import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import org.apache.log4j.BasicConfigurator;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;

public class App {

    public static void main(String[] args) throws IOException, KeeperException, InterruptedException {
        BasicConfigurator.configure();
        ActorSystem system = ActorSystem.create("routes");
        ActorRef storage = system.actorOf(Props.create())
    }

    public static void print(String s) {
        System.out.println(GREEN + s + RESET);
    }
}
