package com.example.factory.persenter.message;

import com.example.factory.model.db.Group;
import com.example.factory.model.db.Message;
import com.example.factory.model.db.User;
import com.example.factory.presenter.BaseContract;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 22:39 2019/11/1
 */
public interface ChatContract {
    interface Presenter extends BaseContract.Presenter{

        void pushText(String content);

        void pushAudio(String path);

        void pushImages(String[] paths);

        boolean rePush(Message message);
    }

    interface View<InitModel> extends BaseContract.RecyclerView<Presenter, Message>{

        void onInit(InitModel model);

    }

    interface UserView extends View<User>{

    }

    interface GroupView extends View<Group>{

    }
}
