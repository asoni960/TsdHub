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
 * Created by abhinav on 19-01-2017.
 */
public class Current_fragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public Current_fragment() {

    }
    private static final String TAG = "Tab 4";
    private  final static String API_KEY="af54b23df48d97a6868fb4f636cdcd27";
    private ArrayList<MainActivityPojo> currentList;
    private RecyclerView currentRv;
    Current_RecyclerAdapter currentRecyclerAdapter;
    private LinearLayoutManager mLayoutManager;
    int currentPage=1;

    private SwipeRefreshLayout swipeRefreshLayout;
    protected ProgressBar nProgressBar;
    protected TextView network;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TAG", TAG);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.main_fragment_current,container,false);
        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.main_swipelayout);

        currentRv= (RecyclerView)v.findViewById(R.id.current_recycler_view);
        currentRv.setHasFixedSize(true);
        mLayoutManager=new LinearLayoutManager(getContext());

        nProgressBar=(ProgressBar)v.findViewById(R.id.loadingSpinner);
        network=(TextView)v.findViewById(R.id.network);

        network.setVisibility(View.GONE);
        nProgressBar.setVisibility(View.GONE);
        currentRv.setVisibility(View.GONE);
        CheckState();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CheckState();
            }
        });

        currentRv.setLayoutManager(mLayoutManager);
        currentRv.setItemAnimator(new DefaultItemAnimator());

        currentRv.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
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
            currentRv.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
        else
        {   network.setVisibility(View.GONE);
            LoadData(currentPage);}
    };

    private void LoadData(int currentPage) {

        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setRefreshing(true);
        currentRv.setVisibility(View.GONE);
        nProgressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.BLACK,Color.GREEN);
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<MainActivityPojoExtra> call = apiService.getTopTv(API_KEY,currentPage);
        call.enqueue(new Callback<MainActivityPojoExtra>() {
            @Override
            public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {
                currentList= response.body().getResults();
                currentRecyclerAdapter = new Current_RecyclerAdapter(currentList, R.layout.main_fragment_current_card,getActivity().getApplicationContext());
                currentRv.setAdapter(currentRecyclerAdapter);
                currentRecyclerAdapter.notifyDataSetChanged();
                nProgressBar.setVisibility(View.GONE);
                network.setVisibility(View.GONE);
                currentRv.setVisibility(View.VISIBLE);
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
        final Call<MainActivityPojoExtra> call = apiService.getTopTv(API_KEY,currentPage);
        call.enqueue(new Callback<MainActivityPojoExtra>() {
            @Override
            public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {
                currentList.addAll(response.body().getResults());
                currentRecyclerAdapter.notifyDataSetChanged();

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
    private class Current_RecyclerAdapter extends RecyclerView.Adapter<Current_RecyclerViewHolder> {

        ArrayList<MainActivityPojo> currentList;
        Context ccontext;
        LayoutInflater cinflater;

        Current_RecyclerAdapter(ArrayList<MainActivityPojo> trendingList, int main_fragment_current_card, Context context) {

            this.ccontext=context;
            cinflater=LayoutInflater.from(context);
            this.currentList=trendingList;
        }


        @Override
        public Current_RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = cinflater.inflate(R.layout.main_fragment_current_card, parent, false);
            Current_RecyclerViewHolder viewHolder = new Current_RecyclerViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final Current_RecyclerViewHolder holder, final int position) {


            holder.title.setText(currentList.get(position).getCardtitle());
            holder.timestamp.setText(currentList.get(position).getTimestamp());
            final String imagrUrl="http://image.tmdb.org/t/p/w342"+currentList.get(position).getImagepath();
            Picasso.with(ccontext).load(imagrUrl).resize(334,500).onlyScaleDown().into(holder.Main_image);

            holder.share_button.setOnClickListener(shareListener);
            holder.share_button.setTag(holder);
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddBookmark.add(getContext(),currentList.get(position));
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //passing data to article_content
                    Intent i= new Intent(getActivity(), ArticleMain.class);
                    //create bundle information
                    i.putExtra("newsList", currentList.get(position));
                    Current_fragment.this.startActivity(i);
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
                Current_fragment.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        };

        @Override
        public int getItemCount() {
            return currentList.size();
        }
    }


    ///View holder Custom Class
    public class  Current_RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView title, timestamp;
        ImageView Main_image,share_button, add;
        RecyclerView rv;
        public Current_RecyclerViewHolder(View itemView){
            super(itemView);
            Typeface customFontBold= Typeface.createFromAsset(getActivity().getAssets(),"fonts/JosefinSans-Bold.ttf");

            rv=(RecyclerView)itemView.findViewById(R.id.current_recycler_view);
            timestamp=(TextView)itemView.findViewById(R.id.current_time_stamp);
            Main_image=(ImageView)itemView.findViewById(R.id.current_card_image);
            title=(TextView)itemView.findViewById(R.id.current_card_title);
            share_button=(ImageView)itemView.findViewById(R.id.current_share_button);
            add=(ImageView)itemView.findViewById(R.id.badButton);
            timestamp.setTypeface(customFontBold);
            title.setTypeface(customFontBold);

        }
    }

}
