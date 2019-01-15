package Recycler;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ok.pinjamlaptop.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by OK on 6/7/2018.
 */

public class DtlPeminjamanAdapter extends BaseAdapter {
    private ArrayList<HashMap<String,String>> data;
    private int res;
    AlertDialog dialog;
    public DtlPeminjamanAdapter(ArrayList<HashMap<String, String>> data, int res) {
        this.data = data;
        this.res = res;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dtl_peminjaman_row_view, parent, false);
        view.findViewById(R.id.ibDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(parent.getContext());

                builder.setTitle("Konfirmasi");
                builder.setMessage("Apakah anda yakin ingin menghapus "
                        + data.get(position).get("NoLaptop") + "?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(parent.getContext(), data.get(position).get("NoLaptop")
                                + " berhasil dihapus", Toast.LENGTH_SHORT).show();
                        remove(position);
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog = builder.create();
                dialog.show();
            }
        });
        ((TextView) view.findViewById(R.id.tvNoLaptop)).setText(data.get(position).get("NoLaptop"));
        ((TextView) view.findViewById(R.id.tvQtyPinjam)).setText(data.get(position).get("QtyPinjam"));
        ((TextView) view.findViewById(R.id.tvKeperluan)).setText(data.get(position).get("Keperluan"));
        return view;
    }


    public void remove(int position) {
        data.remove(position);
        notifyDataSetChanged();
    }
}
