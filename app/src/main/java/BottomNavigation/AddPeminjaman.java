package BottomNavigation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ok.pinjamlaptop.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import LoginRegister.InputValidation;
import LoginRegister.Peminjaman;
import LoginRegister.Pengembalian;
import Recycler.DtlPeminjamanAdapter;
import dmax.dialog.SpotsDialog;

/**
 * Created by OK on 4/12/2018.
 */

public class AddPeminjaman extends AppCompatActivity implements View.OnClickListener{

    private View activityAddPinjaman;
    private TextInputEditText tietKdPinjam;
    private TextInputLayout tilKdPinjam;
    private TextView tTglPinjam;
    private InputValidation inputValidation;
    private DatePickerDialog datePickerDialog;
    private int cYear, cMonth, cDay;
    private Calendar calendar, cTglPinjam;
    private ImageButton ibToolbarBack, ibAdd;
    private final ArrayList<HashMap<String,String>> listHashPengambilan = new ArrayList<HashMap<String,String>>();
    private Spinner spNoAnggota, spNoLaptop;
    private EditText etQtyPinjam, etKeperluan;
    private ListView listDtlPeminjaman;
    public static final String DATE_FORMAT_SAVE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_RETRIEVEOUTPUT = "d/M/yyyy";
    SimpleDateFormat sdfSave, sdfRetrieveOutput;
    ArrayAdapter<String> adapterSpNoAnggota, adapterSpNoLaptop;
    DtlPeminjamanAdapter dtlPeminjamanAdapter;
    AlertDialog dialog;
    private ArrayList<String> anggotaList = new ArrayList<String>();
    private ArrayList<String> laptopList = new ArrayList<String>();
    private String KD_PINJAM, NO_ANGGOTA;
    private Menu menu;
    MenuItem btSave;
    private String stietKdPinjam, sspNoAnggota;
    private ArrayList<HashMap<String,String>> beforeEditedlistHash = new ArrayList<HashMap<String,String>>();
    private android.app.AlertDialog alertDialog;
    private final ArrayList<HashMap<String,String>> listHashQtyPinjam = new ArrayList<HashMap<String,String>>();
    private int POSITION;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpeminjaman);
        activityAddPinjaman = (View)findViewById(R.id.activityAddPinjaman);
        tietKdPinjam = (TextInputEditText)findViewById(R.id.tietKdPinjam);
        tilKdPinjam = (TextInputLayout) findViewById(R.id.tilKdPinjam);
        tTglPinjam = (TextView)findViewById(R.id.tvTglPinjam);
        ibToolbarBack = (ImageButton) findViewById(R.id.ibToolbarBack);
        ibAdd = (ImageButton) findViewById(R.id.ibAdd);
        spNoLaptop = (Spinner) findViewById(R.id.spNoLaptop);
        spNoAnggota = (Spinner) findViewById(R.id.spNoAnggota);
        etQtyPinjam = (EditText) findViewById(R.id.etQtyPinjam);
        etKeperluan = (EditText) findViewById(R.id.etKeperluan);
        listDtlPeminjaman = (ListView)findViewById(R.id.listDtlPeminjaman);
        calendar = Calendar.getInstance();
        cYear = calendar.get(Calendar.YEAR);
        cMonth = calendar.get(Calendar.MONTH);
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        sdfSave = new SimpleDateFormat(DATE_FORMAT_SAVE);
        sdfRetrieveOutput = new SimpleDateFormat(DATE_FORMAT_RETRIEVEOUTPUT);
        inputValidation = new InputValidation(this);
        this.setTitle(null);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar_activity);
        setSupportActionBar(mToolbar);
        /*default back in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        getSpAnggotaData();
        getSpLaptopData();

        getValidasiQtyPinjam();

        Intent intent = getIntent();
        if (intent.getStringExtra("KD_PINJAM") != null) {
            try {
                KD_PINJAM = intent.getStringExtra("KD_PINJAM");
                NO_ANGGOTA = intent.getStringExtra("NO_ANGGOTA");
                String TGL_PINJAM = intent.getStringExtra("TGL_PINJAM");
                POSITION = intent.getIntExtra("POSITION", 0);

                Date changeTglPinjam = sdfSave.parse(TGL_PINJAM);
                cTglPinjam = cTglPinjam.getInstance();
                cTglPinjam.setTime(changeTglPinjam);
                String tglPinjam = sdfRetrieveOutput.format(changeTglPinjam);

                getP_Pinjam();

                tietKdPinjam.setText(KD_PINJAM);
                getTimerspNoAnggota();
                tTglPinjam.setText(tglPinjam);

                tietKdPinjam.setEnabled(false);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        tTglPinjam.setOnClickListener(this);
        ibToolbarBack.setOnClickListener(this);
        ibAdd.setOnClickListener(this);

        listDtlPeminjaman.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> data = (HashMap<String, String>) parent.getAdapter().getItem(position);
                spNoLaptop.setSelection(adapterSpNoLaptop.getPosition(data.get("NoLaptop")));
                etQtyPinjam.setText(data.get("QtyPinjam"));
                etKeperluan.setText(data.get("Keperluan"));
            }
        });
    }

    private void getValidasiQtyPinjam() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
                viewQtyPinjam();
            }
        }, 450);
    }

    public void viewQtyPinjam() {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        String url = Config.URL_VALIDASI_QTY_PINJAM;
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showQtyPinjam(response);
//                progressBar.setVisibility(View.GONE);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(stringRequest);
    }

    public void showQtyPinjam(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            listHashQtyPinjam.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("NoLaptop", jo.getString(Config.TAG_NO_LAPTOP));
                temp.put("QtyTotal", jo.getString(Config.TAG_QTY_TOTAL));
                temp.put("Satuan", jo.getString(Config.TAG_SATUAN));
                temp.put("QtyBaik", jo.getString(Config.TAG_QTY_BAIK));
                temp.put("QtyRusak", jo.getString(Config.TAG_QTY_RUSAK));
                temp.put("QtyHilang", jo.getString(Config.TAG_QTY_HILANG));
                temp.put("QtyPinjam", jo.getString(Config.TAG_QTY_PINJAM));
                listHashQtyPinjam.add(temp);
            }
        } catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    private void getP_Pinjam() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
                viewP_Pinjam();
            }
        }, 450);
    }

    public void viewP_Pinjam() {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        String url = Config.URL_VIEW_P_PINJAM  + KD_PINJAM + "'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showP_Pinjam(response);
//                progressBar.setVisibility(View.GONE);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.add(stringRequest);
    }

    public void showP_Pinjam(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            listHashPengambilan.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("NoLaptop", jo.getString(Config.TAG_NO_LAPTOP));
                temp.put("QtyPinjam", jo.getString(Config.TAG_QTY_PINJAM));
                temp.put("Keperluan", jo.getString(Config.TAG_KEPERLUAN));
                listHashPengambilan.add(temp);
            }

            // TODO: 6/19/2018 : NOT USING
            // beforeEditedlistHash = new ArrayList<HashMap<String,String>>(listHashPengambilan);
            // beforeEditedlistHash.addAll(listHashPengambilan);
            // BECAUSE
            // THEY REFER TO THE SAME OBJECTS OR CONTENTS
            beforeEditedlistHash = new ArrayList<HashMap<String,String>>(listHashPengambilan.size());
            for (Map<String, String> item : listHashPengambilan) {
                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("NoLaptop", item.get("NoLaptop"));
                temp.put("QtyPinjam", item.get("QtyPinjam"));
                temp.put("Keperluan", item.get("Keperluan"));
                beforeEditedlistHash.add(temp);
            }

            dtlPeminjamanAdapter = new DtlPeminjamanAdapter(listHashPengambilan, R.layout.dtl_peminjaman_row_view);
            listDtlPeminjaman.setAdapter(dtlPeminjamanAdapter);
            alertDialog.dismiss();
        } catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    private int getSpinnerIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    private boolean updateLaptopExistInList(String noLaptop, String qtyPinjam, String keperluan) {
        for (final Map<String, String> entry : listHashPengambilan) {
            if (entry.get("NoLaptop").equals(noLaptop)) {
                entry.put("QtyPinjam",qtyPinjam);
                entry.put("Keperluan",keperluan);
                listDtlPeminjaman.setAdapter(dtlPeminjamanAdapter);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;
        btSave = menu.findItem(R.id.Save);
        if (KD_PINJAM != null) {
            btSave.setTitle("Update");
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Save:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                if (btSave.getTitle().equals("Save")) {
                    builder.setTitle("Konfirmasi");
                    builder.setMessage("Apakah anda yakin ingin menyimpan data?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AddPeminjaman();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                } else {
                    builder.setTitle("Konfirmasi");
                    builder.setMessage("Apakah anda yakin ingin mengupdate data?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ModifyPeminjaman();
                            finish();
                        }
                    });

                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }

                dialog = builder.create();
                dialog.show();
                return true;
            /*default back in toolbar
            case android.R.id.home:
                finish();
                return true;*/
            default:
                return false;
        }
    }

    private void populateList(String noLaptop, String qtyPinjam, String keperluan) {
        HashMap<String,String> temp = new HashMap<String,String>();
        temp.put("NoLaptop", noLaptop);
        temp.put("QtyPinjam", qtyPinjam);
        temp.put("Keperluan", keperluan);
        listHashPengambilan.add(temp);
    }

    private void AddPeminjaman() {
        if (!inputValidation.isInputEditTextFilled(tietKdPinjam, tilKdPinjam, "Kode Pinjam harus diisi")) {
            return;
        }
        if (!inputValidation.isInputSpinnerAnggotaChoosed(spNoAnggota, "Anggota harus dipilih")) {
            return;
        }
        if (!inputValidation.isInputTextViewDateChoosed(tTglPinjam, "Tanggal Pinjam harus diisi")) {
            return;
        }
        if (!inputValidation.isListViewEmpty(listHashPengambilan, "Harus ada No. Laptop yang diisi")) {
            return;
        }
        if (isQtyPinjamMinus()) {
            return;
        }

        final Peminjaman peminjaman = new Peminjaman();
        peminjaman.setKd_Pinjam(tietKdPinjam.getText().toString());
        peminjaman.setTgl_Pinjam(sdfSave.format(cTglPinjam.getTime()));
        peminjaman.setNo_Anggota(spNoAnggota.getSelectedItem().toString());

        final Pengembalian pengembalian = new Pengembalian();
        pengembalian.setKd_Pinjam(tietKdPinjam.getText().toString());
        pengembalian.setTgl_Pinjam(sdfSave.format(cTglPinjam.getTime()));
        pengembalian.setFlowKembali(getApplicationContext().getString(R.string.flowNone));

        String url = Config.URL_ADD_M_PINJAM;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_KD_PINJAM, peminjaman.getKd_Pinjam());
                params.put(Config.KEY_NO_ANGGOTA, peminjaman.getNo_Anggota());
                params.put(Config.KEY_TGL_PINJAM, sdfSave.format(cTglPinjam.getTime()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        for (final Map<String, String> entry : listHashPengambilan) {
            String url2 = Config.URL_ADD_P_PINJAM;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_KD_PINJAM, peminjaman.getKd_Pinjam());
                    params.put(Config.KEY_NO_LAPTOP, entry.get("NoLaptop"));
                    params.put(Config.KEY_QTY_PINJAM, entry.get("QtyPinjam"));
                    params.put(Config.KEY_KEPERLUAN, entry.get("Keperluan"));
                    return params;
                }
            };
            String url3b = Config.URL_ADD_INVENTARIS;
            StringRequest stringRequest3b = new StringRequest(Request.Method.POST, url3b,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_QTY_PINJAM, entry.get("QtyPinjam"));
                    params.put(Config.KEY_KD_INVENTARIS, entry.get("NoLaptop"));
                    params.put(Config.KEY_NO_LAPTOP, entry.get("NoLaptop"));
                    params.put(Config.KEY_TGL_PINJAM, sdfSave.format(cTglPinjam.getTime()));
                    params.put(Config.KEY_STATUS, "0");

                    return params;
                }
            };
            String url4 = Config.URL_ADD_QTY_PINJAM;
            StringRequest stringRequest4 = new StringRequest(Request.Method.POST, url4,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_NO_LAPTOP, entry.get("NoLaptop"));
                    params.put(Config.KEY_QTY_PINJAM, entry.get("QtyPinjam"));
                    return params;
                }
            };
            RequestQueue requestQueue2 = Volley.newRequestQueue(this);
            requestQueue2.add(stringRequest2);

            RequestQueue requestQueue3b = Volley.newRequestQueue(this);
            requestQueue3b.add(stringRequest3b);

            RequestQueue requestQueue4 = Volley.newRequestQueue(this);
            requestQueue4.add(stringRequest4);
        }

        Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">Peminjaman berhasil disimpan</font>"), Snackbar.LENGTH_LONG).show();

        PeminjamanFragment.peminjamanList.add(peminjaman);
        PeminjamanFragment.pAdapter.notifyDataSetChanged();
        PengembalianFragment.pengembalianList.add(pengembalian);
        PengembalianFragment.pAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private void ModifyPeminjaman() {
        if (!inputValidation.isInputEditTextFilled(tietKdPinjam, tilKdPinjam, "Kode Pinjam harus diisi")) {
            return;
        }
        if (!inputValidation.isInputSpinnerAnggotaChoosed(spNoAnggota, "Anggota harus dipilih")) {
            return;
        }
        if (!inputValidation.isInputTextViewDateChoosed(tTglPinjam, "Tanggal Pinjam harus diisi")) {
            return;
        }
        if (!inputValidation.isListViewEmpty(listHashPengambilan, "Harus ada No. Laptop yang diisi")) {
            return;
        }
        if (isQtyPinjamMinus()) {
            return;
        }

        final Peminjaman peminjaman = new Peminjaman();
        peminjaman.setKd_Pinjam(tietKdPinjam.getText().toString());
        peminjaman.setTgl_Pinjam(sdfSave.format(cTglPinjam.getTime()));
        peminjaman.setNo_Anggota(spNoAnggota.getSelectedItem().toString());

        final Pengembalian pengembalian = new Pengembalian();
        pengembalian.setKd_Pinjam(tietKdPinjam.getText().toString());
        pengembalian.setTgl_Pinjam(sdfSave.format(cTglPinjam.getTime()));
        pengembalian.setFlowKembali(getApplicationContext().getString(R.string.flowNone));

        hapusQtyPinjam();
        /*hapusP_Pinjam(stietKdPinjam);
        hapusInventaris(sdfSave.format(cTglPinjam.getTime()));*/

        String url5 = Config.URL_DELETE_P_PINJAM + stietKdPinjam + "'";
        StringRequest stringRequest5 = new StringRequest(Request.Method.GET, url5,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        String url6 = Config.URL_DELETE_INVENTARIS + cTglPinjam.getTime() + "'";
        StringRequest stringRequest6 = new StringRequest(Request.Method.GET, url6,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue5 = Volley.newRequestQueue(getApplicationContext());
        requestQueue5.add(stringRequest5);

        RequestQueue requestQueue6 = Volley.newRequestQueue(getApplicationContext());
        requestQueue6.add(stringRequest6);

        // NOT USING METHOD BECAUSE GOT PROBLEMS ON INVENTARIS TABLE
//        updateM_Pinjam();
        String url = Config.URL_UPDATE_M_PINJAM;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_NO_ANGGOTA, peminjaman.getNo_Anggota());
                params.put(Config.KEY_TGL_PINJAM, sdfSave.format(cTglPinjam.getTime()));
                params.put(Config.KEY_KD_PINJAM, peminjaman.getKd_Pinjam());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        for (final Map<String, String> entry : listHashPengambilan) {
            String url4 = Config.URL_ADD_QTY_PINJAM;
            StringRequest stringRequest4 = new StringRequest(Request.Method.POST, url4,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_NO_LAPTOP, entry.get("NoLaptop"));
                    params.put(Config.KEY_QTY_PINJAM, entry.get("QtyPinjam"));
                    return params;
                }
            };
            String url2 = Config.URL_ADD_P_PINJAM;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_KD_PINJAM, stietKdPinjam);
                    params.put(Config.KEY_NO_LAPTOP, entry.get("NoLaptop"));
                    params.put(Config.KEY_QTY_PINJAM, entry.get("QtyPinjam"));
                    params.put(Config.KEY_KEPERLUAN, entry.get("Keperluan"));
                    return params;
                }
            };
            String url3b = Config.URL_ADD_INVENTARIS;
            StringRequest stringRequest3b = new StringRequest(Request.Method.POST, url3b,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_QTY_PINJAM, entry.get("QtyPinjam"));
                    params.put(Config.KEY_KD_INVENTARIS, entry.get("NoLaptop"));
                    params.put(Config.KEY_NO_LAPTOP, entry.get("NoLaptop"));
                    params.put(Config.KEY_TGL_PINJAM, sdfSave.format(cTglPinjam.getTime()));
                    params.put(Config.KEY_STATUS, "0");

                    return params;
                }
            };
            RequestQueue requestQueue4 = Volley.newRequestQueue(this);
            requestQueue4.add(stringRequest4);

            RequestQueue requestQueue2 = Volley.newRequestQueue(this);
            requestQueue2.add(stringRequest2);

            RequestQueue requestQueue3b = Volley.newRequestQueue(this);
            requestQueue3b.add(stringRequest3b);
        }

        /*addQtyPinjam();
        addP_Pinjam();
        addInventaris();*/

        // Snackbar can't show up after killing activity
