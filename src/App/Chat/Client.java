package App.Chat.Groupe;

import java.io.*;
import java.net.Socket;
import java.nio.Buffer;
import java.util.Scanner;

public class Client {
    private Socket socket ;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private  String username ;

    public Client(Socket socket , String username) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.username = username;
        }catch(IOException e){
            closeEverything(socket , bufferedReader , bufferedWriter);
        }
    }
    public void sendMessage() throws IOException {
        try {
            bufferedWriter.write(username);
            bufferedWriter.newLine();
            bufferedWriter.flush();
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                if (messageToSend.startsWith("=>")) {
                    // If the message starts with "=>", it's a private message
                    bufferedWriter.write(messageToSend);
                } else {
                    // Otherwise, it's a regular message
                    bufferedWriter.write(messageToSend);
                }
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenToMessage(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromChat;

                while (socket.isConnected()) {
                    try {
                        msgFromChat = bufferedReader.readLine();
                        System.out.println(msgFromChat);
                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }
    public  void  closeEverything(Socket socket , BufferedReader br , BufferedWriter bw){
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
    public  static void main(String[] args) throws IOException{
        System.out.println("Enter your username for group chat:");
        Scanner scanner = new Scanner(System.in);
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost" , 1234);
        Client client = new Client(socket,username);
        client.listenToMessage();
        client.sendMessage();
    }

}



