
/**
 *
 * @author Claire Illich, Annaliese Johnson, Maddy Lee, Clara Belitz
 * May 2016
 */

import com.microsoft.azure.storage.*;
import com.microsoft.azure.storage.blob.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class Cryptography {

	private static String userFile;
    private static String uploadInfo;
    private static final String UPLOAD = "U";
    private static final String DOWNLOAD = "D";
		//Assigns String storageConnectionString to have the container and the key of Clara's azure account
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
		input.close();
	}

	public static void main(String[] args) throws IOException {


		Cryptography test = new Cryptography(args);

		FileInputStream in = null;
		File inFile;
		File outfile;
		byte[] bFile;
		FileOutputStream out = null;
		FileOutputStream keyfile = null;
		FileOutputStream ivfile = null;
		String[] inputFiles = userFile.split(" ");

		if (uploadInfo.equals(UPLOAD)) {

			for(String inputFile : inputFiles){

				try {
					inFile = new File(inputFile);
					outfile = new File(inputFile+"_encrypted");
					in = new FileInputStream(inFile);
					out = new FileOutputStream(outfile);
					bFile = new byte[(int) inFile.length()];
					keyfile = new FileOutputStream(inputFile+"_key");
					ivfile = new FileOutputStream(inputFile+"_iv");

					SecretKey key = makeKey();

					keyfile.write(key.getEncoded());

					SecureRandom random = new SecureRandom();
			    	byte[] bytes = new byte[16];
			    	random.nextBytes(bytes);

			    	ivfile.write(bytes);

					IvParameterSpec ivspec = new IvParameterSpec(bytes);

					Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
					cipher.init(Cipher.ENCRYPT_MODE, key, ivspec);

					in.read(bFile);
					in.close();

					byte[] encrypted = cipher.doFinal(bFile);
					out.write(encrypted);



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
				} catch (InvalidAlgorithmParameterException e) {
					e.printStackTrace();
				}
				finally {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.flush();
						out.close();
					}
					if (keyfile != null) {
						keyfile.flush();
						keyfile.close();
					}
					if (ivfile != null) {
						ivfile.flush();
						ivfile.close();
					}
				}
				uploadFile(inputFile + "_encrypted");
				System.out.println("Successfully uploaded and encrypted: " + inputFile);
			}

		} else if (uploadInfo.equals(DOWNLOAD)) {
			for(String inputFile : inputFiles){
				downloadFile(inputFile + "_encrypted");
				//System.out.println(inputFile);
				try {
					decrypt(inputFile);
				} catch (IOException e) {
					e.printStackTrace();
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
				} catch (InvalidAlgorithmParameterException e) {
					e.printStackTrace();
				}
				System.out.println("Successfully downloaded and decrypted: " + inputFile);
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

	//Download file method, takes in String filename
	private static void downloadFile(String filename){
		try
		{
		    // Retrieve storage account from connection-string.
		   CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

		   // Create the blob client.
		   CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			 // Retrieve reference to container called aitcryptography on Clara's azure account.
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

	//Method to upload a file taking in String filename
	private static void uploadFile(String filename) {

		try
		{

			// Retrieve storage account from connection-string by parsing the String.
			CloudStorageAccount storageAccount = CloudStorageAccount.parse(storageConnectionString);

			// Create the blob client.
			CloudBlobClient blobClient = storageAccount.createCloudBlobClient();

			// Retrieve reference to container called aitcryptography on Clara's azure account.
			CloudBlobContainer container = blobClient.getContainerReference("aitcryptography");
			container.createIfNotExists();

			// Define the path to a local file.
			final String filePath = System.getProperty("user.dir")+ "/" + filename;

			// Create or overwrite the "myimage.jpg" blob with contents from filename.
			//Use FileInputStream to upload filePath to blob.
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

	private static void decrypt(String filename) throws IOException,
	NoSuchAlgorithmException, NoSuchPaddingException,
	InvalidKeyException, IllegalBlockSizeException,
	BadPaddingException, InvalidAlgorithmParameterException {
		// Input streams for the encrypted file and key
		FileInputStream in = null;
		FileInputStream keyFileIn = null;
		FileInputStream ivfilein = null;

		// Destination file for encrypted file and key
		File inFile;
		File inKey;
		File inIv;
		File outfile;

		// bFile to hold bytes for encrypted file
		byte[] bFile;
		byte[] bKey;
		byte[] bIv;

		// Output stream to stream the decrypted file into a file
		FileOutputStream out = null;

		// Initialize
		inFile = new File(filename + "_encrypted");
		inKey = new File(filename + "_key");
		inIv = new File(filename + "_iv");
		outfile = new File(filename + "_decrypted");
		in = new FileInputStream(inFile);
		keyFileIn = new FileInputStream(inKey);
		ivfilein = new FileInputStream(inIv);
		out = new FileOutputStream(outfile);


		bFile = new byte[(int) inFile.length()];
		bKey = new byte[(int) inKey.length()];
		bIv = new byte[16];

		keyFileIn.read(bKey);
		in.read(bFile);
		ivfilein.read(bIv);


		IvParameterSpec ivspec = new IvParameterSpec(bIv);

		//Re-create the secret key:
		SecretKey originalKey = new SecretKeySpec(bKey, 0, bKey.length, "AES");
		// Resource: http://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa

		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, originalKey, ivspec);// Where originalKey is the SecretKey obtained from the key file

		byte[] decrypted = cipher.doFinal(bFile);

		out.write(decrypted);

		in.close();
		keyFileIn.close();
		out.flush();
		out.close();
		ivfilein.close();

	}



}