//        Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">Peminjaman berhasil diupdate</font>"), Snackbar.LENGTH_LONG).show();
        Toast.makeText(this, "Peminjaman berhasil diupdate", Toast.LENGTH_LONG).show();

        PeminjamanFragment.peminjamanList.set(POSITION, peminjaman);
        PeminjamanFragment.pAdapter.notifyDataSetChanged();
        PengembalianFragment.pengembalianList.set(POSITION, pengembalian);
        PengembalianFragment.pAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private boolean isQtyPinjamMinus() {
        // NOT GETTING LAPTOP BASED ON listHashPengambilan
        // BECAUSE IT MIGHT NOT HAVE ENOUGH TIME TO GET DATA FROM DB
        // IF SQLITE IT IS OKAY
        for (final Map<String, String> entry : listHashPengambilan) {
            for (final Map<String, String> db : listHashQtyPinjam) {
                if (entry.get("NoLaptop").equals(db.get("NoLaptop"))) {
                    int qtyTotal = Integer.parseInt(db.get("QtyTotal"));
                    int qtyTerpinjam = Integer.parseInt(db.get("QtyPinjam"));
                    int qtyPinjam = Integer.parseInt(entry.get("QtyPinjam")) +
                                    qtyTerpinjam;

                    if (qtyTotal < qtyPinjam) {
                        Toast.makeText(this, entry.get("NoLaptop") +
                                " sudah tidak memiliki stok", Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void hapusP_Pinjam (final String kdPinjam) {
        String url = Config.URL_DELETE_P_PINJAM + kdPinjam + "'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void hapusInventaris (final String tglPinjam) {
        String url = Config.URL_DELETE_INVENTARIS + tglPinjam + "'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void hapusQtyPinjam () {
        for (final Map<String, String> entry2 : beforeEditedlistHash) {
            String url = Config.URL_DELETE_QTY_PINJAM;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })  {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_NO_LAPTOP, entry2.get("NoLaptop"));
                    params.put(Config.KEY_QTY_PINJAM, entry2.get("QtyPinjam"));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private void getSpLaptopData() {
        String url = Config.URL_GET_M_LAPTOP;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                JSONArray result = null;
                try {
                    j = new JSONObject(response);
                    result = j.getJSONArray(Config.TAG_JSON_ARRAY);
                    getLaptop(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getLaptop(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                laptopList.add(json.getString(Config.TAG_NO_LAPTOP));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        get String from array in Strings.xml
//        String[] s = getResources().getStringArray(R.array.choose_no_laptop);
        adapterSpNoLaptop =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, laptopList);
        spNoLaptop.setAdapter(adapterSpNoLaptop);
        alertDialog.dismiss();
    }

    private void getSpAnggotaData() {
        alertDialog = new SpotsDialog(this);
        alertDialog.show();
        String url = Config.URL_VIEW_ALL_ANGGOTA;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                JSONArray result = null;
                try {
                    j = new JSONObject(response);
                    result = j.getJSONArray(Config.TAG_JSON_ARRAY);
                    getAnggota(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getAnggota(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                anggotaList.add(json.getString(Config.TAG_NO_ANGGOTA));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterSpNoAnggota =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, anggotaList);
        spNoAnggota.setAdapter(adapterSpNoAnggota);
    }

    private void getTimerspNoAnggota() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                spNoAnggota.setSelection(getSpinnerIndex(spNoAnggota, NO_ANGGOTA));
            }
        }, 5000);
    }

    private void addM_Pinjam(final ArrayList<HashMap<String,String>> listHashPengambilan) {
        String url = Config.URL_ADD_M_PINJAM;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Data sukses disimpan!!!")) {
                            for (final Map<String, String> entry : listHashPengambilan) {
                                /*addP_Pinjam(entry.get("NoLaptop"), entry.get("QtyPinjam"),
                                        entry.get("Keperluan"));*/
//                                getTimeraddP_Pinjam();
                            }
                        } else {
                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_KD_PINJAM, stietKdPinjam);
                params.put(Config.KEY_NO_ANGGOTA, sspNoAnggota);
                params.put(Config.KEY_TGL_PINJAM, sdfSave.format(cTglPinjam.getTime()));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addP_Pinjam() {
        for (final Map<String, String> entry : listHashPengambilan) {
            String url2 = Config.URL_ADD_P_PINJAM;
            StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_KD_PINJAM, stietKdPinjam);
                    params.put(Config.KEY_NO_LAPTOP, entry.get("NoLaptop"));
                    params.put(Config.KEY_QTY_PINJAM, entry.get("QtyPinjam"));
                    params.put(Config.KEY_KEPERLUAN, entry.get("Keperluan"));
                    return params;
                }
            };
            RequestQueue requestQueue2 = Volley.newRequestQueue(this);
            requestQueue2.add(stringRequest2);
        }
    }

    private void addInventaris() {
        for (final Map<String, String> entry : listHashPengambilan) {
            String url3b = Config.URL_ADD_INVENTARIS;
            StringRequest stringRequest3b = new StringRequest(Request.Method.POST, url3b,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_QTY_PINJAM, entry.get("QtyPinjam"));
                    params.put(Config.KEY_KD_INVENTARIS, entry.get("NoLaptop"));
                    params.put(Config.KEY_NO_LAPTOP, entry.get("NoLaptop"));
                    params.put(Config.KEY_TGL_PINJAM, sdfSave.format(cTglPinjam.getTime()));
                    params.put(Config.KEY_STATUS, "0");

                    return params;
                }
            };
            RequestQueue requestQueue3b = Volley.newRequestQueue(this);
            requestQueue3b.add(stringRequest3b);
        }
    }

    private void addQtyPinjam() {
        for (final Map<String, String> entry : listHashPengambilan) {
            String url4 = Config.URL_ADD_QTY_PINJAM;
            StringRequest stringRequest4 = new StringRequest(Request.Method.POST, url4,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_NO_LAPTOP, entry.get("NoLaptop"));
                    params.put(Config.KEY_QTY_PINJAM, entry.get("QtyPinjam"));
                    return params;
                }
            };
            RequestQueue requestQueue4 = Volley.newRequestQueue(this);
            requestQueue4.add(stringRequest4);
        }
    }

    private void updateM_Pinjam() {
        String url = Config.URL_UPDATE_M_PINJAM;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(activityAddPinjaman, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_NO_ANGGOTA, sspNoAnggota);
                params.put(Config.KEY_TGL_PINJAM, sdfSave.format(cTglPinjam.getTime()));
                params.put(Config.KEY_KD_PINJAM, stietKdPinjam);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void getTimeraddM_Pinjam() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                addM_Pinjam(listHashPengambilan);
            }
        }, 1000);
    }

    private void getTimeraddP_Pinjam() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
