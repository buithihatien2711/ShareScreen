package ShareScreen;


import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

public class SendScreen extends Thread{
	Socket socket = null;
	Robot robot = null;
	Rectangle rectangle = null;
	
	public SendScreen(Socket socket, Robot robot, Rectangle rectangle) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.robot = robot;
		this.rectangle = rectangle;
		start();
	}
	
	private static byte[] screenshot() throws AWTException, IOException {
		GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
		Robot robot = new Robot(gDev);
		Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
		Rectangle rectangle=new Rectangle(dim);
		
		//Screenshot and save in file
		//Take screenshot save into bufferImage
		BufferedImage bufferedImage =  robot.createScreenCapture(rectangle);
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", baos);
		byte[] bytes = baos.toByteArray();
		return bytes;
	}
	
	private static byte[] compress(byte[] image) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream(image.length);
		GZIPOutputStream gzip = new GZIPOutputStream(bos);
		gzip.write(image);
		gzip.close();
		byte[] compressed = bos.toByteArray();
		bos.close();
		gzip.close();
		return compressed;
	}

	@Override
	public void run() {
		while (true) {
			byte[] compress;
			try {
//				compress = compress(screenshot());
//				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
//				dos.write(compress);
//				dos.flush();
//				Thread.sleep(10);
				
				
				compress = compress(screenshot());
				DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
				dos.writeInt(compress.length);
				dos.write(compress);
				dos.flush();
				//Thread.sleep(10);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (AWTException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
