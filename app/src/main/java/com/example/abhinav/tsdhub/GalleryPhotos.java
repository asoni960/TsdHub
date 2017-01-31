package com.example.abhinav.tsdhub;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.MainActivityPojo;
import model.MainActivityPojoExtra;
import realm.Bookmark;
import rest.ApiClient;
import rest.ApiInterface;
import rest.CheckNetwork;
import rest.GalleryEndlessRecyclerViewScrollListener;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by abhinav on 19-01-2017.
 */

public class GalleryPhotos extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    Toolbar toolbar;
    protected ProgressBar gProgressBar;
    private DrawerLayout drawerLayout;
    private final static String API_KEY="af54b23df48d97a6868fb4f636cdcd27";
    private RecyclerView galleryRecyclerView;
    private ArrayList<MainActivityPojo> galleryList;
    private GalleryAdapter galleryAdapter;
    int currentPage=1;
    public StaggeredGridLayoutManager gstaggeredGridLayoutManager;
    public LinearLayoutManager glinearLayoutManager;
    private CollapsingToolbarLayout gCollapsingToolbarLayout;
    protected TextView noNetwork;
    private SwipeRefreshLayout swipeRefreshLayout;

    private GalleryEndlessRecyclerViewScrollListener endlessRecyclerViewScroller;
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery);

        noNetwork=(TextView)findViewById(R.id.noNetwork);
        noNetwork.setVisibility(View.GONE);

        swipeRefreshLayout=  (SwipeRefreshLayout)findViewById(R.id.gswipelayout);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        gProgressBar=(ProgressBar)findViewById(R.id.progress_bar);
        gProgressBar.setVisibility(View.GONE);


        //navigation drawer
        initNavigationDrawer();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CheckState();
            }
        });
        //collapsing search bar

        galleryRecyclerView= (RecyclerView)findViewById(R.id.galleryRecyclerView);
        galleryRecyclerView.setHasFixedSize(true);

        glinearLayoutManager=new LinearLayoutManager(getApplicationContext());
        gstaggeredGridLayoutManager=new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        galleryRecyclerView.setLayoutManager(gstaggeredGridLayoutManager);
        galleryRecyclerView.setItemAnimator(new DefaultItemAnimator());
        CheckState();

        endlessRecyclerViewScroller = new GalleryEndlessRecyclerViewScrollListener(gstaggeredGridLayoutManager) {

            @Override
            public void onLoadMore(int page) {
                loadMoreData(currentPage++);
            }

        };

        galleryRecyclerView.addOnScrollListener(endlessRecyclerViewScroller);
 }

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
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_ranking:
                        Toast.makeText(getApplicationContext(),"Ranking", Toast.LENGTH_SHORT).show();
                        goto_Ranking();
                        drawerLayout.closeDrawers();
                        break;
                    case R.id.nav_fixture:
                        goto_schedule();
                    case R.id.nav_gallery:
                        goto_gallery();
                    case R.id.nav_bookmark:
                        gotoBookmark();

                }

                return true;
            }

        });
        View header = navigationView.getHeaderView(0);
        TextView tv_email = (TextView)header.findViewById(R.id.title_nav);
        tv_email.setText(R.string.name_user);
        drawerLayout = (DrawerLayout)findViewById(R.id.main_layout);

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){

            @Override
            public void onDrawerClosed(View v){
                super.onDrawerClosed(v);
            }

            @Override
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
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

            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.search_bar:
                //User search the Item
                //open search activity
                goto_search();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }

        //noinspection SimplifiableIfStatement
    }

    // check fragmnet state
    private void CheckState(){
        if(!CheckNetwork.isConnectionAvailable(getApplicationContext())){
            noNetwork.setVisibility(View.VISIBLE);
            gProgressBar.setVisibility(View.GONE);
            galleryRecyclerView.setVisibility(View.GONE);
           // swipeRefreshLayout.setRefreshing(false);
        }
        else
        {   noNetwork.setVisibility(View.GONE);
            LoadData(currentPage);}
    };
    //Load search data private void LoadData(int currentPage) {
