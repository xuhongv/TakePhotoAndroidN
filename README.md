
# Android进阶封装之“史无前例"一个类实现兼容Android 6.0权限、适配Android 拍照7.0： 相机与相册上传图片就用我好啦！
 
更为详细的封装过程，对应的CSDN博客地址：http://blog.csdn.net/xh870189248/article/details/77389855

## **介绍只需三步的环境集成：**


  - 第一步：把 demo下的res的 xml文件夹整个复制到你的工程res文件夹根目录下：



![第一步](http://img.blog.csdn.net/20170819115918985?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveGg4NzAxODkyNDg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



  - 第二步：在你AndroidManifest.xml下的Application节点下加入以下代码：

    

```
          <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.fileprovider"
            android:exported="false"
            android:grantUriPermissions="true">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_provider_paths"/>
           </provider>
```


 - 如下所示

![这里写图片描述](http://img.blog.csdn.net/20170819120218977?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQveGg4NzAxODkyNDg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)



 - 第三步 ：把demo的就仅仅一个类 TakePictureManager.class 复制过去就可以啦！别忘了在清单文件加相关权限哦！


## **怎么使用？**


示例：


```
TakePictureManager takePictureManager takePictureManager = new TakePictureManager(this);
                //开启裁剪 比例 1:3 宽高 350 350  (默认不裁剪)
                takePictureManager.setTailor(1, 3, 350, 350);
                //拍照方式
                takePictureManager.startTakeWayByCarema();
                //监听回调
                takePictureManager.setTakePictureCallBackListener(new TakePictureManager.takePictureCallBackListener() {
         //成功拿到图片,isTailor 是否裁剪？ ,outFile 拿到的文件 ,filePath拿到的URl
        @Override
        public void successful(boolean isTailor, File outFile, Uri filePath) {
                  }
        //失败回调
        @Override
        public void failed(int errorCode, List<String> deniedPermissions) {

                   }
                });

    //把本地的onActivityResult()方法回调绑定到对象
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        takePictureManager.attachToActivityForResult(requestCode, resultCode, data);
    }

    //onRequestPermissionsResult()方法权限回调绑定到对象
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        takePictureManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

```
使用详细步骤：


- ①、先new一个TakePictureManager 对象，构造方法只需传**this**即可(**不管你在Activity还是在Fragment**)。

- ②、重写onActivityResult()方法，调用对象的attachToActivityForResult()方法，参数依次是onActivityResult()回调的参数。  **实现把拍照或相册回调发数据绑定在对象方法处理。**

- ③、重写onRequestPermissionsResult()方法，调用对象的onRequestPermissionsResult()方法，参数依次是onRequestPermissionsResult()回调的参数。  **实现把权限回调绑定在对象方法处理。**

- ④、这时候，你只需调用对象方法，即可轻松调用相机或相册。具体的方法参数说明如下：



| 方法名 | 参数|说明|
|-------|------|-----------|
| setTailor(int aspectX, int aspectY, int outputX, int outputY) | 要裁剪的宽比例、要裁剪的高比例、要裁剪图片的宽、要裁剪图片的高|一旦调用，表示要裁剪，**默认不裁剪**|
| startTakeWayByAlbum()| 无参数 |调用相册|
| sstartTakeWayByCarema()| 无参数 |调用相机|
| setTakePictureCallBackListener（takePictureCallBackListener listener）| takePictureCallBackListener 回调接口|调用相机或相册后的回调|


| 接口| 方法|说明|
|-------|------|-----------|
| takePictureCallBackListener | successful(boolean isTailor, File outFile, Uri filePath)|成功回调! isTailor : 是否已裁剪, outFile :输出的照片文件 ，filePath ：输出的照片Uri 。|
| | failed(int errorCode, List<String> deniedPermissions)|失败回调！errorCode ：0表示相片已移除或不存在！ 1表示权限被拒绝，deniedPermissions当权限被拒绝时候，会通过list传回|





