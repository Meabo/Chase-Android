package project.chasemvp.Utils.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import project.chasemvp.Model.POJO.Chase;
import project.chasemvp.R;

/**
 * Created by Mehdi on 11/06/2017.
 */

public class ChaseAdapter extends RecyclerView.Adapter<ChaseAdapter.ViewHolder>
{


    private ArrayList<Chase> Chases_list;
    private Context context;

    public ChaseAdapter(Context c)
    {
        this.context = c;
        Chases_list = new ArrayList<>();
    }

    public ArrayList<Chase> getChases_list() {
        return Chases_list;
    }


    public void addChases(List<Chase> chaseList)
    {
        Chases_list.clear();
        Chases_list.addAll(chaseList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mImageView;
        public TextView capacity_max;
        public TextView capacity_actual;
        public TextView duration;

        public ViewHolder(View v) {
            super(v);
            mTextView = (TextView) itemView
                    .findViewById(R.id.list_chases_title);

            mImageView = (ImageView) itemView
                    .findViewById(R.id.list_chases_image);

        /*    capacity_max = (TextView) itemView
                    .findViewById(R.id.capacity_max);

            capacity_actual = (TextView) itemView
                    .findViewById(R.id.capacity_actual);*/


        }
    }

    @Override
    public ChaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType)
    {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_chases, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ChaseAdapter.ViewHolder holder = new ChaseAdapter.ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(ChaseAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        //holder.mTextView.setText(game_list.get(position).getName());
        //Log.d(debug,"Capacity Game : " +  game_list.get(position).getCapacity() + "");
        if (position == 1)
        {
           // holder.mImageView.setImageResource(R.drawable.run1);
            holder.mTextView.setText("Un t-shirt adidas à gagner");
            Picasso.with(context)
                    .load(R.drawable.chase1)
                    .into(holder.mImageView);
        }
        else if (position == 2)
        {
           // holder.mImageView.setImageResource(R.drawable.run2);
            holder.mTextView.setText("Un chèque cadeau de 50 euros");
            Picasso.with(context)
                    .load(R.drawable.chase2)
                    .into(holder.mImageView);
        }
        //holder.capacity_max.setText(game_list.get(position).getCapacity() + "");//game_list.get(position).getCapacityMax());


    }
    @Override
    public int getItemCount() {
        return Chases_list.size();
    }



}
