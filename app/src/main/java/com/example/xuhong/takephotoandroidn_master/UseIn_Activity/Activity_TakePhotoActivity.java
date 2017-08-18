package com.example.xuhong.takephotoandroidn_master.UseIn_Activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xuhong.takephotoandroidn_master.R;
import com.example.xuhong.takephotoandroidn_master.other.FileUtils;
import com.example.xuhong.takephotoandroidn_master.other.PermissionListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.xuhong.takephotoandroidn_master.other.FileUtils.getImageContentUri;

public class Activity_TakePhotoActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView ivShow;

    private TextView tvShow;


    //上传图片用到
    private static final int REQ_TAKE_PHOTO = 100;
    private static final int REQ_ALBUM = 101;
    private static final int REQ_ZOOM = 102;
    private PermissionListener permissionListener;
    private Uri outputUri;
    private String imgPath = FileUtils.generateImgePath(this);

    private static final String FILE_PROVIDER_AUTHORITY = "com.chaychan.androidnadaption.fileprovider";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__take_photo);
        inintView();
    }

    private void inintView() {
        ivShow = (ImageView) findViewById(R.id.ivShow);
        Button btCamera = (Button) findViewById(R.id.btCamera);
        btCamera.setOnClickListener(this);
        Button btAlbum = (Button) findViewById(R.id.btAlbum);
        btAlbum.setOnClickListener(this);
        tvShow = (TextView) findViewById(R.id.tvShow);

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btCamera:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    //如果是6.0或6.0以上，则要申请运行时权限，这里需要申请拍照和写入SD卡的权限
                    requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, new PermissionListener() {
                        @Override
                        public void onGranted() {
                            openCamera();
                        }

                        @Override
                        public void onDenied(List<String> deniedPermissions) {
                            //("拍照权限被拒绝了");
                        }
                    });
                    return;
                }

                openCamera();
                break;
            case R.id.btAlbum:
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        "image/*");
                startActivityForResult(intent, REQ_ALBUM);
                break;
        }
    }

    private void openCamera() {
        // 指定调用相机拍照后照片的储存路径
        //L.i("拍照完图片的路径为：" + imgPath);

        File imgFile = new File(imgPath);
        Uri imgUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //如果是7.0或以上
            imgUri = FileProvider.getUriForFile(this, FILE_PROVIDER_AUTHORITY, imgFile);
        } else {
            imgUri = Uri.fromFile(imgFile);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(intent, REQ_TAKE_PHOTO);

    }


    /**
     * 申请运行时权限
     */
    public void requestRuntimePermission(String[] permissions, PermissionListener listener) {
        permissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK://调用图片选择处理成功
                Bitmap bm = null;
                File temFile = null;
                File srcFile = null;
                File outPutFile = null;
                switch (requestCode) {
                    case REQ_TAKE_PHOTO:
                        //拍照后在这里回调
                        srcFile = new File(imgPath);
                        outPutFile = new File(FileUtils.generateImgePath(this));
                        outputUri = Uri.fromFile(outPutFile);
                        startPhotoZoom(srcFile, outPutFile, REQ_ZOOM);
                        break;

                    case REQ_ALBUM:// 选择相册中的图片
                        if (data != null) {
                            Uri sourceUri = data.getData();
                            String[] proj = {MediaStore.Images.Media.DATA};

                            // 好像是android多媒体数据库的封装接口，具体的看Android文档
                            Cursor cursor = managedQuery(sourceUri, proj, null, null, null);
                            // 按我个人理解 这个是获得用户选择的图片的索引值
                            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                            // 将光标移至开头 ，这个很重要，不小心很容易引起越界
                            cursor.moveToFirst();
                            // 最后根据索引值获取图片路径
                            String imgPath = cursor.getString(column_index);

                            srcFile = new File(imgPath);
                            outPutFile = new File(FileUtils.generateImgePath(this));
                            outputUri = Uri.fromFile(outPutFile);
                            //发起裁剪请求
                            startPhotoZoom(srcFile, outPutFile, REQ_ZOOM);
                        }
                        break;

                    case REQ_ZOOM://裁剪后回调
                        if (data != null) {
                            if (outputUri != null) {
                                bm = decodeUriAsBitmap(outputUri);
                                //如果是拍照的,删除临时文件
                                temFile = new File(imgPath);
                                if (temFile.exists()) {
                                    temFile.delete();
                                }
                                Bitmap bitmap = FileUtils.compressImage(bm, 100);
                                File file = outputIamge(bitmap);
                                Picasso.with(this).load(file).error(R.drawable.ic_warning).into(ivShow);
                               // ivShow.setImageBitmap(bitmap);
                            }
                        } else {
                            //L.e("选择图片发生错误，图片可能已经移位或删除");
                        }
                        break;
                }
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:

                if (grantResults.length > 0) {
                    List<String> deniedPermissions = new ArrayList<>();
                    for (int i = 0; i < grantResults.length; i++) {
                        int grantResult = grantResults[i];
                        String permission = permissions[i];
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissions.add(permission);
                        }
                    }

                    if (deniedPermissions.isEmpty()) {
                        permissionListener.onGranted();
                    } else {
                        permissionListener.onDenied(deniedPermissions);
                    }
                }
                break;
        }
    }


    public void startPhotoZoom( File srcFile, File output, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(getImageContentUri(this, srcFile), "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");

        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 480);
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(output));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        // intent.putExtra("noFaceDetection", true); // no face detection

        startActivityForResult(intent, requestCode);
    }


    public  Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            // 先通过getContentResolver方法获得一个ContentResolver实例，
            // 调用openInputStream(Uri)方法获得uri关联的数据流stream
            // 把上一步获得的数据流解析成为bitmap
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    //在自定义目录创建图片
    private File outputIamge(Bitmap bitmap) {

        //根据日期来命名
        SimpleDateFormat sdf = new SimpleDateFormat("MMddHHmmss");
        String time = sdf.format(new Date());

        //保存在私有目录下
        File outputIamge = new File(getExternalCacheDir()+ time + ".png");

        //创建
        try {
            outputIamge.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream fOut = null;

        try {
            fOut = new FileOutputStream(outputIamge);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);

        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputIamge;
    }
}
