package hlccd.regular.Bill;
/**
 * 账务预算记录适配器
 * 该适配器主要为了适配每一条预算记录
 * 该记录点击delete即可删除该笔记录
 * 记录删除后进行通信以便刷新该界面
 *
 * @author hlccd 2020.6.10
 * @version 1.0
 */

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.List;

import hlccd.regular.R;
import hlccd.regular.User.User;

public class Bill_budget_data_adapter extends RecyclerView.Adapter<Bill_budget_data_adapter.ViewHolder> {
    public List<Bill_budget_data> budgets;
    public Context                context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView  category;
        ImageView delete;
        TextView  surplus;
        TextView  spending;
        TextView  plan;

        public ViewHolder(View view) {
            super(view);
            category = view.findViewById(R.id.bill_budget_data_category);
            delete   = view.findViewById(R.id.bill_budget_data_delete);
            surplus  = view.findViewById(R.id.bill_budget_data_surplus);
            spending = view.findViewById(R.id.bill_budget_data_spending);
            plan     = view.findViewById(R.id.bill_budget_data_plan);
        }
    }

    public Bill_budget_data_adapter(List<Bill_budget_data> budgets, Context context) {
        this.budgets = budgets;
        this.context = context;
    }

    @Override
    public Bill_budget_data_adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bill_budget_data, parent, false);
        return new Bill_budget_data_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(Bill_budget_data_adapter.ViewHolder holder, final int position) {
        final Bill_budget_data data = budgets.get(position);
        holder.category.setText(data.getCategory());
        holder.surplus.setText(String.valueOf(data.getPlan() - data.getSpending()));
        holder.spending.setText(String.valueOf(data.getSpending()));
        holder.plan.setText(String.valueOf(data.getPlan()));
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //锁定该笔预算记录以便进行准确删除
                short                  year    = Short.valueOf(new SimpleDateFormat("yyyy").format(System.currentTimeMillis()));
                short                  month   = Short.valueOf(new SimpleDateFormat("MM").format(System.currentTimeMillis()));
                List<Bill_budget_data> budgets = LitePal.findAll(Bill_budget_data.class);
                for (Bill_budget_data budget : budgets) {
                    if (budget.getYear() == year && budget.getMonth() == month && budget.getU_id().equals(User.getUserId())) {
                        if (budget.getCategory().equals(data.getCategory())) {
                            budget.delete();
                            break;
                        }
                    }
                }
                //删除后发送消息进行界面刷新
                context.sendBroadcast(new Intent("android.refresh.Bill_budget"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return budgets.size();
    }
}
