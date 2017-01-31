package com.example.abhinav.tsdhub;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.squareup.picasso.Picasso;

import model.MovieBookmarkModel;
import realm.AddBookmark;
import realm.Bookmark;
import model.MainActivityPojo;


/**
 * Created by abhinav on 29-06-2016.
 */
public class ArticleMain extends AppCompatActivity {
    private DrawerLayout article_drawer;
    private Toolbar article_toolbar;
    private KenBurnsView aHeadImage;
    public AppBarLayout appBarLayout;
    MainActivityPojo mainActivityPojo;
    public CollapsingToolbarLayout collapsingToolbarLayout;

    ImageView aishare, aibookmark, aiview;
    TextView aititle,aiview_count,aitimestamp,aicontent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.article_head);
        appBarLayout=(AppBarLayout)findViewById(R.id.article_appbar);
        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.article_collapsebar);
        article_toolbar=(Toolbar)findViewById(R.id.article_toolbar);
        setSupportActionBar(article_toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Bundle b = getIntent().getExtras();
        mainActivityPojo= new MainActivityPojo();
        mainActivityPojo = b.getParcelable("newsList");

        //typeface
        Typeface customFontBold= Typeface.createFromAsset(getAssets(),"fonts/JosefinSans-Bold.ttf");
        Typeface customFontRegular= Typeface.createFromAsset(getAssets(),"fonts/JosefinSans-Regular.ttf");

//  recycler view
        article_drawer = (DrawerLayout) findViewById(R.id.article_drawer_layout);
        aHeadImage = (KenBurnsView) findViewById(R.id.article_image);
        aishare=(ImageView)findViewById(R.id.aiShare);
        aibookmark=(ImageView)findViewById(R.id.aiBookamrk);
        aiview=(ImageView)findViewById(R.id.aiViewImage);
        aititle=(TextView)findViewById(R.id.article_title);
        aitimestamp=(TextView)findViewById(R.id.aiTimeStamp);
        aiview_count=(TextView)findViewById(R.id.aiViewText);
        aicontent=(TextView)findViewById(R.id.article_content);

        //setting data
        aititle.setText(mainActivityPojo.getCardtitle());
        aicontent.setText(mainActivityPojo.getContent());
        aitimestamp.setText(mainActivityPojo.getTimestamp());
        aiview_count.setText(mainActivityPojo.getVoteCount());

        aititle.setTypeface(customFontBold);
        aitimestamp.setTypeface(customFontBold);
        aiview_count.setTypeface(customFontRegular);
        aicontent.setTypeface(customFontRegular);

        aibookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddBookmark.add(getApplicationContext(),mainActivityPojo);
            }
        });
       aishare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://api.themoviedb.org/3/movie/?api_key=af54b23df48d97a6868fb4f636cdcd27";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HUB");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        //PICASOOO AND PALETTE
        String imagrUrl = "http://image.tmdb.org/t/p/w342" + mainActivityPojo.getBackPoster();
        Picasso.with(ArticleMain.this).load(imagrUrl).resize(640,960).onlyScaleDown().into(aHeadImage);
 /*       Picasso.with(ArticleMain.this).load(obj.getImagepath()).resize(334,500).onlyScaleDown().into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                assert aHeadImage!=null;
                aHeadImage.setImageBitmap(bitmap);
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch textSwatch= palette.getMutedSwatch();
                        if (textSwatch==null){
                            Toast.makeText(ArticleMain.this,"Null Swatch",  Toast.LENGTH_SHORT).show();
                            return;
                        }
                       mCollapsingToolbarLayout.setStatusBarScrimColor(textSwatch.getRgb());
                        mCollapsingToolbarLayout.setContentScrimColor(textSwatch.getRgb());

                    }
                });
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });*/

        //navigation drawer
        //initNavigationDrawer();

    }

    //////navigation bar
    private void initNavigationDrawer() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.nav_news:
                        Toast.makeText(getApplicationContext(), "News", Toast.LENGTH_SHORT).show();
                        goto_news();
                        article_drawer.closeDrawers();
                        break;
                    case R.id.nav_ranking:
                        Toast.makeText(getApplicationContext(), "Ranking", Toast.LENGTH_SHORT).show();
                        goto_Ranking();
                        article_drawer.closeDrawers();
                        break;
                    case R.id.nav_fixture:
                        goto_schedule();
                }
                return true;
            }

        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView) header.findViewById(R.id.title_nav);
        tv_email.setText(R.string.name_user);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, article_drawer, article_toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        article_drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    private void goto_news() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void goto_schedule() {
        Intent intent = new Intent(this, Schedule_Main.class);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
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
                //user search Item
                goto_search();
                return true;
            case android.R.id.home:
                onBackPressed();
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

    private void gotoBookmark() {
        Intent intent = new Intent(this, Bookmark.class);
        startActivity(intent);
    }

    private void goto_Ranking() {
        Intent intent = new Intent(this, Ranking_Main.class);
        startActivity(intent);
    }

    public void goto_news(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
    private void goto_search() {
        Intent intent = new Intent(this, ArticleSearch.class);
        startActivity(intent);
    }
}