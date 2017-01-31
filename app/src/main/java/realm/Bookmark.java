package realm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.abhinav.tsdhub.ArticleSearch;
import com.example.abhinav.tsdhub.R;

import adapter.BookmarkMoviesAdapter;

/**
 * Created by abhinav on 15-06-2016.
 */
public class Bookmark extends AppCompatActivity {

    Toolbar main_toolbar;
    private LinearLayoutManager bLinearLayoutManager;
    private RecyclerView bookmarkRecyclerView;
    public BookmarkMoviesAdapter bookmarkMoviesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark_activity);

        main_toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        setTitle(R.string.bookmark);

       refreshDataSet();

    }

    public void refreshDataSet(){
        bLinearLayoutManager= new LinearLayoutManager(getApplicationContext());
        bookmarkRecyclerView=(RecyclerView)findViewById(R.id.bookmarkRecyclerView);
        bookmarkRecyclerView.setHasFixedSize(true);
        bookmarkMoviesAdapter=new BookmarkMoviesAdapter(Bookmark.this);
        bookmarkRecyclerView.setLayoutManager(bLinearLayoutManager);
        bookmarkRecyclerView.setItemAnimator(new DefaultItemAnimator());
        bookmarkRecyclerView.setAdapter(bookmarkMoviesAdapter);
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
    private void goto_search() {
        Intent intent = new Intent(this, ArticleSearch.class);
        startActivity(intent);
    }
}