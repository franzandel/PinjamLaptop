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
import LoginRegister.VGA;

//import com.example.ok.Mereklaptop.R;

/**
 * Created by OK on 4/12/2018.
 */

public class AddVGA extends AppCompatActivity implements View.OnClickListener{

    private View activityAddVGA;
    private TextInputEditText tietKdVGA, tietNmVGA;
    private TextInputLayout tilKdVGA, tilNmVGA;
    private InputValidation inputValidation;
    private ImageButton ibToolbarBack;
    private final ArrayList<HashMap<String,String>> listHashPengambilan = new ArrayList<HashMap<String,String>>();
    AlertDialog dialog;
    private String KD_VGA, NM_VGA;
    private Menu menu;
    MenuItem btSave;
    private String stietKdVGA, stietNmVGA;
    private int POSITION;
    private final ArrayList<HashMap<String,String>> listHashQtyMerek = new ArrayList<HashMap<String,String>>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addvga);
        activityAddVGA = (View)findViewById(R.id.activityAddVGA);
        tietKdVGA = (TextInputEditText)findViewById(R.id.tietKdVGA);
        tilKdVGA = (TextInputLayout) findViewById(R.id.tilKdVGA);
        tietNmVGA = (TextInputEditText)findViewById(R.id.tietNmVGA);
        tilNmVGA = (TextInputLayout) findViewById(R.id.tilNmVGA);
        ibToolbarBack = (ImageButton) findViewById(R.id.ibToolbarBack);
        inputValidation = new InputValidation(this);
        this.setTitle(null);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar_activity);
        setSupportActionBar(mToolbar);
        /*default back in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        Intent intent = getIntent();
        if (intent.getStringExtra("KD_VGA") != null) {
            KD_VGA = intent.getStringExtra("KD_VGA");
            NM_VGA = intent.getStringExtra("NM_VGA");
            POSITION = intent.getIntExtra("POSITION", 0);

            tietKdVGA.setText(KD_VGA);
            tietNmVGA.setText(NM_VGA);

            tietKdVGA.setEnabled(false);
            tietNmVGA.requestFocus();
        }

        ibToolbarBack.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;
        btSave = menu.findItem(R.id.Save);
        if (KD_VGA != null) {
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
                            AddVGA();
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
                            ModifyVGA();
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

    private void AddVGA() {
        if (!inputValidation.isInputEditTextFilled(tietKdVGA, tilKdVGA, "Kode VGA harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietNmVGA, tilNmVGA, "Nama VGA harus diisi")) {
            return;
        }
        /*if (isQtyMerekMinus()) {
            return;
        }*/

        final VGA vga = new VGA();
        vga.setKodeVGA(tietKdVGA.getText().toString());
        vga.setNamaVGA(tietNmVGA.getText().toString());

        String url = Config.URL_ADD_VGA;
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
                        Snackbar.make(activityAddVGA, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_KD_VGA, vga.getKodeVGA());
                params.put(Config.KEY_NM_VGA, vga.getNamaVGA());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Snackbar.make(activityAddVGA, Html.fromHtml("<font color=\"#ffffff\">VGA berhasil disimpan</font>"), Snackbar.LENGTH_LONG).show();

        VGAFragment.vgaList.add(vga);
        VGAFragment.vAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private void ModifyVGA() {
        if (!inputValidation.isInputEditTextFilled(tietKdVGA, tilKdVGA, "Kode VGA harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietNmVGA, tilNmVGA, "Nama VGA harus diisi")) {
            return;
        }
        /*if (isQtyMerekMinus()) {
            return;
        }*/

        final VGA vga = new VGA();
        vga.setKodeVGA(tietKdVGA.getText().toString());
        vga.setNamaVGA(tietNmVGA.getText().toString());

        String url = Config.URL_UPDATE_VGA;
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
                        Snackbar.make(activityAddVGA, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_KD_VGA, vga.getKodeVGA());
                params.put(Config.KEY_NM_VGA, vga.getNamaVGA());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Snackbar.make(activityAddVGA, Html.fromHtml("<font color=\"#ffffff\">VGA berhasil diupdate</font>"), Snackbar.LENGTH_LONG).show();

        VGAFragment.vgaList.set(POSITION, vga);
        VGAFragment.vAdapter.notifyDataSetChanged();
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
        tietKdVGA.requestFocus();
        tietKdVGA.setText(null);
        tietNmVGA.setText(null);
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
