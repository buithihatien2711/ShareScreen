package Test;
import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import java.util.zip.InflaterInputStream;

public class Client {
	
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
		gzin.close();
		return uncompressed;
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
	
    
//	public static void main(String[] args) {
//		Socket clientSocket = null;
//		DataInputStream dis = null;
//		FileOutputStream fos = null;
//		FileOutputStream fos_new = null;
//		int i = 0;
//		try {
//			while (true) {
//				clientSocket = new Socket("localhost", 7700);
//				dis = new DataInputStream(clientSocket.getInputStream());
//				byte[] result = new byte[1024*1024];
//				//dis.read(result);
//				
//				int length = dis.readInt();                    // read length of incoming message
//				if(length>0) {
//				    byte[] message = new byte[length];
//				    dis.readFully(message, 0, message.length); // read the message
//					if(isGzipped(result)){
//						//byte[] decompress = decompress(result);
//						byte[] decompress = GZipDecompress(result);
//						
////						fos = new FileOutputStream("D:\\PBL4\\PBL4_MMT\\TestScreenshot\\client.jpeg.gz");
////						fos.write(result);
////						String file = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\client" + i + ".jpeg";
////						fos_new = new FileOutputStream(file);
////						fos_new.write(decompress);
//						
//					//	BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
//					      
//					      
//					     
//					      ++i;
//					}
//				}
//				
////				if(isGzipped(result)){
////					//byte[] decompress = decompress(result);
////					byte[] decompress = GZipDecompress(result);
////					
//////					fos = new FileOutputStream("D:\\PBL4\\PBL4_MMT\\TestScreenshot\\client.jpeg.gz");
//////					fos.write(result);
////					String file = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\client" + i + ".jpeg";
////					fos_new = new FileOutputStream(file);
////					fos_new.write(decompress);
////					
////				//	BufferedImage img = ImageIO.read(new ByteArrayInputStream(bytes));
////				      
////				      
////				     
////				      ++i;
////				}
//			}
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		finally {
//			try {
//				if(clientSocket != null) {
//					clientSocket.close();
//				}
//				if(dis != null) {
//					dis.close();
//				}
//				if(fos != null) {
//					fos.close();
//				}
//				if(fos_new != null) {
//					fos_new.close();
//				}
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
//	}
    
    
    
    public static byte[] decompress(InputStream inputStream) throws IOException {
        GZIPInputStream gzin = new GZIPInputStream(inputStream);
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
    
    
	public static void main(String[] args) {
		Socket clientSocket = null;
		InputStream dis = null;
		GZIPInputStream gzin = null;
		ByteArrayOutputStream byteout = null;
		int i = 0;
		int res;
		byte buf[] = null;
		byte uncompressed[] = null;
		//while (true) {
			try {
				clientSocket = new Socket("localhost", 7700);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				dis = clientSocket.getInputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
		        gzin = new GZIPInputStream(dis);
		        byteout = new ByteArrayOutputStream();
				//Decompress stream dis
				res = 0;
				buf = new byte[1024];
				while (res >= 0) {
				    res = gzin.read(buf, 0, buf.length);
				    if (res > 0) {
				        byteout.write(buf, 0, res);
				    }
				}
				uncompressed = byteout.toByteArray();

				String fileDecompress = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\decompress.jpeg";
				FileOutputStream fos = new FileOutputStream(fileDecompress);
				fos.write(uncompressed);
				
				System.out.println(++i + " : " + ZonedDateTime.now().toInstant().toEpochMilli());

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}
}
