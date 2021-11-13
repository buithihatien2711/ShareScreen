package TestDecompress;

//Java Program to Convert InputStream to Byte Array
//Using read(byte[]) or readAllBytes()

//Importing required classes
import java.io.*;
import java.nio.charset.StandardCharsets;

//Main class
class TestReadInputStream {

	// Main driver method
	public static void main(String[] args)
	{
		// Creating a Input Stream here
		// Usually it comes from files, programs
		InputStream inputStream = new ByteArrayInputStream(
			"GeeksForGeeks".getBytes(
				StandardCharsets.UTF_8));

		// Taking the InputStream data into a byte array
		byte[] byteArray = null;

		// Try block to check for exceptions
		try {
			byteArray = inputStream.readAllBytes();
		}

		// Catch block to handle the exceptions
		catch (IOException e) {

			// Print and dispal ythe exceptions
			System.out.println(e);
		}
		System.out.println(byteArray.length);
		// Iterating over using for each loop
		for (byte b : byteArray)

			// Printing the content of byte array
			System.out.print((char)b + " ");
	}
}
