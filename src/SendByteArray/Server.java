package SendByteArray;

import java.net.*;
import java.util.Arrays;
import java.io.*; 

public class Server 
{ 
 //initialize socket and input stream 
 private Socket   socket = null; 
 private ServerSocket server = null; 
 private InputStream in  = null; 
 private OutputStream out = null;

 // constructor with port 
 public Server(int port) 
 { 
  // starts server and waits for a connection 
  try
  { 
   server = new ServerSocket(port); 
   System.out.println("Server started"); 

   System.out.println("Waiting for a client ..."); 

   socket = server.accept(); 

   System.out.println("Client accepted"); 

   // takes input from the client socket 
   in = new DataInputStream(socket.getInputStream()); 
   //writes on client socket
   out = new DataOutputStream(socket.getOutputStream());

   // Receiving data from client
   ByteArrayOutputStream baos = new ByteArrayOutputStream();
   byte buffer[] = new byte[1024];
   baos.write(buffer, 0 , in.read(buffer));
   
   byte result[] = baos.toByteArray();

   String res = Arrays.toString(result);
   System.out.println("Recieved from client : "+res); 
   
   //echoing back to client
   out.write(result);
   
   System.out.println("Closing connection"); 

   // close connection 
   socket.close(); 
   in.close(); 
  } 
  catch(IOException i) 
  { 
   System.out.println(i); 
  } 
 } 

 public static void main(String args[]) 
 { 
  new Server(5000); 
 } 
}