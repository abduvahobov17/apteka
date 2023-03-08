package com.asgardiateam.aptekaproject.common;


import com.asgardiateam.aptekaproject.entity.Admin;
import com.asgardiateam.aptekaproject.enums.Lang;

public class ThreadLocalSingleton {

    private static final ThreadLocal<Lang> LANG = ThreadLocal.withInitial(() -> Lang.RU);

    private static final ThreadLocal<Admin> ADMIN = ThreadLocal.withInitial(Admin::new);

    public static Admin getUser() {
        return ThreadLocalSingleton.ADMIN.get();
    }

    public static void setUser(Admin user) {
        ThreadLocalSingleton.ADMIN.set(user);
    }

    public static Lang getLang() {
        return ThreadLocalSingleton.LANG.get();
    }

    public static void setLang(Lang lang) {
        ThreadLocalSingleton.LANG.set(lang);
    }
}
