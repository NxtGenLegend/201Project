package websocketCode;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class chatRoom {
    private Vector<serverThread> serverThreads; 

    public chatRoom(int port) {
        try {
            System.out.println("Binding to port " + port);
            ServerSocket ss = new ServerSocket(port);
            System.out.println("Bound to port " + port);
            serverThreads = new Vector<serverThread>();
            while(true) {
                Socket s = ss.accept(); // blocking
                System.out.println("Connection from: " + s.getInetAddress());
                serverThread st = new serverThread (s, this);
                serverThreads.add(st);
            }
         } 
         
         catch (IOException ioe) {
            System.out.println("ioe in ChatRoom constructor: " + ioe.getMessage());
         }
    }
        
         public void broadcast(String message, serverThread st) {
            if (message != null) {
                System.out.println(message);
                    for(serverThread threads : serverThreads) {
                        if (st != threads) {
                            threads.sendMessage(message);
                        }
                    }
            }
        }
        public static void main(String [] args) {
             chatRoom cr = new chatRoom(6789);
            }






}


