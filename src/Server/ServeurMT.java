package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServeurMT  extends Thread{
    private int nbClient ;
    public static void main(String[] args){
        new ServeurMT().start();
    }
    @Override
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(1234);
            System.out.println("Demarrage de serveur ... ");
            while ( true){
                Socket socket = ss.accept();
                ++nbClient;
                new Conversation(socket,nbClient).start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
class Conversation extends Thread{
    private Socket socket;
    private int nbClient;
    public Conversation(Socket s , int num) {
        this.socket = s;
        this.nbClient = num;

    }
    @Override
    public void run(){
        try {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            OutputStream os =socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os,true);
            String IP = socket.getRemoteSocketAddress().toString();
            System.out.println("Connexion du client numero "+nbClient + "IP =" +IP);
            pw.println("Bienvenu vous etes le client numero  " + nbClient);

            while (true){
                String req = br.readLine();
                System.out.println("Le client "+ IP +"a envoyer une requete " +req);
                pw.println(req.length());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
