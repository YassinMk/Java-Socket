package App.Chat.Groupe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//responsable for listening to client and respond new thread to handle that
public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServeur(){
        System.out.println("Server is running .... ");
        try{
            while (!serverSocket.isClosed()){

                Socket socket = serverSocket.accept(); //blocket methode wait to client send
                String IP = socket.getInetAddress().toString();
                System.out.println("A new client has connected  :  " + IP);
                //create communication with client
                ClientHandler clientHandler = new ClientHandler(socket);
                //create for every client a thread to do
                Thread thread = new Thread(clientHandler);
                //started thread
                thread.start();
            }
        }catch (IOException e){
            closeServerSocket();
        }
    }
    //close server when we have a error
    public void closeServerSocket(){
        try {
            if(serverSocket!=null){
                serverSocket.close();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public  static  void main(String[] arg) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Server server = new Server(serverSocket);
        server.startServeur();
    }
}