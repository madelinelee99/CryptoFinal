package come.mytest.com;

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class EncryptTest {

	private static String userFile;

	public EncryptTest(String[] args) {
		java.util.Scanner input = new Scanner(System.in);

		System.out.println("Enter file name: ");
		userFile = input.nextLine();
		System.out.println(userFile);
		input.close();
	}

	public static void main(String[] args) throws IOException {


		EncryptTest test = new EncryptTest(args);

		System.out.println("hey");
		System.out.println(System.getProperty("user.dir"));

		FileInputStream in = null;
		File inFile;
		byte[] bFile;
		FileOutputStream out = null;
		FileOutputStream keyfile = null;
		//		String inputFile = "input.txt";
		String[] inputFiles = userFile.split(" ");
		
		for(String inputFile : inputFiles){

			try {
				in = new FileInputStream(inputFile);
				out = new FileOutputStream(inputFile+"_encrypted");
				inFile = new File(inputFile);
				bFile = new byte[(int) inFile.length()];
				keyfile = new FileOutputStream(inputFile+"_key");

				SecretKey key = makeKey();
				String keystring = Base64.getEncoder().encodeToString(key.getEncoded());

				System.out.println(keystring);
				keyfile.write(key.getEncoded());

				Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
				cipher.init(Cipher.ENCRYPT_MODE, key);

				in.read(bFile);
				in.close();

				byte[] encrypted = cipher.doFinal(bFile);
				out.write(encrypted);



			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalBlockSizeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (BadPaddingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			finally {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
				if (keyfile != null) {
					keyfile.close();
				}
			}
			//uploadFile(inputFile + "_encrypted");
			downloadFile(inputFile + "_encrypted");

		}

		


	}

	private static SecretKey makeKey() throws NoSuchAlgorithmException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");

		if (keygen != null) {
			keygen.init(256);
		}

		return keygen.generateKey();
	}
	
	private static void downloadFile(String filename){
		try
		{
			final String storageConnectionString = 
					"DefaultEndpointsProtocol=http;" +
							"AccountName=aitcryptography;" +
							"AccountKey=RCplSdfiuzxV8NRVktCCSuIMBf10fYKhTDnsm4Rf7RzTlJUhz5Xp4gVFYbg8+5xBJHkstruCplAcKXg6zaoLYg==";

		    // Retrieve storage account from connection-string.
		   CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

		   // Create the blob client.
		   CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

		   // Retrieve reference to a previously created container.
		   CloudBlobContainer container = blobClient.getContainerReference("aitcryptography");

		   // Loop through each blob item in the container.
		   for (ListBlobItem blobItem : container.listBlobs()) {
		       // If the item is a blob, not a virtual directory.
		       if (blobItem instanceof CloudBlob) {
		           // Download the item and save it to a file with the same name.
		            CloudBlob blob = (CloudBlob) blobItem;
		            File myFile = new File("/Users/madelinelee/Downloads/" + filename);
		            if(!myFile.exists()){
		            	myFile.createNewFile();
		            }
		            FileOutputStream oFile = new FileOutputStream(myFile, false);
		            blob.download(oFile);
		        }
		    }
		}
		catch (Exception e)
		{
		    // Output the stack trace.
		    e.printStackTrace();
		}
	}

	private static void uploadFile(String filename) {

		try
		{

			// Define the connection-string with your values
			final String storageConnectionString = 
					"DefaultEndpointsProtocol=http;" +
							"AccountName=aitcryptography;" +
							"AccountKey=RCplSdfiuzxV8NRVktCCSuIMBf10fYKhTDnsm4Rf7RzTlJUhz5Xp4gVFYbg8+5xBJHkstruCplAcKXg6zaoLYg==";

			// Retrieve storage account from connection-string.
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

			// Create the blob client.
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			// Retrieve reference to a previously created container.
			CloudBlobContainer container = blobClient.getContainerReference("aitcryptography");
			container.createIfNotExists();

			// Define the path to a local file.
			final String filePath = System.getProperty("user.dir")+ "/" + filename;


			// Create or overwrite the "myimage.jpg" blob with contents from a local file.
			CloudBlockBlob blob = container.getBlockBlobReference(filename);
			File source = new File(filePath);
			blob.upload(new FileInputStream(source), source.length());
		}
		catch(Exception e)
		{
			System.out.print("Exception encountered: " );
			System.out.println(e.getMessage());
			System.exit(-1);
		}

	}



}