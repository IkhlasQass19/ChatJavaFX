package com.example.chatjavafx.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ServerChat extends Thread {
    private boolean isActive=true;
    private int nombreClient=0;
    private List<Conversation> clients=new ArrayList<Conversation>();
    public static void main(String[]args) {
        new ServerChat().start();
    }

    @Override
    public void run() {
        try {
            ServerSocket serverSocket=new ServerSocket(1234);
            while(isActive){
                Socket socket=serverSocket.accept();
                ++nombreClient;
                Conversation conversation=new Conversation(socket,nombreClient);
                clients.add(conversation);
                conversation.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class Conversation extends Thread{
        protected Socket socketClient;
        protected int numero;
        public Conversation(Socket socketClient,int numero){
               this.socketClient=socketClient;
               this.numero=numero;
        }

        public  void broadcastMessage(String message, Socket socket, int numClient){
            try {
                for (Conversation client : clients) {
                    if(client.socketClient !=socket){
                        if(client.numero==numClient || numClient==-1){
                            PrintWriter printWriter = new PrintWriter(client.socketClient.getOutputStream(), true);
                            printWriter.println(message);
                        }

                    }

                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        @Override
        public void run() {
            try {
                InputStream inputStream=socketClient.getInputStream();
                InputStreamReader isr=new InputStreamReader(inputStream);
                BufferedReader br=new BufferedReader(isr);

                PrintWriter pw=new PrintWriter(socketClient.getOutputStream(),true);
                String ipClient= socketClient.getRemoteSocketAddress().toString();
                pw.println("Bienvenue vous etes le client numero:"+numero);
                System.out.println("Connexion du client numero:"+numero+",IP="+ipClient);

                while (true){
                    String req=br.readLine();
                    if(req.contains("=>")){
                        String [] requestParams=req.split("=>");
                        if(requestParams.length==2);
                        String message=requestParams[1];
                        int numeroClient=Integer.parseInt(requestParams[0]);
                        broadcastMessage(message,socketClient,numeroClient);
                    }
                    else{
                        broadcastMessage(req,socketClient,-1);
                    }


                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
