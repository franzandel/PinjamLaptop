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

public class ViewDtlPengembalianAdapter extends BaseAdapter {
    private ArrayList<HashMap<String,String>> pengembalianList;
    public ViewDtlPengembalianAdapter(ArrayList<HashMap<String, String>> pengembalianList) {
        this.pengembalianList = pengembalianList;
    }

    @Override
    public int getCount() {
        return pengembalianList.size();
    }

    @Override
    public Object getItem(int position) {
        return pengembalianList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_dtl_kembali, parent, false);

        ((TextView) view.findViewById(R.id.tvKdInventaris)).setText(pengembalianList.get(position).get("KdInventaris"));
        ((TextView) view.findViewById(R.id.tvTglKembali)).setText(pengembalianList.get(position).get("TglKembali"));
        ((TextView) view.findViewById(R.id.tvStatusKembali)).setText(pengembalianList.get(position).get("StsKembali"));
        return view;
    }
}
