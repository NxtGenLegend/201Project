package websocketCode;
import java.io.BufferedReader;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.io.PrintWriter;
 import java.net.Socket;
 

 public class serverThread extends Thread {

    private PrintWriter pw;
    private BufferedReader br;
    private chatRoom cr;
    public serverThread(Socket s, chatRoom cr) {
    try {
        this.cr = cr;
        pw = new PrintWriter(s.getOutputStream());
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        this.start();
    
        }    
        catch (IOException ioe) {
            System.out.println("ioe in ServerThread constructor: " + ioe.getMessage());
        }
    }
    public void sendMessage(String message) {
        pw.println(message);
        pw.flush();
     }



     public void run() {
        try {
            while(true) {
                String line = br.readLine();
                cr.broadcast(line, this);
            }
         } 
         catch (IOException ioe) {
            System.out.println("ioe in ServerThread.run(): " + ioe.getMessage());
         }
     }



 }

 

