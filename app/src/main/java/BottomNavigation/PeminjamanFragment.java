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
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
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
import android.widget.ListView;
import android.widget.SearchView;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LoginRegister.Peminjaman;
import LoginRegister.Pengembalian;
import Recycler.MyDividerItemDecoration;
import Recycler.PeminjamanAdapter;
import Recycler.RecyclerTouchListener;
import Recycler.ViewDtlPeminjamanAdapter;
import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class PeminjamanFragment extends Fragment implements AlertDialogHelper.AlertDialogListener {

    private View fragmentPeminjaman;
    public static List<Peminjaman> peminjamanList = new ArrayList<>();
    public RecyclerView recyclerView;
    public static PeminjamanAdapter pAdapter;
    private FloatingActionButton faAdd;
    private Peminjaman peminjaman;
    SwipeRefreshLayout swipeToRefresh;
    private Paint p = new Paint();
    final ArrayList<HashMap<String,String>> listHashPeminjaman = new ArrayList<HashMap<String,String>>();
    private boolean isUndoClicked = false;
    List<Peminjaman> filteredPeminjaman = new ArrayList<>();
    private boolean isMultiSelect = false;
    ArrayList<Peminjaman> multiSelectpeminjamanList = new ArrayList<>();
    ActionMode mActionMode;
    Menu context_menu;
    AlertDialogHelper alertDialogHelper;
    private ListView listDtlPeminjaman;
    ViewDtlPeminjamanAdapter viewDtlPeminjamanAdapter;
    private android.app.AlertDialog alertDialog;

    public PeminjamanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_peminjaman, container, false);

        fragmentPeminjaman = (View)view.findViewById(R.id.fragmentPeminjaman);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        faAdd = (FloatingActionButton)view.findViewById(R.id.fabAdd);
        swipeToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);
        listDtlPeminjaman = (ListView)view.findViewById(R.id.listDtlPeminjaman);
        peminjaman = new Peminjaman();
        alertDialogHelper = new AlertDialogHelper(this, getContext());

        swipeToRefresh.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), android.R.color.holo_blue_bright),
                ContextCompat.getColor(getActivity(), android.R.color.holo_green_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_orange_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));

        pAdapter = new PeminjamanAdapter(getActivity(), peminjamanList, multiSelectpeminjamanList);
        viewDtlPeminjamanAdapter = new ViewDtlPeminjamanAdapter(listHashPeminjaman);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(pAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else {
                    Peminjaman peminjaman = peminjamanList.get(position);
                    viewP_Pinjam(peminjaman);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View rootView = inflater.inflate(R.layout.view_p_pinjam, null);
                    listDtlPeminjaman = (ListView)rootView.findViewById(R.id.listDtlPeminjaman);
                    builder.setView(rootView);
                    builder.setCancelable(true);

                    builder.setTitle("Detail " + peminjaman.getKd_Pinjam());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setLayout(1000, 800);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiSelectpeminjamanList = new ArrayList<Peminjaman>();
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
                showAddPeminjaman(peminjaman, 0);
            }
        });

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getM_Pinjam();
            }
        });

        initSwipe();
        getM_Pinjam();

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
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    final Peminjaman deletedItem = peminjamanList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    if (hasPengembalian(deletedItem)) {
                        Toast.makeText(getActivity(), "Sudah ada data Pengembalian", Toast.LENGTH_LONG).show();
                        pAdapter.notifyDataSetChanged();
                        return;
                    }

                    pAdapter.removeItem(position);

                    Snackbar snackbar = Snackbar.make(fragmentPeminjaman, Html.fromHtml("<font color=\"#ffffff\">DELETED</font>"), Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isUndoClicked = true;
                            pAdapter.restoreItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                    snackbar.addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            if (!isUndoClicked) {
                                viewP_Pinjam(deletedItem);
                                hapusQtyPinjam();
                                hapusM_Pinjam(deletedItem);
                                hapusInventaris(deletedItem);
                                Toast.makeText(getActivity(), "Peminjaman berhasil dihapus", Toast.LENGTH_LONG).show();
                            }
                            isUndoClicked = false;
                        }
                    });
                } else {
                    Peminjaman peminjaman = peminjamanList.get(position);
                    showAddPeminjaman(peminjaman, position);
                    pAdapter.notifyDataSetChanged();
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

    public void getM_Pinjam() {
        alertDialog = new SpotsDialog(getActivity());
        alertDialog.show();
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                viewM_Pinjam();
            }
        }, 3000);
    }

    public void viewM_Pinjam() {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        String url = Config.URL_VIEW_M_PINJAM;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showM_Pinjam(response);
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

    public void showM_Pinjam(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            peminjamanList.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                Peminjaman peminjaman = new Peminjaman();
                peminjaman.setKd_Pinjam(jo.getString(Config.TAG_KD_PINJAM));
                peminjaman.setNo_Anggota(jo.getString(Config.TAG_NO_ANGGOTA));
                peminjaman.setTgl_Pinjam(jo.getString(Config.TAG_TGL_PINJAM));
                peminjamanList.add(peminjaman);
            }

            filteredPeminjaman.clear();
            filteredPeminjaman.addAll(peminjamanList);

            pAdapter.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
            alertDialog.dismiss();
        } catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    public void viewP_Pinjam(final Peminjaman peminjaman) {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        String url = Config.URL_VIEW_P_PINJAM + peminjaman.getKd_Pinjam() + "'";
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
                        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(stringRequest);
    }

    public void showP_Pinjam(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            listHashPeminjaman.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("NoLaptop", jo.getString(Config.TAG_NO_LAPTOP));
                temp.put("QtyPinjam", jo.getString(Config.TAG_QTY_PINJAM));
                temp.put("Keperluan", jo.getString(Config.TAG_KEPERLUAN));
                listHashPeminjaman.add(temp);
            }

            viewDtlPeminjamanAdapter = new ViewDtlPeminjamanAdapter(listHashPeminjaman);
            if (listDtlPeminjaman == null) return;
            listDtlPeminjaman.setAdapter(viewDtlPeminjamanAdapter);
        } catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    private void hapusM_Pinjam (final Peminjaman peminjaman) {
        String url = Config.URL_DELETE_M_PINJAM + peminjaman.getKd_Pinjam() + "'";
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

    private void hapusInventaris (final Peminjaman peminjaman) {
        String url = Config.URL_DELETE_INVENTARIS + peminjaman.getTgl_Pinjam() + "'";
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

    private void hapusQtyPinjam () {
        for (final Map<String, String> entry : listHashPeminjaman) {
            String url = Config.URL_DELETE_QTY_PINJAM;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Toast.makeText(getActivity(), response, Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })  {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(Config.KEY_NO_LAPTOP, entry.get("NoLaptop"));
                    params.put(Config.KEY_QTY_PINJAM, entry.get("QtyPinjam"));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    private boolean hasPengembalian(Peminjaman deletedItem) {
        List<Pengembalian> pengembalianList = PengembalianFragment.pengembalianList;
        for (Pengembalian kembali : pengembalianList) {
            if (deletedItem.getKd_Pinjam().equals(kembali.getKd_Pinjam())) {
                if (!kembali.getFlowKembali().equals(getResources().getString(R.string.flowNone))) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showAddPeminjaman(Peminjaman peminjaman, int position) {
        Intent accountsIntent = new Intent(getActivity(), AddPeminjaman.class);
        if (peminjaman.getKd_Pinjam() != null) {
            accountsIntent.putExtra("KD_PINJAM", peminjaman.getKd_Pinjam().trim());
            accountsIntent.putExtra("NO_ANGGOTA", peminjaman.getNo_Anggota().trim());
            accountsIntent.putExtra("TGL_PINJAM", peminjaman.getTgl_Pinjam());
            accountsIntent.putExtra("POSITION", position);
        }
        startActivity(accountsIntent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        if (menu.size() == 0) {
            menu.clear();
            inflater.inflate(R.menu.search_menu, menu);

            final MenuItem item = menu.findItem(R.id.item_search_data);
            final android.widget.SearchView searchView = (android.widget.SearchView) MenuItemCompat.getActionView(item);
            searchView.setQueryHint(getString(R.string.search_peminjaman));
            searchView.setIconified(true);
            // To Overcome
            // When you perform filter and then Switch between tabs in TabLayout
            // In such scenario the recyclerview won’t return the original set of data
            MenuItemCompat.setOnActionExpandListener(item,
                    new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            pAdapter.setFilter(peminjamanList);
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
                    filteredPeminjaman = filterPeminjaman(peminjamanList, newText);

                    pAdapter.setFilter(filteredPeminjaman);
                    return true;
                }
            });
            super.onCreateOptionsMenu(menu, inflater);
//        }
    }

    private List<Peminjaman> filterPeminjaman(List<Peminjaman> listPinjam, String query) {
        query = query.toLowerCase();
        final List<Peminjaman> filteredPeminjaman = new ArrayList<>();

        for (Peminjaman pinjam : listPinjam) {
            final String kdPinjam = pinjam.getKd_Pinjam().toLowerCase();
            String tglPinjam = pinjam.getTgl_Pinjam().toLowerCase();
            String tglPinjamNoTime = tglPinjam.substring(0, tglPinjam.length() - 9);
            final String noAnggota = pinjam.getNo_Anggota().toLowerCase();
            if (kdPinjam.contains(query) ||
                    tglPinjamNoTime.contains(query) ||
                        noAnggota.contains(query)) {
                filteredPeminjaman.add(pinjam);
            }
        }

        return filteredPeminjaman;
    }

    public void multi_selectAll() {
        if (mActionMode != null) {
            if (multiSelectpeminjamanList.size() < peminjamanList.size()) {
                multiSelectpeminjamanList.clear();
                multiSelectpeminjamanList.addAll(peminjamanList);
            } else {
                multiSelectpeminjamanList.removeAll(peminjamanList);
            }

            if (multiSelectpeminjamanList.size() > 0)
                mActionMode.setTitle(multiSelectpeminjamanList.size() + " Selected");
            else
                mActionMode.setTitle("0 Selected");

            refreshAdapter();
        }
    }

    public void refreshAdapter()
    {
        pAdapter.selected_peminjamanList = multiSelectpeminjamanList;
        pAdapter.peminjamanList = peminjamanList;
        pAdapter.notifyDataSetChanged();
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiSelectpeminjamanList.contains(peminjamanList.get(position)))
                multiSelectpeminjamanList.remove(peminjamanList.get(position));
            else
                multiSelectpeminjamanList.add(peminjamanList.get(position));

            if (multiSelectpeminjamanList.size() > 0)
                mActionMode.setTitle(multiSelectpeminjamanList.size() + " Selected");
            else
                mActionMode.setTitle("0 Selected");

            refreshAdapter();

        }
    }

    private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
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
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_delete:
                    alertDialogHelper.showAlertDialog("","Delete Peminjaman","DELETE","CANCEL",1,false);
                    return true;
                case R.id.action_selectAll:
                    multi_selectAll();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            isMultiSelect = false;
            multiSelectpeminjamanList = new ArrayList<Peminjaman>();
            refreshAdapter();
        }
    };

    @Override
    public void onPositiveClick(int from) {
        if(from==1)
        {
            if(multiSelectpeminjamanList.size() > 0)
            {
                final List<Peminjaman> deletedItem = new ArrayList<>();
                final List<Integer> deletedIndex = new ArrayList<>();

                final int totalMultiSelect = multiSelectpeminjamanList.size();

                // WHY SEPERATE TWO LOOPS?
                // TO EXECUTE DELETE ONLY IF ALL SELECTION HAVEN'T HAD PEMINJAMAN
                for(int i = 0; i < multiSelectpeminjamanList.size(); i++) {
                    if (hasPengembalian(multiSelectpeminjamanList.get(i))) {
                        Toast.makeText(getActivity(), multiSelectpeminjamanList.get(i).getKd_Pinjam() +
                                " sudah ada data Pengembalian", Toast.LENGTH_LONG).show();
                        return;
                    }
                    deletedItem.add(multiSelectpeminjamanList.get(i));
                    deletedIndex.add(peminjamanList.indexOf(multiSelectpeminjamanList.get(i)));
                }

                for(int i = 0; i < multiSelectpeminjamanList.size(); i++) {
                    peminjamanList.remove(deletedItem.get(i));
                }

                Snackbar snackbar = Snackbar.make(fragmentPeminjaman, Html.fromHtml("<font color=\"#ffffff\">DELETED</font>"), Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isUndoClicked = true;
                        for (int i = 0; i < deletedIndex.size(); i++) {
                            pAdapter.restoreItem(deletedItem.get(i), deletedIndex.get(i));
                        }
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (!isUndoClicked) {
                            for (Peminjaman peminjaman : deletedItem) {
                                viewP_Pinjam(peminjaman);
                                hapusQtyPinjam();
                                hapusM_Pinjam(peminjaman);
                                hapusInventaris(peminjaman);
                            }
                            Toast.makeText(getActivity(), totalMultiSelect +
                                    " item(s) deleted", Toast.LENGTH_LONG).show();
                        }
                        isUndoClicked = false;
                    }
                });

                pAdapter.notifyDataSetChanged();

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
            pAdapter.notifyDataSetChanged();*/

        }
    }

    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }
}
