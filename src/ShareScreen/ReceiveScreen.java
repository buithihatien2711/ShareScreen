package ShareScreen;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.zip.GZIPInputStream;
import java.awt.Image;
import java.awt.Graphics;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ReceiveScreen extends Thread{
	private Socket cSocket = null;
	private DataInputStream dis = null;
	private JPanel cPanel = null;
	private boolean continueLoop = true;
	InputStream oin = null;
	Image image1 = null;

	public ReceiveScreen(DataInputStream dis,JPanel p){
		this.dis = dis;
		cPanel = p;
		start();
	}
	
	public ReceiveScreen(Socket socket,JPanel p){
		this.cSocket = socket;
		cPanel = p;
		start();
	}
	
	 public static boolean isGZipped(InputStream in) {
		 if (!in.markSupported()) {
			 in = new BufferedInputStream(in);
		 }
	 	in.mark(2);
	 	int magic = 0;
	 	try {
	 		magic = in.read() & 0xff | ((in.read() << 8) & 0xff00);
	 		in.reset();
	 	} catch (IOException e) {
	 		e.printStackTrace(System.err);
	 		return false;
		}
	 	return magic == GZIPInputStream.GZIP_MAGIC;
	 }
	 
	 public static boolean isGzipped(byte[] data) {
		 if (data == null || data.length < 2) {
			 return false;
	     }
	     int byte1 = data[0];
	     int byte2 = data[1] & 0xff; // Remove sign, since bytes are signed in Java.
	     return (byte1 | (byte2 << 8)) == GZIPInputStream.GZIP_MAGIC;
	 }
	
    public static byte[] GZipDecompress(byte[] compressedData) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                compressedData);

        try {
            GZIPInputStream gzipInputStream = new GZIPInputStream(
                    byteArrayInputStream);

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length;
            while ((length = gzipInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }
            gzipInputStream.close();
            byteArrayInputStream.close();
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
	@Override
	public void run() {
		while (continueLoop) {
			try {
//				byte[] result = new byte[1024*1024];
				DataInputStream in = new DataInputStream(cSocket.getInputStream());
//				in.read(result);
//				if(result != null) {
//					if(isGzipped(result)){
//						byte[] decompress = GZipDecompress(result);
//						image1 = ImageIO.read(new ByteArrayInputStream(decompress));
//						image1 = image1.getScaledInstance(cPanel.getWidth(),cPanel.getHeight(),Image.SCALE_FAST);
//						//Draw the received screenshots
//						Graphics graphics = cPanel.getGraphics();
//						graphics.drawImage(image1, 0, 0, cPanel.getWidth(), cPanel.getHeight(), cPanel);
//					}
//				}
				
				
				int length = in.readInt();                    // read length of incoming message
				if(length>0) {
				    byte[] result = new byte[length];
				    in.readFully(result, 0, result.length); // read the message
					if(isGzipped(result)){
						byte[] decompress = GZipDecompress(result);
						image1 = ImageIO.read(new ByteArrayInputStream(decompress));
						image1 = image1.getScaledInstance(cPanel.getWidth(),cPanel.getHeight(),Image.SCALE_FAST);
						//Draw the received screenshots
						Graphics graphics = cPanel.getGraphics();
						graphics.drawImage(image1, 0, 0, cPanel.getWidth(), cPanel.getHeight(), cPanel);
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
