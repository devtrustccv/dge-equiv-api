package dge.dge_equiv_api.Utils;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
    public class AESUtil {

        private static final String SECRET_KEY = "1234567890123456"; // 16 caracteres (AES-128)

        public static String decrypt(String encrypted) throws Exception {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decoded = Base64.getUrlDecoder().decode(encrypted); // URL-safe Base64
            byte[] original = cipher.doFinal(decoded);
            return new String(original);
        }
    }


