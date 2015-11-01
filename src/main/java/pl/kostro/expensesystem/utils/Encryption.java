package pl.kostro.expensesystem.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {
  
  SecretKeySpec key;
  
  public Encryption(String keyString) {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    key = new SecretKeySpec(getkey(keyString), "AES");
  }
  
  private static byte[] getkey(String key) {
    return Arrays.copyOf(key.getBytes(), 16);
  }

  public byte[] encryption(String input) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
      cipher.init(Cipher.ENCRYPT_MODE, key);
      byte[] cipherText = new byte[cipher.getOutputSize(input.getBytes().length)];
      int ctLength = cipher.update(input.getBytes(), 0, input.getBytes().length, cipherText, 0);
      ctLength += cipher.doFinal(cipherText, ctLength);
      return cipherText;
    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException
        | ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  public String decryption(byte[] cipherText) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
      cipher.init(Cipher.DECRYPT_MODE, key);
      byte[] plainText = new byte[cipher.getOutputSize(cipherText.length)];
      int ptLength = cipher.update(cipherText, 0, cipherText.length, plainText, 0);
      ptLength += cipher.doFinal(plainText, ptLength);
      return truncateString(plainText);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidKeyException | ShortBufferException | IllegalBlockSizeException | BadPaddingException e) {
      e.printStackTrace();
    }
    return null;
  }
  
  private static String truncateString(byte[] plainText) {
    int i;
    for (i = plainText.length; i > 0; i--)
      if (plainText[i - 1] != (byte) 0)
        break;
    if (i==0) return "";
    else return new String(Arrays.copyOf(plainText, i));
  }
  
  public static void main(String[] args) {
    Encryption encryption = new Encryption("NitcheWa02stx_RC98Mike007!");
    byte[] encryptionByte = encryption.encryption("www.java2s.com");
    System.out.println(encryption.decryption(encryptionByte));
  }

}