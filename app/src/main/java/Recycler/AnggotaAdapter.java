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

import LoginRegister.Anggota;

/**
 * Created by ryuze on 6/25/2018.
 */

public class AnggotaAdapter extends RecyclerView.Adapter<AnggotaAdapter.MyViewHolder> {

    Context context;
    public List<Anggota> anggotaList;
    public ArrayList<Anggota> selected_anggotaList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView noAnggota, nmAnggota, gender;
        public RelativeLayout rl_listItemAnggota;

        public MyViewHolder(View view) {
            super(view);
            noAnggota = (TextView) view.findViewById(R.id.noAnggota);
            nmAnggota = (TextView) view.findViewById(R.id.nmAnggota);
            gender = (TextView) view.findViewById(R.id.gender);
            rl_listItemAnggota = (RelativeLayout) view.findViewById(R.id.rl_listItemAnggota);
        }
    }

    public AnggotaAdapter(Context context, List<Anggota> anggotaList, ArrayList<Anggota> selectedList) {
        this.context = context;
        this.anggotaList = anggotaList;
        this.selected_anggotaList = selectedList;
    }

    @Override
    public AnggotaAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.anggota_list_row, parent, false);

        return new AnggotaAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Anggota anggota = anggotaList.get(position);

        holder.noAnggota.setText(anggota.getNomorAnggota());
        holder.nmAnggota.setText(anggota.getNamaAnggota());
        holder.gender.setText(anggota.getGender());

        if(selected_anggotaList.contains(anggotaList.get(position)))
            holder.rl_listItemAnggota.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
        else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            holder.rl_listItemAnggota.setBackgroundResource(outValue.resourceId);
        }

    }

    @Override
    public int getItemCount() {
        return anggotaList.size();
    }

    public void removeItem(int position) {
        anggotaList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, anggotaList.size());
    }

    public void restoreItem(Anggota item, int position) {
        anggotaList.add(position, item);
        notifyItemInserted(position);
    }

    public void setFilter(List<Anggota> merek) {
        anggotaList = new ArrayList<>();
        anggotaList.addAll(merek);
        notifyDataSetChanged();
    }

}
