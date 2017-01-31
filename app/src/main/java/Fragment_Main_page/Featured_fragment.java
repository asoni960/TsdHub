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
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import model.Featured_Card_Data;
import model.MainActivityPojo;
import model.MainActivityPojoExtra;
import realm.AddBookmark;
import realm.TargetImage;
import rest.ApiClient;
import rest.ApiInterface;
import rest.CheckNetwork;
import rest.EndlessRecyclerViewScrollListener;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by abhinav on 14-06-2016.
 */
public class Featured_fragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public Featured_fragment() {
 
    }
    private static final String TAG = "Tab 2";
    private  final static String API_KEY="af54b23df48d97a6868fb4f636cdcd27";
    private ArrayList<MainActivityPojo> featuredList;
    private RecyclerView featuredRv;
    Featured_RecyclerAdapter featuredRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    int currentPage=1;

    private SwipeRefreshLayout swipeRefreshLayout;
    protected ProgressBar nProgressBar;
    protected TextView network;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.main_fragment_featured,container,false);
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.main_swipelayout);

        featuredRv= (RecyclerView)v.findViewById(R.id.featured_recycler_view);
        featuredRv.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getContext());

        nProgressBar=(ProgressBar)v.findViewById(R.id.loadingSpinner);
        network=(TextView)v.findViewById(R.id.network);

        network.setVisibility(View.GONE);
        nProgressBar.setVisibility(View.GONE);
        featuredRv.setVisibility(View.GONE);
        CheckState();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CheckState();
            }
        });

        featuredRv.setLayoutManager(mLayoutManager);
        featuredRv.setItemAnimator(new DefaultItemAnimator());

        featuredRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage, int totalItemCount, RecyclerView view) {
                loadMoreData(currentPage++);
            }
        });
        return v;
    }

    // check fragmnet state
    private void CheckState(){
        if(!CheckNetwork.isConnectionAvailable(getContext())){
            network.setVisibility(View.VISIBLE);
            nProgressBar.setVisibility(View.GONE);
            featuredRv.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
        else
        {   network.setVisibility(View.GONE);
            LoadData(currentPage);}
    };

    private void LoadData(int currentPage) {

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        featuredRv.setVisibility(View.GONE);
        nProgressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.BLACK,Color.GREEN);
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<MainActivityPojoExtra> call = apiService.getTopRatedMovies(API_KEY,currentPage);
        call.enqueue(new Callback<MainActivityPojoExtra>() {
            @Override
            public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {
                featuredList= response.body().getResults();
                featuredRecyclerAdapter = new Featured_RecyclerAdapter(featuredList, R.layout.main_fragment_featured_card,getActivity().getApplicationContext());
                featuredRv.setAdapter(featuredRecyclerAdapter);
                featuredRecyclerAdapter.notifyDataSetChanged();
                nProgressBar.setVisibility(View.GONE);
                network.setVisibility(View.GONE);
                featuredRv.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<MainActivityPojoExtra> call, Throwable t) {
                Log.e(TAG, t.toString());
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
                featuredList.addAll(response.body().getResults());
                featuredRecyclerAdapter.notifyDataSetChanged();

            }


            @Override
            public void onFailure(Call<MainActivityPojoExtra> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public void onRefresh() {
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

/// Adapter Data


    ////Adapter class
    private class Featured_RecyclerAdapter extends RecyclerView.Adapter<Featured_RecyclerViewHolder> {

       ArrayList<MainActivityPojo> featuredList;
        Context fcontext;
        LayoutInflater finflater;

        public Featured_RecyclerAdapter(ArrayList<MainActivityPojo> featuredList, int main_fragment_featured_card, Context context) {

            this.fcontext=context;
            finflater=LayoutInflater.from(context);
            this.featuredList=featuredList;
        }


        @Override
        public Featured_RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = finflater.inflate(R.layout.main_fragment_featured_card, parent, false);
            Featured_RecyclerViewHolder viewHolder = new Featured_RecyclerViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final Featured_RecyclerViewHolder holder, final int position) {


            holder.title.setText(featuredList.get(position).getCardtitle());
            holder.timestamp.setText(featuredList.get(position).getTimestamp());
            final String imagrUrl="http://image.tmdb.org/t/p/w342"+featuredList.get(position).getImagepath();
            Picasso.with(fcontext).load(imagrUrl).resize(334,500).onlyScaleDown().into(holder.Main_image);

            holder.share_button.setOnClickListener(shareListener);
            holder.share_button.setTag(holder);
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddBookmark.add(getContext(),featuredList.get(position));
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //passing data to article_content
                    Intent i= new Intent(getActivity(), ArticleMain.class);
                    //create bundle information
                    i.putExtra("newsList", featuredList.get(position));
                    Featured_fragment.this.startActivity(i);
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
                Featured_fragment.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        };

        @Override
        public int getItemCount() {
            return featuredList.size();
        }
    }


    ///View holder Custom Class
    public class  Featured_RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title, timestamp;
        ImageView Main_image,share_button, add;
        RecyclerView rv;
        public Featured_RecyclerViewHolder(View itemView){
            super(itemView);
            Typeface customFontBold= Typeface.createFromAsset(getActivity().getAssets(),"fonts/JosefinSans-Bold.ttf");

            rv=(RecyclerView)itemView.findViewById(R.id.featured_recycler_view);
            timestamp=(TextView)itemView.findViewById(R.id.featured_time_stamp);
            Main_image=(ImageView)itemView.findViewById(R.id.featured_card_image);
            title=(TextView)itemView.findViewById(R.id.featured_card_title);
            share_button=(ImageView)itemView.findViewById(R.id.featured_share_button);
            add=(ImageView)itemView.findViewById(R.id.badButton);
            timestamp.setTypeface(customFontBold);
            title.setTypeface(customFontBold);

        }
    }

}
