package hlccd.regular.Friend;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import hlccd.regular.API.IM;
import hlccd.regular.Homepage;
import hlccd.regular.R;
import hlccd.regular.User.User;

public class Friend_search_data_adapter extends RecyclerView.Adapter<Friend_search_data_adapter.ViewHolder> {
    public List<Friend_search_data> searchs;

    static class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout layout;
        ImageView        portrait;
        TextView         name;
        TextView         id;
        ImageView        add;

        public ViewHolder(View view) {
            super(view);
            layout   = view.findViewById(R.id.friend_search_data_layout);
            portrait = view.findViewById(R.id.friend_search_data_portrait);
            name     = view.findViewById(R.id.friend_search_data_name);
            id       = view.findViewById(R.id.friend_search_data_id);
            add      = view.findViewById(R.id.friend_search_data_add);
        }
    }

    public Friend_search_data_adapter( List<Friend_search_data> searchs) {
        this.searchs  = searchs;
    }

    @Override
    public Friend_search_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_search_data, parent, false);
        return new Friend_search_data_adapter.ViewHolder(view);
    }

    @SuppressLint ("SetTextI18n")
    @Override
    public void onBindViewHolder(Friend_search_data_adapter.ViewHolder holder, int position) {
        Friend_search_data search = searchs.get(position);
        holder.name.setText(search.getName());
        holder.portrait.setImageResource(search.getPortrait());
        holder.id.setText(search.getId() + "");
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.add.setVisibility(View.GONE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject json = new JSONObject();
                        try {
                            json.put("mes", "");
                            json.put("id", User.getUserId());
                            if (search.getType().equals("user")){
                                json.put("receiver", search.getId());
                                json.put("type", 3);
                                json.put("group", 0);
                            }else{
                                json.put("receiver", 0);
                                json.put("type", 4);
                                json.put("group", search.getId());
                            }
                            json.put("timestamp", System.currentTimeMillis());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (!IM.getSocket().isClosed()) {
                            IM.getWriter().println(json);
                            IM.getWriter().flush();
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchs.size();
    }
}
