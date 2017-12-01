package com.upup8.rpicker;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.upup8.rfilepicker.RPickerActivity;
import com.upup8.rpicker.databinding.ActivityMainBinding;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    String[] perms = {Manifest.permission.CAMERA, Manifest.permission.CHANGE_WIFI_STATE};
    private final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private final int REQUEST_CODE_CAMERA = 1, REQUEST_CODE_PERMISSIONS = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btnOpenFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (EasyPermissions.hasPermissions(MainActivity.this, PERMISSIONS)) {
                    //...
                    openFilePicker();
                } else {
                    //...
                    EasyPermissions.requestPermissions(MainActivity.this, "拍照需要摄像头权限",
                            REQUEST_CODE_PERMISSIONS, perms);
                }

            }
        });



    }

    private void openFilePicker(){
        Intent intent = new Intent(MainActivity.this, RPickerActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data != null) {
            //ArrayList<FileInfo> list = data.getParcelableArrayListExtra(ChooseFileActivity.FILELISTDATA);
            StringBuilder str = new StringBuilder();
//            for (int i = 0; i < list.size(); i++) {
//                FileInfo fileInfo = list.get(i);
//                str.append(fileInfo.getFile_path() + "\n");
//            }
            binding.tvShow.setText(str.toString());


        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
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
