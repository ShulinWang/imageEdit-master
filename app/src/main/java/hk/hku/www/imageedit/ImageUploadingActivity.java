package hk.hku.www.imageedit;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.util.List;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.model.PhotoInfo;
public class ImageUploadingActivity extends AppCompatActivity {

    //图片选择器
    private final int REQUEST_CODE_GALLERY = 1001;
    private Button openButton;
    private ImageView selectResult;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageuplading);
        selectResult=findViewById(R.id.iv_select_image_result);
        openButton = (Button)findViewById(R.id.btn_open_gallery);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GalleryFinal.openGallerySingle(REQUEST_CODE_GALLERY, mOnHanlderResultCallback);
            }
        });
    }

    /**
     * 选取图片后的回调
     */
    private GalleryFinal.OnHanlderResultCallback mOnHanlderResultCallback = new GalleryFinal.OnHanlderResultCallback() {
        @Override
        public void onHanlderSuccess(int reqeustCode, List<PhotoInfo> resultList) {
            if (resultList != null) {
                Log.e("onHanlderSuccess: ", resultList.get(0).getPhotoPath());
                uri=Uri.fromFile(new File(resultList.get(0).getPhotoPath()));
                selectResult.setImageURI(uri);

            }
        }

        @Override
        public void onHanlderFailure(int requestCode, String errorMsg) {
            Log.e("onHanlderSuccess: ", errorMsg);
        }
    };
}
