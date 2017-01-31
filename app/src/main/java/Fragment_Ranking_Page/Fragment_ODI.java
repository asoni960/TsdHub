package Fragment_Ranking_Page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhinav.tsdhub.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import model.Ranking_ODI_data;

public class Fragment_ODI extends Fragment {    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ranking_fragment_team, container, false);

        sectionAdapter = new SectionedRecyclerViewAdapter();

        sectionAdapter.addSection(new TestSection("Batsman", getTestRankList()));
        sectionAdapter.addSection(new OdiSection("Bowler", getTestRankList()));
        sectionAdapter.addSection(new T20Section("All Rounder",getTestRankList()));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }

    private List<Ranking_ODI_data> getTestRankList(){
        List<Ranking_ODI_data> rank_list = new ArrayList<>();

        String[] rank_a = {"1","2","3","4","5","6","7","8","9","10"};
       // int[] country_a = {R.drawable.flag,R.drawable.flag,R.drawable.flag,R.drawable.flag,R.drawable.flag,R.drawable.flag,R.drawable.flag,R.drawable.flag,R.drawable.flag,R.drawable.flag};
        String[] name_a ={"AB de Villiers","Virat Kohli","Hahim M Amla","KS Williamson","Martin J Guptil","Rohit Sharma","Joe Root","Shikhar Dhawan","Quinton de Kock","TilakRatne M Dilshan"};
        String[] rating_a = {"887", "813", "778", "752", "751", "750", "741", "737", "735", "734"};

        for(int i=0;i<rank_a.length;i++){
            Ranking_ODI_data current=  new Ranking_ODI_data();
            current.rank=rank_a[i];
            current.name= name_a[i];
           // current.country=country_a[i];
            current.rating=rating_a[i];
            rank_list.add(current);
        }
        return rank_list;
    }

    class TestSection extends StatelessSection {

        String title;
        List<Ranking_ODI_data> rank_list= Collections.emptyList();

        public TestSection(String title, List<Ranking_ODI_data> rank_list) {
            super(R.layout.rankingfragment_odi_header, R.layout.rankingfragment_odi_item);

            this.title = title;
            this.rank_list = rank_list;
        }

        @Override
        public int getContentItemsTotal() {
            return rank_list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            final Ranking_ODI_data current= rank_list.get(position);
            itemHolder.rank.setText(current.rank);
            //itemHolder.country.setImageResource(current.country);
            itemHolder.name.setText(current.name);
            itemHolder.rating.setText(current.rating);
            //itemHolder.imgItem.setImageResource(name.hashCode() % 2 == 0 ? R.drawable.ic_bubble_chart_black : R.drawable.ic_nav_donut_icon);

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), String.format("Clicked on position #%s of Section %s", sectionAdapter.getSectionPosition(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.header_title.setText(title);
        }
    }
    /////////////////

    class OdiSection extends StatelessSection{
        String title;
        List<Ranking_ODI_data> rank_list;

        public OdiSection(String title, List<Ranking_ODI_data> rank_list) {
            super(R.layout.rankingfragment_odi_header, R.layout.rankingfragment_odi_item);

            this.title = title;
            this.rank_list = rank_list;
        }

        @Override
        public int getContentItemsTotal() {
            return rank_list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;
            final Ranking_ODI_data current= rank_list.get(position);
            itemHolder.rank.setText(current.rank);
            itemHolder.name.setText(current.name);
            //itemHolder.country.setImageResource(current.country);
            itemHolder.rating.setText(current.rating);
            //itemHolder.imgItem.setImageResource(name.hashCode() % 2 == 0 ? R.drawable.ic_bubble_chart_black : R.drawable.ic_nav_donut_icon);

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), String.format("Clicked on position #%s of Section %s", sectionAdapter.getSectionPosition(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.header_title.setText(title);
        }
    }

    ////////////


    class T20Section extends StatelessSection {

        String title;
        List<Ranking_ODI_data> rank_list= Collections.emptyList();

        public T20Section(String title, List<Ranking_ODI_data> rank_list) {
            super(R.layout.rankingfragment_odi_header, R.layout.rankingfragment_odi_item);

            this.title = title;
            this.rank_list = rank_list;
        }

        @Override
        public int getContentItemsTotal() {
            return rank_list.size();
        }

        @Override
        public RecyclerView.ViewHolder getItemViewHolder(View view) {
            return new ItemViewHolder(view);
        }

        @Override
        public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
            final ItemViewHolder itemHolder = (ItemViewHolder) holder;

            final Ranking_ODI_data current= rank_list.get(position);
            itemHolder.rank.setText(current.rank);
            //itemHolder.country.setImageResource(current.country);
            itemHolder.name.setText(current.name);
            itemHolder.rating.setText(current.rating);
            //itemHolder.imgItem.setImageResource(name.hashCode() % 2 == 0 ? R.drawable.ic_bubble_chart_black : R.drawable.ic_nav_donut_icon);

            itemHolder.rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), String.format("Clicked on position #%s of Section %s", sectionAdapter.getSectionPosition(itemHolder.getAdapterPosition()), title), Toast.LENGTH_SHORT).show();
                }
            });
        }

        @Override
        public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
            return new HeaderViewHolder(view);
        }

        @Override
        public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;

            headerHolder.header_title.setText(title);
        }
    }



    /////////////////////
    class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView header_title,header_rank,header_country,header_name,header_rating;

        public HeaderViewHolder(View view) {
            super(view);

            header_title = (TextView) view.findViewById(R.id.rankingheader_title);
            header_rank = (TextView) view.findViewById(R.id.rankingheader_rank);
            header_name = (TextView) view.findViewById(R.id.rankingheader_name);
            header_country = (TextView) view.findViewById(R.id.rankingheader_country);
            header_rating = (TextView) view.findViewById(R.id.rankingheader_rating);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private TextView rank ,name, rating ;
        private ImageView country;

        public ItemViewHolder(View view) {
            super(view);

            rootView = view;
            rank = (TextView) view.findViewById(R.id.rankingODI_rank);
            country= (ImageView) view.findViewById(R.id.rankingODI_country);
            name = (TextView) view.findViewById(R.id.rankingODI_name);
            rating = (TextView) view.findViewById(R.id.rankingODI_ratings);
        }
    }
}
