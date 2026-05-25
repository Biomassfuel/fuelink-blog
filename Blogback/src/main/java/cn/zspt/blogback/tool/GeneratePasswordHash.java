package cn.zspt.blogback.tool;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class GeneratePasswordHash {
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("使用方法: java GeneratePasswordHash 你的密码");
            return;
        }

        String password = args[0];
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        try {
            String hash = argon2.hash(3, 65536, 1, password.toCharArray());
            System.out.println("========================================");
            System.out.println("原始密码: " + password);
            System.out.println("加密后的哈希:");
            System.out.println(hash);
            System.out.println("========================================");

            // SQL语句
            System.out.println("\nSQL插入语句:");
            System.out.println("INSERT INTO users (id, username, password) VALUES (1, '你的用户名', '" + hash + "');");

        } finally {
            argon2.wipeArray(password.toCharArray());
        }
    }
}