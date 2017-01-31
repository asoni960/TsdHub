package com.example.abhinav.tsdhub;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.MainActivityPojo;
import model.MainActivityPojoExtra;
import rest.ApiClient;
import rest.ApiInterface;
import rest.CheckNetwork;
import rest.SearchEndlessRecyclerViewScrollListener;
import retrofit2.Call;
import retrofit2.Callback;

import static java.security.AccessController.getContext;

/**
 * Created by abhinav on 19-12-2016.
 */

public class ArticleSearch extends AppCompatActivity {
    Toolbar toolbarSearch;
    protected ProgressBar sProgressBar;
    private final static String API_KEY="af54b23df48d97a6868fb4f636cdcd27";
    private RecyclerView searchRecyclerView;
    private ArrayList<MainActivityPojo> searchList;
    private Search_RecyclerAdapter searchRecyclerAdapter;
    int currentPage=1;
    public LinearLayoutManager sLayoutManager;
    private CollapsingToolbarLayout sCollapsingToolbarLayout;

    public SearchView searchView;
    protected TextView noSearch, noNetwork;
    protected MenuItem searchMenuItem;

    private SearchEndlessRecyclerViewScrollListener endlessRecyclerViewScroller;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_search);

        noSearch=(TextView)findViewById(R.id.noResult);
        noNetwork=(TextView)findViewById(R.id.noNetwork);
        noNetwork.setVisibility(View.GONE);
        noSearch.setVisibility(View.GONE);



        toolbarSearch= (Toolbar) findViewById(R.id.search_toolbar);
        setSupportActionBar(toolbarSearch);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        sProgressBar=(ProgressBar)findViewById(R.id.progress_bar);
        sProgressBar.setVisibility(View.GONE);

        //collapsing search bar
        sCollapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.searchCollapsingBar);
        sCollapsingToolbarLayout.setTitleEnabled(false);

        searchRecyclerView= (RecyclerView)findViewById(R.id.search_recyclerView);
        searchRecyclerView.setHasFixedSize(true);

        sLayoutManager=new LinearLayoutManager(getApplicationContext());
        searchRecyclerView.setLayoutManager(sLayoutManager);
        searchRecyclerView.setItemAnimator(new DefaultItemAnimator());
        NetworkInfo info= ((ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(info==null){
           noNetwork.setVisibility(View.VISIBLE);
           searchRecyclerView.setVisibility(View.GONE);
        }

        endlessRecyclerViewScroller = new SearchEndlessRecyclerViewScrollListener(sLayoutManager) {

            @Override
            public void onLoadMore(int currentPage,String query, int totalItemCount, RecyclerView view) {
                //  query=
                Log.i("List Items", String.valueOf(searchList.size()));
                LoadMoreSearchData(currentPage,query);
            }
        };

        searchRecyclerView.addOnScrollListener(endlessRecyclerViewScroller);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        searchMenuItem= menu.findItem(R.id.action_search);
        searchView=(SearchView) searchMenuItem.getActionView();
        searchView.onActionViewExpanded();
        searchView.setQueryHint("\uD83D\uDD0D Circle of Cricket");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String newText) {
                Toast.makeText(getApplicationContext(), newText, Toast.LENGTH_SHORT).show();
   //sending search query
                searchRecyclerView.setVisibility(View.GONE);
                noNetwork.setVisibility(View.GONE);
                noSearch.setVisibility(View.GONE);
                sProgressBar.setVisibility(View.VISIBLE);
                LoadSearchData(currentPage,newText);

                endlessRecyclerViewScroller.setQuery(newText);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // newText is text entered by user to SearchView
                //for static data
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Load search data
    private void LoadSearchData(int currentPage, String query) {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<MainActivityPojoExtra> call = apiService.getSearchMovieData(API_KEY,query,currentPage);
        call.enqueue(new Callback<MainActivityPojoExtra>() {
            @Override
            public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {
                int statusCode = response.code();
                Toast.makeText(getApplicationContext(),"this is wht"+statusCode,Toast.LENGTH_SHORT).show();
                searchList= response.body().getResults();
                Log.i("first list size", String.valueOf(searchList.size()));

                int f=searchList.size();
                if (f<=0){
                    noSearch.setVisibility(View.VISIBLE);
                    searchRecyclerView.setVisibility(View.GONE);}
                else{
                searchRecyclerAdapter = new Search_RecyclerAdapter(searchList, R.layout.a_search_cardview,getApplicationContext());
                searchRecyclerView.setAdapter(searchRecyclerAdapter);
                searchRecyclerAdapter.notifyDataSetChanged();
                noSearch.setVisibility(View.GONE);
                searchRecyclerView.setVisibility(View.VISIBLE);
                }
                sProgressBar.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<MainActivityPojoExtra> call, Throwable t) {
                Log.e("failed", t.toString());
            }
        });
    }

    //Load more search Data
    private void LoadMoreSearchData(int Page, String query) {

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Log.v("onLoadMore Query", query);
        final Call<MainActivityPojoExtra> call = apiService.getSearchMovieData(API_KEY,query,Page);
        call.enqueue(new Callback<MainActivityPojoExtra>() {
            @Override
            public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {
                Log.d("after in function", String.valueOf(response.body().getResults()));
                searchList.addAll(response.body().getResults());
                searchRecyclerAdapter.notifyDataSetChanged();

            }


            @Override
            public void onFailure(Call<MainActivityPojoExtra> call, Throwable t) {
                Log.e("Failed", t.toString());
            }
        });
    }

    public class Search_RecyclerAdapter extends RecyclerView.Adapter<Search_RecyclerAdapter.Search_RecyclerViewHolder> {
        private List<MainActivityPojo> searchList;
        Context context;

        public Search_RecyclerAdapter(List<MainActivityPojo> searchList, int a_search_cardview, Context context){
            this.context = context;
            this.searchList=searchList;
        }


        @Override
        public Search_RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View v= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.a_search_cardview, viewGroup, false);
            Search_RecyclerViewHolder viewHolder=new Search_RecyclerViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(Search_RecyclerViewHolder holder, final int position) {

            holder.s_Title.setText(searchList.get(position).getCardtitle());
            holder.s_Timestamp.setText(searchList.get(position).getTimestamp());
            String imageUrl="http://image.tmdb.org/t/p/w342"+searchList.get(position).getImagepath();
            Picasso.with(context).load(imageUrl).resize(334,500).onlyScaleDown().into(holder.s_Image);
            holder.s_ShareButton.setOnClickListener(shareListener);
            holder.s_ShareButton.setTag(holder);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i= new Intent(getApplicationContext(),ArticleMain.class);
                    i.putExtra("newsList",searchList.get(position));
                    startActivity(i);
                }
            });

        }

        //share button
        View.OnClickListener shareListener= new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://api.themoviedb.org/3/movie/?api_key=af54b23df48d97a6868fb4f636cdcd27";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HUB");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent,"Share via"));
            }
        };

        @Override
        public int getItemCount() {
            return searchList.size();
        }

        protected class Search_RecyclerViewHolder extends RecyclerView.ViewHolder {
            protected TextView s_Title, s_Timestamp;
            protected ImageView s_Image,s_logo, s_ShareButton;
            public Search_RecyclerViewHolder(View v) {
                super(v);
                this.s_Image=(ImageView)v.findViewById(R.id.search_cardImage);
                this.s_logo=(ImageView)v.findViewById(R.id.search_cocLogo);
                this.s_ShareButton=(ImageView)v.findViewById(R.id.search_shareButton);
                this.s_Title=(TextView)v.findViewById(R.id.search_cardTitle);
                this.s_Timestamp=(TextView)v.findViewById(R.id.search_cardTimestamp);
            }
        }
    }
}
