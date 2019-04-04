package org.grd_p.grd_project.mainFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;

import org.grd_p.grd_project.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Fragment_main extends Fragment{
    ImageView posture;
    Button refresh;
    TextView alarm_msg;
    TextView display_time;
    String img_url;

    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd HH:mm");
    String[] posture_alarm;
    int i=1;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main,container,false);
        posture = rootView.findViewById(R.id.imageView);
        img_url="http://101.101.163.32/image";

        alarm_msg = rootView.findViewById(R.id.alarm_msg);
        display_time = rootView.findViewById(R.id.display_time);

        posture_alarm = getResources().getStringArray(R.array.posture_alarm);

        ImageLoaderUtility.getInstance().initImageLoader(getContext());
        ImageLoader.getInstance().displayImage(img_url, posture, ImageLoaderUtility.getInstance().getProfileImageOptions(), new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                Log.d("DBGLOG","onLoadingStarted");
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                Log.d("DBGLOG","onLoadingFailed :"+failReason);
                display_time.setText("Fail to load Image :( \nPlease try again..!");
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                Log.d("DBGLOG","onLoadingComplete");
                // 이미지 받아온 시간 textView에 반영
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                String getTime = simpleDateFormat.format(date);

                display_time.setText("Last update time: "+getTime);
                // 자세 상태 textView 변경
                alarm_msg.setText(posture_alarm[0]); //서버에서 받아온 값으로 설정해야
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                Log.d("DBGLOG","onLoadingCancelled");

            }
        });

        refresh = rootView.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageLoader.getInstance().displayImage(img_url, posture, ImageLoaderUtility.getInstance().getProfileImageOptions(), new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        Log.d("DBGLOG","onLoadingStarted by Button");
                    }

                    @Override
                    public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                        Log.d("DBGLOG","onLoadingFailed by Button :"+failReason);
                        display_time.setText("Fail to load Image :( \nPlease try again..!");
                    }

                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        Log.d("DBGLOG","onLoadingComplete by Button");
                        // 이미지 받아온 시간 textView에 반영
                        long now = System.currentTimeMillis();
                        Date date = new Date(now);
                        String getTime = simpleDateFormat.format(date);

                        display_time.setText("Last update time: "+getTime);
                        // 자세 상태 textView 변경
                        if(i==6)
                            i=0;
                        alarm_msg.setText(posture_alarm[i++]); //서버에서 받아온 값으로 설정해야
                    }

                    @Override
                    public void onLoadingCancelled(String imageUri, View view) {
                        Log.d("DBGLOG","onLoadingCancelled");

                    }
                });
            }
        });

        return rootView;
    }
}
class ImageLoaderUtility{
    private static ImageLoaderUtility instance;
    private ImageLoaderUtility(){}

    public static ImageLoaderUtility getInstance() {
        if (instance == null) {
            instance = new ImageLoaderUtility();
        }
        return instance;
    }
    public void initImageLoader(Context context){
        if(!ImageLoader.getInstance().isInited()){
            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .threadPriority(Thread.NORM_PRIORITY-2)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCacheSize(50*1024*1024)
                    .tasksProcessingOrder(QueueProcessingType.LIFO)
                    .writeDebugLogs()
                    .build();
            ImageLoader.getInstance().init(config);
        }
    }
    public DisplayImageOptions getDefaultOptions(){
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .build();
    }
    public DisplayImageOptions getProfileImageOptions(){
        return new DisplayImageOptions.Builder()
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageForEmptyUri(R.drawable.loading)
                .showImageOnFail(R.drawable.loading)
                .build();
    }
    public void removeFromCache(String imageUri){
        DiskCacheUtils.removeFromCache(imageUri, ImageLoader.getInstance().getDiskCache());
        MemoryCacheUtils.removeFromCache(imageUri,ImageLoader.getInstance().getMemoryCache());
    }

}
