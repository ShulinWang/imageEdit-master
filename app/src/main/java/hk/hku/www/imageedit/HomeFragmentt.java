package hk.hku.www.imageedit;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.http.HttpResponseCache;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class HomeFragmentt extends TabFragment {
    private View view;
    //图片选择器
    private static final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
    private static final OkHttpClient client = new OkHttpClient();
    private final int REQUEST_CODE_GALLERY = 1001;
    private Button openButton,uploadButton;
    private ImageView selectResult,transformResult;
    private Handler handler=new Handler(Looper.getMainLooper());
    private Uri uri;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle bundle) {
        view=inflater.inflate(R.layout.activity_imageuplading,container,false);
        Log.i("wangshulin", "onCreateView: "+getView());
        selectResult=view.findViewById(R.id.iv_select_image_result);
        transformResult=view.findViewById(R.id.iv_upload_image_result);
        openButton = (Button)view.findViewById(R.id.btn_open_gallery);
        uploadButton=(Button)view.findViewById(R.id.btn_upload_image);
        transformResult.setVisibility(View.GONE);
        selectResult.setVisibility(View.GONE);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
            }
        });
        return view;
    }
    /**
     * 选取图片后的回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        File file;
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                Log.e("onHanlderSuccess: ", resultList.get(0).getPhotoPath());
                file=new File(resultList.get(0).getPhotoPath());
                uri=Uri.fromFile(file);
                selectResult.setImageURI(uri);
                selectResult.setVisibility(View.VISIBLE);
                uploadButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            run(file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Log.e("onHanlderSuccess: ", errorMsg);
        }
    };
    private void  run(File f) throws Exception {
        final File file=f;
        new Thread() {
            @Override
            public void run() {
                //子线程需要做的工作
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file",file.getName(),
                                RequestBody.create(MEDIA_TYPE_JPG, file))
                        .build();
                //设置为自己的ip地址
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:5000/uploadImage")
                        .post(requestBody)
                        .build();
                try(Response response = client.newCall(request).execute()){
                    if (!response.isSuccessful()){
                        Log.i("TAG", "run: "+response.code());
                        Log.i("TAG", "run: "+response.body().string());;
                        throw new IOException("Unexpected code " + response);
                    }else{
                        if(response.code()==200){
                            ResponseBody responseBody=response.body();
                            String filename=responseBody.string();
                            String jpgToPng=filename.split("\\.")[0]+".png";
                            String url="http://10.0.2.2:5000/downloadImage/"+jpgToPng;
                            Request request1=new Request.Builder()
                                    .url(url)
                                    .build();
                            ResponseBody response1=client.newCall(request1).execute().body();
                            InputStream in =response1.byteStream();
                            final Bitmap bitmap = BitmapFactory.decodeStream(in);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                   transformResult.setImageBitmap(bitmap);
                                   transformResult.setVisibility(View.VISIBLE);
                                }
                            });
                        }

                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}

