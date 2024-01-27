package app.controller;

import app.model.GeneratePassword;
import app.model.User;
import app.service.AuthService;
import app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDateTime;
import java.util.Map;


import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Base64;

@RestController
public class UserController {
    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @GetMapping("/init")
    public Map<String, String> init(String authSecure) {
        System.out.println("init");
        System.out.println(RequestContextHolder.currentRequestAttributes().getSessionId());
        System.out.println("________________________________________________________");


        if (userService.findOptByAuthSecure(authSecure).isPresent()) {
            User user = userService.findOptByAuthSecure(authSecure).get();
            user.setLastAction(LocalDateTime.now());
            user.setStatus(1);
            userService.save(user);
            return Map.of("result", RequestContextHolder.currentRequestAttributes().getSessionId());
        }
        return Map.of("result","refuse");
    }

    @PostMapping("/auth")
    public Map<String, Boolean> auth(@RequestParam String email) {
        System.out.println("auth");
        System.out.println(RequestContextHolder.currentRequestAttributes().getSessionId());
        System.out.println("________________________________________________________");


        if (userService.findByEmail(email).isPresent()) {
            User user = userService.findByEmail(email).get();
            user.setAuthSecure(user.getEncryptedEmail(), EmailSender.sendAuthCode(user.getEncryptedEmail(), GeneratePassword.generatePassword()));
            userService.save(user);
            return Map.of(email, true);
        }
        return Map.of(email, false);
    }


    @PostMapping("/new_account")
    public Map<String, Boolean> createNewAccount(@RequestParam String encryptedEmail,
                                                 @RequestParam String username) {
        System.out.println("new_acc");
        System.out.println(RequestContextHolder.currentRequestAttributes().getSessionId());
        System.out.println("________________________________________________________");


        if (userService.findOptionalByName(username).isPresent() || userService.findByEmail(encryptedEmail).isPresent()) {
            return Map.of("result", false);
        }

        userService.createUser(encryptedEmail, username);
        return Map.of("result", true);
    }

    @PostMapping("/userStatus")
    public Map<String, String> userStatus() {
        User owner = userService.findBySessionId();
        //return userService.userStatus(owner);
        return Map.of("status", owner.getSessionId());
    }



    private String generatePubPriv() {
        Security.addProvider(new BouncyCastleProvider());

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA", "BC");
            keyPairGenerator.initialize(4096);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            String publicKeyString = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
            String privateKeyString = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

            return publicKeyString + "\n" + privateKeyString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "\n";
    }

