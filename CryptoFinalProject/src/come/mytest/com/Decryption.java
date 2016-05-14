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
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Decryption {
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
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
		in = new FileInputStream("input.txt_encrypted");
		keyFileIn = new FileInputStream("input.txt_key");
		out = new FileOutputStream("input.txt_decrypted");
		inFile = new File("input.txt_encrpyted");
		inKey = new File("input.txt_key");
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