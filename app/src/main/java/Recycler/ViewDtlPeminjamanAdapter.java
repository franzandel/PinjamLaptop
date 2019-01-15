package Recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ok.pinjamlaptop.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by OK on 6/7/2018.
 */

public class ViewDtlPeminjamanAdapter extends BaseAdapter {
    private ArrayList<HashMap<String,String>> peminjamanList;
    public ViewDtlPeminjamanAdapter(ArrayList<HashMap<String, String>> peminjamanList) {
        this.peminjamanList = peminjamanList;
    }

    @Override
    public int getCount() {
        return peminjamanList.size();
    }

    @Override
    public Object getItem(int position) {
        return peminjamanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_dtl_p_pinjam, parent, false);

        ((TextView) view.findViewById(R.id.tvNoLaptop)).setText(peminjamanList.get(position).get("NoLaptop"));
        ((TextView) view.findViewById(R.id.tvQtyPinjam)).setText(peminjamanList.get(position).get("QtyPinjam"));
        ((TextView) view.findViewById(R.id.tvKeperluan)).setText(peminjamanList.get(position).get("Keperluan"));
        return view;
    }
}
