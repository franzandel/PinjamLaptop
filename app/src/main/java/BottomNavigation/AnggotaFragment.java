package BottomNavigation;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Html;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LoginRegister.Anggota;
import Recycler.MyDividerItemDecoration;
import Recycler.RecyclerTouchListener;
import Recycler.AnggotaAdapter;

/**
 * Created by ryuze on 6/26/2018.
 */

public class AnggotaFragment extends Fragment implements AlertDialogHelper.AlertDialogListener {

    private View fragmentAnggota;
    public static List<Anggota> anggotaList = new ArrayList<>();
    public RecyclerView recyclerView;
    public static AnggotaAdapter aAdapter;
    private FloatingActionButton faAdd;
    private Anggota anggota;
    ArrayList<HashMap<String, String>> dataList;
    SwipeRefreshLayout swipeToRefresh;
    private Paint p = new Paint();
    private boolean isUndoClicked = false;
    List<Anggota> filteredAnggota = new ArrayList<>();
    private boolean isMultiSelect = false;
    ArrayList<Anggota> multiSelectAnggotaList = new ArrayList<>();
    ActionMode mActionMode;
    Menu context_menu;
    AlertDialogHelper alertDialogHelper;
    private android.app.AlertDialog alertDialog;
    TextInputEditText tietAlamat, tietNoTelp, tietEmailAnggota;
    final ArrayList<HashMap<String,String>> listHashPeminjaman = new ArrayList<HashMap<String,String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_anggota, container, false);

        fragmentAnggota = (View)view.findViewById(R.id.fragmentAnggota);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        faAdd = (FloatingActionButton)view.findViewById(R.id.fabAdd);
        swipeToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);
        dataList = new ArrayList<HashMap<String, String>>();
        anggota = new Anggota();
        alertDialogHelper = new AlertDialogHelper(this, getContext());

        swipeToRefresh.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), android.R.color.holo_blue_bright),
                ContextCompat.getColor(getActivity(), android.R.color.holo_green_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_orange_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));

        aAdapter = new AnggotaAdapter(getActivity(), anggotaList, multiSelectAnggotaList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(aAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else {
                    Anggota anggota = anggotaList.get(position);
                    viewEachAnggota(anggota);
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View rootView = inflater.inflate(R.layout.view_anggota, null);
                    TextInputEditText tietNoAnggota = (TextInputEditText)rootView.findViewById(R.id.tietNoAnggota);
                    TextInputEditText tietNmAnggota = (TextInputEditText)rootView.findViewById(R.id.tietNmAnggota);
                    Spinner spGender = (Spinner)rootView.findViewById(R.id.spGender);
                    tietAlamat = (TextInputEditText)rootView.findViewById(R.id.tietAlamat);
                    tietNoTelp = (TextInputEditText)rootView.findViewById(R.id.tietNoTelp);
                    tietEmailAnggota = (TextInputEditText)rootView.findViewById(R.id.tietEmailAnggota);

                    tietNoAnggota.setText(anggota.getNomorAnggota());
                    tietNmAnggota.setText(anggota.getNamaAnggota());
                    spGender.setSelection(getSpinnerIndex(spGender, anggota.getGender()));

                    tietNoAnggota.setEnabled(false);
                    tietNmAnggota.setEnabled(false);
                    spGender.setEnabled(false);
                    tietAlamat.setEnabled(false);
                    tietNoTelp.setEnabled(false);
                    tietEmailAnggota.setEnabled(false);

                    builder.setView(rootView);
                    builder.setCancelable(true);

                    builder.setTitle("Detail " + anggota.getNomorAnggota());
                    android.support.v7.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setLayout(1000, 1200);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiSelectAnggotaList = new ArrayList<Anggota>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = getActivity().startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);
            }
        }));

        faAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddAnggota(anggota, 0);
            }
        });

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTimerAnggota();
            }
        });

        initSwipe();
        getPeminjaman();
        getTimerAnggota();

        return view;
    }

    private void initSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                final int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    final Anggota deletedItem = anggotaList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    String url = Config.URL_VALIDASI_ANGGOTA + deletedItem.getNomorAnggota() + "'";
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.length() == 13) {
                                        aAdapter.removeItem(position);

                                        Snackbar snackbar = Snackbar.make(fragmentAnggota, Html.fromHtml("<font color=\"#ffffff\">DELETED</font>"), Snackbar.LENGTH_LONG);
                                        snackbar.setAction("UNDO", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                isUndoClicked = true;
                                                aAdapter.restoreItem(deletedItem, deletedIndex);
                                            }
                                        });
                                        snackbar.setActionTextColor(Color.YELLOW);
                                        snackbar.show();
                                        snackbar.addCallback(new Snackbar.Callback() {
                                            @Override
                                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                                if (!isUndoClicked) {
                                                    hapusAnggota(deletedItem);
                                                    Toast.makeText(getActivity(), "Anggota berhasil dihapus", Toast.LENGTH_LONG).show();
                                                }
                                                isUndoClicked = false;
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), deletedItem.getNomorAnggota()
                                                + " sudah ada data Laptop", Toast.LENGTH_LONG).show();
                                        aAdapter.notifyDataSetChanged();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {
                                    Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                    RequestQueue rq = Volley.newRequestQueue(getActivity());
                    rq.add(stringRequest);
                } else {
                    Anggota anggota = anggotaList.get(position);
                    showAddAnggota(anggota, position);
                    aAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){

                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    if(dX > 0){
                        p.setColor(Color.parseColor("#388E3C"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_edit);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    } else if (dX < 0){
                        p.setColor(Color.parseColor("#D32F2F"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_delete);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        c.drawBitmap(icon,null,icon_dest,p);
                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_master) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAddAnggota(Anggota anggota, int position) {
        Intent accountsIntent = new Intent(getActivity(), AddAnggota.class);
        if (anggota.getNomorAnggota() != null) {
            accountsIntent.putExtra("NO_ANGGOTA", anggota.getNomorAnggota().trim());
            accountsIntent.putExtra("NM_ANGGOTA", anggota.getNamaAnggota().trim());
            accountsIntent.putExtra("GENDER", anggota.getGender().trim());
            accountsIntent.putExtra("POSITION", position);
        }
        startActivity(accountsIntent);
    }

    protected void getTimerAnggota() {
        new Handler().postDelayed(new Runnable(){
            public void run() {
                viewDataAnggota();
            }
        }, 3000);
    }

    public void viewDataAnggota() {
        String url = Config.URL_VIEW_ALL_ANGGOTA;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showDataAnggota(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(stringRequest);
    }

    public void showDataAnggota(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            anggotaList.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                Anggota anggota = new Anggota();
                anggota.setNomorAnggota(jo.getString(Config.TAG_NO_ANGGOTA));
                anggota.setNamaAnggota(jo.getString(Config.TAG_NM_ANGGOTA));
                anggota.setGender(jo.getString(Config.TAG_GENDER_ANGGOTA));
                anggotaList.add(anggota);
            }

            filteredAnggota.clear();
            filteredAnggota.addAll(anggotaList);

            aAdapter.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
        } catch (JSONException jse){
            jse.printStackTrace();
        }

    }

    private void hapusAnggota (final Anggota anggota) {
        String url = Config.URL_DELETE_ANGGOTA + anggota.getNomorAnggota() + "'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
//                        Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() != 0) {
            menu.clear();
        }

        inflater.inflate(R.menu.search_menu, menu);

        final MenuItem item = menu.findItem(R.id.item_search_data);
        final android.widget.SearchView searchView = (android.widget.SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.search_anggota));
        searchView.setIconified(true);
        // To Overcome
        // When you perform filter and then Switch between tabs in TabLayout
        // In such scenario the recyclerview won’t return the original set of data
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        aAdapter.setFilter(anggotaList);
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }
                });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filteredAnggota = filterAnggota(anggotaList, newText);

                aAdapter.setFilter(filteredAnggota);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private List<Anggota> filterAnggota(List<Anggota> listAnggota, String query) {
        query = query.toLowerCase();
        final List<Anggota> filteredAnggota = new ArrayList<>();

        for (Anggota anggota : listAnggota) {
            final String kdAnggota = anggota.getNomorAnggota().toLowerCase();
            final String nmAnggota = anggota.getNamaAnggota().toLowerCase();
            final String gender = anggota.getGender().toLowerCase();
            if (kdAnggota.contains(query) ||
                    nmAnggota.contains(query) ||
                        gender.contains(query)) {
                filteredAnggota.add(anggota);
            }
        }

        return filteredAnggota;
    }

    private boolean getPeminjaman() {
        String url = Config.URL_VIEW_M_PINJAM;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getDataPeminjaman(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(stringRequest);
        return false;
    }

    public void getDataPeminjaman(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            listHashPeminjaman.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("KdPinjam", jo.getString(Config.TAG_KD_PINJAM));
                temp.put("NoAnggota", jo.getString(Config.TAG_NO_ANGGOTA));
            temp.put("TglPinjam", jo.getString(Config.TAG_TGL_PINJAM));
            listHashPeminjaman.add(temp);
        }
    } catch (JSONException jse){
        jse.printStackTrace();
    }
}

    public boolean hasPeminjaman(Anggota deletedItem) {
        for (final Map<String, String> data : listHashPeminjaman) {
            if (deletedItem.getNomorAnggota().equals(data.get("NoAnggota"))) {
                return true;
            }
        }
        return false;
    }

    public void multi_selectAll() {
        if (mActionMode != null) {
            if (multiSelectAnggotaList.size() < anggotaList.size()) {
                multiSelectAnggotaList.clear();
                multiSelectAnggotaList.addAll(anggotaList);
            } else {
                multiSelectAnggotaList.removeAll(anggotaList);
            }

            if (multiSelectAnggotaList.size() > 0)
                mActionMode.setTitle(multiSelectAnggotaList.size() + " Selected");
            else
                mActionMode.setTitle("0 Selected");

            refreshAdapter();
        }
    }

    public void refreshAdapter()
    {
        aAdapter.selected_anggotaList = multiSelectAnggotaList;
        aAdapter.anggotaList = anggotaList;
        aAdapter.notifyDataSetChanged();
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiSelectAnggotaList.contains(anggotaList.get(position)))
                multiSelectAnggotaList.remove(anggotaList.get(position));
            else
                multiSelectAnggotaList.add(anggotaList.get(position));

            if (multiSelectAnggotaList.size() > 0)
                mActionMode.setTitle(multiSelectAnggotaList.size() + " Selected");
            else
                mActionMode.setTitle("0 Selected");

            refreshAdapter();
        }
    }

    public void viewEachAnggota(Anggota anggota) {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        String url = Config.URL_VIEW_EACH_ANGGOTA  + anggota.getNomorAnggota() + "'";
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
                        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(stringRequest);
    }

    public void showEachAnggota(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject jo = data.getJSONObject(0);

            tietAlamat.setText(jo.getString(Config.TAG_ALAMAT));
            tietNoTelp.setText(jo.getString(Config.TAG_NO_TELP));
            tietEmailAnggota.setText(jo.getString(Config.TAG_EMAIL_ANGGOTA));
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

    private android.view.ActionMode.Callback mActionModeCallback = new android.view.ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(android.view.ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.menu_multi_select, menu);

            for (int i = 0; i < menu.size(); i++) {
                Drawable drawable = menu.getItem(i).getIcon();
                drawable.mutate();
                drawable.setColorFilter(ContextCompat.getColor(getContext(), R.color.colorBlack), PorterDuff.Mode.SRC_IN);
            }

            context_menu = menu;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(android.view.ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(android.view.ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    alertDialogHelper.showAlertDialog("","Delete Anggota?","DELETE","CANCEL",1,false);
                    return true;
                case R.id.action_selectAll:
                    multi_selectAll();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(android.view.ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiSelectAnggotaList = new ArrayList<Anggota>();
            refreshAdapter();
        }
    };

    @Override
    public void onPositiveClick(int from) {
        if(from==1)
        {
            if(multiSelectAnggotaList.size() > 0)
            {
                final List<Anggota> deletedItem = new ArrayList<>();
                final List<Integer> deletedIndex = new ArrayList<>();

                final int totalMultiSelect = multiSelectAnggotaList.size();

                // WHY SEPERATE TWO LOOPS?
                // TO EXECUTE DELETE ONLY IF ALL SELECTION HAVEN'T HAD PEMINJAMAN
                for(int i = 0; i < multiSelectAnggotaList.size(); i++) {
                    if (hasPeminjaman(multiSelectAnggotaList.get(i))) {
                        Toast.makeText(getActivity(), multiSelectAnggotaList.get(i).getNomorAnggota() +
                                " sudah ada data Laptop", Toast.LENGTH_LONG).show();
                        return;
                    }
                    deletedItem.add(multiSelectAnggotaList.get(i));
                    deletedIndex.add(anggotaList.indexOf(multiSelectAnggotaList.get(i)));
                }

                for(int i = 0; i < multiSelectAnggotaList.size(); i++) {
                    anggotaList.remove(multiSelectAnggotaList.get(i));
                }

                Snackbar snackbar = Snackbar.make(fragmentAnggota, Html.fromHtml("<font color=\"#ffffff\">DELETED</font>"), Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isUndoClicked = true;
                        for (int i = 0; i < deletedIndex.size(); i++) {
                            aAdapter.restoreItem(deletedItem.get(i), deletedIndex.get(i));
                        }
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (!isUndoClicked) {
                            for (Anggota anggota : deletedItem) {
                                hapusAnggota(anggota);
                            }
                            Toast.makeText(getActivity(), totalMultiSelect +
                                    " item(s) deleted", Toast.LENGTH_LONG).show();
                        }
                        isUndoClicked = false;
                    }
                });

                aAdapter.notifyDataSetChanged();

                if (mActionMode != null) {
                    mActionMode.finish();
                }
            }
        }
        else if(from==2)
        {
            if (mActionMode != null) {
                mActionMode.finish();
            }

            /*Peminjaman peminjaman = new Peminjaman();
            peminjaman.
            peminjamanList.add(peminjaman);
            aAdapter.notifyDataSetChanged();*/

        }
    }


    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }
}
