package com.marinovskiy.ekreativetestproject.db;

import android.content.Context;

import com.rightutils.rightutils.db.RightDBUtils;

public class DbUtils extends RightDBUtils {

    public static DbUtils newInstance(Context context, String name, int version) {
        DbUtils dbUtils = new DbUtils();
        dbUtils.setDBContext(context, name, version);
        return dbUtils;
    }

}
