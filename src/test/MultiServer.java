package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class MultiServer extends Thread {
	
	Socket s;
	DataInputStream din;
	DataOutputStream dout;
	Server ss;
	String FileName;
	boolean quite=false;
	
	public MultiServer(Socket OurSocket,Server OurServer)
	{
		super("MultiServerConnection");//server connection thread
		this.s=OurSocket;
		this.ss=OurServer;
	}
	
	public void ServerOutClientIn(String OutText)
	{
		try {
			long ThreadID=this.getId();
			dout.writeUTF(OutText);
			dout.flush();//this is because of a buffer error :<
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ServerOutAllClientIn(String OutText)
	{
		for(int i=0;i<ss.OurDomainsConnections.size();i++)
		{
			MultiServer Connection=ss.OurDomainsConnections.get(i);
			Connection.ServerOutClientIn(OutText);
		}
	}
	
	public void run()
	{
		try {
			din=new DataInputStream(s.getInputStream());
			dout=new DataOutputStream(s.getOutputStream());
			
			while(!quite)
			{
				while(din.available()==0)
				{
					try {
						Thread.sleep(1);//sleep if there is not data coming
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				String ComingText=din.readUTF();
				if(ComingText.equals("one"))
				{
					ss.mat.MatA=ss.transposeMatrix();
					try {
						ss.Serialize("fout"+FileName);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ServerOutAllClientIn("fout"+FileName);
				}
				else if(ComingText.equals("two"))
				{
					int D=ss.determinantOfMatrix(ss.mat.MatA, ss.mat.RowsA, ss.mat.ColA);
					String X=String.valueOf(D);
					System.out.println(X);
					ServerOutAllClientIn(X);
				}
				else
				{
					FileName=ComingText;
					try {
						ss.Desrialize(ComingText);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ServerOutAllClientIn("Succes in taking input");
				}	
			}
			din.close();
			dout.close();
			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}