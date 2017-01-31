package Fragment_Schedule_Page;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import model.Schedule_Recent_CardData;

/**
 * Created by abhinav on 21-07-2016.
 */
public class Fragment_Recent extends Fragment {
    public Fragment_Recent() {
        // Required empty public constructor

    }
    RecyclerView schedule_Recent_recycler_view;
    Schedule_Recent_RecyclerAdapter schedule_Recent_adapter;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.schedule_recent_fragment,container,false);

        //Inflating the recycler view
        schedule_Recent_recycler_view= (RecyclerView)v.findViewById(R.id.schedule_recent_recycler_OdiT20);
        schedule_Recent_recycler_view.setHasFixedSize(true);

        //List<Card_data> data = null;
        schedule_Recent_adapter=new Schedule_Recent_RecyclerAdapter(getActivity(),getData());
        schedule_Recent_recycler_view.setAdapter(schedule_Recent_adapter);
        schedule_Recent_recycler_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        return v;
    }

    /// Adapter Data
    public static List<Schedule_Recent_CardData> getData(){
        List<Schedule_Recent_CardData> Recent_recyclerview_data=new ArrayList<>();

        int[] team1_logo={R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo};
        int[] team2_logo={R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo,R.drawable.coc_logo};
        String[] Series_title={"West-Ind Series","Micromax series","Gavaskar Cup","Intex series","Moto Series","Gabbar Series","Basanti Series","Teri Meri Kahani"};
        String[] Name_team1={"Aus","IND","WI","SA","ENG","PAK","BAN","NZ"};
        String[] Name_team2={"IND","WI","NZ","PAK","BAN","AUS","SA","ENG"};
        String[] Score_team1={"203/5(45.4)","201/6(44.3)","302/9(128.4)","203/5(45.4)","201/6(44.3)","302/9(128.4)","302/9(128.4)","203/5(45.4)"};
        String[] Score_team2={"204/6(47.3)","205/2(40.2)","197(90.5)","204/6(47.3)","205/2(40.2)","197(90.5)","197(90.5)","204/6(47.3)"};
        String[] Venue={"LONDON","BRISBANE","PERTH","KOLKATA","LONDON","BRISBANE","PERTH","KOLKATA"};
        String[] Format={"Test","One Day","Test","One Day","One Day","Test","Test","One Day"};

        //inserting data

        for(int i=0;i<Series_title.length && i<team1_logo.length && i<Venue.length;i++){
            Schedule_Recent_CardData SRcurrent;//Su= Schedule Recent
            SRcurrent = new Schedule_Recent_CardData();
            SRcurrent.Recent_team1_image = team1_logo[i];
            SRcurrent.Recent_team2_image = team2_logo[i];
            SRcurrent.Recent_title = Series_title[i];
            SRcurrent.Recent_venue = Venue[i];
            SRcurrent.Recent_team1name = Name_team1[i];
            SRcurrent.Recent_team2name = Name_team2[i];
            SRcurrent.Recent_team1score1 = Score_team1[i];
            SRcurrent.Recent_team2score1 = Score_team2[i];
            SRcurrent.Recent_match_Format=Format[i];
            Recent_recyclerview_data.add(SRcurrent);
        }
        return Recent_recyclerview_data;
    }

    ////Adapter class
    private class Schedule_Recent_RecyclerAdapter extends RecyclerView.Adapter<Schedule_Recent_RecyclerViewHolder> {

        List<Schedule_Recent_CardData> Recent_recyclerview_data= Collections.emptyList();
        Context schedule_Recent_context;
        LayoutInflater schedule_Recent_inflater;

        private static final int type_test = 0;
        private static final int type_odi = 1;
        public Schedule_Recent_RecyclerAdapter(Context context, List<Schedule_Recent_CardData> Recent_recylerview_data) {

            this.schedule_Recent_context=context;
            schedule_Recent_inflater=LayoutInflater.from(context);
            this.Recent_recyclerview_data=Recent_recylerview_data;
        }

/// setting the desired layout
        @Override
        public int getItemViewType(int position){
            int m;
            m= position%2;
            return (m == 0?type_test : type_odi);
        }
        @Override
        public Schedule_Recent_RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case type_test:
                    return new Schedule_Recent_Test_ViewHolder(LayoutInflater.from(schedule_Recent_context).inflate(R.layout.schedule_recent_card_test, parent, false));
                case type_odi:
                    return new Schedule_Recent_Odi_ViewHolder(LayoutInflater.from(schedule_Recent_context).inflate(R.layout.schedule_recent_card_odit20, parent, false));
            }
            return null;

          /*  View v = schedule_Recent_inflater.inflate(R.layout.schedule_recent_card_odit20, parent, false);
            Log.d("Abhinav","onCreateViewholder Called");
            Schedule_Recent_RecyclerViewHolder viewHolder = new Schedule_Recent_RecyclerViewHolder(v);
            return viewHolder;*/
        }

        @Override
        public void onBindViewHolder(final Schedule_Recent_RecyclerViewHolder holder, final int position) {
//odi holder
            if(holder.getItemViewType()==type_odi){

                Schedule_Recent_Odi_ViewHolder odiHolder =(Schedule_Recent_Odi_ViewHolder) holder;

                final Schedule_Recent_CardData current= Recent_recyclerview_data.get(position);
                Log.d("Abhinav","onBindViewholder Called"+position);
                odiHolder.Recent_title.setText(current.Recent_title);// name defined in recyclerviewholder= name defined in data class
                odiHolder.Recent_nameteam1.setText(current.Recent_team1name);
                odiHolder.Recent_nameteam2.setText(current.Recent_team2name);
                odiHolder.Recent_scoreteam1.setText(current.Recent_team1score1);
                odiHolder.Recent_scoreteam2.setText(current.Recent_team2score1);
                odiHolder.Recent_venue.setText(current.Recent_venue);
                odiHolder.Recent_icon_team1.setImageResource(current.Recent_team1_image);
                odiHolder.Recent_icon_team2.setImageResource(current.Recent_team2_image);
                odiHolder.Recent_match_format.setText(current.Recent_match_Format);
                odiHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(schedule_Recent_context, "Card clicked at "+(position+1), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            //test holder
            else{
                Schedule_Recent_Test_ViewHolder testHolder= (Schedule_Recent_Test_ViewHolder) holder;

                final Schedule_Recent_CardData current= Recent_recyclerview_data.get(position);
                Log.d("Abhinav","onBindViewholder Called"+position);
                testHolder.Recent_title.setText(current.Recent_title);// name defined in recyclerviewholder= name defined in data class
                testHolder.Recent_nameteam1.setText(current.Recent_team1name);
                testHolder.Recent_nameteam2.setText(current.Recent_team2name);
                testHolder.Recent_score1team1.setText(current.Recent_team1score1);
                testHolder.Recent_score1team2.setText(current.Recent_team2score1);
                testHolder.Recent_venue.setText(current.Recent_venue);
                testHolder.Recent_icon_team1.setImageResource(current.Recent_team1_image);
                testHolder.Recent_icon_team2.setImageResource(current.Recent_team2_image);
                testHolder.Recent_match_format.setText(current.Recent_match_Format);
                testHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(schedule_Recent_context, "Card clicked at "+(position+1), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return Recent_recyclerview_data.size();
        }
    }


    ///View holder Custom Class
    public class  Schedule_Recent_RecyclerViewHolder extends RecyclerView.ViewHolder {

        public Schedule_Recent_RecyclerViewHolder(View itemView){
            super(itemView);
        }
    }
//odi view holder
    private class Schedule_Recent_Odi_ViewHolder extends Schedule_Recent_RecyclerViewHolder {

        TextView Recent_title,Recent_date,Recent_time;
        TextView Recent_day,Recent_venue,Recent_match_format;
        TextView Recent_scoreteam1,Recent_scoreteam2,Recent_nameteam1, Recent_nameteam2;
        ImageView Recent_icon_team1, Recent_icon_team2;

        public Schedule_Recent_Odi_ViewHolder(View itemView){
            super(itemView);

            Recent_title=(TextView)itemView.findViewById(R.id.schedule_Recent_Series_title);
            Recent_date=(TextView) itemView.findViewById(R.id.schedule_Recent_date);
            Recent_day=(TextView)itemView.findViewById(R.id.schedule_Recent_day);
            Recent_time=(TextView) itemView.findViewById(R.id.schedule_Recent_time);
            Recent_venue=(TextView) itemView.findViewById(R.id.Recent_venue);
            Recent_match_format=(TextView) itemView.findViewById(R.id.Recent_match_type);
            Recent_scoreteam1=(TextView) itemView.findViewById(R.id.Recent_team1_score);
            Recent_scoreteam2=(TextView)itemView.findViewById(R.id.Recent_team2_score);
            Recent_nameteam1=(TextView)itemView.findViewById(R.id.Recent_team1_name);
            Recent_nameteam2=(TextView)itemView.findViewById(R.id.Recent_team2_name);
            Recent_icon_team1=(ImageView)itemView.findViewById(R.id.Recent_team1_logo);
            Recent_icon_team2=(ImageView)itemView.findViewById(R.id.Recent_team2_logo);

        }
    }

    private class Schedule_Recent_Test_ViewHolder extends Schedule_Recent_RecyclerViewHolder {
        TextView Recent_title,Recent_date,Recent_time;
        TextView Recent_day,Recent_venue,Recent_match_format;
        TextView Recent_score1team1,Recent_score1team2,Recent_nameteam1, Recent_nameteam2;
        TextView Recent_score2team1,Recent_score2team2;
        TextView Recent_inning1, Recent_inning2;
        ImageView Recent_icon_team1, Recent_icon_team2;

        public Schedule_Recent_Test_ViewHolder(View itemView){
            super(itemView);

            Recent_title=(TextView)itemView.findViewById(R.id.schedule_Recent_Series_title);
            Recent_date=(TextView) itemView.findViewById(R.id.schedule_Recent_date);
            Recent_day=(TextView)itemView.findViewById(R.id.schedule_Recent_day);
            Recent_time=(TextView) itemView.findViewById(R.id.schedule_Recent_time);
            Recent_venue=(TextView) itemView.findViewById(R.id.Recent_venue);
            Recent_match_format=(TextView) itemView.findViewById(R.id.Recent_match_type);
            Recent_score1team1=(TextView) itemView.findViewById(R.id.Recent_team1_score1);
            Recent_score1team2=(TextView)itemView.findViewById(R.id.Recent_team2_score1);
            Recent_nameteam1=(TextView)itemView.findViewById(R.id.Recent_team1_name);
            Recent_nameteam2=(TextView)itemView.findViewById(R.id.Recent_team2_name);
            Recent_inning1 =(TextView) itemView.findViewById(R.id.Recent_test_1stinnings);
            Recent_inning2= (TextView)itemView.findViewById(R.id.Recent_test_2stinnings);
            Recent_score2team2 =(TextView) itemView.findViewById(R.id.Recent_team1_score2);
            Recent_score2team2 =(TextView) itemView.findViewById(R.id.Recent_team2_score2);
            Recent_icon_team1=(ImageView)itemView.findViewById(R.id.Recent_team1_logo);
            Recent_icon_team2=(ImageView)itemView.findViewById(R.id.Recent_team2_logo);

        }
    }
}
