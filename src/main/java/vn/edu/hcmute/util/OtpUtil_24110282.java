package vn.edu.hcmute.util;

import java.security.SecureRandom;

public class OtpUtil_24110282 {
    private static final SecureRandom RANDOM = new SecureRandom();

    private OtpUtil_24110282() {
    }

    public static String generateOtp() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}
