package hlccd.regular.My;
/**
 * 月度报表记录适配器
 * 该适配器主要为了适配每一条月度报表记录并展示
 *
 * @author hlccd 2020.6.19
 * @version 1.0
 */
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import hlccd.regular.R;

import java.util.List;

public class My_statement_data_adapter extends RecyclerView.Adapter<My_statement_data_adapter.ViewHolder> {
    public List<My_statement_data> statements;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView ratio;
        TextView cheek;

        public ViewHolder(View view) {
            super(view);
            //适配界面中的控件
            name  = view.findViewById(R.id.my_statement_data_name);
            ratio = view.findViewById(R.id.my_statement_data_radio);
            cheek = view.findViewById(R.id.my_statement_data_cheek);
        }
    }

    public My_statement_data_adapter(List<My_statement_data> statements) {
        this.statements = statements;
    }

    @Override
    public My_statement_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_statement_data, parent, false);
        return new My_statement_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(My_statement_data_adapter.ViewHolder holder, int position) {
        //从该月的报表中取出某一条记录并展示
        My_statement_data statement = statements.get(position);
        holder.name.setText(statement.getName());
        //如果该记录的金额小于0.005即小于最小金额的一半，则视为0
        if (statement.getCheek() < 0.005) {
            holder.ratio.setText("0.00%");
        } else {
            holder.ratio.setText(String.format("%.2f", statement.getCheek() / statements.get(0).getCheek() * 100) + "%");
        }
        holder.cheek.setText(String.format("%.2f", statement.getCheek()));
    }

    @Override
    public int getItemCount() {
        return statements.size();
    }
}
