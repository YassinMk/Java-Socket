package Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MyClient {
    public static void main(String[] args) throws IOException {
        System.out.println("je me connecte au serveur ");
        //utiliser le socket
        Socket s = new Socket("localhost" , 1234);
        // input et output utilise socket
        InputStream is = s.getInputStream();
        OutputStream os = s.getOutputStream();
        //saisire le nombre dans le clavier
        Scanner sc =  new Scanner(System.in);
        System.out.println("Donner un nombre entier  : ");
        // lire un octet
        int nb = sc.nextInt();
        System.out.println("J envoie le nombre "+nb + "au serveur");
        os.write(nb);
        System.out.println("J attend la reponse du  serveur ....  " );
        int res = is.read() ;
        System.out.println("Reponse du serveur est : " + res);
    }
}
