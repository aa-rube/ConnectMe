package app.model;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GeneratePassword {
    public static String generatePassword() {
        String chars = "NMLJdQP5E1K0zyxwrIHGkeq432Fn90zyxwrIHkeq43876mlfcbjihgODoTSRCavutsZYXWVUpBA";
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, 11)
                .map(i -> random.nextInt(chars.length()))
                .mapToObj(randomIndex -> String.valueOf(chars.charAt(randomIndex)))
                .collect(Collectors.joining());
    }
}
