package come.mytest.com;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;

public class EncryptTest {

	public static void main(String[] args) throws IOException {
		System.out.println("hey");
		System.out.println(System.getProperty("user.dir"));

		FileInputStream in = null;
		FileOutputStream out = null;
		FileOutputStream keyfile = null;
		String inputFile = "input.txt";

		try {
			in = new FileInputStream(inputFile);
			out = new FileOutputStream(inputFile + "_encrypted");
			keyfile = new FileOutputStream(inputFile+"_key");

			SecretKey key = makeKey();
			String keystring = Base64.getEncoder().encodeToString(key.getEncoded());
			
			System.out.println(keystring);
			keyfile.write(key.getEncoded());
			
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
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
	}
	
	private static SecretKey makeKey() throws NoSuchAlgorithmException {
		KeyGenerator keygen = KeyGenerator.getInstance("AES");
		
		if (keygen != null) {
			keygen.init(256);
		}
		
		return keygen.generateKey();
	}

}
