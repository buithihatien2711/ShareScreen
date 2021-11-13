package Test;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

public class Server{
	private ServerSocket serverSocket;
	
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
		return compressed;
	}

//	public static void main(String args[]) throws Exception{
//		ServerSocket serversk = new ServerSocket(7700);
//		Socket socket = null;
//		DataOutputStream dos = null;
//		try {
//			byte[] compress = compress(screenshot());
//			//byte[] compress = {10, 20, 20};
//			while (true) {
//				System.out.println("Do dai compress: " + compress.length);
//				socket = serversk.accept();
//				dos = new DataOutputStream(socket.getOutputStream());
//				
//				dos.writeInt(compress.length); // write length of the message
//				dos.write(compress);
//				//dos.write(compress);
//				
////				FileOutputStream fileOutputStream = new FileOutputStream("D:\\PBL4\\PBL4_MMT\\TestScreenshot\\test.jpeg");
////				fileOutputStream.write(compress);
//			}
//		} catch (Exception e) {
//			// TODO: handle exception
//			System.out.println("Server that bai");
//		}
////		finally {
////			serversk.close();
////			socket.close();
////			dos.close();
////		}
//	}
	
	
	public static void main(String args[]) throws Exception{
		ServerSocket serversk = new ServerSocket(7700);
		Socket socket = null;
		DataOutputStream dos = null;
		byte[] compress = null;
		GraphicsEnvironment gEnv;
		GraphicsDevice gDev;
		Robot robot;
		Dimension dim;
		Rectangle rectangle;
		BufferedImage bufferedImage;
		ByteArrayOutputStream baos;
		byte[] bytes;
		byte[] image;
		ByteArrayOutputStream bos;
		GZIPOutputStream gzip;
		try {
			
			//System.out.println("Do dai compress: " + compress.length);
			//while (true) {
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
				
				String fileScreenshot = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\screenshot.jpeg";
				FileOutputStream fos_sc = new FileOutputStream(fileScreenshot);
				fos_sc.write(image);
				
				//Compress image
				bos = new ByteArrayOutputStream(image.length);
				gzip = new GZIPOutputStream(bos);
				gzip.write(image);
				gzip.close();
				compress = bos.toByteArray();
				bos.close();
				
				String fileCompress = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\compress.jpeg.gz";
				FileOutputStream fos = new FileOutputStream(fileCompress);
				fos.write(compress);
				
				//Send compressed image to client
				socket = serversk.accept();
				dos = new DataOutputStream(socket.getOutputStream());
				dos.write(compress);
			//}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Server that bai");
		}

	}
}
