package BottomNavigation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
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
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import LoginRegister.InputValidation;
import LoginRegister.Laptop;
import dmax.dialog.SpotsDialog;

//import com.example.ok.Mereklaptop.R;

/**
 * Created by OK on 4/12/2018.
 */

public class AddLaptop extends AppCompatActivity implements View.OnClickListener{

    private View activityAddLaptop;
    private TextInputEditText tietNoLaptop, tietSatuan, tietQtyTotal, tietModel, tietRAM, tietHDD;
    private TextInputLayout tilNoLaptop, tilSatuan, tilQtyTotal, tilModel, tilRAM, tilHDD;
    private InputValidation inputValidation;
    private ImageButton ibToolbarBack;
    private final ArrayList<HashMap<String,String>> listHashPengambilan = new ArrayList<HashMap<String,String>>();
    AlertDialog dialog;
    private String NO_LAPTOP, SATUAN, KD_MEREK, KD_PROCESSOR, KD_VGA, MODEL, RAM, HDD;
    private int QTY_TOTAL;
    private int POSITION;
    private Menu menu;
    MenuItem btSave;
    private String stietNoLaptop, stietSatuan, stietQtyTotal, sspKdMerek, sspKdProcessor, sspKdVGA, stietModel, stietRAM, stietHDD;
    private final ArrayList<HashMap<String,String>> listHashQtyMerek = new ArrayList<HashMap<String,String>>();
    private Spinner spKdMerek, spKdProcessor, spKdVGA;
    private android.app.AlertDialog alertDialog;
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
    private static final int GALLERY = 1;
    Uri imageUri;
    private ArrayList<String> laptopList= new ArrayList<String>();
    private ArrayList<String> processorList = new ArrayList<String>();
    private ArrayList<String> vgaList = new ArrayList<String>();
    ArrayAdapter<String> adapterSpKdMerek, adapterSpKdProcessor, adapterSpKdVGA;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlaptop);
        activityAddLaptop = (View)findViewById(R.id.activityAddLaptop);
        tietNoLaptop = (TextInputEditText)findViewById(R.id.tietNoLaptop);
        tilNoLaptop = (TextInputLayout) findViewById(R.id.tilNoLaptop);
        tietSatuan = (TextInputEditText)findViewById(R.id.tietSatuan);
        tilSatuan = (TextInputLayout) findViewById(R.id.tilSatuan);
        tietQtyTotal = (TextInputEditText)findViewById(R.id.tietQtyTotal);
        tilQtyTotal = (TextInputLayout) findViewById(R.id.tilQtyTotal);
        spKdMerek = (Spinner)findViewById(R.id.spKdMerek);
        spKdProcessor = (Spinner)findViewById(R.id.spKdProcessor);
        spKdVGA = (Spinner)findViewById(R.id.spKdVGA);
        tietModel = (TextInputEditText)findViewById(R.id.tietModel);
        tilModel = (TextInputLayout) findViewById(R.id.tilModel);
        tietRAM = (TextInputEditText)findViewById(R.id.tietRAM);
        tilRAM = (TextInputLayout) findViewById(R.id.tilRAM);
        tietHDD = (TextInputEditText)findViewById(R.id.tietHDD);
        tilHDD = (TextInputLayout) findViewById(R.id.tilHDD);
        ibToolbarBack = (ImageButton) findViewById(R.id.ibToolbarBack);
        inputValidation = new InputValidation(this);
        this.setTitle(null);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar_activity);
        setSupportActionBar(mToolbar);
        /*default back in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        getSpKdMerek();
        getSpKdProcessor();
        getSpKdVGA();

        Intent intent = getIntent();
        if (intent.getStringExtra("NO_LAPTOP") != null) {
            NO_LAPTOP = intent.getStringExtra("NO_LAPTOP");
            SATUAN = intent.getStringExtra("SATUAN");
            QTY_TOTAL = intent.getIntExtra("QTY_TOTAL", 0);
            KD_PROCESSOR = intent.getStringExtra("KD_PROCESSOR");
            RAM = intent.getStringExtra("RAM");
            POSITION = intent.getIntExtra("POSITION", 0);

            getP_Laptop();

            tietNoLaptop.setText(NO_LAPTOP);
            tietSatuan.setText(SATUAN);
            tietQtyTotal.setText(String.valueOf(QTY_TOTAL));
            getTimerspKdProcessor();
            tietRAM.setText(RAM);

            tietNoLaptop.setEnabled(false);
            tietQtyTotal.setEnabled(false);
            tietSatuan.requestFocus();
        }

        ibToolbarBack.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;
        btSave = menu.findItem(R.id.Save);
        if (NO_LAPTOP != null) {
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
                            AddLaptop();
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
                            ModifyLaptop();
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

    private void AddLaptop() {
        if (!inputValidation.isInputEditTextFilled(tietNoLaptop, tilNoLaptop, "Nomor Laptop harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietSatuan, tilSatuan, "Satuan harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietQtyTotal, tilQtyTotal, "Qty Total harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietModel, tilModel, "Model harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietRAM, tilRAM, "RAM harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietHDD, tilHDD, "HDD harus diisi")) {
            return;
        }
        /*if (isQtyMerekMinus()) {
            return;
        }*/

        final Laptop laptop = new Laptop();
        laptop.setNo_Laptop(tietNoLaptop.getText().toString());
        laptop.setSatuan(tietSatuan.getText().toString());
        laptop.setQty_total(Integer.parseInt(tietQtyTotal.getText().toString()));

        laptop.setKd_merek(spKdMerek.getSelectedItem().toString());
        laptop.setKd_processor(spKdProcessor.getSelectedItem().toString());
        laptop.setKd_vga(spKdVGA.getSelectedItem().toString());
        laptop.setModel(tietModel.getText().toString());
        laptop.setRam(tietRAM.getText().toString());
        laptop.setHdd(tietHDD.getText().toString());

        String url = Config.URL_ADD_M_LAPTOP;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Snackbar.make(activityAddLaptop, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(activityAddLaptop, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_NO_LAPTOP, laptop.getNo_Laptop());
                params.put(Config.KEY_SATUAN, laptop.getSatuan());
                params.put(Config.KEY_QTY_TOTAL, String.valueOf(laptop.getQty_total()));
                params.put(Config.KEY_QTY_BAIK, String.valueOf(laptop.getQty_total()));
                params.put(Config.KEY_QTY_RUSAK, "0");
                params.put(Config.KEY_QTY_HILANG, "0");
                params.put(Config.KEY_QTY_PINJAM, "0");
                return params;
            }
        };
        String url2 = Config.URL_ADD_P_LAPTOP;
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Snackbar.make(activityAddLaptop, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(activityAddLaptop, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_NO_LAPTOP, laptop.getNo_Laptop());
                params.put(Config.KEY_KD_MEREK, laptop.getKd_merek());
                params.put(Config.KEY_KD_PROCESSOR, laptop.getKd_processor());
                params.put(Config.KEY_KD_VGA, laptop.getKd_vga());
                params.put(Config.KEY_MODEL, laptop.getModel());
                params.put(Config.KEY_RAM, laptop.getRam());
                params.put(Config.KEY_HDD, laptop.getHdd());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);

        Snackbar.make(activityAddLaptop, Html.fromHtml("<font color=\"#ffffff\">Laptop berhasil disimpan</font>"), Snackbar.LENGTH_LONG).show();

        LaptopFragment.laptopList.add(laptop);
        LaptopFragment.lAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private void ModifyLaptop() {
        if (!inputValidation.isInputEditTextFilled(tietNoLaptop, tilNoLaptop, "Nomor Laptop harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietSatuan, tilSatuan, "Satuan harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietQtyTotal, tilQtyTotal, "Qty Total harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietModel, tilModel, "Model harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietRAM, tilRAM, "RAM harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietHDD, tilHDD, "HDD harus diisi")) {
            return;
        }
        /*if (isQtyMerekMinus()) {
            return;
        }*/

        final Laptop laptop = new Laptop();
        laptop.setNo_Laptop(tietNoLaptop.getText().toString());
        laptop.setSatuan(tietSatuan.getText().toString());
        laptop.setQty_total(Integer.parseInt(tietQtyTotal.getText().toString()));

        laptop.setKd_merek(spKdMerek.getSelectedItem().toString());
        laptop.setKd_processor(spKdProcessor.getSelectedItem().toString());
        laptop.setKd_vga(spKdVGA.getSelectedItem().toString());
        laptop.setModel(tietModel.getText().toString());
        laptop.setRam(tietRAM.getText().toString());
        laptop.setHdd(tietHDD.getText().toString());

        String url = Config.URL_UPDATE_M_LAPTOP;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Snackbar.make(activityAddLaptop, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(activityAddLaptop, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_NO_LAPTOP, laptop.getNo_Laptop());
                params.put(Config.KEY_SATUAN, laptop.getSatuan());
                return params;
            }
        };
        String url2 = Config.URL_UPDATE_P_LAPTOP;
        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Snackbar.make(activityAddLaptop, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(activityAddLaptop, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_NO_LAPTOP, laptop.getNo_Laptop());
                params.put(Config.KEY_KD_MEREK, laptop.getKd_merek());
                params.put(Config.KEY_KD_PROCESSOR, laptop.getKd_processor());
                params.put(Config.KEY_KD_VGA, laptop.getKd_vga());
                params.put(Config.KEY_MODEL, laptop.getModel());
                params.put(Config.KEY_RAM, laptop.getRam());
                params.put(Config.KEY_HDD, laptop.getHdd());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        RequestQueue requestQueue2 = Volley.newRequestQueue(this);
        requestQueue2.add(stringRequest2);

        Snackbar.make(activityAddLaptop, Html.fromHtml("<font color=\"#ffffff\">Laptop berhasil diupdate</font>"), Snackbar.LENGTH_LONG).show();

        LaptopFragment.laptopList.set(POSITION, laptop);
        LaptopFragment.lAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private void ClearAll() {
        tietNoLaptop.requestFocus();
        tietNoLaptop.setText(null);
        tietSatuan.setText(null);
        tietQtyTotal.setText(null);
        spKdMerek.setSelection(0);
        spKdProcessor.setSelection(0);
        spKdVGA.setSelection(0);
        tietModel.setText(null);
        tietRAM.setText(null);
        tietHDD.setText(null);
    }

    private int getSpinnerIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }

        return 0;
    }

    // FIRES AFTER CLOSED GALLERY
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY && resultCode != 0) {
            imageUri = data.getData();
            try {
                Image = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                if (getOrientation(getApplicationContext(), imageUri) != 0) {
                    Matrix matrix = new Matrix();
                    matrix.postRotate(getOrientation(getApplicationContext(), imageUri));
                    if (rotateImage != null)
                        rotateImage.recycle();
                    rotateImage = Bitmap.createBitmap(Image, 0, 0, Image.getWidth(), Image.getHeight(), matrix,true);
//                    ivFotoAnggota.setImageBitmap(rotateImage);
                } else {

                }
//                    ivFotoAnggota.setImageBitmap(Image);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // CHECK IF LANDSCAPE OR POTRAIT BEFORE SHOWING TO IMAGEVIEW
    public static int getOrientation(Context context, Uri photoUri) {
        Cursor cursor = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION },null, null, null);

        if (cursor.getCount() != 1) {
            return -1;
        }
        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibToolbarBack:
                finish();
                break;
            case R.id.ivFotoAnggota:
//                ivFotoAnggota.setImageBitmap(null);
                if (Image != null)
                    Image.recycle();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
                break;
        }
    }

    private void getSpKdMerek() {
        alertDialog = new SpotsDialog(this);
        alertDialog.show();
        String url = Config.URL_VIEW_ALL_MEREK;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                JSONArray result = null;
                try {
                    j = new JSONObject(response);
                    result = j.getJSONArray(Config.TAG_JSON_ARRAY);
                    getKdMerek(result);
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

    private void getKdMerek(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                laptopList.add(json.getString(Config.TAG_KD_MEREK));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterSpKdMerek =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, laptopList);
        spKdMerek.setAdapter(adapterSpKdMerek);
    }

    private void getSpKdProcessor() {
        String url = Config.URL_VIEW_ALL_PROCESSOR;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                JSONArray result = null;
                try {
                    j = new JSONObject(response);
                    result = j.getJSONArray(Config.TAG_JSON_ARRAY);
                    getKdProcessor(result);
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

    private void getKdProcessor(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                processorList.add(json.getString(Config.TAG_KD_PROCESSOR));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterSpKdProcessor =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, processorList);
        spKdProcessor.setAdapter(adapterSpKdProcessor);
    }

    private void getSpKdVGA() {
        String url = Config.URL_VIEW_ALL_VGA;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject j = null;
                JSONArray result = null;
                try {
                    j = new JSONObject(response);
                    result = j.getJSONArray(Config.TAG_JSON_ARRAY);
                    getKdVGA(result);
                    alertDialog.dismiss();
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

    private void getKdVGA(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                vgaList.add(json.getString(Config.TAG_KD_VGA));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterSpKdVGA =
                new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, vgaList);
        spKdVGA.setAdapter(adapterSpKdVGA);
    }

    private void getP_Laptop() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
                viewP_Laptop();
            }
        }, 450);
    }

    public void viewP_Laptop() {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        String url = Config.URL_VIEW_EACH_P_LAPTOP  + NO_LAPTOP + "'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showP_Laptop(response);
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

    public void showP_Laptop(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                KD_MEREK = jo.getString(Config.TAG_KD_MEREK);
                KD_VGA = jo.getString(Config.TAG_KD_VGA);
                MODEL = jo.getString(Config.TAG_MODEL);
                HDD = jo.getString(Config.TAG_HDD);

                spKdMerek.setSelection(getSpinnerIndex(spKdMerek, KD_MEREK));
                spKdVGA.setSelection(getSpinnerIndex(spKdVGA, KD_VGA));
                tietModel.setText(MODEL);
                tietHDD.setText(HDD);
            }

            alertDialog.dismiss();
        } catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    private void getTimerspKdProcessor() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                spKdProcessor.setSelection(getSpinnerIndex(spKdProcessor, KD_PROCESSOR));
            }
        }, 4000);
    }
}
