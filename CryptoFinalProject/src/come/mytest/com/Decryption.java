package come.mytest.com;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Decryption {
	public static void main(String[] args) throws IOException, NoSuchAlgorithmException, 
	NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, 
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
		inFile = new File("input.txt_encrypted");
		inKey = new File("input.txt_key");
		inIv = new File("input.txt_iv");
		outfile = new File("input.txt_decrypted");
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
		
		// Get the Base64 encoded key string: 
		//byte[] decodedKey = Base64.getDecoder().decode(bKey);
		// Re-create the secret key: 
		SecretKey originalKey = new SecretKeySpec(bKey, 0, bKey.length, "AES");
		// Resource: http://stackoverflow.com/questions/5355466/converting-secret-key-into-a-string-and-vice-versa
		
		Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, originalKey, ivspec);// Where originalKey is the SecretKey obtained from the key file
		
		byte[] decrypted = cipher.doFinal(bFile);
		
		System.out.println(decrypted);
		
		out.write(decrypted);
		
		in.close();
		keyFileIn.close();
		out.flush();
		out.close();
		ivfilein.close();

		
		
	}
}