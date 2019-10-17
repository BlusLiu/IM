package com.example.im2.frags.account;


import android.content.Context;
import android.os.Bundle;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.common.APP.Fragment;
import com.example.im2.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private AccountTrigger accountTrigger;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 拿到我们的activity的应用
        accountTrigger = (AccountTrigger) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        accountTrigger.triggerView();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_login;
    }

}
