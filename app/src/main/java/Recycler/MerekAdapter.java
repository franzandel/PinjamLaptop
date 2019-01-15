package Recycler;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ok.pinjamlaptop.R;

import java.util.ArrayList;
import java.util.List;

import LoginRegister.Merek;

/**
 * Created by ryuze on 6/25/2018.
 */

public class MerekAdapter extends RecyclerView.Adapter<MerekAdapter.MyViewHolder> {

    Context context;
    public List<Merek> merekList;
    public ArrayList<Merek> selected_merekList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kdMerek, nmMerek;
        public RelativeLayout rl_listItemMerek;

        public MyViewHolder(View view) {
            super(view);
            kdMerek = (TextView) view.findViewById(R.id.kdMerek);
            nmMerek = (TextView) view.findViewById(R.id.nmMerek);
            rl_listItemMerek = (RelativeLayout) view.findViewById(R.id.rl_listItemMerek);
        }
    }

    public MerekAdapter(Context context, List<Merek> merekList, ArrayList<Merek> selectedList) {
        this.context = context;
        this.merekList = merekList;
        this.selected_merekList = selectedList;
    }

    @Override
    public MerekAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.merek_list_row, parent, false);

        return new MerekAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Merek merek = merekList.get(position);

        holder.kdMerek.setText(merek.getKodeMerek());
        holder.nmMerek.setText(merek.getNamaMerek());

        if(selected_merekList.contains(merekList.get(position)))
            holder.rl_listItemMerek.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
        else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            holder.rl_listItemMerek.setBackgroundResource(outValue.resourceId);
        }

    }

    @Override
    public int getItemCount() {
        return merekList.size();
    }

    public void removeItem(int position) {
        merekList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, merekList.size());
    }

    public void restoreItem(Merek item, int position) {
        merekList.add(position, item);
        notifyItemInserted(position);
    }

    public void setFilter(List<Merek> merek) {
        merekList = new ArrayList<>();
        merekList.addAll(merek);
        notifyDataSetChanged();
    }

}
