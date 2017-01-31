package Fragment_Main_page;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhinav.tsdhub.ArticleMain;
import com.example.abhinav.tsdhub.MainActivity;
import com.example.abhinav.tsdhub.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.MainActivityPojo;
import model.MainActivityPojoExtra;
import model.Trending_Card_Data;
import realm.AddBookmark;
import rest.ApiClient;
import rest.ApiInterface;
import rest.CheckNetwork;
import rest.EndlessRecyclerViewScrollListener;
import retrofit2.Call;
import retrofit2.Callback;

import rest.Spinner;
/**
 * Created by abhinav on 14-06-2016.
 */
public class Trending_fragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "Tab 3";
    private  final static String API_KEY="af54b23df48d97a6868fb4f636cdcd27";
    RecyclerView trendingRecyclerView;
    Trending_RecyclerAdapter trendingRecyclerAdapter;
    private ArrayList<MainActivityPojo> trendingList;
    int currentPage=1;
    public LinearLayoutManager mLayoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;
    protected ProgressBar nProgressBar;
    protected TextView network;

    public Trending_fragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.main_fragment_trending, container, false);
        trendingRecyclerView= (RecyclerView)v.findViewById(R.id.trending_recycler_view);
        trendingRecyclerView.setHasFixedSize(true);

        mLayoutManager=new LinearLayoutManager(getContext());
        trendingRecyclerView.setLayoutManager(mLayoutManager);
        trendingRecyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.main_swipelayout);
        nProgressBar=(ProgressBar)v.findViewById(R.id.loadingSpinner);
        network=(TextView)v.findViewById(R.id.network);


        network.setVisibility(View.GONE);
        nProgressBar.setVisibility(View.GONE);
        trendingRecyclerView.setVisibility(View.GONE);
        CheckState();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CheckState();
            }
        });
        trendingRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage, int totalItemCount, RecyclerView view) {
                nProgressBar.setVisibility(View.VISIBLE);
                loadMoreData(currentPage++);
            }
        });
        return v;
    }

    // check fragmnet state
    private void CheckState(){
        if(CheckNetwork.isConnectionAvailable(getContext())==false){
            network.setVisibility(View.VISIBLE);
            nProgressBar.setVisibility(View.GONE);
            trendingRecyclerView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
        else
        {   network.setVisibility(View.GONE);
            LoadData(currentPage);}
    };

    private void LoadData(int currentPage) {
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        trendingRecyclerView.setVisibility(View.GONE);
        nProgressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.BLACK,Color.GREEN);
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<MainActivityPojoExtra> call = apiService.getUpcomingMovies(API_KEY,currentPage);
        call.enqueue(new Callback<MainActivityPojoExtra>() {
            @Override
            public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {
                trendingList= response.body().getResults();

                trendingRecyclerAdapter = new Trending_RecyclerAdapter(trendingList, R.layout.main_fragment_trending_card,getActivity().getApplicationContext());
                trendingRecyclerView.setAdapter(trendingRecyclerAdapter);
                trendingRecyclerAdapter.notifyDataSetChanged();
                nProgressBar.setVisibility(View.GONE);
                network.setVisibility(View.GONE);
                trendingRecyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MainActivityPojoExtra> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
        swipeRefreshLayout.setRefreshing(false);
    }


    //load endless data
    private void loadMoreData(int page) {
        Log.e("second url page", String.valueOf(page));
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<MainActivityPojoExtra> call = apiService.getUpcomingMovies(API_KEY,page);
        call.enqueue(new Callback<MainActivityPojoExtra>() {
            @Override
            public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {
                trendingList.addAll(response.body().getResults());
                trendingRecyclerAdapter.notifyDataSetChanged();
                nProgressBar.setVisibility(View.GONE);

            }


            @Override
            public void onFailure(Call<MainActivityPojoExtra> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

    }

    //refresh function
    @Override
    public void onRefresh()
    {

        //NetworkInfo info= (NetworkInfo)((ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(CheckNetwork.isConnectionAvailable(getContext())){
            LoadData(currentPage);
        }
        else {
            swipeRefreshLayout.setRefreshing(true);
            swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.BLACK,Color.GREEN);
            Toast.makeText(getActivity(),"No Internet connection",Toast.LENGTH_SHORT).show();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private class Trending_RecyclerAdapter extends RecyclerView.Adapter<Trending_RecyclerViewHolder> {

        private ArrayList<MainActivityPojo> trendingList;
        Context tcontext;
        LayoutInflater tinflater;

        public Trending_RecyclerAdapter(ArrayList<MainActivityPojo> trendingList,int main_trending_fragment_card, Context context) {

            this.tcontext=context;
            tinflater=LayoutInflater.from(context);
            this.trendingList= trendingList;
        }


        @Override
        public Trending_RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = tinflater.inflate(R.layout.main_fragment_trending_card, parent, false);
            Trending_RecyclerViewHolder viewHolder = new Trending_RecyclerViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final Trending_RecyclerViewHolder holder, final int position) {
            
            holder.title.setText(trendingList.get(position).getCardtitle());
            holder.timestamp.setText(trendingList.get(position).getTimestamp());
            String imagrUrl="http://image.tmdb.org/t/p/w342"+trendingList.get(position).getImagepath();
            Picasso.with(tcontext).load(imagrUrl).resize(334,500).onlyScaleDown().into(holder.Main_image);
            holder.share_button.setOnClickListener(shareListener);
            holder.share_button.setTag(holder);
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddBookmark.add(getContext(),trendingList.get(position));
                }
            });
            
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i= new Intent(getActivity(), ArticleMain.class);
                    //create bundle information
                    i.putExtra("newsList", trendingList.get(position));
                    Trending_fragment.this.startActivity(i);
                }
            });

        }
        //share button
        View.OnClickListener shareListener= new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://api.themoviedb.org/3/movie/?api_key=af54b23df48d97a6868fb4f636cdcd27";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HUB");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                Trending_fragment.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        };

        @Override
        public int getItemCount() {
            return trendingList.size();
        }
    }


    ///View holder Custom Class
    public class  Trending_RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title, timestamp;
        ImageView Main_image,share_button,add;
        RecyclerView rv;

        public Trending_RecyclerViewHolder(View itemView){
            super(itemView);
            Typeface customFontBold= Typeface.createFromAsset(getActivity().getAssets(),"fonts/JosefinSans-Bold.ttf");

            rv= (RecyclerView)itemView.findViewById(R.id.trending_recycler_view);
            timestamp=(TextView)itemView.findViewById(R.id.trending_time_stamp);
            Main_image=(ImageView)itemView.findViewById(R.id.trending_card_image);
            title=(TextView)itemView.findViewById(R.id.trending_card_title);
            share_button=(ImageView)itemView.findViewById(R.id.trending_share_button);
            add=(ImageView)itemView.findViewById(R.id.badButton);
            timestamp.setTypeface(customFontBold);
            title.setTypeface(customFontBold);

        }
    }

}
