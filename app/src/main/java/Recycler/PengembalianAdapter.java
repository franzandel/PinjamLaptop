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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import LoginRegister.Pengembalian;

/**
 * Created by OK on 4/9/2018.
 */

public class PengembalianAdapter extends RecyclerView.Adapter<PengembalianAdapter.MyViewHolder> {
    public List<Pengembalian> pengembalianList;
    public ArrayList<Pengembalian> selected_pengembalianList = new ArrayList<>();
    public static final String DATE_FORMAT_DB = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_NEW = "dd-MM-yyyy";
    SimpleDateFormat sdfInput, sdfOutput;
    Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kdPinjam, tglPinjam, flowKembali;
        public RelativeLayout rl_listItemPengembalian;

        public MyViewHolder(View view) {
            super(view);
            kdPinjam = (TextView) view.findViewById(R.id.kdPinjam);
            tglPinjam = (TextView) view.findViewById(R.id.tglPinjam);
            flowKembali = (TextView) view.findViewById(R.id.flowKembali);
            rl_listItemPengembalian = (RelativeLayout) view.findViewById(R.id.rl_listItemPengembalian);
            sdfInput = new SimpleDateFormat(DATE_FORMAT_DB);
            sdfOutput = new SimpleDateFormat(DATE_FORMAT_NEW);
        }
    }


    public PengembalianAdapter(Context context, List<Pengembalian> pengembalianList, ArrayList<Pengembalian> selectedList) {
        this.context = context;
        this.pengembalianList = pengembalianList;
        this.selected_pengembalianList = selectedList;
    }

    @Override
    public PengembalianAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pengembalian_list_row, parent, false);
        context = parent.getContext();

        return new PengembalianAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PengembalianAdapter.MyViewHolder holder, int position) {
        try {
            Pengembalian pengembalian = pengembalianList.get(position);
            Date changeTglPinjam = sdfInput.parse(pengembalian.getTgl_Pinjam());
            String tglPinjam = sdfOutput.format(changeTglPinjam);
            if (pengembalian.getFlowKembali().equals(context.getString(R.string.flowDone))) {
                holder.flowKembali.setText(R.string.flowDone);
                holder.flowKembali.setTextColor(ContextCompat.getColor(context, R.color.flowDone));
            } else if (pengembalian.getFlowKembali().equals(context.getString(R.string.flowPartial))) {
                holder.flowKembali.setText(R.string.flowPartial);
                holder.flowKembali.setTextColor(ContextCompat.getColor(context, R.color.flowPartial));
            } else {
                holder.flowKembali.setText(R.string.flowNone);
                holder.flowKembali.setTextColor(ContextCompat.getColor(context, R.color.flowNone));
            }

            holder.kdPinjam.setText(pengembalian.getKd_Pinjam());
            holder.tglPinjam.setText(tglPinjam);

            if(selected_pengembalianList.contains(pengembalianList.get(position)))
                holder.rl_listItemPengembalian.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
            else {
                TypedValue outValue = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                holder.rl_listItemPengembalian.setBackgroundResource(outValue.resourceId);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return pengembalianList.size();
    }

    public void removeItem(int position) {
        pengembalianList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, pengembalianList.size());
    }

    public void restoreItem(Pengembalian item, int position) {
        pengembalianList.add(position, item);
        notifyItemInserted(position);
    }

    public void setItem(Pengembalian item, int position) {
        pengembalianList.set(position, item);
        notifyItemChanged(position);
    }

    public void setFilter(List<Pengembalian> pengembalian) {
        pengembalianList = new ArrayList<>();
        pengembalianList.addAll(pengembalian);
        notifyDataSetChanged();
    }
}
