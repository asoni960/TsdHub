package com.example.abhinav.tsdhub;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

//import Fragment_Main_page.News_fragment;
import Fragment_Ranking_Page.Fragment_Team;
import Fragment_Ranking_Page.Fragment_ODI;
import adapter.ViewPagerAdapter;
import realm.Bookmark;


public class Ranking_Main extends AppCompatActivity  {
    TabLayout ranking_tabLayout;
    ViewPager ranking_viewPager;
    DrawerLayout main_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ranking_main);

        //navigation drawer
        initNavigationDrawer();

        //app icon
        //getSupportActionBar().setIcon(R.drawable.ic_account_balance_white_18dp);

        ranking_viewPager =(ViewPager) findViewById(R.id.ranking_viewPager);
        setupViewPager(ranking_viewPager);

        ranking_tabLayout= (TabLayout) findViewById(R.id.ranking_tabs);
        if (ranking_tabLayout != null) {
            ranking_tabLayout.setupWithViewPager(ranking_viewPager);
        }


        /*final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }*/
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
                }
                return true;
            }

        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.title_nav);
        tv_email.setText(R.string.name_user);
        main_drawer = (DrawerLayout)findViewById(R.id.ranking_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,main_drawer,R.string.drawer_open,R.string.drawer_close){

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

    private void goto_news() {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    private void goto_schedule() {
        Intent intent = new Intent(this,Schedule_Main.class);
        startActivity(intent);
    }

    //tab layout
    private void setupViewPager(ViewPager ranking_viewPager) {
        ViewPagerAdapter ranking_adapter= new ViewPagerAdapter(getSupportFragmentManager());
        ranking_adapter.addFragment(new Fragment_Team(),"Team");
        ranking_adapter.addFragment(new Fragment_ODI(),"ODI");
        ranking_adapter.addFragment(new Fragment_ODI(),"T20");
        ranking_adapter.addFragment(new Fragment_ODI(),"Test");
        ranking_viewPager.setAdapter(ranking_adapter);
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

          /*  case R.id.action_bookmark:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                goto_bookmark();
                return true;*/

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

        //noinspection SimplifiableIfStatement

    }

    private void goto_bookmark() {
        Intent intent = new Intent(this, Bookmark.class);
        startActivity(intent);
    }

    private void goto_Ranking() {
        Intent intent = new Intent(this, Ranking_Main.class);
        startActivity(intent);
    }

    public void goto2(View view) {
        Intent intent = new Intent(this, Ranking_Main.class);
        startActivity(intent);
    }

}

