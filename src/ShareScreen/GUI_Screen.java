package ShareScreen;

import java.awt.BorderLayout;
import java.beans.PropertyVetoException;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GUI_Screen extends Thread{

	String width="", height="";
	private JPanel cPanel = new JPanel();
	private JFrame frame = new JFrame();
	private Socket cSocket = null;
	private JInternalFrame interFrame = new JInternalFrame("Server Screen", true, true, true);
	private JDesktopPane desktop = new JDesktopPane();
	
	/**
	 * Create the frame.
	 */
	public void GUI() {
		frame.add(desktop, BorderLayout.CENTER);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		//Show thr frame in maximized state
	
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);		//CHECK THIS LINE
		frame.setVisible(true);
		interFrame.setLayout(new BorderLayout());
		interFrame.getContentPane().add(cPanel, BorderLayout.CENTER);
		interFrame.setSize(100,100);
		desktop.add(interFrame);

		try {
			//Initially show the internal frame maximized
			interFrame.setMaximum(true);
		}catch (PropertyVetoException ex) { 
				ex.printStackTrace();
		}

		//This allows to handle KeyListener events
		cPanel.setFocusable(true);
		interFrame.setVisible(true);
	}
	
	public GUI_Screen(Socket socket) {
		this.cSocket = socket;
		start();
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		InputStream inputStream;
		try {
			inputStream = cSocket.getInputStream();
			GUI();
			new ReceiveScreen(inputStream , cPanel);
			//new ReceiveScreen(cSocket, cPanel);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
