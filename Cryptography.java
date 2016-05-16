/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author clarabelitz
 */

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class Cryptography {

	private static String userFile;
    private static String uploadInfo;
    private static final String UPLOAD = "U";
    private static final String DOWNLOAD = "D";
    private static final String storageConnectionString = 
					"DefaultEndpointsProtocol=http;" +
							"AccountName=aitcryptography;" +
							"AccountKey=RCplSdfiuzxV8NRVktCCSuIMBf10fYKhTDnsm4Rf7RzTlJUhz5Xp4gVFYbg8+5xBJHkstruCplAcKXg6zaoLYg==";


	public Cryptography(String[] args) {
		java.util.Scanner input = new Scanner(System.in);

                System.out.println("Upload or download? Enter 'U' or 'D'");
                uploadInfo = input.nextLine().toUpperCase();
		System.out.println("Enter file name: ");
		userFile = input.nextLine();
		System.out.println(userFile);
		input.close();
	}

	public static void main(String[] args) throws IOException {


		Cryptography test = new Cryptography(args);

//		System.out.println("hey");
                
                
//              System.out.println(System.getProperty("user.dir"));
          
        
		FileInputStream in = null;
		File inFile;
		byte[] bFile;
		FileOutputStream out = null;
		FileOutputStream keyfile = null;
		String[] inputFiles = userFile.split(" ");

		if (uploadInfo.equals(UPLOAD)) {
			
			for(String inputFile : inputFiles){

				try {
					in = new FileInputStream(inputFile);
					out = new FileOutputStream(inputFile+"_encrypted");
					inFile = new File(inputFile);
					bFile = new byte[(int) inFile.length()];
					keyfile = new FileOutputStream(inputFile+"_key");

					SecretKey key = makeKey();
					//byte[] keystring = Base64.getEncoder().encode(key.getEncoded());
							//encodeToString(key.getEncoded());

					//System.out.println(keystring);
					keyfile.write(key.getEncoded());

					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
					cipher.init(Cipher.ENCRYPT_MODE, key);

					in.read(bFile);
					in.close();

					byte[] encrypted = cipher.doFinal(bFile);
					out.write(Base64.getEncoder().encode(encrypted));



				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				}
	//                                catch (FileNotFoundException e) {
	//                            e.printStackTrace();
	//                        }
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
				uploadFile(inputFile + "_encrypted");
				//downloadFile(inputFile + "_encrypted");
			}

		} else if (uploadInfo.equals(DOWNLOAD)) {
			for(String inputFile : inputFiles){
				downloadFile(inputFile + "_encrypted");	
				decrypt(inputFile);
			}
		} else {
			System.out.println("Incorrect upload/download input. Exiting...");
			System.exit(1);
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
			// final String storageConnectionString = 
			// 		"DefaultEndpointsProtocol=http;" +
			// 				"AccountName=aitcryptography;" +
			// 				"AccountKey=RCplSdfiuzxV8NRVktCCSuIMBf10fYKhTDnsm4Rf7RzTlJUhz5Xp4gVFYbg8+5xBJHkstruCplAcKXg6zaoLYg==";

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
		            File myFile = new File(filename);
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
			// final String storageConnectionString = 
			// 		"DefaultEndpointsProtocol=http;" +
			// 				"AccountName=aitcryptography;" +
			// 				"AccountKey=RCplSdfiuzxV8NRVktCCSuIMBf10fYKhTDnsm4Rf7RzTlJUhz5Xp4gVFYbg8+5xBJHkstruCplAcKXg6zaoLYg==";

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

	private static void decrypt(String filename) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		// Input streams for the encrypted file and key
		FileInputStream in = null;
		FileInputStream keyFileIn = null;
		
		// Destination file for encrypted file and key
		File inFile;
		File inKey;
		
		// bFile to hold bytes for encrypted file
		byte[] bFile;
		byte[] bKey;
		
		// Output stream to stream the decrypted file into a file
		FileOutputStream out = null;
		
		// Initialize
		in = new FileInputStream(filename + "_encrypted");
		keyFileIn = new FileInputStream(filname + "_key");
		out = new FileOutputStream(filename + "_decrypted");
		inFile = new File(filename + "_encrpyted");
		inKey = new File(filename + "_key");
		bFile = new byte[(int) inFile.length()];
		bKey = new byte[(int) inKey.length()];
		
		keyFileIn.read(bKey);
		in.read(Base64.getDecoder().decode(bFile));		
		
		// Get the Base64 encoded key string: 
		//byte[] decodedKey = Base64.getDecoder().decode(bKey);
		// Re-create the secret key: 
		SecretKeySpec originalKey = new SecretKeySpec(bKey, 0, bKey.length, "AES/CBC/PKCS5Padding");
		// Resource: http://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, originalKey);// Where originalKey is the SecretKey obtained from the key file
		
		byte[] decrypted = cipher.doFinal(bFile);
		
		out.write(decrypted);
		
		in.close();
		keyFileIn.close();
		out.close();

	}



}
