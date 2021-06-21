package pl.kostro.expensesystem.utils.encryption;

import com.vaadin.server.VaadinSession;
import pl.kostro.expensesystem.model.entity.ExpenseSheetEntity;

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

  public static SecretKeySpec getSecretKey(String key) {
    Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
    return new SecretKeySpec(Arrays.copyOf(key.getBytes(), 16), "AES");
  }

  public static byte[] encryption(String input) {
    if (input == null) return null;
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
      cipher.init(Cipher.ENCRYPT_MODE, VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class).getSecretKey());
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
  
  public static String decryption(byte[] cipherText) {
    try {
      Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");
      cipher.init(Cipher.DECRYPT_MODE, VaadinSession.getCurrent().getAttribute(ExpenseSheetEntity.class).getSecretKey());
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

}