private void LoadData(int currentPage){
    swipeRefreshLayout.setOnRefreshListener(this);
    swipeRefreshLayout.setRefreshing(true);
    galleryRecyclerView.setVisibility(View.GONE);
    gProgressBar.setVisibility(View.VISIBLE);
    swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.BLACK,Color.GREEN);
    final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    final Call<MainActivityPojoExtra> call = apiService.getTopRatedMovies(API_KEY,currentPage);
    call.enqueue(new Callback<MainActivityPojoExtra>() {
        @Override
        public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {
            int statusCode = response.code();
            galleryList= response.body().getResults();
            galleryAdapter = new GalleryAdapter(galleryList, R.layout.gallery_staggered,getApplicationContext());
            galleryRecyclerView.setAdapter(galleryAdapter);
            galleryAdapter.notifyDataSetChanged();
            gProgressBar.setVisibility(View.GONE);
            noNetwork.setVisibility(View.GONE);
            galleryRecyclerView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onFailure(Call<MainActivityPojoExtra> call, Throwable t) {
           //
        }
    });
    swipeRefreshLayout.setRefreshing(false);
}

    private void loadMoreData(int currentPage) {
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<MainActivityPojoExtra> call = apiService.getTopRatedMovies(API_KEY,currentPage);
        call.enqueue(new Callback<MainActivityPojoExtra>() {
            @Override
            public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {
                galleryList.addAll(response.body().getResults());
                galleryAdapter.notifyDataSetChanged();

            }


            @Override
            public void onFailure(Call<MainActivityPojoExtra> call, Throwable t) {
                //nothing
            }
        });
    }

    @Override
    public void onRefresh() {
        //NetworkInfo info= (NetworkInfo)((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(CheckNetwork.isConnectionAvailable(getApplicationContext())){
            LoadData(currentPage);
        }
        else {
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.BLACK,Color.GREEN);
           noNetwork.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
    }
//-----------------------Adapter---------------------------

    public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.Gallery_RecyclerViewHolder>{
        private ArrayList<MainActivityPojo> galleryList;
        private final LayoutInflater inflater;
        private Context context;

        public GalleryAdapter(ArrayList<MainActivityPojo> galleryList, int gallery_staggered, Context context) {
            inflater=LayoutInflater.from(context);
            this.context=context;
            this.galleryList = galleryList;

            }

        @Override
        public Gallery_RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= inflater.inflate(R.layout.gallery_staggered,parent,false);
            Gallery_RecyclerViewHolder holder= new Gallery_RecyclerViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(Gallery_RecyclerViewHolder holder, final int position) {
            final String imagrUrl="http://image.tmdb.org/t/p/w342"+galleryList.get(position).getImagepath();
            Picasso.with(context).load(imagrUrl).into(holder.GMainimage);
            /*holder.Bshare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "https://api.themoviedb.org/3/movie/?api_key=af54b23df48d97a6868fb4f636cdcd27";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HUB");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });*/
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*Intent i= new Intent( context,ArticleMain.class);
                    //create bundle information
                    i.putExtra("newsList", galleryList.get(position));
                    context.startActivity(i);*/
                    Toast.makeText(getApplicationContext(),"clicked"+galleryList.get(position),Toast.LENGTH_SHORT).show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return galleryList.size();
        }

        public class Gallery_RecyclerViewHolder extends RecyclerView.ViewHolder{
            ImageView GMainimage;
            public Gallery_RecyclerViewHolder(View itemView) {
                super(itemView);
                //    Typeface customFontBold= Typeface.createFromAsset(getAssets(),"fonts/JosefinSans-Bold.ttf");
                GMainimage=(ImageView)itemView.findViewById(R.id.galleryImage);
            }
        }
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