//                addP_Pinjam();
            }
        }, 1000);
    }

    private void getTimeraddInventaris() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                addInventaris();
            }
        }, 1000);
    }

    private void getTimeraddQtyPinjam() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                addQtyPinjam();
            }
        }, 1000);
    }

    private void ClearDetail() {
        spNoLaptop.setSelection(0);
        etQtyPinjam.setText(null);
        etKeperluan.setText(null);
    }

    private void ClearAll() {
        calendar = Calendar.getInstance();

        tietKdPinjam.requestFocus();
        tietKdPinjam.setText(null);
        spNoAnggota.setSelection(0);
        tTglPinjam.setText("Pilih Tanggal Pinjam");
//        calendar.clear();
        spNoLaptop.setSelection(0);
        etQtyPinjam.setText(null);
        etKeperluan.setText(null);
        listHashPengambilan.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTglPinjam:
                showDatePicker();
                break;
            case R.id.ibToolbarBack:
                finish();
                break;
            case R.id.ibAdd:
                AddData();
                break;
        }
    }

    private void showDatePicker() {
        datePickerDialog = new DatePickerDialog(AddPeminjaman.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.setTime(Calendar.getInstance().getTime());
                cYear = year;
                cMonth = month;
                cDay = dayOfMonth;
                tTglPinjam.setText(new StringBuilder().append(dayOfMonth).append("/")
                        .append(month + 1).append("/").append(year));
                calendar.set(cYear, cMonth, cDay);
                cTglPinjam = calendar;
            }
        }, cYear, cMonth, cDay);
        if(!((Activity) AddPeminjaman.this).isFinishing())
        {
            datePickerDialog.show();
        }
    }

    private void AddData() {
        if (etQtyPinjam.getText().toString().equals(null) ||
                etQtyPinjam.getText().toString().equals("") ||
                    etQtyPinjam.getText().toString().equals("0")) {
            etQtyPinjam.requestFocus();
            Toast.makeText(AddPeminjaman.this, "Qty harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }
        if (updateLaptopExistInList(
                spNoLaptop.getSelectedItem().toString(),
                etQtyPinjam.getText().toString(),
                etKeperluan.getText().toString())) {
            Toast.makeText(AddPeminjaman.this,
                    spNoLaptop.getSelectedItem().toString() + " sudah terupdate", Toast.LENGTH_SHORT).show();
            ClearDetail();
            return;
        }
                /*SimpleAdapter p_pinjamAdapter = new SimpleAdapter(
                        AddPeminjaman.this,
                        listHashPengambilan,
                        R.layout.custom_row_view,
                        new String[] {"NoLaptop","QtyPinjam","Keperluan"},
                        new int[] {R.id.tvNoLaptop, R.id.tvQtyPinjam,
                                R.id.tvKeperluan}
                );*/
        dtlPeminjamanAdapter = new DtlPeminjamanAdapter(listHashPengambilan, R.layout.dtl_peminjaman_row_view);
        populateList(spNoLaptop.getSelectedItem().toString(),
                etQtyPinjam.getText().toString(),
                etKeperluan.getText().toString());

        listDtlPeminjaman.setAdapter(dtlPeminjamanAdapter);
        ClearDetail();
    }
}
