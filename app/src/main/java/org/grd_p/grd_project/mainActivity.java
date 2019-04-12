package org.grd_p.grd_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class mainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = getIntent();
        String user_id = intent.getExtras().getString("user_id");

        final TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.addTab(tabLayout.newTab());
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPager viewPager = findViewById(R.id.container);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),user_id);
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


    }
}
