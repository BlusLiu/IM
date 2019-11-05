package com.example.im2.frags.assist;


import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.im2.R;
import com.example.im2.activites.MainActivity;
import com.example.im2.frags.media.GalleryFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * A simple {@link Fragment} subclass.
 * 权限申请
 */
public class PermissionsFragment extends BottomSheetDialogFragment implements EasyPermissions.PermissionCallbacks{
    //申请标识，用于回调自己的tag
    private static final int RC = 0x0100;

    public PermissionsFragment() {
        // Required empty public constructor
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new GalleryFragment.TransStatusBottomSheetDialog(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root =  inflater.inflate(R.layout.fragment_permissions, container, false);
        root.findViewById(R.id.btn_submit)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        requestPerm();
                    }
                });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshState(getView());
    }

    /**
     * 刷新布局中图片的状态
     * @param root
     */
    private void refreshState(View root){

        if (root == null)
            return;
        Context context = getContext();

        root.findViewById(R.id.im_state_permission_network)
                .setVisibility(haveNetwork(context) ? View.VISIBLE : View.GONE);

        root.findViewById(R.id.im_state_permission_read)
                .setVisibility(haveRead(context) ? View.VISIBLE : View.GONE);

        root.findViewById(R.id.im_state_permission_record)
                .setVisibility(haveRecord(context) ? View.VISIBLE : View.GONE);

    }

    /**
     * 检查网络权限
     * @param context
     * @return
     */
    private static boolean haveNetwork(Context context){
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }
    /**
     * 检查读写权限
     * @param context
     * @return
     */
    private static boolean haveRead(Context context){
        String[] perms = new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        return EasyPermissions.hasPermissions(context, perms);
    }
    /**
     * 检查录音权限
     * @param context
     * @return
     */
    private static boolean haveRecord(Context context){
        String[] perms = new String[]{
                Manifest.permission.RECORD_AUDIO
        };

        return EasyPermissions.hasPermissions(context, perms);
    }

    /**
     * 私有的show
     * @param manager
     */
    private static void show(FragmentManager manager){
        new PermissionsFragment()
                .show(manager, PermissionsFragment.class.getName());
    }

    public static boolean haveAll(Context context,FragmentManager manager){
        boolean haveall = haveNetwork(context) && haveRead(context) && haveRecord(context);

        if (! haveall){
            show(manager);
        }

        return haveall;
    }



    /**
     *
     * 申请权限
     */
    @AfterPermissionGranted(RC)
    private void requestPerm(){
        String[] perms = new String[]{
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

        };

        if (EasyPermissions.hasPermissions(getContext(),perms)){
            // Applocation.showToast(R.string.label_permission_ok);
            // 得到跟布局，需要oncreate后
            refreshState(getView());
            MainActivity.show(getActivity());
        }else {
            EasyPermissions.requestPermissions(this, getString(R.string.title_assist_permissions),RC,perms);
        }

    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        // 用户自己设置
        Toast.makeText(getContext(),"失败", Toast.LENGTH_LONG);
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog
                    .Builder(this)
                    .build()
                    .show();
        }
    }

    /**
     * 权限申请的回调
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this);
    }
}
