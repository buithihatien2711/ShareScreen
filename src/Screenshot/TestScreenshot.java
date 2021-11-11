package Screenshot;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;

public class TestScreenshot {
	private static byte[] screenshot() throws AWTException, IOException {
		System.out.println("screenshot");
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
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
			
			FileOutputStream fos_compress = new FileOutputStream(gzfile);
			byte[] compress = compress(screenshot);
			fos_compress.write(compress);
	
			FileOutputStream fos_new = new FileOutputStream(newFile);
			byte[] decompress = GZipDecompress(compress);
			fos_new.write(decompress);

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
//		int i = 0;
//		
//		while (true) {
//			//				String file1 = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\test" + i + ".jpeg";
////				FileOutputStream fos = new FileOutputStream(file1);
////				byte[] screenshot = screenshot();
////				fos.write(screenshot);
//			try {
//				screenshot();
//			} catch (AWTException | IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			System.out.println(ZonedDateTime.now().toInstant().toEpochMilli());
////				i++;
//			
//		}
		
	}
}
