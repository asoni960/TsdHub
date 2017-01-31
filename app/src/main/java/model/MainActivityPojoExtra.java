package model;

import com.example.abhinav.tsdhub.MainActivity;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by abhinav on 12-10-2016.
 */

public class MainActivityPojoExtra  {
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private ArrayList<MainActivityPojo> results;
    @SerializedName("total_results")
    private int totalResults;
    @SerializedName("total_pages")
    private int totalPages;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public ArrayList<MainActivityPojo> getResults() {
        return results;
    }

    public void setResults(ArrayList<MainActivityPojo> results) {
        this.results = results;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}
