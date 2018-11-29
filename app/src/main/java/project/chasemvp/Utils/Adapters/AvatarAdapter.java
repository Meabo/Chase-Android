package project.chasemvp.Utils.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import project.chasemvp.Model.POJO.Avatar;
import project.chasemvp.R;

/**
 * Created by Mehdi on 03/07/2017.
 */

public class AvatarAdapter extends RecyclerView.Adapter<AvatarAdapter.ViewHolder> {



    private ArrayList<Avatar> avatars;

    public ArrayList<Avatar> getAvatars() {
        return avatars;
    }
    public AvatarAdapter() {
       avatars =  new ArrayList<>();
    }

    public void addAvatars(List<Avatar> avatarList)
    {
        avatars.clear();
        avatars.addAll(avatarList);
        notifyDataSetChanged();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.avatar_layout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position)
    {
        Picasso.with(holder.itemView.getContext())
                .load(avatars.get(position).getUrl_image())
                .into(holder.image);

       // holder.image.setImageResource(R.drawable.docmehdi);

    }

    @Override
    public int getItemCount() {
        return avatars.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.avatar_image);
        }
    }
}