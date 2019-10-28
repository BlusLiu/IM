package com.example.factory.data.helper;

import com.example.factory.model.db.Session;
import com.example.factory.model.db.Session_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 15:28 2019/10/27
 */
public class SessionHelper {
    public static Session finFromLocal(String id) {
        return SQLite.select()
                .from(Session.class)
                .where(Session_Table.id.eq(id))
                .querySingle();

    }
}
