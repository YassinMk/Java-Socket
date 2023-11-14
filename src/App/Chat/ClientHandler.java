package App.Chat.Groupe;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler  implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    //read message from the serveur
    private BufferedReader bufferedReader;
    //write data for the server
    private BufferedWriter bufferedWriter;
    private String usernameClient ;
    public ClientHandler(Socket socket) throws IOException {
        try {
            this.socket =socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter((socket.getOutputStream())));
            this.bufferedReader = new BufferedReader(new InputStreamReader((socket.getInputStream())));
            this.usernameClient = bufferedReader.readLine();
            clientHandlers.add(this);
            broadcastMessage("SERVER:  " +usernameClient + "has entred chat");
        }catch (IOException e){
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }
    @Override
    public void run()  {
        String messageFromClient ;
        while (socket.isConnected()){
            try{
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient != null) {
                    if (messageFromClient.startsWith("=>")) {
                        // Handle private message
                        handlePrivateMessage(messageFromClient);
                    } else {
                        // Broadcast normal message
                        broadcastMessage(usernameClient + ": " + messageFromClient);
                    }
                }
            }catch (IOException e){
                try {
                    closeEverything(socket,bufferedReader,bufferedWriter);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                break;
            }
        }
    }
    private void handlePrivateMessage(String privateMessage) throws IOException {
        String[] parts = privateMessage.split(" ", 2);
        if (parts.length == 2) {
            String targetUsername = parts[0].substring(2); // Remove the "=>" prefix
            String message =  usernameClient + ": " + parts[1];
            sendPrivateMessage(targetUsername, message);
        }
    }

    public  void broadcastMessage(String messageTosend) throws IOException {
        for (ClientHandler clientHandler : clientHandlers){
            try {
                if(!clientHandler.usernameClient.equals(usernameClient)){
                    clientHandler.bufferedWriter.write(messageTosend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch (IOException e){
                closeEverything(socket,bufferedReader,bufferedWriter);
                break;
            }
        }
    }
    private void sendPrivateMessage(String targetUsername, String message) throws IOException {
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.usernameClient.equals(targetUsername)) {
                clientHandler.bufferedWriter.write(message);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
                return; // Stop after sending the message to the first matching user
            }
        }
        // Notify the sender if the target user was not found
        bufferedWriter.write("SERVER: User " + targetUsername + " not found.");
        bufferedWriter.newLine();
        bufferedWriter.flush();
    }
    public void removeClientHandler() throws IOException {
        clientHandlers.remove(this);
        broadcastMessage("SERVER " +usernameClient +  "has left chat ");
    }
    public  void  closeEverything(Socket socket , BufferedReader br , BufferedWriter bw) throws IOException {
        removeClientHandler();
        try{
            if(br!= null){
                br.close();
            }
            if (bw != null){
                bw.close();
            }
            if(socket !=null){
                socket.close();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}

