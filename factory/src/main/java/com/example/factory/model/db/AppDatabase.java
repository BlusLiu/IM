package com.example.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 10:06 2019/10/20
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;

}
