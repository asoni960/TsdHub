package adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abhinav.tsdhub.ArticleMain;
import com.example.abhinav.tsdhub.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import model.MainActivityPojo;

/**
 * Created by abhinav on 02-01-2017.
 */



//Bookmark Adapter

public class BookmarkMoviesAdapter extends RecyclerView.Adapter<BookmarkMoviesAdapter.BookmarkMovies_RecyclerViewHolder>{

    private Realm realm;
    private ArrayList<MainActivityPojo> bookmarkList;
    private final LayoutInflater inflater;
    private Context context;

    public BookmarkMoviesAdapter(Context context) {
        inflater=LayoutInflater.from(context);
        this.context=context;
        realm=Realm.getDefaultInstance();
        bookmarkList= new ArrayList<>();

        //Querying All data stored in Realm
        RealmQuery<MainActivityPojo> query=realm.where(MainActivityPojo.class);
        RealmResults<MainActivityPojo> realmResults=query.findAll();//Async check
        for(MainActivityPojo results : realmResults){
            bookmarkList.add(results);
        }


    }

    @Override
    public BookmarkMovies_RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.bookmark_cardview,parent,false);
        BookmarkMovies_RecyclerViewHolder holder= new BookmarkMovies_RecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BookmarkMovies_RecyclerViewHolder holder, final int position) {


        holder.BTitle.setText(bookmarkList.get(position).getCardtitle());
        Log.d("Views", bookmarkList.get(position).getVoteCount());
        Log.e("Bookmark Title", bookmarkList.get(position).getCardtitle());
        holder.Btimestamp.setText(bookmarkList.get(position).getTimestamp());
        //String imagrUrl="http://image.tmdb.org/t/p/w342"+bookmarkList.get(position).getImagepath();
        Picasso.with(context).load(bookmarkList.get(position).getImagepath()).resize(334,500).onlyScaleDown().into(holder.BMainimage);
        holder.Bshare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "https://api.themoviedb.org/3/movie/?api_key=af54b23df48d97a6868fb4f636cdcd27";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "HUB");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent( context,ArticleMain.class);
                //create bundle information
                i.putExtra("newsList", bookmarkList.get(position));
                context.startActivity(i);
            }
        });
        holder.Bdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set dialog message and title
                AlertDialog.Builder builder= new AlertDialog.Builder(context);
                builder.setTitle("Remove BookMark");

                //set when yes click
                builder.setPositiveButton("Remove", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MainActivityPojo newsBookmark=bookmarkList.get(position);
                        realm.beginTransaction();
                        MainActivityPojo currentBookmark=realm.where(MainActivityPojo.class).equalTo("movieid", newsBookmark.getMovieid()).findFirst();//Async
                        currentBookmark.deleteFromRealm();
                        realm.commitTransaction();
                        bookmarkList.remove(position);
                        notifyDataSetChanged();
                    }
                });

                //Set When Cancel Button Click
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        //Dismissing the alertDialog
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookmarkList.size();
    }

    public class BookmarkMovies_RecyclerViewHolder extends RecyclerView.ViewHolder{
        ImageView BMainimage, Bdelete, Bshare;
        TextView BTitle, Btimestamp;

        public BookmarkMovies_RecyclerViewHolder(View itemView) {
            super(itemView);

        //    Typeface customFontBold= Typeface.createFromAsset(getAssets(),"fonts/JosefinSans-Bold.ttf");
            BMainimage=(ImageView)itemView.findViewById(R.id.bookmark_cardImage);
            Bshare=(ImageView)itemView.findViewById(R.id.bookmark_shareButton);
            Bdelete=(ImageView)itemView.findViewById(R.id.deleteButton);
            BTitle=(TextView)itemView.findViewById(R.id.bookmark_cardTitle);
            Btimestamp=(TextView)itemView.findViewById(R.id.bookmark_cardTimestamp);
        }
    }
}

