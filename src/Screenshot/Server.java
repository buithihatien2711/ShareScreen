package Screenshot;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

public class Server {
	public Socket socket;
	
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
	
	private static byte[] decompress(byte[] compressed) throws IOException {
		
		ByteArrayInputStream bytein = new ByteArrayInputStream(compressed);
		GZIPInputStream gzin = new GZIPInputStream(bytein);
		ByteArrayOutputStream byteout = new ByteArrayOutputStream();
		
		//Compress and write to ByteArrayOutputStream byteout, then convert to byte array
		int res = 0;
		byte buf[] = new byte[1024];
		while (res >= 0) {
		    res = gzin.read(buf, 0, buf.length);
		    if (res > 0) {
		        byteout.write(buf, 0, res);
		    }
		}
		byte uncompressed[] = byteout.toByteArray();
		return uncompressed;
	}
	
    private static byte[] compressGzipFile(String file, String gzipFile, Socket socket) {
    	OutputStream fos = null;
    	FileInputStream fis = null;
    	GZIPOutputStream gzipOS = null;
    	byte[] buffer = null;
    	GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
		Robot robot = null;
		try {
			robot = new Robot(gDev);
			Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
			Rectangle rectangle=new Rectangle(dim);
			
			//Screenshot and save in file
			//Take screenshot save into bufferImage
			BufferedImage bufferedImage =  robot.createScreenCapture(rectangle);
			FileOutputStream fos_sc;
			fos_sc = new FileOutputStream(file);
			//Write from bufferedImage to FileOutputStream (save in file)
			ImageIO.write(bufferedImage, "jpeg", fos_sc); 
			
			
			
			//Compress image
			fos = new FileOutputStream(gzipFile);
			fis = new FileInputStream(file);
			gzipOS = new GZIPOutputStream(fos);
			buffer = new byte[1024*1024];
	        int len;
	        
	        //Read from stream input to buffer then write from buffer into stream gzipOS
	        while((len=fis.read(buffer)) != -1){
	            gzipOS.write(buffer, 0, len);
//	                    fos.write(buffer);
	        }
	        
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
				gzipOS.close();
				fis.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
		}
		
    	return buffer;
    }
	
	public static void main(String[] args){
		ServerSocket server;
		String file = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\test.jpeg";
		String gzfile = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\testCP.jpeg.gz";
		String newFile = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\testDE.jpeg";
		
		
		
//		try {
//			server = new ServerSocket(7788);
//			System.out.println("Server is started");
//			while (true) {
//				Socket socket = server.accept();
//				compressGzipFile(file, gzfile, socket);
//				socket.close();
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		try {
			FileOutputStream fos = new FileOutputStream(file);
			byte[] screenshot = screenshot();
			fos.write(screenshot);
			System.out.println("Kich thuoc anh chup man hinh: " + screenshot.length);
			System.out.println("2 byte cuoi: " + Integer.toHexString(screenshot[screenshot.length - 2]) + Integer.toHexString(screenshot[screenshot.length - 1]));
			
			FileOutputStream fos_compress = new FileOutputStream(gzfile);
			byte[] compress = compress(screenshot);
			fos_compress.write(compress);
			System.out.println("Kich thuoc anh sau khi nen: " + compress.length);
			System.out.println("2 byte cuoi: " + Integer.toHexString(compress[compress.length - 2]) + Integer.toHexString(compress[compress.length - 1]));

			FileOutputStream fos_new = new FileOutputStream(newFile);
			byte[] decompress = decompress(compress);
			fos_new.write(decompress);
			System.out.println("Kich thuoc anh sau khi giai nen: " + decompress.length);
			System.out.println("2 byte cuoi: " + decompress[decompress.length - 2] + decompress[decompress.length - 1]);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AWTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
