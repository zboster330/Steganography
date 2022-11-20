package steganography;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.IOException;
import static java.nio.file.StandardCopyOption.*;
import java.nio.file.*;
import java.util.Scanner;

public class SteganographyMain
{
	public static File inputfile = new File("sample.wav");
	public static File outputfile = new File("Output.wav");
	public static String message = "sample";
	
	 public static void main(String[] args)
	 {
		 Scanner scan = new Scanner(System.in);
		 System.out.println("Enter e to encrypt or d to decrypt: ");
		 String choice = scan.next();
		 if (choice.equals("e")) 
		 {
			 System.out.println("Enter desired hidden message: ");
			 message = scan.next();
			 System.out.println("Enter the .wav file to use: ");
			 inputfile = new File(scan.next());
			 System.out.println("Computing....");
			 encrypt();
			 System.out.println("Hidden Message successfully created in file Output.wav");
		 }
		 else if (choice.equals("d"))
		 {
			 System.out.println("Enter the .wav file being decrypted: ");
			 outputfile = new File(scan.next());
			 System.out.println("Computing...");
			 System.out.println("The hidden message is: " + decrypt());
		 }
	 }
	 
	 
	 public static void encrypt()
	 {
		 try 
		 {
			 Files.copy(inputfile.toPath(), outputfile.toPath(), REPLACE_EXISTING);
			 RandomAccessFile r = new RandomAccessFile(outputfile, "rw");
			 r.skipBytes(100);
			 r.writeInt(message.length());
			 int[] location = dataLocation(r.getFilePointer(), outputfile.length(), message.length());
			 for (int i = 0; i < message.length(); i++)
			 {
				r.seek(location[i]);
				r.writeChar(message.charAt(i));
				if (i == message.length() - 1) 
				{
					r.writeLong(0);
				} else 
				{
					r.writeLong(location[i+1]);
				}
			}
		 } 
		 catch (IOException a) 
		 {
			 System.out.println("error");
		 }
	}
	 
	 public static String decrypt() 
	 {
		 try 
		 {
			 RandomAccessFile r = new RandomAccessFile(outputfile, "r");
			 r.skipBytes(100);
			 int length = r.readInt();
			 char [] message = new char[length];
			 for (int i = 0; i < length; i++) 
			 {
				 message[i] = r.readChar();
				 long location = r.readLong();
				 r.seek(location);
			 }
			 return String.valueOf(message);
		 }
		 catch (IOException a)
		 {
			 System.out.println("error");
			 return null;
		 }
	 }
	 
	 
	public static int[] dataLocation(long start, long stop, int dataSize) 
	{
		int[] temp = new int[dataSize];
		long avaliable = stop - start;
		long gap = avaliable / dataSize;
		for(int i = 0; i < dataSize; i++) 
		{
			temp[i] = (int) (start + i * gap);
		}
			
		return temp;
	}
}
