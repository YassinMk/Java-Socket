package Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class GameMT extends Thread {
    private int nbClient ;
    private boolean isActive =true;
    private  int secretNumber ;
    public static void main(String[] args){

        new GameMT().start();
    }
    @Override
    public void run(){
        try {
            ServerSocket ss = new ServerSocket(1234);
            secretNumber = new Random().nextInt(1000);
            System.out.println("Demarrage de serveur ... ");
            System.out.println("nombre secret est : " + secretNumber);
            while (isActive){
                Socket socket = ss.accept();
                ++nbClient;
                new GameHandler(socket,nbClient,secretNumber).start();

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

class GameHandler extends Thread {
    private Socket socket;
    private int nbClient;
    private boolean fin =false;
    private  int secretNb;

    private String gagnant ;

    public GameHandler(Socket s, int num , int secretNumber) {
        this.socket = s;
        this.nbClient = num;
        this.secretNb = secretNumber;

    }

    @Override
    public void run() {
        try {
            InputStream is = socket.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            OutputStream os = socket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            String IP = socket.getRemoteSocketAddress().toString();
            System.out.println( "Connexion du client numero " + nbClient + "IP =" + IP);
            pw.println("Bienvenu vous etes le client numero  " + nbClient);
            pw.println("Deviner le nombre secret .... ?");

            while (true) {

                String req = br.readLine();
                int nomber = 0;
                boolean coorectFormatRequest = false ;
                try{
                    nomber = Integer.parseInt(req);
                    coorectFormatRequest = true;
                }catch (NumberFormatException e){
                    coorectFormatRequest = false;
                }
                System.out.println("Client" +IP + "Tentative avec le numero " + nomber);
                if(coorectFormatRequest){
                    if(fin == false){
                        if(nomber > secretNb){
                            pw.println("Votre nombre est superieur au nombre secret");
                        }
                        else if(nomber<secretNb) {
                            pw.println("Votre Nombre est inf au nombre secret");
                        }
                        else {
                            pw.println("BRAVO , vous avez gagner ");
                            gagnant =IP;
                            fin =true;

                        }
                    }else{
                        pw.println("Jeu terminé le gagnat est "+gagnant);
                    }
                }else{
                    pw.println("Format de nombre incorrect ");
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
