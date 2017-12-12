package com.upup8.rpicker;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.upup8.rfilepicker.RPickerActivity;
import com.upup8.rfilepicker.data.RFilePickerConst;
import com.upup8.rfilepicker.utils.PermissionUtils;
import com.upup8.rpicker.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Context mContext;

    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.CHANGE_WIFI_STATE};
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int REQUEST_CODE_CAMERA = 1, REQUEST_CODE_PERMISSIONS = 2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btnOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (EasyPermissions.hasPermissions(MainActivity.this, PERMISSIONS)) {
//                    //...
//                    openFilePicker();
//                } else {
//                    //...
//                    EasyPermissions.requestPermissions(MainActivity.this, "拍照需要摄像头权限",
//                            REQUEST_CODE_PERMISSIONS, perms);
//                }


                PermissionUtils.checkMorePermissions(mContext, PERMISSIONS,
                        new PermissionUtils.PermissionCheckCallBack() {
                            @Override
                            public void onHasPermission() {
                                // 已授予权限
                                openFilePicker();
                            }

                            @Override
                            public void onUserHasAlreadyTurnedDown(String... permission) {
                                // 上一次申请权限被拒绝，可用于向用户说明权限原因，然后调用权限申请方法。
                            }

                            @Override
                            public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                                // 第一次申请权限或被禁止申请权限，建议直接调用申请权限方法。
                            }
                        });


            }
        });


    }

    private void openFilePicker() {
        Intent intent = new Intent(MainActivity.this, RPickerActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            ArrayList<String> list = data.getStringArrayListExtra(RFilePickerConst.EXTRA_FILE_TYPE);
            StringBuilder str = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                str.append(list.get(i) + "\n");
            }
            binding.tvShow.setText(str.toString());

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        //EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        switch (requestCode) {
            case REQUEST_CODE_CAMERA:
                if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
                    // 权限申请成功
                    openFilePicker();
                } else {
                    Toast.makeText(mContext, "打开相机失败", Toast.LENGTH_SHORT).show();
                }
                break;
            case REQUEST_CODE_PERMISSIONS:
                PermissionUtils.onRequestMorePermissionsResult(mContext, PERMISSIONS, new PermissionUtils.PermissionCheckCallBack() {
                    @Override
                    public void onHasPermission() {
                        openFilePicker();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDown(String... permission) {
                        Toast.makeText(mContext, "我们需要" + Arrays.toString(permission) + "权限", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUserHasAlreadyTurnedDownAndDontAsk(String... permission) {
                        Toast.makeText(mContext, "我们需要" + Arrays.toString(permission) + "权限", Toast.LENGTH_SHORT).show();
                        showToAppSettingDialog();
                    }
                });


        }
    }

    /**
     * 显示前往应用设置Dialog
     */
    private void showToAppSettingDialog() {
        new AlertDialog.Builder(mContext)
                .setTitle("需要权限")
                .setMessage("我们需要相关权限，才能实现功能，点击前往，将转到应用的设置界面，请开启应用的相关权限。")
                .setPositiveButton("前往", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PermissionUtils.toAppSetting(mContext);
                    }
                })
                .setNegativeButton("取消", null).show();
    }


//    //成功
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> list) {
//        // Some permissions have been granted
//        // ...
//    }
//
//    //失败
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> list) {
//        // Some permissions have been denied
//        // ...
//    }
}
