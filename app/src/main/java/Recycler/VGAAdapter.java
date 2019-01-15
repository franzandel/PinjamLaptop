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

import LoginRegister.VGA;

/**
 * Created by ryuze on 6/25/2018.
 */

public class VGAAdapter extends RecyclerView.Adapter<VGAAdapter.MyViewHolder> {

    Context context;
    public List<VGA> vgaList;
    public ArrayList<VGA> selected_vgaList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kdVGA, nmVGA;
        public RelativeLayout rl_listItemVGA;

        public MyViewHolder(View view) {
            super(view);
            kdVGA = (TextView) view.findViewById(R.id.kdVGA);
            nmVGA = (TextView) view.findViewById(R.id.nmVGA);
            rl_listItemVGA = (RelativeLayout) view.findViewById(R.id.rl_listItemVGA);
        }
    }

    public VGAAdapter(Context context, List<VGA> vgaList, ArrayList<VGA> selectedList) {
        this.context = context;
        this.vgaList = vgaList;
        this.selected_vgaList = selectedList;
    }

    @Override
    public VGAAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vga_list_row, parent, false);

        return new VGAAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        VGA vga = vgaList.get(position);

        holder.kdVGA.setText(vga.getKodeVGA());
        holder.nmVGA.setText(vga.getNamaVGA());

        if(selected_vgaList.contains(vgaList.get(position)))
            holder.rl_listItemVGA.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
        else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            holder.rl_listItemVGA.setBackgroundResource(outValue.resourceId);
        }

    }

    @Override
    public int getItemCount() {
        return vgaList.size();
    }

    public void removeItem(int position) {
        vgaList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, vgaList.size());
    }

    public void restoreItem(VGA item, int position) {
        vgaList.add(position, item);
        notifyItemInserted(position);
    }

    public void setFilter(List<VGA> merek) {
        vgaList = new ArrayList<>();
        vgaList.addAll(merek);
        notifyDataSetChanged();
    }

}
