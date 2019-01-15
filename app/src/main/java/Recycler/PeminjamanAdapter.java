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

import LoginRegister.Peminjaman;

/**
 * Created by OK on 4/9/2018.
 */

public class PeminjamanAdapter extends RecyclerView.Adapter<PeminjamanAdapter.MyViewHolder> {

    // TODO: LIST & ARRAYLIST BEHAVES IDENTICALLY (TUTORIAL : BOTH ARRAYLIST)
    public List<Peminjaman> peminjamanList;
    public ArrayList<Peminjaman> selected_peminjamanList = new ArrayList<>();
    Context context;
    public static final String DATE_FORMAT_DB = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_NEW = "dd-MM-yyyy";
    SimpleDateFormat sdfInput, sdfOutput;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kdPinjam, noAnggota, tglPinjam;
        public RelativeLayout rl_listItemPeminjaman;

        public MyViewHolder(View view) {
            super(view);
            kdPinjam = (TextView) view.findViewById(R.id.kdPinjam);
            noAnggota = (TextView) view.findViewById(R.id.noAnggota);
            tglPinjam = (TextView) view.findViewById(R.id.tglPinjam);
            rl_listItemPeminjaman = (RelativeLayout) view.findViewById(R.id.rl_listItemPeminjaman);
            sdfInput = new SimpleDateFormat(DATE_FORMAT_DB);
            sdfOutput = new SimpleDateFormat(DATE_FORMAT_NEW);
        }
    }


    public PeminjamanAdapter(Context context, List<Peminjaman> peminjamanList, ArrayList<Peminjaman> selectedList) {
        this.context=context;
        this.peminjamanList = peminjamanList;
        this.selected_peminjamanList = selectedList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.peminjaman_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        try {
            Peminjaman peminjaman = peminjamanList.get(position);
            Date changeTglPinjam = sdfInput.parse(peminjaman.getTgl_Pinjam());
            String tglPinjam = sdfOutput.format(changeTglPinjam);

            holder.kdPinjam.setText(peminjaman.getKd_Pinjam());
            holder.noAnggota.setText(peminjaman.getNo_Anggota());
            holder.tglPinjam.setText(tglPinjam);

            if(selected_peminjamanList.contains(peminjamanList.get(position)))
                holder.rl_listItemPeminjaman.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
            else {
                TypedValue outValue = new TypedValue();
                context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
                holder.rl_listItemPeminjaman.setBackgroundResource(outValue.resourceId);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        return peminjamanList.size();
    }

    public void removeItem(int position) {
        peminjamanList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, peminjamanList.size());
    }

    public void restoreItem(Peminjaman item, int position) {
        peminjamanList.add(position, item);
        notifyItemInserted(position);
    }

    public void setFilter(List<Peminjaman> peminjamen) {
        peminjamanList = new ArrayList<>();
        peminjamanList.addAll(peminjamen);
        notifyDataSetChanged();
    }
}
