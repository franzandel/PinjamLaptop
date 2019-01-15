package BottomNavigation;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ok.pinjamlaptop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import LoginRegister.InputValidation;
import LoginRegister.Processor;

//import com.example.ok.Mereklaptop.R;

/**
 * Created by OK on 4/12/2018.
 */

public class AddProcessor extends AppCompatActivity implements View.OnClickListener{

    private View activityAddProcessor;
    private TextInputEditText tietKdProcessor, tietNmProcessor;
    private TextInputLayout tilKdProcessor, tilNmProcessor;
    private InputValidation inputValidation;
    private ImageButton ibToolbarBack;
    private final ArrayList<HashMap<String,String>> listHashPengambilan = new ArrayList<HashMap<String,String>>();
    AlertDialog dialog;
    private String KD_PROCESSOR, NM_PROCESSOR;
    private Menu menu;
    MenuItem btSave;
    private int POSITION;
    private final ArrayList<HashMap<String,String>> listHashQtyMerek = new ArrayList<HashMap<String,String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addprocessor);
        activityAddProcessor = (View)findViewById(R.id.activityAddProcessor);
        tietKdProcessor = (TextInputEditText)findViewById(R.id.tietKdProcessor);
        tilKdProcessor = (TextInputLayout) findViewById(R.id.tilKdProcessor);
        tietNmProcessor = (TextInputEditText)findViewById(R.id.tietNmProcessor);
        tilNmProcessor = (TextInputLayout) findViewById(R.id.tilNmProcessor);
        ibToolbarBack = (ImageButton) findViewById(R.id.ibToolbarBack);
        inputValidation = new InputValidation(this);
        this.setTitle(null);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar_activity);
        setSupportActionBar(mToolbar);
        /*default back in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        Intent intent = getIntent();
        if (intent.getStringExtra("KD_PROCESSOR") != null) {
            KD_PROCESSOR = intent.getStringExtra("KD_PROCESSOR");
            NM_PROCESSOR = intent.getStringExtra("NM_PROCESSOR");
            POSITION = intent.getIntExtra("POSITION", 0);

            tietKdProcessor.setText(KD_PROCESSOR);
            tietNmProcessor.setText(NM_PROCESSOR);

            tietKdProcessor.setEnabled(false);
            tietNmProcessor.requestFocus();
        }

        ibToolbarBack.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;
        btSave = menu.findItem(R.id.Save);
        if (KD_PROCESSOR != null) {
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
                            AddProcessor();
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
                            ModifyProcessor();
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

    private void AddProcessor() {
        if (!inputValidation.isInputEditTextFilled(tietKdProcessor, tilKdProcessor, "Kode Processor harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietNmProcessor, tilNmProcessor, "Nama Processor harus diisi")) {
            return;
        }
        /*if (isQtyMerekMinus()) {
            return;
        }*/

        final Processor processor = new Processor();
        processor.setKodeProcessor(tietKdProcessor.getText().toString());
        processor.setNamaProcessor(tietNmProcessor.getText().toString());

        String url = Config.URL_ADD_PROCESSOR;
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
                        Snackbar.make(activityAddProcessor, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_KD_PROCESSOR, processor.getKodeProcessor());
                params.put(Config.KEY_NM_PROCESSOR, processor.getNamaProcessor());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Snackbar.make(activityAddProcessor, Html.fromHtml("<font color=\"#ffffff\">Processor berhasil disimpan</font>"), Snackbar.LENGTH_LONG).show();

        ProcessorFragment.processorList.add(processor);
        ProcessorFragment.pAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private void ModifyProcessor() {
        if (!inputValidation.isInputEditTextFilled(tietKdProcessor, tilKdProcessor, "Kode Processor harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietNmProcessor, tilNmProcessor, "Nama Processor harus diisi")) {
            return;
        }
        /*if (isQtyMerekMinus()) {
            return;
        }*/

        final Processor processor = new Processor();
        processor.setKodeProcessor(tietKdProcessor.getText().toString());
        processor.setNamaProcessor(tietNmProcessor.getText().toString());

        String url = Config.URL_UPDATE_PROCESSOR;
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
                        Snackbar.make(activityAddProcessor, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_KD_PROCESSOR, processor.getKodeProcessor());
                params.put(Config.KEY_NM_PROCESSOR, processor.getNamaProcessor());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Snackbar.make(activityAddProcessor, Html.fromHtml("<font color=\"#ffffff\">Processor berhasil diupdate</font>"), Snackbar.LENGTH_LONG).show();

        ProcessorFragment.processorList.set(POSITION, processor);
        ProcessorFragment.pAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private boolean isMerekUsed() {
        // NOT GETTING LAPTOP BASED ON listHashPengambilan
        // BECAUSE IT MIGHT NOT HAVE ENOUGH TIME TO GET DATA FROM DB
        // IF SQLITE IT IS OKAY
        for (final Map<String, String> entry : listHashPengambilan) {
            for (final Map<String, String> db : listHashQtyMerek) {
                if (entry.get("NoLaptop").equals(db.get("NoLaptop"))) {
                    int qtyTotal = Integer.parseInt(db.get("QtyTotal"));
                    int qtyTerMerek = Integer.parseInt(db.get("QtyMerek"));
                    int qtyMerek = Integer.parseInt(entry.get("QtyMerek")) +
                                    qtyTerMerek;

                    if (qtyTotal < qtyMerek) {
                        Toast.makeText(this, entry.get("NoLaptop") +
                                " sudah tidak memiliki stok", Toast.LENGTH_LONG).show();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void ClearAll() {
        tietKdProcessor.requestFocus();
        tietKdProcessor.setText(null);
        tietNmProcessor.setText(null);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibToolbarBack:
                finish();
                break;
        }
    }
}
