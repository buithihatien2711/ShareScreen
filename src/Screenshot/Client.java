package Screenshot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.zip.GZIPInputStream;

public class Client {
	
    private static void decompressGzipFile(Socket socket, String newFile) {
    	GZIPInputStream gzin = null;
    	FileOutputStream fos = null;
    	try {
    		
    		//byte[] byteIn = compressGzipFile(file, gzipFile);
    		byte[] bytes = new byte[1024*1024];
    		//InputStream inputStream = new ByteArrayInputStream(byteIn);
    		InputStream inputStream = socket.getInputStream();
    		int count = 0;
    		count += inputStream.read(bytes, count, bytes.length - count);
    		do {
				count += inputStream.read(bytes, count, bytes.length - count);
			} while (!(count > 4  && bytes[count-2]==(byte)-1 && bytes[count-1]==(byte)-39 ));
    		
	         gzin = new GZIPInputStream(inputStream);
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
				gzin.close();
				fos.close();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
    }
	
	public static void main(String[] args) throws Exception{
		Socket socket = new Socket("localhost", 7788);
		String newFile = "D:\\PBL4\\PBL4_MMT\\TestScreenshot\\testDE.jpeg";
		decompressGzipFile(socket, newFile);
		socket.close();
	}
}
