package test;

import java.io.*;
import java.net.*;
import java.util.Random;
import java.util.Scanner;



public class Client  
{
	public static int [][]MatA;
	public static int RowsA;
	public static int ColA;
	public static int op;
	MultiClients ClientThread;
	public String FileSerilize;
	public static void main(String args[]) throws Exception
	{
		 new Client();
	}
	public Client()throws Exception
	{
		try {
			Socket s=new Socket("localhost",3333);
			ClientThread =new MultiClients(s,this);
			Scanner in = new Scanner(System.in);
			String name=in.nextLine();
			ClientThread.setName(name);
			ClientThread.start();
			ReadDataFromUser();
			ListenForInput();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ClientThread.CloseClient();
	}
	public void ListenForInput()throws Exception
	{
		//Scanners are used to read input of user from conceal
		@SuppressWarnings("resource")
		Scanner console=new Scanner(System.in);
		while(true)
		{
			//waiting for a line form console
			while(!console.hasNextLine())//only run upon pressing run
			{//make sure not to leave thread awake, my cpu was overloaded XD
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String input=console.nextLine();
			if(input.toLowerCase().equals("quit"))
			{
				break;
			}
			if(input.equals("two"))
			{
				if(RowsA!=ColA)
				{
					System.out.println("Cant Calculate the D of none nxn matrix");
					continue;
				}
			}
			String X=ClientThread.getName();
			Serialize(X+".txt");
			ClientThread.ClientOutServerIn(X+".txt");
			System.out.println(X+".txt");
			ClientThread.ClientOutServerIn(input);
		}
		ClientThread.CloseClient();
	}
	static void ReadDataFromUser()throws Exception
	{
		 System.out.println("Please enter the Number of rows of Matrix A");
		 @SuppressWarnings("resource")
		 Scanner in = new Scanner(System.in);
		 RowsA = in.nextInt();
		 System.out.println("Please enter the Number of cols of Matrix A");
		 ColA = in.nextInt();
		 MatA = new int[RowsA][ColA];
		 System.out.println("Enter Elements of Matrix A: Row by Row");
		 for(int i=0;i<RowsA;i++)
		 {
			 for(int j=0;j<ColA;j++)
			 {
				 System.out.println("Enter the Element"+i+","+j);
				 MatA[i][j]=in.nextInt();
			 }
		 }
		 
	}
	static void Serialize(String FileName)throws Exception
	{
		Matrix S=new Matrix(RowsA,ColA,MatA);
		FileOutputStream fout=new FileOutputStream(FileName);  
		//create an object outstream to write the stream in the file
		ObjectOutputStream out=new ObjectOutputStream(fout);  
		// write the object
		out.writeObject(S);  
		//flush to make sure it is written
		out.flush();  
		out.close();
		System.out.println("success");
	}
	void Desrialize(String X)throws Exception
	{
		ObjectInputStream in=new ObjectInputStream(new FileInputStream(X));
	      //we cast the bytes back to its original typpe
	       Matrix s=(Matrix)in.readObject();   
	      for(int i=0;i<s.RowsA;i++)
			 {
				 for(int j=0;j<s.ColA;j++)
				 {
					 System.out.print(s.MatA[i][j]+" ");
				 }
				 System.out.println();
			 }
	      in.close();
	}
}
class Matrix implements Serializable
{
	public int [][]MatA;
	public int RowsA;
	public int ColA;
	public int op;
	public Matrix(int Rows,int Col,int [][]mat)
	{
		this.RowsA=Rows;
		this.ColA=Col;
		this.MatA=new int[RowsA][ColA];
		for(int i=0;i<RowsA;i++)
		 {
			 for(int j=0;j<ColA;j++)
			 {
				 this.MatA[i][j]=mat[i][j];
			 }
		 }
	}
}
