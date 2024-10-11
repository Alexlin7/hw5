package com.systex.hw4.util;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;

public class PasswordUtil{
    private static final Argon2 argon2 = Argon2Factory.create();

    public static String hashPassword(String password){
        //使用Argon加密(遞迴次數, 使用記憶體, 密碼)
        return argon2.hash(2, 65536, 1, password);
    }

    // 驗證密碼
    public static boolean verifyPassword(String password, String hashedPassword) {
        return argon2.verify(hashedPassword, password);
    }

}
