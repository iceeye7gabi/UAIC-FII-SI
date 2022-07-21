import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AESComponent {

    private static SecretKeySpec mySecretKeySpec;
    private static byte[] myKey;

    public static void setKey(String inputKey)
    {
        MessageDigest mySha = null;
        try {
            AESComponent.myKey = inputKey.getBytes("UTF-8");
            mySha = MessageDigest.getInstance("SHA-1");
            AESComponent.myKey = mySha.digest(AESComponent.myKey);
            AESComponent.myKey = Arrays.copyOf(AESComponent.myKey, 16);
            mySecretKeySpec = new SecretKeySpec(AESComponent.myKey, "AES");
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encryptMessage(String inputString, String inputSecretString)
    {
        try
        {
            setKey(inputSecretString);
            Cipher myCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            myCipher.init(Cipher.ENCRYPT_MODE, mySecretKeySpec);
            return Base64.getEncoder().encodeToString(myCipher.doFinal(inputString.getBytes("UTF-8")));
        }
        catch (Exception e)
        {
            System.out.println("Eroare la encriptare!");
        }
        return null;
    }

    public static String decryptMessage(String inputString, String inputSecretString)
    {
        try
        {
            setKey(inputSecretString);
            Cipher myCipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            myCipher.init(Cipher.DECRYPT_MODE, mySecretKeySpec);
            return new String(myCipher.doFinal(Base64.getDecoder().decode(inputString)));
        }
        catch (Exception e)
        {
            System.out.println("Eroare la encriptare!");
        }
        return null;
    }

    public static byte[] getKey() {
        return myKey;
    }
}