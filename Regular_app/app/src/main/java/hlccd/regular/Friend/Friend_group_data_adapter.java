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

import hlccd.regular.R;

public class Friend_group_data_adapter extends RecyclerView.Adapter<Friend_group_data_adapter.ViewHolder> {
    public List<Friend_group_data> groups;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView        portrait;
        TextView         name;
        TextView         group;

        public ViewHolder(View view) {
            super(view);
            layout   = view.findViewById(R.id.layout);
            portrait = view.findViewById(R.id.portrait);
            name     = view.findViewById(R.id.name);
            group      = view.findViewById(R.id.group);
        }
    }

    public Friend_group_data_adapter( List<Friend_group_data> groups) {
        this.groups = groups;
    }

    @Override
    public Friend_group_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_group_data, parent, false);
        return new Friend_group_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Friend_group_data_adapter.ViewHolder holder, int position) {
        Friend_group_data group = groups.get(position);
        holder.name.setText(group.getName());
        holder.portrait.setImageResource(group.getPortrait());
        holder.group.setText(group.getGroup()+"");
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Friend_chat.class);
                intent.putExtra("Friend_user_chat", String.valueOf(0));
                intent.putExtra("Friend_group_chat", group.getGroup()+"");
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
