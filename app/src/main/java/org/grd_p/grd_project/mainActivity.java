package org.grd_p.grd_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import org.grd_p.grd_project.Firebase.Constants;

public class mainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPager viewPager = (ViewPager)findViewById(R.id.container);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.user_b);
        tabLayout.getTabAt(1).setIcon(R.drawable.analytics);
        tabLayout.getTabAt(2).setIcon(R.drawable.video);
        tabLayout.getTabAt(3).setIcon(R.drawable.settings);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                tabLayout.getTabAt(0).setIcon(R.drawable.user);
                tabLayout.getTabAt(1).setIcon(R.drawable.analytics);
                tabLayout.getTabAt(2).setIcon(R.drawable.video);
                tabLayout.getTabAt(3).setIcon(R.drawable.settings);

                switch (i){
                    case 0:
                        tabLayout.getTabAt(0).setIcon(R.drawable.user_b);
                        break;
                    case 1:
                        tabLayout.getTabAt(1).setIcon(R.drawable.analytics_b);
                        break;
                    case 2:
                        tabLayout.getTabAt(2).setIcon(R.drawable.video_b);
                        break;
                    case 3:
                        tabLayout.getTabAt(3).setIcon(R.drawable.settings_b);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);

            mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);

            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotificationManager.createNotificationChannel(mChannel);
        }
    }
}
