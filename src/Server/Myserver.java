package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Myserver {
    public static void main(String[] args) throws IOException {
        //
        ServerSocket ss =new ServerSocket(1234);
        System.out.println("J attend la connexion... ");
        //accept la connexion de client
        Socket s = ss.accept();
        InputStream is = s.getInputStream();
        OutputStream os = s.getOutputStream();
        System.out.println("J attend que le client repond envoie une requete ");
        int nb = is.read();
        int res = nb*5 ;
        System.out.println("J envoie au client la reponse ");
        os.write(res);
        s.close();

    }
}