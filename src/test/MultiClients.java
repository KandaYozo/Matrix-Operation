package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MultiClients extends Thread {
	
	Socket s;
	Client c;
	DataInputStream din;
	DataOutputStream dout;
	boolean quite=false;
	
	public MultiClients(Socket OurMultiSocket, Client Clients)
	{
		c=Clients;
		s=OurMultiSocket;
	}
	public void ClientOutServerIn(String Text)
	{
		//write the line from console to server
		try {
			dout.writeUTF(Text);
			dout.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public void run()
	{
		try {
			din=new DataInputStream(s.getInputStream());
			dout=new DataOutputStream(s.getOutputStream());
			while(!quite)
			{
				try {
					while(din.available()==0)
					{
						try {
							Thread.sleep(1);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					//if there is something just show it on console
					//and then go back and do the same
					String reply=din.readUTF();
					if(reply.equals("fout"+c.ClientThread.getName()+".txt")) 
					{
						try {
							c.Desrialize(reply);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					else
					{
						System.out.println(reply);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						din.close();
						dout.close();
						s.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}	
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				din.close();
				dout.close();
				s.close();
			} catch (IOException x) {
				// TODO Auto-generated catch block
				x.printStackTrace();
			}
		}
	}
	
	public void CloseClient()
	{
		try {
			din.close();
			dout.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
