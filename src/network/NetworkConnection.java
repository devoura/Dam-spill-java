package network;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * Created by Webby Debby on 11/22/2016.
 */
public abstract class NetworkConnection {

    private final ConnectionThread connThread = new ConnectionThread();
    private final Consumer<Serializable> onReceiveCallback;

    NetworkConnection(Consumer<Serializable> onReceiveCallback) {
        this.onReceiveCallback = onReceiveCallback;
        connThread.setDaemon(true);
    }

    void startConnection() throws Exception {
        connThread.start();
    }

    public void send(Serializable data) throws Exception{
        connThread.out.writeObject(data);
    }


    protected abstract boolean isServer();
    protected abstract boolean isLocal();
    protected abstract String getIP();
    protected abstract int getPort();

    private class ConnectionThread extends Thread {
        private ObjectOutputStream out;

        @Override
        public void run(){
            //setter opp server og klient socket og streams
            try(ServerSocket server = isServer() ? (isLocal() ? new ServerSocket(getPort()) : new ServerSocket(getPort(), 50, InetAddress.getLocalHost())): null;
                    Socket socket = isServer() ? server.accept() : new Socket(getIP(), getPort());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream())) {

                this.out = out;

                while (true){
                    Serializable data = (Serializable)in.readObject();
                    onReceiveCallback.accept(data);
                }

            } catch (Exception e){
                //hvis noen lukker sitt vindu får den andre en liten melding i chatboxen
                onReceiveCallback.accept("Connection closed");
            }
        }
    }
}
