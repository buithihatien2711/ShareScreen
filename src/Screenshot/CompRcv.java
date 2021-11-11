package Screenshot;


import java.io.BufferedReader;
import java.io.FileReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class CompRcv {
  public static void main(String[] args) throws Exception {
    ServerSocket ssock = new ServerSocket(Integer.parseInt(args[0]));
    System.out.println("Listening");
    Socket sock = ssock.accept();
    GZIPInputStream zip = new GZIPInputStream(sock.getInputStream());
    while (true) {
      int c;
      c = zip.read();
      if (c == -1)
        break;
      System.out.print((char) c);
    }
  }
}

class CompSend {
  public static void main(String[] args) throws Exception {
    Socket sock = new Socket(args[0], Integer.parseInt(args[1]));
    GZIPOutputStream zip = new GZIPOutputStream(sock.getOutputStream());
    String line;
    BufferedReader bis = new BufferedReader(new FileReader(args[2]));
    while (true) {
      try {
        line = bis.readLine();
        if (line == null)
          break;
        line = line + "\n";
        zip.write(line.getBytes(), 0, line.length());
      } catch (Exception e) {
        break;
      }
    }
    zip.finish();
    zip.close();
    sock.close();
  }
}
     
