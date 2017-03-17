package io.gitlab.druzyna_a.totp4j;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author Damian Terlecki
 */
@RunWith(Parameterized.class)
public class TOTPTest {

    private static final String HMAC_ALGORITHM = "HmacSha256";
    private static final String KEY = "test123";
    private static final int TOKEN_LENGTH = 8;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {0, true}, {1, true}, {2, true}, {5, true}, {10, true}, {15, true},
            {20, true}, {25, true}, {28, true}, {29, true}, {30, false}, {31, false},
            {-1, true}, {-2, true}, {-28, true}, {-29, true}, {-30, false}, {-31, false},
            {213123, false}, {-12311, false}, {23452, false}, {5345, false}, {13245120, false}, {-1414235, false}
        });
    }

    private int generatedToken;
    private long t0;
    private final int secFromNow;
    private final boolean shouldBeValid;

    public TOTPTest(int secFromNow, boolean shouldBeValid) {
        this.secFromNow = secFromNow;
        this.shouldBeValid = shouldBeValid;
    }

    @Before
    public void generateToken() throws Exception {
        t0 = System.currentTimeMillis() / 1000;
        generatedToken = new TOTP.Builder()
                .setInterval(INTERVAL)
                .setKey(KEY)
                .setT0(t0)
                .setTokenLength(TOKEN_LENGTH)
                .setAlgorithm(HMAC_ALGORITHM)
                .createTOTP()
                .generateToken();
    }
    private static final int INTERVAL = 30;

    @Test
    public void testGenerateToken() throws Exception {
        int validatedToken = new TOTP.Builder()
                .setInterval(INTERVAL)
                .setKey(KEY)
                .setT0(t0 + secFromNow)
                .setTokenLength(TOKEN_LENGTH)
                .setAlgorithm(HMAC_ALGORITHM)
                .createTOTP()
                .generateToken();
        if (shouldBeValid) {
            assertEquals(generatedToken, validatedToken);
        } else {
            assertNotEquals(generatedToken, validatedToken);
        }
    }

    @Test
    public void testValidateToken() throws Exception {
        TOTP totp = new TOTP.Builder()
                .setInterval(INTERVAL)
                .setKey(KEY)
                .setT0(t0 + secFromNow)
                .setTokenLength(TOKEN_LENGTH)
                .setAlgorithm(HMAC_ALGORITHM)
                .createTOTP();
        if (shouldBeValid) {
            assertTrue(totp.isTokenValid(generatedToken));
        } else {
            assertFalse(totp.isTokenValid(generatedToken));
        }
    }

}
