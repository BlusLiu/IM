package com.example.common.APP;




import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

import com.example.common.R;

/**
 * @Author: liuzhen
 * @Description:
 * @Date: Create in 20:38 2019/10/25
 */
public abstract class ToolbarActivity extends Activity{
    protected Toolbar mToolbar;

    @Override
    protected void initWidget() {
        super.initWidget();
        initToolbar((Toolbar) findViewById(R.id.toolbar));
    }

    public void initToolbar(Toolbar toolbar){
        if (toolbar != null){
            setSupportActionBar(toolbar);
        }
        initTitleNeedBack();
    }

    protected void initTitleNeedBack(){
        // 注意是不是appcompat包
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);

        }
    }

}
