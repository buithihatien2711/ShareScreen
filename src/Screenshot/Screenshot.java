package Screenshot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.util.Iterator;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.imageio.ImageIO;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import java.awt.image.BufferedImage;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;

public class Screenshot {
	
//    private static void decompressGzipFile(String gzipFile, String newFile) {
//    	try {
//    		byte[] compress = compressGzipFile(gzipFile);
//        	InputStream inputStream = new ByteArrayInputStream(compress);
//        	byte[] bytes = new byte[1024*1024];
//    		int count = 0;
//    		count += inputStream.read(bytes, count, bytes.length - count);
//    		
//    		//Ket thuc cua luong hinh anh la 0xFFD9 
//    		do {
//    			count += inputStream.read(bytes, count, bytes.length - count);
//    		} while (!(count > 4  && bytes[count-2]==(byte)-1 && bytes[count-1]==(byte)-39 ));
//    		
//    		//Convert byte to InputStream
//    		//is is stream of input stream of gzip file
//    		InputStream is = new ByteArrayInputStream(bytes);
//    		
//    		//Decompress image
//    		GZIPInputStream gzipIS = new GZIPInputStream(is);
//    		byte[] buffer = new byte[1024*1024];
//    		int len = gzipIS.read(buffer);
//    		
//    		//Doc tu gzip vao buffer roi doc tu buffer vao file output stream
//    		OutputStream fos = new FileOutputStream(newFile);
//    		
//    		while((len = gzipIS.read(buffer)) != -1){
//                fos.write(buffer, 0, len);
//            }
//    		
//    		
////    		//image read from input stream
////    		InputStream inputs = new ByteArrayInputStream(buffer);
////    		Image image = ImageIO.read(inputs);
//    		
//    		
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//    }
	
	
    private static void decompressGzipFile(String file, String gzipFile, String newFile) {
    	GZIPInputStream gzin = null;
    	FileOutputStream fos = null;
    	try {
//    		
//    		byte[] byteIn = compressGzipFile(file, gzipFile);
//    		byte[] bytes = new byte[1024*1024];
//    		InputStream inputStream = new ByteArrayInputStream(byteIn);
//    		int count = 0;
//    		count += inputStream.read(bytes, count, bytes.length - count);
//    		do {
//				count += inputStream.read(bytes, count, bytes.length - count);
//			} while (!(count > 4  && bytes[count-2]==(byte)-1 && bytes[count-1]==(byte)-39 ));
//    		
    		
    		
    		
    		
    	FileInputStream fis = new FileInputStream(gzipFile);
          gzin = new GZIPInputStream(fis);
          fos = new FileOutputStream(newFile);
          byte[] buffer = new byte[1024];
          int len;
          while((len = gzin.read(buffer)) != -1){
              fos.write(buffer, 0, len);
          }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
			try {
				fos.close();
				gzin.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
    }
	
//    private static void decompressGzipFile(String file, String gzipFile, String newFile) {
//    	GZIPInputStream gzin = null;
//    	FileOutputStream fos = null;
//    	try {
//    		FileInputStream fis = new FileInputStream(gzipFile);
//            gzin = new GZIPInputStream(fis);
//            fos = new FileOutputStream(newFile);
//            byte[] buffer = new byte[1024];
//            int len;
//            while((len = gzin.read(buffer)) != -1){
//                fos.write(buffer, 0, len);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally {
//			try {
//				fos.close();
//				gzin.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//		}
//    }
	
    private static byte[] compressGzipFile(String file, String gzipFile) {
    	OutputStream fos = null;
    	FileInputStream fis = null;
    	GZIPOutputStream gzipOS = null;
    	byte[] buffer = null;
    	ByteArrayOutputStream byteStream = null;
    	
    	try {
    		GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
    		GraphicsDevice gDev = gEnv.getDefaultScreenDevice();
    		Robot robot=new Robot(gDev);
    		Dimension dim=Toolkit.getDefaultToolkit().getScreenSize();
    		Rectangle rectangle=new Rectangle(dim);
    		
    		BufferedImage bufferedImage =  robot.createScreenCapture(rectangle);
    		FileOutputStream fos_sc = new FileOutputStream(file);
    		ImageIO.write(bufferedImage, "jpeg", fos_sc);
    		
    		fos = new FileOutputStream(gzipFile);
    		fis = new FileInputStream(file);
    		gzipOS = new GZIPOutputStream(fos);
    		buffer = new byte[1024*1024];
            int len;
            //Read from stream input to buffer then write from buffer into stream gzipOS
            while((len=fis.read(buffer)) != -1){
                gzipOS.write(buffer, 0, len);
//                fos.write(buffer);
            }
    		
            
            
//    		gzipOS = new GZIPOutputStream(fos);
//    		ByteArrayOutputStream boas = new ByteArrayOutputStream();
//    		ImageIO.write(bufferedImage, "jpeg", boas);
//    		byte[] bytes = boas.toByteArray();
//    		
//    		//Convert a BufferedImage to InputStream
//    		InputStream is = new ByteArrayInputStream(boas.toByteArray());
//    		
//    		
//    		//Compress image
//    		buffer = new byte[1024*1024];
//    		int len;
//    		while ((len = is.read(buffer)) != -1) {
//    			gzipOS.write(buffer, 0, len);
//    		}
    		
//    		fos.write(buffer);
		} catch (Exception e) {
			// TODO: handle exception
		}
    	finally {
    		//close resources
            try {
				gzipOS.close();
				fos.close();
	            fis.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	return buffer;
    }
    
	public static void main(String[] args) {
		try {
			String file = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\test.jpeg";
			String gzfile = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\testCP.jpeg.gz";
			String newFile = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\testDE.jpeg";
			
			compressGzipFile(file, gzfile);
			System.out.println("compress success");
			
//			//Decompress
			decompressGzipFile(file, gzfile, newFile);
			System.out.println("decompress success");
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("fail");
		}
	}
}
