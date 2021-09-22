package hlccd.regular.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import hlccd.regular.R;
import hlccd.regular.util.GV_data;

public class GV_adapter extends BaseAdapter {
    private Context       context;
    private List<GV_data> images;

    public GV_adapter(Context context, List<GV_data> images) {
        this.context = context;
        this.images  = images;
    }

    static class ViewHolder {
        public ImageView imageView;
        public TextView  describe;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        if (view == null) {
            view             = LayoutInflater.from(context).inflate(R.layout.gv_data, null);
            holder.imageView = (ImageView) view.findViewById(R.id.image);
            holder.describe  = (TextView) view.findViewById(R.id.describe);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        final GV_data image = images.get(position);
        holder.imageView.setImageResource(image.getImage());
        holder.describe.setText(image.getDescribe());
        holder.describe.setTextColor(view.getContext().getResources().getColor(R.color.mainColor));
        return view;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int position) {
        return images.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
