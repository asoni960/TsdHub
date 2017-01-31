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
import com.example.abhinav.tsdhub.ArticlePart2;
import com.example.abhinav.tsdhub.MainActivity;
import com.example.abhinav.tsdhub.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import model.MainActivityPojoExtra;
import model.MainActivityPojo;
import realm.AddBookmark;
import rest.ApiClient;
import rest.ApiInterface;
import rest.CheckNetwork;
import rest.EndlessRecyclerViewScrollListener;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by abhinav on 11-06-2016.
 */
public class News_fragment extends android.support.v4.app.Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private static final String TAG = "Tab 1";
    private  final static String API_KEY="af54b23df48d97a6868fb4f636cdcd27";
    private RecyclerView newsRecyclerView;

    private ArrayList<MainActivityPojo> newsList;
    private  News_RecyclerAdapter newsRecyclerAdapter;
    int currentPage=1;
    public LinearLayoutManager mLayoutManager;

    private SwipeRefreshLayout swipeRefreshLayout;
    protected ProgressBar nProgressBar;
    protected TextView network;
    public News_fragment() {}
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.main_fragment_news,container,false);

        swipeRefreshLayout=(SwipeRefreshLayout)v.findViewById(R.id.main_swipelayout);
        newsRecyclerView= (RecyclerView)v.findViewById(R.id.news_recycler_view);
        newsRecyclerView.setHasFixedSize(true);
        mLayoutManager= new LinearLayoutManager(getContext());

        nProgressBar=(ProgressBar)v.findViewById(R.id.loadingSpinner);
        network=(TextView)v.findViewById(R.id.network);

        network.setVisibility(View.GONE);
        nProgressBar.setVisibility(View.GONE);
        newsRecyclerView.setVisibility(View.GONE);
        CheckState();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                CheckState();
            }
        });
        newsRecyclerView.setLayoutManager(mLayoutManager);
        newsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        newsRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(mLayoutManager) {
            @Override
            public void onLoadMore(int currentPage, int totalItemCount, RecyclerView view) {
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
            newsRecyclerView.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
        }
        else
        {   network.setVisibility(View.GONE);
            LoadData(currentPage);}
    };

    // retrofit loading function
    private void LoadData(int currentPage) {
        swipeRefreshLayout.setOnRefreshListener(this);
        newsRecyclerView.setVisibility(View.GONE);
        nProgressBar.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeColors(Color.RED,Color.BLUE,Color.BLACK,Color.GREEN);
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<MainActivityPojoExtra> call = apiService.getPopularMovies(API_KEY,currentPage);
        call.enqueue(new Callback<MainActivityPojoExtra>() {
            @Override
            public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {

                newsList= response.body().getResults();
                newsRecyclerAdapter = new News_RecyclerAdapter(newsList, R.layout.main_fragment_news_card,getActivity().getApplicationContext());
                newsRecyclerView.setAdapter(newsRecyclerAdapter);
                nProgressBar.setVisibility(View.GONE);
                newsRecyclerAdapter.notifyDataSetChanged();
                network.setVisibility(View.GONE);
                newsRecyclerView.setVisibility(View.VISIBLE);
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
        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        final Call<MainActivityPojoExtra> call = apiService.getPopularMovies(API_KEY,page);
        call.enqueue(new Callback<MainActivityPojoExtra>() {
            @Override
            public void onResponse(Call<MainActivityPojoExtra> call, retrofit2.Response<MainActivityPojoExtra> response) {
                newsList.addAll(response.body().getResults());
                newsRecyclerAdapter.notifyDataSetChanged();

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

    public class News_RecyclerAdapter extends RecyclerView.Adapter<News_RecyclerViewHolder> {

        private List<MainActivityPojo> newsList;
        Context context;
        LayoutInflater inflater;

        public News_RecyclerAdapter(List<MainActivityPojo> newsList, int main_fragment_news_card, Context context) {
            this.context = context;
            this.newsList= newsList;
            inflater = LayoutInflater.from(context);
        }


        @Override
        public News_RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.main_fragment_news_card, parent, false);
            News_RecyclerViewHolder viewHolder = new News_RecyclerViewHolder(v);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(News_RecyclerViewHolder holder, final int position) {


            holder.tv1.setText(newsList.get(position).getCardtitle());
            holder.tv2.setText(newsList.get(position).getTimestamp());
            String imagrUrl="http://image.tmdb.org/t/p/w342"+newsList.get(position).getImagepath();
            Picasso.with(context).load(imagrUrl).resize(334,500).onlyScaleDown().into(holder.imageView);
            holder.share.setTag(holder);
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                     AddBookmark.add(getContext(),newsList.get(position));
                }
            });

            holder.share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = "https://api.themoviedb.org/3/movie/?api_key=af54b23df48d97a6868fb4f636cdcd27";
                    sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HUB");
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    News_fragment.this.startActivity(Intent.createChooser(sharingIntent, "Share via"));
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i= new Intent(getActivity(), ArticlePart2.class);
                    i.putExtra("newsList", newsList.get(position));
                    News_fragment.this.startActivity(i);

                }
            });
        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

    }


    public class News_RecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView tv1, tv2;
        ImageView imageView, share, add;
        RecyclerView rv;
        SwipeRefreshLayout swipeRefreshLayout;

        public News_RecyclerViewHolder(View itemView) {
            super(itemView);
            Typeface customFontBold= Typeface.createFromAsset(getActivity().getAssets(),"fonts/JosefinSans-Bold.ttf");

            swipeRefreshLayout=(SwipeRefreshLayout)itemView.findViewById(R.id.main_swipelayout);
            rv= (RecyclerView)itemView.findViewById(R.id.news_recycler_view);
            tv1 = (TextView) itemView.findViewById(R.id.news_card_title);
            tv2 = (TextView) itemView.findViewById(R.id.time_stamp);
            tv1.setTypeface(customFontBold);
            tv2.setTypeface(customFontBold);
            imageView = (ImageView) itemView.findViewById(R.id.news_card_image);
            share=(ImageView)itemView.findViewById(R.id.share_button);
            add=(ImageView)itemView.findViewById(R.id.badButton);
        }


    }
}
