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

import LoginRegister.Laptop;

/**
 * Created by OK on 4/9/2018.
 */

public class LaptopAdapter extends RecyclerView.Adapter<LaptopAdapter.MyViewHolder> {

    Context context;
    public List<Laptop> laptopList;
    public ArrayList<Laptop> selected_laptopList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView noLaptop, qty, kdProcessor, ram;
        public RelativeLayout rl_listItemLaptop;

        public MyViewHolder(View view) {
            super(view);
            noLaptop = (TextView) view.findViewById(R.id.noLaptop);
            qty = (TextView) view.findViewById(R.id.qty);
            kdProcessor = (TextView) view.findViewById(R.id.kdProcessor);
            ram = (TextView) view.findViewById(R.id.ram);
            rl_listItemLaptop = (RelativeLayout) view.findViewById(R.id.rl_listItemLaptop);
        }
    }

    public LaptopAdapter(Context context, List<Laptop> laptopList, ArrayList<Laptop> selectedList) {
        this.context = context;
        this.laptopList = laptopList;
        this.selected_laptopList = selectedList;
    }

    @Override
    public LaptopAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.laptop_list_row, parent, false);

        return new LaptopAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Laptop laptop = laptopList.get(position);

        holder.noLaptop.setText(laptop.getNo_Laptop());
        holder.qty.setText(String.valueOf(laptop.getQty_total()) + " " + laptop.getSatuan());
        holder.kdProcessor.setText(laptop.getKd_processor());
        holder.ram.setText(laptop.getRam());

        if(selected_laptopList.contains(laptopList.get(position)))
            holder.rl_listItemLaptop.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
        else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            holder.rl_listItemLaptop.setBackgroundResource(outValue.resourceId);
        }

    }

    @Override
    public int getItemCount() {
        return laptopList.size();
    }

    public void removeItem(int position) {
        laptopList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, laptopList.size());
    }

    public void restoreItem(Laptop item, int position) {
        laptopList.add(position, item);
        notifyItemInserted(position);
    }

    public void setFilter(List<Laptop> merek) {
        laptopList = new ArrayList<>();
        laptopList.addAll(merek);
        notifyDataSetChanged();
    }

}
