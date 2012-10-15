package com.ticketfly;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import com.ticketfly.TFlyService.TFlyServiceException;

public class ServerThread extends Thread
{
	protected Socket clientSock;
	protected TFlyService handler;
	protected BufferedReader in;
	protected BufferedWriter out;
	protected int retryCount;
	protected boolean run;
	protected static int sequenceNumber = 0;
	
	public ServerThread(Socket clientSock,int retryCount)
	{
		this.clientSock = clientSock;
		this.handler = new TFlyService();
		this.retryCount = retryCount;
		this.run = true;
	}
	
	private void cleanup()
	{
		try
		{   this.in.close(); this.out.close(); this.clientSock.close();
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static int nextSequenceNumber()
	{
		synchronized(ServerThread.class)
		{
			return ++sequenceNumber;
		}
	}
	
	private static int alignSequenceNumber(int newNumber)
	{
		synchronized(ServerThread.class)
		{
			sequenceNumber = newNumber;
			return ++sequenceNumber;
		}
	}
	
	public void run()
	{
		while(this.run)
		{
			String str = new String();
			try
			{
				this.in = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
				this.out = new BufferedWriter(new OutputStreamWriter(clientSock.getOutputStream()));
				str = this.in.readLine();
				if(str.equalsIgnoreCase("."))
				{
					this.cleanup();
					break;
				}
				String[] tokens = str.split(" ");
				int num = 0; boolean overrideSequence = false;
				if(tokens.length > 1)
				{
					String lastToken = tokens[tokens.length-1];				
					try
					{
						num = Integer.parseInt(lastToken);
						str = str.substring(0, str.lastIndexOf(' '));
						overrideSequence = true;
					}
					catch(NumberFormatException e)
					{
						// Do nothing
					}
				}
				int count =0; boolean success = false;
				while(count < this.retryCount && !success)
				{
					try
					{
						str = handler.execute(str);
						success = true;
					}
					catch (TFlyServiceException e) {
						// TODO: handle exception
						count++;
					}
				}
				if(!success)
				{
					throw new TFlyServiceException("TflyService failed after "+retryCount+" retries");
				}
				if(overrideSequence)
				{
					out.write(str + " " + alignSequenceNumber(num) + "\n");
				}
				else
				{
					out.write(str + " " + nextSequenceNumber() + "\n");
				}
				out.flush();
			}
			catch (Exception e)
			{	
				// TODO Auto-generated catch block
				//e.printStackTrace();
				this.cleanup();
			}
		}
	}
}
