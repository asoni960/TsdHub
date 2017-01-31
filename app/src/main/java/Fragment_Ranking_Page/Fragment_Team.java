package Fragment_Ranking_Page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.abhinav.tsdhub.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;
import model.Ranking_Team_Data;

public class Fragment_Team extends Fragment {

    private SectionedRecyclerViewAdapter sectionAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ranking_fragment_team, container, false);

       sectionAdapter = new SectionedRecyclerViewAdapter();

/*
        for(char alphabet = 'A'; alphabet <= 'Z';alphabet++) {
            List<String> contacts = getContactsWithLetter(alphabet);

            if (contacts.size() > 0) {
                sectionAdapter.addSection(new ContactsSection(String.valueOf(alphabet), contacts));
            }
        }
*/

        sectionAdapter.addSection(new TestSection("Test", getTestRankList()));
        sectionAdapter.addSection(new OdiSection("ODI", getTestRankList()));
        sectionAdapter.addSection(new T20Section("T20",getTestRankList()));
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(sectionAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof AppCompatActivity) {
            AppCompatActivity activity = ((AppCompatActivity) getActivity());
            if (activity.getSupportActionBar() != null)
                activity.getSupportActionBar().setTitle("Activity 1");
        }
    }

  private List<Ranking_Team_Data> getTestRankList(){
      List<Ranking_Team_Data> rank_list = new ArrayList<>();

      String[] rank_a ={"1","2","3","4","5","6","7","8","9","10"};
      String[] country_a ={"Australia","New Zealand","India","South Africa","England","Sri Lanka","Bangladesh","West Indies","Pakistan","Afghanistan"};
      String[] match_a ={"40","51","48","42","44","49","52","47","50","49"};
      String[] point_a ={"4281","4716","5284","4465","4236","4106","4698","5203","4586","5458"};
      String[] rating_a ={"123","113","110","110","106","102","98","94","87","49"};

      for(int i=0;i<rank_a.length;i++){
          Ranking_Team_Data current=  new Ranking_Team_Data();
          current.rank=rank_a[i];
          current.country=country_a[i];
          current.match=match_a[i];
          current.point=point_a[i];
          current.rating=rating_a[i];
          rank_list.add(current);
      }
      return rank_list;
  }

    class TestSection extends StatelessSection {

        String title;
        List<Ranking_Team_Data> rank_list= Collections.emptyList();

        public TestSection(String title, List<Ranking_Team_Data> rank_list) {
            super(R.layout.rankingfragment_team_header, R.layout.rankingfragment_team_item);

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

            final Ranking_Team_Data current= rank_list.get(position);
            itemHolder.rank.setText(current.rank);
            itemHolder.country.setText(current.country);
            itemHolder.match.setText(current.match);
            itemHolder.point.setText(current.point);
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
        List<Ranking_Team_Data> rank_list;

        public OdiSection(String title, List<Ranking_Team_Data> rank_list) {
            super(R.layout.rankingfragment_team_header, R.layout.rankingfragment_team_item);

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
            final Ranking_Team_Data current= rank_list.get(position);
            itemHolder.rank.setText(current.rank);
            itemHolder.country.setText(current.country);
            itemHolder.match.setText(current.match);
            itemHolder.point.setText(current.point);
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
        List<Ranking_Team_Data> rank_list= Collections.emptyList();

        public T20Section(String title, List<Ranking_Team_Data> rank_list) {
            super(R.layout.rankingfragment_team_header, R.layout.rankingfragment_team_item);

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

            final Ranking_Team_Data current= rank_list.get(position);
            itemHolder.rank.setText(current.rank);
            itemHolder.country.setText(current.country);
            itemHolder.match.setText(current.match);
            itemHolder.point.setText(current.point);
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

        TextView header_title,header_rank,header_country,header_match,header_point,header_rating;

        public HeaderViewHolder(View view) {
            super(view);

            header_title = (TextView) view.findViewById(R.id.rankingheader_title);
            header_rank = (TextView) view.findViewById(R.id.rankingheader_rank);
            header_country = (TextView) view.findViewById(R.id.rankingheader_country);
            header_match = (TextView) view.findViewById(R.id.rankingheader_match);
            header_point = (TextView) view.findViewById(R.id.rankingheader_point);
            header_rating = (TextView) view.findViewById(R.id.rankingheader_rating);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final View rootView;
        private TextView country,rank,match, point, rating ;

        public ItemViewHolder(View view) {
            super(view);

            rootView = view;
            rank = (TextView) view.findViewById(R.id.rankingTeam_rank);
            country= (TextView) view.findViewById(R.id.rankingTeam_country);
            match = (TextView) view.findViewById(R.id.rankingTeam_matches);
            point = (TextView) view.findViewById(R.id.rankingTeam_points);
            rating = (TextView) view.findViewById(R.id.rankingTeam_ratings);
        }
    }

}
