package project.chasemvp.Utils.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import project.chasemvp.Model.POJO.Chaser;
import project.chasemvp.R;
import project.chasemvp.Utils.CustomTextView;

/**
 * Created by Mehdi on 19/06/2017.
 */

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.ViewHolder>
{

    public ArrayList<Chaser> getPlayers() {
        return players;
    }

    private ArrayList<Chaser> players;
    Context context;

    public RoomAdapter(Context c)
    {
        players = new ArrayList<>();
        context = c;
    }


    public void addPlayers(List<Chaser> new_players)
    {
        players.clear();
        players.addAll(new_players);
        notifyDataSetChanged();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public CustomTextView pseudo;
        public CustomTextView level;
        public CustomTextView ready;
        public CircularImageView image;


        public ViewHolder(View v)
        {
            super(v);
            pseudo = (CustomTextView) itemView.findViewById(R.id.room_item_pseudo);
            ready = (CustomTextView) itemView.findViewById(R.id.room_item_ready);
            level = (CustomTextView) itemView.findViewById(R.id.room_item_level);
            image = (CircularImageView) itemView.findViewById(R.id.room_item_image);
        }
    }

    @Override
    public RoomAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

            int layoutId = R.layout.room_item;;

            View v = LayoutInflater.from(viewGroup.getContext())
                    .inflate(layoutId, viewGroup, false);
            // set the view's size, margins, paddings and layout parameters
            RoomAdapter.ViewHolder holder = new RoomAdapter.ViewHolder(v);
            return holder;
    }

    @Override
    public void onBindViewHolder(RoomAdapter.ViewHolder viewHolder, int i)
    {
        viewHolder.pseudo.setText(players.get(i).getPseudo());
        viewHolder.level.setText("Level " + players.get(i).getLevel());

        //viewHolder.level.setText(players.get(i).getLevel());

        Picasso.with(context)
                .load(players.get(i).getUrl_image())
                .placeholder(R.mipmap.ic_launcher_round)
                .into(viewHolder.image);

    }


    @Override
    public int getItemCount() {
        return players.size();
    }




}
