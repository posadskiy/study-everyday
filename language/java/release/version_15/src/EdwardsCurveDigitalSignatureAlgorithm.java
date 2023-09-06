import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EdECPoint;
import java.security.spec.EdECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.NamedParameterSpec;
import java.util.Arrays;

/**
 * JEP 339: Edwards-Curve Digital Signature Algorithm (EdDSA)
 * <a href="https://openjdk.org/jeps/339">Docs</a>
 */
public class EdwardsCurveDigitalSignatureAlgorithm {

    public static final String ALGORITHM = "Ed25519";

    private final static Logger log = System.getLogger("default");


    public static void main(String[] args) {
        log.log(Level.INFO, Arrays.toString(produceSignature()));
        log.log(Level.INFO, Arrays.toString(producePublicKey().getEncoded()));
    }

    private static byte[] produceSignature() {
        try {
            final KeyPair keyPair = KeyPairGenerator.getInstance(ALGORITHM).generateKeyPair();

            final Signature signature = Signature.getInstance(ALGORITHM);
            signature.initSign(keyPair.getPrivate());
            signature.update((byte) 0);
            return signature.sign();
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    private static PublicKey producePublicKey() {
        try {
            final KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            final EdECPublicKeySpec edECPublicKeySpec = new EdECPublicKeySpec(new NamedParameterSpec(ALGORITHM),
                new EdECPoint(true, BigInteger.TEN));
            return keyFactory.generatePublic(edECPublicKeySpec);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

}
