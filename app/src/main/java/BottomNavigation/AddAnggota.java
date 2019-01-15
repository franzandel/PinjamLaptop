package BottomNavigation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageButton;
import android.widget.ImageView;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import LoginRegister.Anggota;
import LoginRegister.InputValidation;
import dmax.dialog.SpotsDialog;

//import com.example.ok.Mereklaptop.R;

/**
 * Created by OK on 4/12/2018.
 */

public class AddAnggota extends AppCompatActivity implements View.OnClickListener{

    private View activityAddAnggota;
    private TextInputEditText tietNoAnggota, tietNmAnggota, tietAlamat, tietNoTelp, tietEmailAnggota;
    private TextInputLayout tilNoAnggota, tilNmAnggota, tilAlamat, tilNoTelp, tilEmailAnggota;
    private InputValidation inputValidation;
    private ImageButton ibToolbarBack;
    private final ArrayList<HashMap<String,String>> listHashPengambilan = new ArrayList<HashMap<String,String>>();
    AlertDialog dialog;
    private String NO_ANGGOTA, NM_ANGGOTA, GENDER, ALAMAT, NO_TELP, EMAIL_ANGGOTA, FOTO_ANGGOTA;
    private int POSITION;
    private Menu menu;
    MenuItem btSave;
    private String stietNoAnggota, stietNmAnggota, sspGender, stietAlamat, stietNoTelp, stietEmailAnggota;
    private final ArrayList<HashMap<String,String>> listHashQtyMerek = new ArrayList<HashMap<String,String>>();
    private Spinner spGender;
    private android.app.AlertDialog alertDialog;
    private static Bitmap Image = null;
    private static Bitmap rotateImage = null;
    private ImageView ivFotoAnggota;
    private static final int GALLERY = 1;
    Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addanggota);
        activityAddAnggota = (View)findViewById(R.id.activityAddAnggota);
        ivFotoAnggota = (ImageView)findViewById(R.id.ivFotoAnggota);
        tietNoAnggota = (TextInputEditText)findViewById(R.id.tietNoAnggota);
        tilNoAnggota = (TextInputLayout) findViewById(R.id.tilNoAnggota);
        tietNmAnggota = (TextInputEditText)findViewById(R.id.tietNmAnggota);
        tilNmAnggota = (TextInputLayout) findViewById(R.id.tilNmAnggota);
        tietAlamat = (TextInputEditText)findViewById(R.id.tietAlamat);
        tilAlamat = (TextInputLayout) findViewById(R.id.tilAlamat);
        tietNoTelp = (TextInputEditText)findViewById(R.id.tietNoTelp);
        tilNoTelp = (TextInputLayout) findViewById(R.id.tilNoTelp);
        tietEmailAnggota = (TextInputEditText)findViewById(R.id.tietEmailAnggota);
        tilEmailAnggota = (TextInputLayout) findViewById(R.id.tilEmailAnggota);
        ibToolbarBack = (ImageButton) findViewById(R.id.ibToolbarBack);
        spGender = (Spinner)findViewById(R.id.spGender);
        inputValidation = new InputValidation(this);
        this.setTitle(null);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar_activity);
        setSupportActionBar(mToolbar);
        /*default back in toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        Intent intent = getIntent();
        if (intent.getStringExtra("NO_ANGGOTA") != null) {
            NO_ANGGOTA = intent.getStringExtra("NO_ANGGOTA");
            NM_ANGGOTA = intent.getStringExtra("NM_ANGGOTA");
            GENDER = intent.getStringExtra("GENDER");
            POSITION = intent.getIntExtra("POSITION", 0);

            getEachAnggota();

            tietNoAnggota.setText(NO_ANGGOTA);
            tietNmAnggota.setText(NM_ANGGOTA);
            spGender.setSelection(getSpinnerIndex(spGender, GENDER));

            tietNoAnggota.setEnabled(false);
            tietNmAnggota.requestFocus();
        }

        ibToolbarBack.setOnClickListener(this);
        ivFotoAnggota.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        this.menu = menu;
        btSave = menu.findItem(R.id.Save);
        if (NO_ANGGOTA != null) {
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
                            AddAnggota();
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
                            ModifyAnggota();
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

    private void AddAnggota() {
        if (!inputValidation.isInputEditTextFilled(tietNoAnggota, tilNoAnggota, "Kode Anggota harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietNmAnggota, tilNmAnggota, "Nama Anggota harus diisi")) {
            return;
        }
        if (!inputValidation.isInputSpinnerGenderChoosed(spGender, "Gender harus dipilih")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietAlamat, tilAlamat, "Alamat harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietNoTelp, tilNoTelp, "Nomor Telepon harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(tietEmailAnggota, tilEmailAnggota, "Email harus diisi")) {
            return;
        }
        /*if (isQtyMerekMinus()) {
            return;
        }*/

        final Anggota anggota = new Anggota();
        anggota.setNomorAnggota(tietNoAnggota.getText().toString());
        anggota.setNamaAnggota(tietNmAnggota.getText().toString());
        anggota.setGender(spGender.getSelectedItem().toString());
        anggota.setAlamat(tietAlamat.getText().toString());
        anggota.setNoTelepon(tietNoTelp.getText().toString());
        anggota.setEmailAnggota(tietEmailAnggota.getText().toString());

        String url = Config.URL_ADD_ANGGOTA;
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
                        Snackbar.make(activityAddAnggota, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_NO_ANGGOTA, anggota.getNomorAnggota());
                params.put(Config.KEY_NM_ANGGOTA, anggota.getNamaAnggota());
                params.put(Config.KEY_GENDER, anggota.getGender());
                params.put(Config.KEY_ALAMAT, anggota.getAlamat());
                params.put(Config.KEY_NO_TELP, anggota.getNoTelepon());
                params.put(Config.KEY_EMAIL_ANGGOTA, anggota.getEmailAnggota());
                params.put(Config.KEY_FOTO_ANGGOTA, imageUri == null ? "" : getRealPathFromURI(imageUri));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Snackbar.make(activityAddAnggota, Html.fromHtml("<font color=\"#ffffff\">Anggota berhasil disimpan</font>"), Snackbar.LENGTH_LONG).show();

        AnggotaFragment.anggotaList.add(anggota);
        AnggotaFragment.aAdapter.notifyDataSetChanged();
        ClearAll();
    }

    private void ModifyAnggota() {
        if (!inputValidation.isInputEditTextFilled(tietNoAnggota, tilNoAnggota, "Kode Anggota harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietNmAnggota, tilNmAnggota, "Nama Anggota harus diisi")) {
            return;
        }
        if (!inputValidation.isInputSpinnerGenderChoosed(spGender, "Gender harus dipilih")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietAlamat, tilAlamat, "Alamat harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(tietNoTelp, tilNoTelp, "Nomor Telepon harus diisi")) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(tietEmailAnggota, tilEmailAnggota, "Email harus diisi")) {
            return;
        }
        /*if (isQtyMerekMinus()) {
            return;
        }*/

        final Anggota anggota = new Anggota();
        anggota.setNomorAnggota(tietNoAnggota.getText().toString());
        anggota.setNamaAnggota(tietNmAnggota.getText().toString());
        anggota.setGender(spGender.getSelectedItem().toString());
        anggota.setAlamat(tietAlamat.getText().toString());
        anggota.setNoTelepon(tietNoTelp.getText().toString());
        anggota.setEmailAnggota(tietEmailAnggota.getText().toString());

        String url = Config.URL_UPDATE_ANGGOTA;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Snackbar.make(activityAddAnggota, Html.fromHtml("<font color=\"#ffffff\">" + response + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Snackbar.make(activityAddAnggota, Html.fromHtml("<font color=\"#ffffff\">" + volleyError.getMessage() + "</font>"), Snackbar.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put(Config.KEY_NO_ANGGOTA, anggota.getNomorAnggota());
                params.put(Config.KEY_NM_ANGGOTA, anggota.getNamaAnggota());
                params.put(Config.KEY_GENDER, anggota.getGender());
                params.put(Config.KEY_ALAMAT, anggota.getAlamat());
                params.put(Config.KEY_NO_TELP, anggota.getNoTelepon());
                params.put(Config.KEY_EMAIL_ANGGOTA, anggota.getEmailAnggota());
                params.put(Config.KEY_FOTO_ANGGOTA, imageUri == null ? "" : getRealPathFromURI(imageUri));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

        Snackbar.make(activityAddAnggota, Html.fromHtml("<font color=\"#ffffff\">Anggota berhasil diupdate</font>"), Snackbar.LENGTH_LONG).show();

        AnggotaFragment.anggotaList.set(POSITION, anggota);
        AnggotaFragment.aAdapter.notifyDataSetChanged();
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
        tietNoAnggota.requestFocus();
        ivFotoAnggota.setImageResource(R.drawable.ic_empty_user);
        tietNoAnggota.setText(null);
        tietNmAnggota.setText(null);
        spGender.setSelection(0);
        tietAlamat.setText(null);
        tietNoTelp.setText(null);
        tietEmailAnggota.setText(null);
    }

    private void getEachAnggota() {
        alertDialog = new SpotsDialog(this);
        alertDialog.show();
        new Handler().postDelayed(new Runnable(){
            public void run() {
                viewEachAnggota();
            }
        }, 450);
    }

    public void viewEachAnggota() {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        String url = Config.URL_VIEW_EACH_ANGGOTA  + NO_ANGGOTA + "'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showEachAnggota(response);
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

    public void showEachAnggota(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject jo = data.getJSONObject(0);

            ALAMAT = jo.getString(Config.TAG_ALAMAT);
            NO_TELP = jo.getString(Config.TAG_NO_TELP);
            EMAIL_ANGGOTA = jo.getString(Config.TAG_EMAIL_ANGGOTA);
            FOTO_ANGGOTA = jo.getString(Config.TAG_FOTO_ANGGOTA);

            tietAlamat.setText(ALAMAT);
            tietNoTelp.setText(NO_TELP);
            tietEmailAnggota.setText(EMAIL_ANGGOTA);

            File imgFile = new File(FOTO_ANGGOTA);

            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                ivFotoAnggota.setImageBitmap(myBitmap);
            }

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

    public String getRealPathFromURI(Uri contentUri) {
        String path = null, image_id = null;

        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            image_id = cursor.getString(0);
            image_id = image_id.substring(image_id.lastIndexOf(":") + 1);
            cursor.close();
        }

        cursor = getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);
        if (cursor!=null) {
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
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
                    ivFotoAnggota.setImageBitmap(rotateImage);
                } else
                    ivFotoAnggota.setImageBitmap(Image);
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
                ivFotoAnggota.setImageBitmap(null);
                if (Image != null)
                    Image.recycle();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY);
                break;
        }
    }
}
