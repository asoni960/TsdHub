package com.example.abhinav.tsdhub;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;


import Fragment_Main_page.Current_fragment;
import Fragment_Main_page.Featured_fragment;
import Fragment_Main_page.News_fragment;
import Fragment_Main_page.Trending_fragment;
import adapter.ViewPagerAdapter;
import realm.Bookmark;

public class MainActivity extends AppCompatActivity{
    //variables
     Toolbar main_toolbar;
     TabLayout main_tabLayout;
     ViewPager main_viewPager;
     DrawerLayout main_drawer;
     private CollapsingToolbarLayout mCollapsingToolbarLayout;
     String name;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        setContentView(R.layout.activity_main);

        main_toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(main_toolbar);

        //navigation drawer
        initNavigationDrawer();
        mCollapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.mainActivityCollapsingBar);
        mCollapsingToolbarLayout.setTitleEnabled(false);
        main_viewPager =(ViewPager) findViewById(R.id.main_viewPager);
        name ="Current";
        setupViewPager(main_viewPager, name);


        main_tabLayout= (TabLayout) findViewById(R.id.main_tabs);
        if (main_tabLayout != null) {
            main_tabLayout.setupWithViewPager(main_viewPager);
        }

       // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    }





//////navigation bar
    private void initNavigationDrawer() {
        NavigationView navigationView =(NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id){
                    case R.id.nav_news:
                        Toast.makeText(getApplicationContext(),"News",Toast.LENGTH_SHORT).show();
                        goto_news();
                        main_drawer.closeDrawers();
                        break;
                    case R.id.nav_ranking:
                        Toast.makeText(getApplicationContext(),"Ranking", Toast.LENGTH_SHORT).show();
                        goto_Ranking();
                        main_drawer.closeDrawers();
                        break;
                    case R.id.nav_fixture:
                        goto_schedule();
                    case R.id.nav_gallery:
                        goto_gallery();

                        }

                return true;
                }

        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.title_nav);
        Typeface customFontBold= Typeface.createFromAsset(getAssets(),"fonts/JosefinSans-Bold.ttf");
        tv_email.setTypeface(customFontBold);
        tv_email.setText(R.string.name_user);
        main_drawer = (DrawerLayout)findViewById(R.id.main_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,main_drawer,main_toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        main_drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    //tab layout
    private void setupViewPager(ViewPager main_viewPager, String name) {
        ViewPagerAdapter adapter= new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new News_fragment(),"News");
        adapter.addFragment(new Trending_fragment(),"Trending");
        adapter.addFragment(new Featured_fragment(),"Featured");
        adapter.addFragment(new Current_fragment(),name);
        main_viewPager.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
       inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

           case R.id.search_bar:
               //User search the Item
               //open search activity
                goto_search();
                return true;
            case R.id.action_bookmark:
                // User chose the "Bookmark" action, mark the current item
                // as a Bookmark...
               gotoBookmark();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

        //noinspection SimplifiableIfStatement

    }

    private void goto_search() {
        Intent intent = new Intent(this, ArticleSearch.class);
        startActivity(intent);
    }

    private void goto_Ranking() {
        Intent intent = new Intent(this, Ranking_Main.class);
        startActivity(intent);
    }

    public void gotoBookmark() {
        Intent intent = new Intent(this, Bookmark.class);
        startActivity(intent);
    }

    private void goto_gallery() {
        Intent intent = new Intent(this,GalleryPhotos.class);
        startActivity(intent);
    }

    private void goto_news() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void goto_schedule() {
        Intent intent = new Intent(this,Schedule_Main.class);
        startActivity(intent);
    }

    }