    public static void checkTheVerify() {
        Security.addProvider(new BouncyCastleProvider());

        try {
            // Заданные строки с открытым и закрытым ключами
            String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvdkzy0Xt9fQ93n+IWnD0RHLJ+vUMDjQNijLUz7/QCUj3urbVsEimsPcw1+KeAUTiqqGXQ8O/a2skblzSg0t7tp5VO+Fcvvp2L6evytCvFlL3yS/h7gyBF6axRaJ5UaLoz9TXto1UnI7tXB7sR7jx1fJD+158FSd9/XAkbadbUirwx+dl0z8gyNWESY4moisV1wQG1P6q4v6KFbqq6a8Hv229g6/qfId0At9tYmbYeb/FYLW2pEWTTZJW1b3uyFAieIECxuUf05N8g+REOY4/cWg9Za63WcQqDbLHjQfyvUGkWzR2pPWujMaDFf8rwqNKSIlFv/ceGaPPsdfCdHXyMwIDAQAB";
            String privateKeyString = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQC92TPLRe319D3ef4hacPREcsn69QwONA2KMtTPv9AJSPe6ttWwSKaw9zDX4p4BROKqoZdDw79rayRuXNKDS3u2nlU74Vy++nYvp6/K0K8WUvfJL+HuDIEXprFFonlRoujP1Ne2jVScju1cHuxHuPHV8kP7XnwVJ339cCRtp1tSKvDH52XTPyDI1YRJjiaiKxXXBAbU/qri/ooVuqrprwe/bb2Dr+p8h3QC321iZth5v8VgtbakRZNNklbVve7IUCJ4gQLG5R/Tk3yD5EQ5jj9xaD1lrrdZxCoNsseNB/K9QaRbNHak9a6MxoMV/yvCo0pIiUW/9x4Zo8+x18J0dfIzAgMBAAECggEAA82zNPJctx3Ck4+Yir9SlYB7PXTlXe97CfbEvrjIETeOBwact2BtPoRlTR30LnBT3jlIj4n/bsaBv1hKu2iqvNjlOkLkmcrozUGdywFtUJQu9K/p5dwIRhIH4S5FIfaSlX9dJwIj3NMcPEk+UJ2K8s9cv0+ASug0pPw3Lp84vCepL05H8/pOE9v2TyTUarxmZ4WEPus7o3693eqiq9gIEe52pU/nrO+nxdK9yT0HVXvRQRGwc2ot0l9+o2F1lnogkwUTimGzFyIH6tgKdbTEHf5/H1xwzEJcDR6rIRJQlZvECZVB5mTNZlxSdiakSq0DpgLvdDbJrrrJr93Z5NQD2QKBgQD4z1Lj6/Tzvgtmx069Q+Pe/ZHrPkPZH13k94oWKX/PYQEAmIK8oULdgSyrUHbHebQIzxSgQcZQNASh2Fm1GE+G1g5rVe+0JtTLUjej+Jnk/fx077+62xk6A2zNotiZdEj+Vgvhp3rNYqVhYo+cQnVZlHBbMVlHK37ViqATNBXHxQKBgQDDVa/A1FO4Bq/0vRpB35TJSifeSEHTxFVQkO5e2DlgJo7w00hIZ2OlOzZw9xFLllc+DaSwyIpUWEX0YWTQ/81R5+0e28HbCYdPV4HeHA99M+9eCHGWYyVSBKx70VY3Lf8m1FuSIjFqMvUaMx+JATExCill8jNnf+SOAqcPoX15lwKBgQDxdrMpAutcSUljW3Ws9GAHHusn8+uAoZJMDXfzfh7NsYR5gYkPaykrMpuZPqOnfxMboWyRAoRlfbYpI4Ab4hX7821W+bWBjlXhZE8fULp5o4wIKExYGR3oUT6carfuZQ4Z1oP9YL4q6Ns7IZ3hthWQ9B/+QGw2R531u20Sde/VSQKBgQCT/faDnRq0wXRMc31STUfX3bQpCAu/mu+cc8H139JQk8YWla0dFh/7zsnnenkDEKuWmxWczsNnNd4CUkvkwPnZRWN0zsCn6Cc5KK5/Djl5/YAseS895m4fHuVjhe3RLqsyyw95vyg7MtJagEMzNNEFO5Kz53Wkw9Pw9/eEszByjwKBgQDyIOP5ak88TLirOp+kkMlaxkPtctH4JZMgAfTBbCmxjm9zsAiw/alZQRhKsGBB4ahN1yJ9wEyfMsdht1v8pdfxA/gs0rmPPe6ArAyrI3eZUFazGPhiRtC9fa1DDKHRUl+GPoOZ9PaFuPWq1/F7Pl/sadGR6to9aP+/OdjhJecD9g==";

            // Декодирование строк в массивы байт
            byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);
            byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);

            // Получение объектов ключей из массивов байт
            KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

            // Подпись и проверка подписи для проверки соответствия ключей
            Signature signature = Signature.getInstance("SHA256withRSA", "BC");
            signature.initSign(privateKey);
            signature.update("Сообщение для подписи".getBytes());
            byte[] signedData = signature.sign();

            signature.initVerify(publicKey);
            signature.update("Сообщение для подписи".getBytes());

            if (signature.verify(signedData)) {
                System.out.println("Публичный ключ соответствует приватному ключу.");
            } else {
                System.out.println("Публичный ключ НЕ соответствует приватному ключу.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}