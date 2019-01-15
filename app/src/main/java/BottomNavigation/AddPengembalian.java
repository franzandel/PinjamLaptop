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
import LoginRegister.Pengembalian;
import Recycler.DtlPengembalianAdapter;
import dmax.dialog.SpotsDialog;

/**
 * Created by OK on 4/12/2018.
 */

public class AddPengembalian extends AppCompatActivity implements View.OnClickListener{

    private View activityAddPengembalian;
    private TextInputEditText tietKdPinjam;
    private TextInputLayout tilKdPinjam;
    private TextView tTglKembali;
    private InputValidation inputValidation;
    private DatePickerDialog datePickerDialog;
    private int cYear, cMonth, cDay;
    private Calendar calendar;
    private AlertDialog dialog;
    private ImageButton ibToolbarBack, ibAdd;
    private Menu menu;
    MenuItem btSave;
    private String KD_PINJAM, TGL_PINJAM;
    private final ArrayList<HashMap<String,String>> listHashPengembalian = new ArrayList<HashMap<String,String>>();
    private ListView listDtlPengembalian;
    DtlPengembalianAdapter adapter;
    private String stietKdPinjam;
    public static final String DATE_FORMAT_SAVE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_NEW = "dd/MM/yyyy";
    SimpleDateFormat sdfSave, sdfOutput;
    private ArrayList<String> kdInventarisList = new ArrayList<String>();
    ArrayAdapter<String> adapterSpKdInventaris, adapterSpStsKembali;
    private Spinner spKdInventaris, spStsKembali;
    private ArrayList<Calendar> listHashCalendar = new ArrayList<Calendar>();
    private int i, j = 0;
    private ArrayList<HashMap<String,String>> beforeEditedlistHash = new ArrayList<HashMap<String,String>>();
    private android.app.AlertDialog alertDialog;
    private int POSITION;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addpengembalian);
        activityAddPengembalian = (View)findViewById(R.id.activityAddPengembalian);
        tietKdPinjam = (TextInputEditText)findViewById(R.id.tietKdPinjam);
        tilKdPinjam = (TextInputLayout) findViewById(R.id.tilKdPinjam);
        tTglKembali = (TextView)findViewById(R.id.tvTglKembali);
        ibToolbarBack = (ImageButton) findViewById(R.id.ibToolbarBack);
        ibAdd = (ImageButton) findViewById(R.id.ibAdd);
        listDtlPengembalian = (ListView)findViewById(R.id.listDtlPengembalian);
        spKdInventaris = (Spinner) findViewById(R.id.spKdInventaris);
        spStsKembali = (Spinner) findViewById(R.id.spStsKembali);
        adapterSpStsKembali =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, getResources().getStringArray(R.array.choose_sts_kembali));
        calendar = Calendar.getInstance();
        cYear = calendar.get(Calendar.YEAR);
        cMonth = calendar.get(Calendar.MONTH);
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        inputValidation = new InputValidation(this);
        inputValidation = new InputValidation(this);
        sdfSave = new SimpleDateFormat(DATE_FORMAT_SAVE);
        sdfOutput = new SimpleDateFormat(DATE_FORMAT_NEW);
        this.setTitle(null);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar_activity);
        setSupportActionBar(mToolbar);

        Intent intent = getIntent();
        if (intent.getStringExtra("KD_PINJAM") != null) {
            KD_PINJAM = intent.getStringExtra("KD_PINJAM");
            TGL_PINJAM = intent.getStringExtra("TGL_PINJAM");
            POSITION = intent.getIntExtra("POSITION", 0);
            getSpKdInventarisData(TGL_PINJAM);

            getKembali();

            tietKdPinjam.setText(KD_PINJAM);
            tietKdPinjam.setEnabled(false);
        }

        tTglKembali.setOnClickListener(this);
        ibToolbarBack.setOnClickListener(this);
        ibAdd.setOnClickListener(this);
        /*tTglKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog = new DatePickerDialog(AddPengembalian.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        cYear = year;
                        cMonth = month;
                        cDay = dayOfMonth;
                        tTglKembali.setText(new StringBuilder().append(dayOfMonth).append("/")
                                .append(month + 1).append("/").append(year));
                        calendar.set(cYear, cMonth, cDay);
                    }
                }, cYear, cMonth, cDay);
                if(!((Activity) AddPengembalian.this).isFinishing())
                {
                    datePickerDialog.show();
                }
            }
        });

        ibToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tTglKembali.getText().toString().equals("Tgl Kembali")) {
                    Toast.makeText(AddPengembalian.this, "Tgl Kembali harus dipilih", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (updateLaptopExistInList(
                        spKdInventaris.getSelectedItem().toString(),
                        tTglKembali.getText().toString(),
                        spStsKembali.getSelectedItem().toString())) {
                    Toast.makeText(AddPengembalian.this,
                            spKdInventaris.getSelectedItem().toString() + " sudah terupdate", Toast.LENGTH_SHORT).show();
                    ClearDetail();
                    return;
                }
                *//*SimpleAdapter p_pinjamAdapter = new SimpleAdapter(
                        AddPeminjaman.this,
                        listHashPengambilan,
                        R.layout.custom_row_view,
                        new String[] {"NoLaptop","QtyPinjam","Keperluan"},
                        new int[] {R.id.tvNoLaptop, R.id.tvQtyPinjam,
                                R.id.tvKeperluan}
                );*//*
                listHashCalendar.add(calendar);
                adapter = new DtlPengembalianAdapter(listHashPengembalian, R.layout.dtl_peminjaman_row_view);
                populateList(spKdInventaris.getSelectedItem().toString(),
                        tTglKembali.getText().toString(),
                        spStsKembali.getSelectedItem().toString());

                listDtlPengembalian.setAdapter(adapter);
                ClearDetail();
            }
        });*/

        listDtlPengembalian.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> data = (HashMap<String, String>) parent.getAdapter().getItem(position);
                spKdInventaris.setSelection(adapterSpKdInventaris.getPosition(data.get("KdInventaris")));
                tTglKembali.setText(data.get("TglKembali"));
                spStsKembali.setSelection(adapterSpStsKembali.getPosition(data.get("StsKembali")));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;
        btSave = menu.findItem(R.id.Save);

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
                            AddPengembalian();
                            finish();
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
                            ModifyPengembalian();
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

    private void getKembali() {
        alertDialog = new SpotsDialog(this);
        alertDialog.show();
        new Handler().postDelayed(new Runnable(){
            public void run() {
                viewKembali();
            }
        }, 450);
    }

    public void viewKembali() {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        String url = Config.URL_VIEW_KEMBALI  + KD_PINJAM + "'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showKembali(response);
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

    public void showKembali(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            listHashPengembalian.clear();
            listHashCalendar.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("KdInventaris", jo.getString(Config.TAG_KD_INVENTARIS));

                try {
                    calendar = Calendar.getInstance();
                    Date changeTglKembali = sdfSave.parse(jo.getString(Config.TAG_TGL_KEMBALI));
                    calendar.setTime(changeTglKembali);
                    listHashCalendar.add(calendar);
                    String tglKembali = sdfOutput.format(changeTglKembali);
                    temp.put("TglKembali", tglKembali);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String stsKembaliIndex = jo.getString(Config.TAG_STATUS_KEMBALI);
                String stsKembali =
                        stsKembaliIndex.equals("0") ? "Baik" :
                                stsKembaliIndex.equals("1") ? "Rusak" :
                                        "Hilang";

                temp.put("StsKembali", stsKembali);
                listHashPengembalian.add(temp);
            }

            // TODO: 6/19/2018 : NOT USING
            // beforeEditedlistHash = new ArrayList<HashMap<String,String>>(listHash);
            // beforeEditedlistHash.addAll(listHash);
            // BECAUSE
            // THEY REFER TO THE SAME OBJECTS OR CONTENTS

            // ONLY NEED KdInventaris FROM THIS ARRAYLIST
            beforeEditedlistHash = new ArrayList<HashMap<String,String>>(listHashPengembalian.size());
            for (Map<String, String> item : listHashPengembalian) {
                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("KdInventaris", item.get("KdInventaris"));
                temp.put("TglKembali", item.get("TglKembali"));
                temp.put("StsKembali", item.get("StsKembali"));
                beforeEditedlistHash.add(temp);
            }

            if (listHashPengembalian.size() != 0) {
                btSave.setTitle("Update");
            }

            adapter = new DtlPengembalianAdapter(listHashPengembalian, R.layout.dtl_pengembalian_row_view);
            listDtlPengembalian.setAdapter(adapter);
            alertDialog.dismiss();
        } catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    private void getSpKdInventarisData(final String tglPinjam) {
        String url = Config.URL_GET_INVENTARIS + tglPinjam + "'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                JSONArray result = null;
                try {
                    j = new JSONObject(response);
                    result = j.getJSONArray(Config.TAG_JSON_ARRAY);
                    getKdInventaris(result);
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

    private void getKdInventaris(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                kdInventarisList.add(json.getString(Config.TAG_KD_INVENTARIS));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
//        get String from array in Strings.xml
//        String[] s = getResources().getStringArray(R.array.choose_no_laptop);
        adapterSpKdInventaris =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, kdInventarisList);
        spKdInventaris.setAdapter(adapterSpKdInventaris);
    }

    private boolean updateLaptopExistInList(String kdInventaris, String tglKembali, String stsKembali) {
        int k = 0;
        for (final Map<String, String> entry : listHashPengembalian) {
            if (entry.get("KdInventaris").equals(kdInventaris)) {
                listHashCalendar.set(k, calendar);
                entry.put("TglKembali",tglKembali);
                entry.put("StsKembali",stsKembali);
                listDtlPengembalian.setAdapter(adapter);
                return true;
            }
            k += 1;
        }
        return false;
    }

    private void populateList(String kdInventaris, String tglKembali, String stsKembali) {
        HashMap<String,String> temp = new HashMap<String,String>();
        temp.put("KdInventaris", kdInventaris);
        temp.put("TglKembali", tglKembali);
        temp.put("StsKembali", stsKembali);
        listHashPengembalian.add(temp);
    }

    private void AddPengembalian() {
        if (!inputValidation.isInputEditTextFilled(tietKdPinjam, tilKdPinjam, "Kode Pinjam harus diisi")) {
            return;
        }
        if (!inputValidation.isListViewEmpty(listHashPengembalian, "Harus ada Kode Inventaris yang diisi")) {
            return;
        }

        final Pengembalian pengembalian = new Pengembalian();
        pengembalian.setKd_Pinjam(tietKdPinjam.getText().toString());
        pengembalian.setTgl_Pinjam(TGL_PINJAM);
        pengembalian.setFlowKembali(
                kdInventarisList.size() == listHashPengembalian.size() ?
                        getApplication().getString(R.string.flowDone) :
                        getApplication().getString(R.string.flowPartial));

        for (final Map<String, String> entry : listHashPengembalian) {
            String url = Config.URL_ADD_KEMBALI;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPengembalian, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPengembalian, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String stsKembali = entry.get("StsKembali");
                    String stsKembaliIndex =
                            stsKembali.equals("Baik") ? "0" :
                                    stsKembali.equals("Rusak") ? "1" :
                                            "2";
                    Calendar tglKembali = listHashCalendar.get(i);
                    i++;

                    params.put(Config.KEY_KD_PINJAM, pengembalian.getKd_Pinjam());
                    params.put(Config.KEY_KD_INVENTARIS, entry.get("KdInventaris"));
                    params.put(Config.KEY_TGL_KEMBALI, sdfSave.format(tglKembali.getTime()));
                    params.put(Config.KEY_STATUS_KEMBALI, stsKembaliIndex);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

        updateInventaris();
        hapusQtyPinjam();
        // Snackbar can't show up after killing activity
//        Snackbar.make(activityAddPengembalian, Html.fromHtml("<font color=\"#ffffff\">Pengembalian berhasil ditambah</font>"), Snackbar.LENGTH_LONG).show();
        Toast.makeText(this, "Pengembalian berhasil ditambah", Toast.LENGTH_LONG).show();

        PengembalianFragment.pengembalianList.set(POSITION, pengembalian);
        PengembalianFragment.pAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private void ModifyPengembalian() {
        if (!inputValidation.isInputEditTextFilled(tietKdPinjam, tilKdPinjam, "Kode Pinjam harus diisi")) {
            return;
        }
        if (!inputValidation.isListViewEmpty(listHashPengembalian, "Harus ada Kode Inventaris yang diisi")) {
            return;
        }

        final Pengembalian pengembalian = new Pengembalian();
        pengembalian.setKd_Pinjam(tietKdPinjam.getText().toString());
        pengembalian.setTgl_Pinjam(TGL_PINJAM);

        addQtyPinjam();
        hapusKembali(pengembalian.getKd_Pinjam());
        hapusTgl_Kembali(TGL_PINJAM);
        /*getTimerHapusKembali(stietKdPinjam);
        getTimerTgl_Kembali(TGL_PINJAM);*/

        for (final Map<String, String> entry : listHashPengembalian) {
            String url = Config.URL_ADD_KEMBALI;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPengembalian, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPengembalian, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String stsKembali = entry.get("StsKembali");
                    String stsKembaliIndex =
                            stsKembali.equals("Baik") ? "0" :
                                    stsKembali.equals("Rusak") ? "1" :
                                            "2";
                    Calendar tglKembali = listHashCalendar.get(i);
                    i++;

                    params.put(Config.KEY_KD_PINJAM, pengembalian.getKd_Pinjam());
                    params.put(Config.KEY_KD_INVENTARIS, entry.get("KdInventaris"));
                    params.put(Config.KEY_TGL_KEMBALI, sdfSave.format(tglKembali.getTime()));
                    params.put(Config.KEY_STATUS_KEMBALI, stsKembaliIndex);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }

        updateInventaris();
//        hapusQtyPinjam();

        for (final Map<String, String> entry : listHashPengembalian) {
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
                    String kdInventaris = entry.get("KdInventaris");
                    String noLaptop = kdInventaris.substring(0, kdInventaris.length() - 5);

                    params.put(Config.KEY_NO_LAPTOP, noLaptop);
                    params.put(Config.KEY_QTY_PINJAM, "1");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }

        /*getTimerUpdateInventaris();
        getTimerHapusQtyPinjam();*/
        // Snackbar can't show up after killing activity
//        Snackbar.make(activityAddPengembalian, Html.fromHtml("<font color=\"#ffffff\">Pengembalian berhasil diupdate</font>"), Snackbar.LENGTH_LONG).show();
        Toast.makeText(this, "Pengembalian berhasil diupdate", Toast.LENGTH_LONG).show();

        PengembalianFragment.pengembalianList.set(POSITION, pengembalian);
        PengembalianFragment.pAdapter.notifyDataSetChanged();
        ClearAll();
        finish();
    }

    private void hapusQtyPinjam() {
        for (final Map<String, String> entry : listHashPengembalian) {
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
                    String kdInventaris = entry.get("KdInventaris");
                    String noLaptop = kdInventaris.substring(0, kdInventaris.length() - 5);
                    
                    params.put(Config.KEY_NO_LAPTOP, noLaptop);
                    params.put(Config.KEY_QTY_PINJAM, "1");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private void updateInventaris() {
        for (final Map<String, String> entry : listHashPengembalian) {
            String url = Config.URL_UPDATE_INVENTARIS;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Snackbar.make(activityAddPengembalian, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Snackbar.make(activityAddPengembalian, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    String stsKembali = entry.get("StsKembali");
                    String stsKembaliIndex =
                            stsKembali.equals("Baik") ? "0" :
                                    stsKembali.equals("Rusak") ? "1" :
                                            "2";
                    Calendar tglKembali = listHashCalendar.get(j);
                    j++;

                    params.put(Config.KEY_KD_INVENTARIS, entry.get("KdInventaris"));
                    params.put(Config.KEY_STATUS, stsKembaliIndex);
                    params.put(Config.KEY_TGL_KEMBALI, sdfSave.format(tglKembali.getTime()));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            requestQueue.add(stringRequest);
        }
    }

    private void addQtyPinjam () {
        for (final Map<String, String> entry : beforeEditedlistHash) {
            String url = Config.URL_ADD_QTY_PINJAM;
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
                    String kdInventaris = entry.get("KdInventaris");
                    String noLaptop = kdInventaris.substring(0, kdInventaris.length() - 5);

                    params.put(Config.KEY_NO_LAPTOP, noLaptop);
                    params.put(Config.KEY_QTY_PINJAM, "1");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }
    }

    private void hapusKembali (final String kdPinjam) {
        String url = Config.URL_DELETE_KEMBALI + kdPinjam + "'";
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

    private void hapusTgl_Kembali (final String tglPinjam) {
        String url = Config.URL_DELETE_TGL_KEMBALI + tglPinjam + "'";
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

    private void getTimerHapusKembali(final String kdPinjam) {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                hapusKembali(kdPinjam);
            }
        }, 1000);
    }

    private void getTimerTgl_Kembali(final String tglPinjam) {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                hapusTgl_Kembali(tglPinjam);
            }
        }, 1000);
    }

    private void getTimerUpdateInventaris() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                updateInventaris();
            }
        }, 1000);
    }

    private void getTimerHapusQtyPinjam() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                hapusQtyPinjam();
            }
        }, 1000);
    }

    private void ClearDetail() {
        spKdInventaris.setSelection(0);
        tTglKembali.setText("Tgl Kembali");
        spStsKembali.setSelection(0);
        calendar = Calendar.getInstance();
    }

    private void ClearAll() {
        calendar = Calendar.getInstance();

        tietKdPinjam.requestFocus();
        tietKdPinjam.setText(null);
        spKdInventaris.setSelection(0);
        tTglKembali.setText("Tgl Kembali");
        spStsKembali.setSelection(0);
        listHashPengembalian.clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvTglKembali:
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
        datePickerDialog = new DatePickerDialog(AddPengembalian.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                cYear = year;
                cMonth = month;
                cDay = dayOfMonth;
                tTglKembali.setText(new StringBuilder().append(dayOfMonth).append("/")
                        .append(month + 1).append("/").append(year));
                calendar.set(cYear, cMonth, cDay);
            }
        }, cYear, cMonth, cDay);
        if(!((Activity) AddPengembalian.this).isFinishing())
        {
            datePickerDialog.show();
        }
    }

    private void AddData() {
        if (tTglKembali.getText().toString().equals("Tgl Kembali")) {
            Toast.makeText(AddPengembalian.this, "Tgl Kembali harus dipilih", Toast.LENGTH_SHORT).show();
            return;
        }
        if (updateLaptopExistInList(
                spKdInventaris.getSelectedItem().toString(),
                tTglKembali.getText().toString(),
                spStsKembali.getSelectedItem().toString())) {
            Toast.makeText(AddPengembalian.this,
                    spKdInventaris.getSelectedItem().toString() + " sudah terupdate", Toast.LENGTH_SHORT).show();
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
        listHashCalendar.add(calendar);
        adapter = new DtlPengembalianAdapter(listHashPengembalian, R.layout.dtl_peminjaman_row_view);
        populateList(spKdInventaris.getSelectedItem().toString(),
                tTglKembali.getText().toString(),
                spStsKembali.getSelectedItem().toString());

        listDtlPengembalian.setAdapter(adapter);
        ClearDetail();
    }
}
