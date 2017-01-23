package network;

import java.io.Serializable;
import java.util.function.Consumer;

/**
 * Created by Webby Debby on 11/22/2016.
 */
class Server extends NetworkConnection {

    private final boolean isLocal;

    Server(Consumer<Serializable> onReceiveCallback, boolean isLocal) {
        super(onReceiveCallback);
        this.isLocal = isLocal;
    }

    @Override
    protected boolean isServer() {
        return true;
    }

    @Override
    protected boolean isLocal() {
        return isLocal;
    }

    @Override
    protected String getIP() {
        return null;
    }

    @Override
    protected int getPort() {
    	//tilfeldig valgt portnummer
        return 1515;
    }
}
