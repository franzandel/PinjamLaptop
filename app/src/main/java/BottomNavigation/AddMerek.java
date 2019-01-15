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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ok.pinjamlaptop.R;

import java.util.HashMap;
import java.util.Map;

import LoginRegister.InputValidation;
import LoginRegister.Merek;

/**
 * Created by OK on 4/12/2018.
 */

public class AddMerek extends AppCompatActivity implements View.OnClickListener{

    private View activityAddMerek;
    private TextInputEditText tietKdMerek, tietNmMerek;
    private TextInputLayout tilKdMerek, tilNmMerek;
    private InputValidation inputValidation;
    private ImageButton ibToolbarBack;
    AlertDialog dialog;
    private String KD_MEREK, NM_MEREK;
    private int POSITION;
    private Menu menu;
    MenuItem btSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmerek);
        activityAddMerek = (View)findViewById(R.id.activityAddMerek);
        tietKdMerek = (TextInputEditText)findViewById(R.id.tietKdMerek);
        tilKdMerek = (TextInputLayout) findViewById(R.id.tilKdMerek);
        tietNmMerek = (TextInputEditText)findViewById(R.id.tietNmMerek);
        tilNmMerek = (TextInputLayout) findViewById(R.id.tilNmMerek);
        ibToolbarBack = (ImageButton) findViewById(R.id.ibToolbarBack);
        inputValidation = new InputValidation(this);
        this.setTitle(null);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar_activity);
        setSupportActionBar(mToolbar);
        /*default back in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        Intent intent = getIntent();
        if (intent.getStringExtra("KD_MEREK") != null) {
            KD_MEREK = intent.getStringExtra("KD_MEREK");
            NM_MEREK = intent.getStringExtra("NM_MEREK");
            POSITION = intent.getIntExtra("POSITION", 0);

            tietKdMerek.setText(KD_MEREK);
            tietNmMerek.setText(NM_MEREK);

            tietKdMerek.setEnabled(false);
            tietNmMerek.requestFocus();
        }

        ibToolbarBack.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;
        btSave = menu.findItem(R.id.Save);
        if (KD_MEREK != null) {
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
                            AddMerek();
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
                            ModifyMerek();
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

    private void AddMerek() {
        if (!inputValidation.isInputEditTextFilled(tietKdMerek, tilKdMerek, "Kode Merek harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietNmMerek, tilNmMerek, "Nama Merek harus diisi")) {
            return;
        }
        /*if (isQtyMerekMinus()) {
            return;
        }*/

        final Merek merek = new Merek();
        merek.setKodeMerek(tietKdMerek.getText().toString());
        merek.setNamaMerek(tietNmMerek.getText().toString());

        String url = Config.URL_ADD_MEREK;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Snackbar.make(activityAddMerek, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(activityAddMerek, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_KD_MEREK, merek.getKodeMerek());
                params.put(Config.KEY_NM_MEREK, merek.getNamaMerek());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Snackbar.make(activityAddMerek, Html.fromHtml("<font color=\"#ffffff\">Merek berhasil disimpan</font>"), Snackbar.LENGTH_LONG).show();

        MerekFragment.merekList.add(merek);
        MerekFragment.mAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private void ModifyMerek() {
        if (!inputValidation.isInputEditTextFilled(tietKdMerek, tilKdMerek, "Kode Merek harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietNmMerek, tilNmMerek, "Nama Merek harus diisi")) {
            return;
        }
        /*if (isQtyMerekMinus()) {
            return;
        }*/

        final Merek merek = new Merek();
        merek.setKodeMerek(tietKdMerek.getText().toString());
        merek.setNamaMerek(tietNmMerek.getText().toString());

        String url = Config.URL_UPDATE_MEREK;
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
                        Snackbar.make(activityAddMerek, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_KD_MEREK, merek.getKodeMerek());
                params.put(Config.KEY_NM_MEREK, merek.getNamaMerek());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Snackbar.make(activityAddMerek, Html.fromHtml("<font color=\"#ffffff\">Merek berhasil diupdate</font>"), Snackbar.LENGTH_LONG).show();

        MerekFragment.merekList.set(POSITION, merek);
        MerekFragment.mAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private void ClearAll() {
        tietKdMerek.requestFocus();
        tietKdMerek.setText(null);
        tietNmMerek.setText(null);
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
