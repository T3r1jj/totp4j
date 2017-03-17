package io.gitlab.druzyna_a.totp4j;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Deals with token generation and validation based on TOTP. Requires key,
 * interval, hmacAlgorithm and tokenLength to be securely shared beforehand.
 *
 * @author Damian Terlecki
 */
public class TOTP {

    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};
    private final String hmacAlgorithm;
    private final String key;
    private final long t0;
    private final int interval;
    private final int N;

    /**
     *
     * @param key in plain String
     * @param t0 generation time in global epoch sec
     * @param interval time in sec
     * @param hmacAlgorithm String representing Hmac algorithm i.e. HmacSHA256
     * @param tokenLength min 2, max 8
     */
    public TOTP(String key, long t0, int interval, String hmacAlgorithm, int tokenLength) {
        this.key = key;
        this.t0 = t0;
        this.interval = interval;
        this.hmacAlgorithm = hmacAlgorithm;
        this.N = tokenLength;
        if (tokenLength < 2 || tokenLength > 8) {
            throw new RuntimeException("Token length must be between 2 and 8");
        }
    }

    public int generateToken() throws NoSuchAlgorithmException, InvalidKeyException {
        byte[] K = key.getBytes();
        byte[] TC = BigInteger.valueOf((System.currentTimeMillis() / 1000 - t0) / interval).toByteArray();
        byte[] H = calculateHmacSha1(K, TC);
        int O = H[H.length - 1] & 0xf;
        int I = ((H[O] & 0x7f) << 24) | ((H[O + 1] & 0xff) << 16) | ((H[O + 2] & 0xff) << 8) | (H[O + 3] & 0xff);
        int token = I % DIGITS_POWER[N];
        return token;
    }
    
    public boolean isTokenValid(int token) throws NoSuchAlgorithmException, InvalidKeyException {
        return token == generateToken();
    }

    private byte[] calculateHmacSha1(byte[] key, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key, hmacAlgorithm);
        Mac mac = Mac.getInstance(hmacAlgorithm);
        mac.init(signingKey);
        return mac.doFinal(data);
    }

    public static class Builder {

        private String key;
        private long t0;
        private int interval;
        private String hmacAlgorithm;
        private int tokenLength;

        public Builder() {
        }

        /**
         *
         * @param key in plain String
         * @return
         */
        public Builder setKey(String key) {
            this.key = key;
            return this;
        }

        /**
         *
         * @param t0 generation time in global epoch sec
         * @return
         */
        public Builder setT0(long t0) {
            this.t0 = t0;
            return this;
        }

        /**
         *
         * @param interval time in sec
         * @return
         */
        public Builder setInterval(int interval) {
            this.interval = interval;
            return this;
        }

        /**
         *
         * @param hmacAlgorithm String representing Hmac algorithm i.e.
         * HmacSHA256
         * @return
         */
        public Builder setAlgorithm(String hmacAlgorithm) {
            this.hmacAlgorithm = hmacAlgorithm;
            return this;
        }

        /**
         *
         * @param tokenLength min 2, max 8
         * @return
         */
        public Builder setTokenLength(int tokenLength) {
            this.tokenLength = tokenLength;
            return this;
        }

        public TOTP createTOTP() {
            return new TOTP(key, t0, interval, hmacAlgorithm, tokenLength);
        }

    }
}
