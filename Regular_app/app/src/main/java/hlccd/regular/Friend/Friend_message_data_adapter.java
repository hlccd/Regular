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

public class Friend_message_data_adapter extends RecyclerView.Adapter<Friend_message_data_adapter.ViewHolder> {
    public Homepage                  homepage;
    public List<Friend_message_data> messages;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView        portrait;
        TextView         name;
        TextView         mes;

        public ViewHolder(View view) {
            super(view);
            layout   = view.findViewById(R.id.friend_message_data_layout);
            portrait = view.findViewById(R.id.friend_message_data_portrait);
            name     = view.findViewById(R.id.friend_message_data_name);
            mes      = view.findViewById(R.id.friend_message_data_mes);
        }
    }

    public Friend_message_data_adapter(Homepage homepage, List<Friend_message_data> messages) {
        this.homepage = homepage;
        this.messages = messages;
    }

    @Override
    public Friend_message_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_message_data, parent, false);
        return new Friend_message_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Friend_message_data_adapter.ViewHolder holder, int position) {
        Friend_message_data message = messages.get(position);
        holder.name.setText(message.getName());
        holder.portrait.setImageResource(message.getPortrait());
        holder.mes.setText(message.getMes());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(view.getContext(), Friend_chat.class);
                intent.putExtra("Friend_user_chat", String.valueOf(message.getSender()));
                intent.putExtra("Friend_group_chat", String.valueOf(message.getGroup()));
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}