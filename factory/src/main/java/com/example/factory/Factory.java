package com.example.factory;


import android.util.Log;

import androidx.annotation.StringRes;

import com.example.common.APP.Applocation;
import com.example.factory.data.DataSource;
import com.example.factory.data.group.GroupCenter;
import com.example.factory.data.group.GroupDispatcher;
import com.example.factory.data.message.MessageCenter;
import com.example.factory.data.message.MessageDispatcher;
import com.example.factory.data.user.UserCenter;
import com.example.factory.data.user.UserDispatcher;
import com.example.factory.model.api.PushModel;
import com.example.factory.model.api.RspModel;
import com.example.factory.model.card.GroupCard;
import com.example.factory.model.card.GroupMemberCard;
import com.example.factory.model.card.MessageCard;
import com.example.factory.model.card.UserCard;
import com.example.factory.model.db.Group_Table;
import com.example.factory.model.db.Message;
import com.example.factory.persistence.Account;
import com.example.factory.utils.DBFlowExclusionStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by liuzhen on 2019/10/13
 */
public class Factory {
    private static final Factory instance;
    private final Executor executor;
    private final Gson gson;

    static {
        instance = new Factory();
    }

    private Factory() {
        executor = Executors.newFixedThreadPool(4);
        gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
                // TODO 设置一个过滤器，数据库级别的MODEL不进行JSOn转换
                .setExclusionStrategies(new DBFlowExclusionStrategy())
                .create();
    }

    public static Applocation app() {
        return Applocation.getInstance();
    }

    public static void runOnAsync(Runnable runnable) {
        instance.executor.execute(runnable);
    }

    public static Gson getGson() {
        return instance.gson;
    }


    private static void decodeRspCode(@StringRes final int resId,
                                      final DataSource.FailedCallback callback) {
        if (callback != null)
            callback.onDataNotLoaded(resId);
    }


    /**
     * 收到账户退出的消息需要进行账户退出重新登录
     */
    private void logout() {

    }


    /**
     * 处理推送来的消息
     *
     * @param message 消息
     */
    public static void dispatchPush(String message) {
        if (!Account.isLogin())
            return;

        PushModel model = PushModel.decode(message);
        if (model == null)
            return;


        for (PushModel.Entity entity : model.getEntities()) {
            Log.e("dispatchPush_entity:", entity.toString());
            switch (entity.type) {
                case PushModel.ENTITY_TYPE_LOGOUT:
                    instance.logout();
                    return;
                case PushModel.ENTITY_TYPE_MESSAGE: {
                    MessageCard card = getGson().fromJson(entity.content, MessageCard.class);
                    getMessageCenter().dispatch(card);
                    break;

                }

                case PushModel.ENTITY_TYPE_ADD_FRIEND: {
                    UserCard card = getGson().fromJson(entity.content, UserCard.class);
                    getUserCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP: {
                    GroupCard card = getGson().fromJson(entity.content, GroupCard.class);
                    getGroupCenter().dispatch(card);
                    break;
                }

                case PushModel.ENTITY_TYPE_ADD_GROUP_MEMBERS:
                case PushModel.ENTITY_TYPE_MODIFY_GROUP_MEMBERS: {
                    Type type = new TypeToken<List<GroupMemberCard>>() {

                    }.getType();

                    List<GroupMemberCard> card = getGson().fromJson(entity.content, type);
                    getGroupCenter().dispatch(card.toArray(new GroupMemberCard[0]));
                    break;
                }
                case PushModel.ENTITY_TYPE_EXIT_GROUP_MEMBERS: {

                    break;
                }

            }
        }
    }

    public static void setup() {
        FlowManager.init(new FlowConfig.Builder(app())
                .openDatabasesOnInit(true)
                .build());

        Account.load(app());
    }

    /**
     * 错误数据的解析
     *
     * @param model
     * @param callback
     */
    public static void decodeRspCode(RspModel model, DataSource.FailedCallback callback) {
        if (model == null)
            return;

        // 进行Code区分
        switch (model.getCode()) {
            case RspModel.SUCCEED:
                return;
            case RspModel.ERROR_SERVICE:
                decodeRspCode(R.string.data_rsp_error_service, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_USER:
                decodeRspCode(R.string.data_rsp_error_not_found_user, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP:
                decodeRspCode(R.string.data_rsp_error_not_found_group, callback);
                break;
            case RspModel.ERROR_NOT_FOUND_GROUP_MEMBER:
                decodeRspCode(R.string.data_rsp_error_not_found_group_member, callback);
                break;
            case RspModel.ERROR_CREATE_USER:
                decodeRspCode(R.string.data_rsp_error_create_user, callback);
                break;
            case RspModel.ERROR_CREATE_GROUP:
                decodeRspCode(R.string.data_rsp_error_create_group, callback);
                break;
            case RspModel.ERROR_CREATE_MESSAGE:
                decodeRspCode(R.string.data_rsp_error_create_message, callback);
                break;
            case RspModel.ERROR_PARAMETERS:
                decodeRspCode(R.string.data_rsp_error_parameters, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_ACCOUNT:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_account, callback);
                break;
            case RspModel.ERROR_PARAMETERS_EXIST_NAME:
                decodeRspCode(R.string.data_rsp_error_parameters_exist_name, callback);
                break;
            case RspModel.ERROR_ACCOUNT_TOKEN:
                Applocation.showToast(R.string.data_rsp_error_account_token);
                instance.logout();
                break;
            case RspModel.ERROR_ACCOUNT_LOGIN:
                decodeRspCode(R.string.data_rsp_error_account_login, callback);
                break;
            case RspModel.ERROR_ACCOUNT_REGISTER:
                decodeRspCode(R.string.data_rsp_error_account_register, callback);
                break;
            case RspModel.ERROR_ACCOUNT_NO_PERMISSION:
                decodeRspCode(R.string.data_rsp_error_account_no_permission, callback);
                break;
            case RspModel.ERROR_UNKNOWN:
            default:
                decodeRspCode(R.string.data_rsp_error_unknown, callback);
                break;
        }
    }

    public static UserCenter getUserCenter() {
        return UserDispatcher.instance();
    }

    public static MessageCenter getMessageCenter() {
        return MessageDispatcher.instance();
    }

    public static GroupCenter getGroupCenter() {
        return GroupDispatcher.instance();
    }
}


