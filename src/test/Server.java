package test;


import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Random;

public class Server
{
	static ObjectInputStream in;
	public Matrix mat; 
	ServerSocket ss;
	boolean quite=false;
	ArrayList<MultiServer> OurDomainsConnections=new ArrayList<MultiServer>();
	public static void main(String args[])throws Exception{  
		new Server();
	}
	public Server() {
		try {
			//TODO use method to take this as an input from user)
			ss=new ServerSocket(3333);//here we are using connection 3333 (change as you want
			while(!quite)
			{
				Socket s=ss.accept();//when a connection to the domain is found we accept it
				MultiServer OurConnection = new MultiServer(s,this);
				OurConnection.start();//Start Thread
				OurDomainsConnections.add(OurConnection);//add connection to our Domain Connection
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//make sure its bloody same with client it took my 15 min to realize that XD
	}
	public int[][] transposeMatrix(){
	    int m = mat.MatA.length;
	    int n = mat.MatA[0].length;

	    int[][] transposedMatrix = new int[n][m];
	    for(int x = 0; x < n; x++) {
	        for(int y = 0; y < m; y++) {
	            transposedMatrix[x][y] = mat.MatA[y][x];
	        }
	    }
	    mat.RowsA=n;
	    mat.ColA=m;
	    return transposedMatrix;
	}
	void Desrialize(String X)throws Exception
	{
		in=new ObjectInputStream(new FileInputStream(X));
	      //we cast the bytes back to its original typpe
	       mat=(Matrix)in.readObject();   
	      for(int i=0;i<mat.RowsA;i++)
			 {
				 for(int j=0;j<mat.ColA;j++)
				 {
					 System.out.print(mat.MatA[i][j]+" ");
				 }
				 System.out.println();
			 }
	      in.close();
	}
	static void getCofactor(int mat[][], int temp[][],int N, int p, int q, int n) 
	{ 
	    int i = 0, j = 0; 
	  
	    // Looping for each element of the matrix 
	    for (int row = 0; row < n; row++) 
	    { 
	        for (int col = 0; col < n; col++) 
	        { 
	            //  Copying into temporary matrix only those element 
	            //  which are not in given row and column 
	            if (row != p && col != q) 
	            { 
	                temp[i][j++] = mat[row][col]; 
	  
	                // Row is filled, so increase row index and 
	                // reset col index 
	                if (j == n - 1) 
	                { 
	                    j = 0; 
	                    i++; 
	                } 
	            } 
	        } 
	    } 
	} 
	int determinantOfMatrix(int mat[][],int N, int n) 
	{ 
	    int D = 0; // Initialize result 
	  
	    //  Base case : if matrix contains single element 
	    if (n == 1) 
	        return mat[0][0]; 
	  
	    int[][] temp=new int[N][N]; // To store cofactors 
	  
	    int sign = 1;  // To store sign multiplier 
	  
	     // Iterate for each element of first row 
	    for (int f = 0; f < n; f++) 
	    { 
	        // Getting Cofactor of mat[0][f] 
	        getCofactor(mat, temp,N, 0, f, n); 
	        D += sign * mat[0][f] * determinantOfMatrix(temp,N, n - 1); 
	  
	        // terms are to be added with alternate sign 
	        sign = -sign; 
	    } 
	  
	    return D; 
	} 
	void Serialize(String FileName)throws Exception
	{
		FileOutputStream fout=new FileOutputStream(FileName);  
		//create an object outstream to write the stream in the file
		ObjectOutputStream out=new ObjectOutputStream(fout);  
		// write the object
		out.writeObject(mat);  
		//flush to make sure it is written
		out.flush();  
		out.close();
		System.out.println("success");
	}
}


