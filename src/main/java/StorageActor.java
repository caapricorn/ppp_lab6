import akka.actor.AbstractActor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StorageActor extends AbstractActor {

    private List<String> servers = new ArrayList<>();
    private final Random random = new Random();
}
