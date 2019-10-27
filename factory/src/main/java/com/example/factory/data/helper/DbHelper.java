package com.example.factory.data.helper;

import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.User;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 10:00 2019/10/27
 */
public class DbHelper {
    private static final DbHelper instance;

    static {
        instance = new DbHelper();
    }

    // 为什么要重写构造方法
    private DbHelper(){
    }

    public static<Model extends BaseModel> void save(final Class<Model> tClass, final Model... models){
        if (models == null || models.length == 0)
            return;
        // 管理者
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        // 提交一个事务
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // zx
                FlowManager.getModelAdapter(tClass)
                        .saveAll(Arrays.asList(models));

                instance.notifiSave(tClass, models);
            }
        }).build().execute();
    }

    public static<Model extends BaseModel> void delete(final Class<Model> tClass, final Model... models){
        if (models == null || models.length == 0)
            return;
        // 管理者
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        // 提交一个事务
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                // zx
                FlowManager.getModelAdapter(tClass)
                        .deleteAll(Arrays.asList(models));

                instance.notifiDelete(tClass, models);

            }
        }).build().execute();
    }

    private final <Model extends BaseModel> void notifiSave(final Class<Model> tClass, final Model... models){
        // TODO
    }

    private final <Model extends BaseModel> void notifiDelete(final Class<Model> tClass, final Model... models){
        // TODO
    }


/*    public static void save(){
        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);

        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                FlowManager.getModelAdapter(User.class)
                        .saveAll();
            }
        }).build().execute();
    }*/


}
