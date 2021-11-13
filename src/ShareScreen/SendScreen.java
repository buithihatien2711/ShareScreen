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
	
//	private static byte[] screenshot() throws AWTException, IOException {
//		GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
//		GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
//		Robot robot = new Robot(gDev);
//		Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
//		Rectangle rectangle=new Rectangle(dim);
//		
//		//Screenshot and save in file
//		//Take screenshot save into bufferImage
//		BufferedImage bufferedImage =  robot.createScreenCapture(rectangle);
//		
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//		ImageIO.write(bufferedImage, "jpg", baos);
//		byte[] bytes = baos.toByteArray();
//		return bytes;
//	}
//	
//	private static byte[] compress(byte[] image) throws IOException {
//		ByteArrayOutputStream bos = new ByteArrayOutputStream(image.length);
//		GZIPOutputStream gzip = new GZIPOutputStream(bos);
//		gzip.write(image);
//		gzip.close();
//		byte[] compressed = bos.toByteArray();
//		bos.close();
//		gzip.close();
//		return compressed;
//	}

	@Override
	public void run() {
		DataOutputStream dos = null;
		byte[] compress = null;
		GraphicsEnvironment gEnv;
		GraphicsDevice gDev;
		Robot robot;
		Dimension dim;
		Rectangle rectangle;
		BufferedImage bufferedImage;
		ByteArrayOutputStream baos;
		byte[] image;
		ByteArrayOutputStream bos;
		GZIPOutputStream gzip;
		try {
			
			//System.out.println("Do dai compress: " + compress.length);
			while (true) {
				//Take screenshot save into image byte array
				gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
				gDev = gEnv.getDefaultScreenDevice();
				robot = new Robot(gDev);
				dim=Toolkit.getDefaultToolkit().getScreenSize();
				rectangle=new Rectangle(dim);
				
				bufferedImage =  robot.createScreenCapture(rectangle);
				
				baos = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, "jpg", baos);
				
				image = baos.toByteArray();
				
				//Compress image
				bos = new ByteArrayOutputStream(image.length);
				gzip = new GZIPOutputStream(bos);
				gzip.write(image);
				gzip.close();
				compress = bos.toByteArray();
				bos.close();
				
				//Send compressed image to client
				dos = new DataOutputStream(socket.getOutputStream());
				dos.write(compress);
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Server that bai");
		}
	}
}
