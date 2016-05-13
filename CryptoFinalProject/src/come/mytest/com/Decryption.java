package come.mytest.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class Decryption {
	public static void main(String[] args) throws IOException {
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
		in = new FileInputStream("input_encrypted.txt");
		keyFileIn = new FileInputStream("input.txt_key");
		out = new FileOutputStream("output.txt");
		inFile = new File("input_encrypted.txt");
		inKey = new File("input.txt_key");
		bFile = new byte[(int) inFile.length()];
		bKey = new byte[(int) inKey.length()];
		
		
		// Get the Base64 encoded key string: byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
		// Re-create the secret key: SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
		// Resource: http://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, originalKey);// Where originalKey is the SecretKey obtained from the key file
		
		

		
		
	}
}