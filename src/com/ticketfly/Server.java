package com.ticketfly;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	
	
	protected ServerSocket sock;
	
	public Server(int port)
	{
		try
		{
			this.sock = new ServerSocket(port);
		}
		catch (IOException e) {
			// TODO: handle exception
			System.out.println("IOException while binding to port: "+port+" stack: "+e.getStackTrace());
		}
	}
	
	public static void main(String args[])
	{
		int port = -1;
		if(args.length > 1)
		{
			System.out.println("Incorrect usage, please enter a single valid port number to operate on.");
			return;
		}
		try
		{
			port = Integer.parseInt(args[0]);
		}
		catch (NumberFormatException e) 
		{
			// TODO: handle exception
			System.out.println("Incorrect usage, the port number has to be an integer.");
			return;
		}
		if( port < 0 || port > 65535 )
		{
			System.out.println("port number out of range, enter a value in the range (0,65535), you entered "+port);
			return;
		}
		
		Server serve = new Server(port);
		while(true)
		{
			try {
				Socket clientSock = serve.sock.accept();
				if(!clientSock.isClosed())
				{
					new ServerThread(clientSock, 3).start();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
		}
	}

}
