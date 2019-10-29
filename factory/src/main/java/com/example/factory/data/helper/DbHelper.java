package com.example.factory.data.helper;

import com.example.factory.model.db.AppDatabase;
import com.example.factory.model.db.Group;
import com.example.factory.model.db.GroupMember;
import com.example.factory.model.db.Group_Table;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.Session;
import com.example.factory.model.db.User;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    /**
     * 观察者很多
     */
    private final Map<Class<?>, Set<ChangedListener>> changedListeners = new HashMap<>();

    private <Model extends BaseModel> Set<ChangedListener> getChangedListener(Class<Model> tClass){
        if (instance.changedListeners.containsKey(tClass)){
            return instance.changedListeners.get(tClass);
        }
        return null;
    }

    public static<Model extends BaseModel> void addChangedListener(final Class<Model> tClass,
                                                                   ChangedListener<Model> listener){
        Set<ChangedListener> changedListeners = instance.getChangedListener(tClass);
        if (changedListeners == null){
            changedListeners = new HashSet<>();
            instance.changedListeners.put(tClass,changedListeners);
        }
        changedListeners.add(listener);
    }

    public static<Model extends BaseModel> void removeChangedListener(final Class<Model> tClass,
                                                                   ChangedListener<Model> listener){
        Set<ChangedListener> changedListeners = instance.getChangedListener(tClass);
        if (changedListeners == null){
            return;
        }
        changedListeners.remove(listener);
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

    // 观察者
    private final <Model extends BaseModel> void notifiSave(final Class<Model> tClass, final Model... models){
        // TODO
        final Set<ChangedListener> listeners = getChangedListener(tClass);
        if (listeners != null && listeners.size() > 0){
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataSave(models);
            }
        }

        // 群成员，消息变更
        if (GroupMember.class.equals(tClass)){
            updateGroup((GroupMember[]) models);
        }else if (Message.class.equals(tClass)){
            updateSession((Message[]) models);
        }
    }

    private final <Model extends BaseModel> void notifiDelete(final Class<Model> tClass, final Model... models){
        // TODO
        final Set<ChangedListener> listeners = getChangedListener(tClass);
        if (listeners != null && listeners.size() > 0){
            for (ChangedListener<Model> listener : listeners) {
                listener.onDataDelete(models);
            }
        }

        // 群成员，消息变更
        if (GroupMember.class.equals(tClass)){
            updateGroup((GroupMember[]) models);
        }else if (Message.class.equals(tClass)){
            updateSession((Message[]) models);
        }
    }


    public interface ChangedListener<Data extends BaseModel>{
        void onDataSave(Data... list);

        void onDataDelete(Data... list);
    }

    private void updateGroup(GroupMember... members){

        final Set<String> groupIds = new HashSet<>();
        for (GroupMember member : members) {
            groupIds.add(member.getGroup().getId());
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                List<Group> groups = SQLite.select()
                        .from(Group.class)
                        .where(Group_Table.id.in(groupIds))
                        .queryList();
                instance.notifiSave(Group.class, groups.toArray(new Group[0]));
            }
        }).build().execute();

    }
    private void updateSession(Message... messages){
        final Set<Session.Identify> identifies = new HashSet<>();
        for (Message message : messages) {
            Session.Identify identify = Session.createSessionIdentify(message);
            identifies.add(identify);
        }

        DatabaseDefinition definition = FlowManager.getDatabase(AppDatabase.class);
        definition.beginTransactionAsync(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                ModelAdapter<Session> adapter = FlowManager.getModelAdapter(Session.class);
                Session[] sessions = new Session[identifies.size()];
                int index = 0;
                for (Session.Identify identify : identifies) {
                    Session session = SessionHelper.finFromLocal(identify.id);
                    if (session == null){
                        // 第一次聊天
                        session = new Session(identify);
                    }

                    session.refreshToNow();
                    adapter.save(session);
                    sessions[index++] = session;
                }
                instance.notifiSave(Session.class, sessions);
            }
        }).build().execute();
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
