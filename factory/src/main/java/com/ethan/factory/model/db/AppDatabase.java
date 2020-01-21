package com.ethan.factory.model.db;

import com.raizlabs.android.dbflow.annotation.Database;

/**
 * Basic info of the database
 */
@Database(name = AppDatabase.NAME, version = AppDatabase.VERSION)
public class AppDatabase {
    public static final String NAME = "AppDatabase";
    public static final int VERSION = 1;
}
