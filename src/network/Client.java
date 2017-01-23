package network;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Created by Webby Debby on 11/22/2016.
 */
class Client extends NetworkConnection {

    private final String ip;

    Client(String ip, Consumer<Serializable> onReceiveCallback) {
        super(onReceiveCallback);
        this.ip = ip;
    }

    @Override
    protected boolean isServer() {
        return false;
    }

    @Override
    protected boolean isLocal() {
        return false;
    }

    @Override
    protected String getIP() {
        return ip;
    }

    @Override
    protected int getPort() {
    	return 1515;
    }
}
