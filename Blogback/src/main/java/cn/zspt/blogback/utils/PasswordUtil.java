package cn.zspt.blogback.utils;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {
    private final Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

    public String hash(String password) {
        try {
            return argon2.hash(3, 65536, 1, password.toCharArray());
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }

    public boolean verify(String hash, String password) {
        try {
            return argon2.verify(hash, password.toCharArray());
        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }
}