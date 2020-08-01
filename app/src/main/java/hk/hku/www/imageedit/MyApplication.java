package hk.hku.www.imageedit;

/*
@author https://github.com/pengjianbo/GalleryFinal
 */
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import cn.finalteam.galleryfinal.BuildConfig;
import cn.finalteam.galleryfinal.CoreConfig;
import cn.finalteam.galleryfinal.FunctionConfig;
import cn.finalteam.galleryfinal.GalleryFinal;
import cn.finalteam.galleryfinal.ImageLoader;
import cn.finalteam.galleryfinal.ThemeConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        com.nostra13.universalimageloader.core.ImageLoader imageLoader = com.nostra13.universalimageloader.core.ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        //set the theme of our application
        //ThemeConfig.CYAN
        ColorDrawable colorDrawable=(ColorDrawable) getDrawable(R.drawable.color_drawable);
        ThemeConfig theme = new ThemeConfig.Builder()
                .setIconCrop(R.drawable.ic_crop)
                .setIconCamera(R.drawable.ic_camera)
                .setIconRotate(R.drawable.ic_rotate)
                .setEditPhotoBgTexture(colorDrawable)
                .build();
        //set the function of our appication
        FunctionConfig functionConfig = new FunctionConfig.Builder()
                .setEnableCamera(true)
                .setEnableEdit(true)
                .setEnableCrop(true)
                .setEnableRotate(true)
                .setCropSquare(true)
                .setEnableRotate(true)
                .setEnablePreview(true)
                .build();
        //set the imageloader
        ImageLoader imageloader = new UILImageLoader();
        //set the cor configuration of imageloader
        CoreConfig coreConfig = new CoreConfig.Builder(this, imageloader, theme)
                .setDebug(BuildConfig.DEBUG)
                .setFunctionConfig(functionConfig)
                .setNoAnimcation(false)
                .build();
        GalleryFinal.init(coreConfig);
    }
}
