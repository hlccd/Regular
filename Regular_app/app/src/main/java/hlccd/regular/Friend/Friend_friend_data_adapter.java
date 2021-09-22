package hlccd.regular.Friend;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hlccd.regular.Homepage;
import hlccd.regular.R;

public class Friend_friend_data_adapter extends RecyclerView.Adapter<Friend_friend_data_adapter.ViewHolder> {
    public List<Friend_friend_data> friends;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView        portrait;
        TextView         name;
        TextView         id;

        public ViewHolder(View view) {
            super(view);
            layout   = view.findViewById(R.id.layout);
            portrait = view.findViewById(R.id.portrait);
            name     = view.findViewById(R.id.name);
            id       = view.findViewById(R.id.id);
        }
    }

    public Friend_friend_data_adapter(List<Friend_friend_data> friends) {
        this.friends = friends;
    }

    @Override
    public Friend_friend_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_friend_data, parent, false);
        return new Friend_friend_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Friend_friend_data_adapter.ViewHolder holder, int position) {
        Friend_friend_data friend = friends.get(position);
        holder.name.setText(friend.getName());
        holder.portrait.setImageResource(friend.getPortrait());
        holder.id.setText(friend.getFriend()+"");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Friend_chat.class);
                intent.putExtra("Friend_user_chat", String.valueOf(friend.getFriend()));
                intent.putExtra("Friend_group_chat", String.valueOf(0));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }
}
