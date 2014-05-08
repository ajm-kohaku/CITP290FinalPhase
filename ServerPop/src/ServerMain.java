import murph32.model.RequestHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Dukat on 5/2/2014.
 */
public class ServerMain {
    public static void main(String[] args) {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(12345);
        } catch (IOException e) {
            e.printStackTrace();
        }
        boolean t = true;

        while (t) {
            try {
                Socket s = serverSocket.accept();
                new Thread(new RequestHandler(s)).start();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
