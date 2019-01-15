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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import LoginRegister.Pengembalian;
import Recycler.MyDividerItemDecoration;
import Recycler.PengembalianAdapter;
import Recycler.RecyclerTouchListener;
import Recycler.ViewDtlPengembalianAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class PengembalianFragment extends Fragment  implements AlertDialogHelper.AlertDialogListener {

    private View fragmentPengembalian;
    public static List<Pengembalian> pengembalianList = new ArrayList<>();
    public RecyclerView recyclerView;
    public static PengembalianAdapter pAdapter;
    ArrayList<HashMap<String, String>> dataList;
    SwipeRefreshLayout swipeToRefresh;
    private Paint p = new Paint();
    private final ArrayList<HashMap<String,String>> listHashPengembalian = new ArrayList<HashMap<String,String>>();
    private boolean isUndoClicked = false;
    List<Pengembalian> filteredPengembalian = new ArrayList<>();
    private boolean isMultiSelect = false;
    ArrayList<Pengembalian> multiSelectPengembalianList = new ArrayList<>();
    ActionMode mActionMode;
    Menu context_menu;
    AlertDialogHelper alertDialogHelper;
    private ListView listDtlPengembalian;
    ViewDtlPengembalianAdapter viewDtlPengembalianAdapter;
    private Calendar calendar;
    private ArrayList<Calendar> listHashCalendar = new ArrayList<Calendar>();
    public static final String DATE_FORMAT_SAVE = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT_NEW = "dd/MM/yyyy";
    SimpleDateFormat sdfSave, sdfOutput;

    public PengembalianFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pengembalian, container, false);

        fragmentPengembalian = (View)view.findViewById(R.id.fragmentPengembalian);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        swipeToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);
        listDtlPengembalian = (ListView)view.findViewById(R.id.listDtlPengembalian);
        dataList = new ArrayList<HashMap<String, String>>();
        alertDialogHelper = new AlertDialogHelper(this, getContext());
        sdfSave = new SimpleDateFormat(DATE_FORMAT_SAVE);
        sdfOutput = new SimpleDateFormat(DATE_FORMAT_NEW);

        swipeToRefresh.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), android.R.color.holo_blue_bright),
                ContextCompat.getColor(getActivity(), android.R.color.holo_green_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_orange_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));

        pAdapter = new PengembalianAdapter(getActivity(), pengembalianList, multiSelectPengembalianList);
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
                    if (pengembalianList.get(position)
                            .getFlowKembali().equals(getContext().getString(R.string.flowNone))) {
                        Toast.makeText(getActivity(), "Belum ada data Pengembalian", Toast.LENGTH_LONG).show();
                        pAdapter.notifyDataSetChanged();
                        return;
                    }

                    Pengembalian pengembalian = pengembalianList.get(position);
                    viewP_Kembali(pengembalian);
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View rootView = inflater.inflate(R.layout.view_kembali, null);
                    listDtlPengembalian = (ListView)rootView.findViewById(R.id.listDtlPengembalian);
                    builder.setView(rootView);
                    builder.setCancelable(true);

                    builder.setTitle("Detail " + pengembalian.getKd_Pinjam());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setLayout(1000, 800);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiSelectPengembalianList = new ArrayList<Pengembalian>();
                    isMultiSelect = true;

                    if (mActionMode == null) {
                        mActionMode = getActivity().startActionMode(mActionModeCallback);
                    }
                }

                multi_select(position);
            }
        }));

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getKembali();
            }
        });

        initSwipe();
        getKembali();

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
                    if (pengembalianList.get(viewHolder.getAdapterPosition())
                            .getFlowKembali().equals(getContext().getString(R.string.flowNone))) {
                        Toast.makeText(getActivity(), "Belum ada data Pengembalian", Toast.LENGTH_LONG).show();
                        pAdapter.notifyDataSetChanged();
                        return;
                    }

                    final Pengembalian deletedItem = pengembalianList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();
                    final Pengembalian fixDeletedItem = new Pengembalian(deletedItem);
                    fixDeletedItem.setFlowKembali(getActivity().getString(R.string.flowNone));
                    pAdapter.setItem(fixDeletedItem, position);

                    Snackbar snackbar = Snackbar.make(fragmentPengembalian, Html.fromHtml("<font color=\"#ffffff\">DELETED</font>"), Snackbar.LENGTH_LONG);
                    snackbar.setAction("UNDO", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            isUndoClicked = true;
                            pAdapter.setItem(deletedItem, deletedIndex);
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                    snackbar.addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            if (!isUndoClicked) {
                                viewP_Kembali(deletedItem);
                                getTimerAddQtyPinjam();
                                hapusKembali(deletedItem);
                                hapusTgl_Kembali(deletedItem);
                                Toast.makeText(getActivity(), "Pengembalian berhasil dihapus", Toast.LENGTH_LONG).show();
                            }
                            isUndoClicked = false;
                        }
                    });
                } else {
                    Pengembalian pengembalian = pengembalianList.get(position);
                    showAddPengembalian(pengembalian, position);
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

    public void viewP_Kembali(final Pengembalian pengembalian) {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        String url = Config.URL_VIEW_KEMBALI + pengembalian.getKd_Pinjam() + "'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showP_Kembali(response);
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

    public void showP_Kembali(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            listHashPengembalian.clear();
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

            viewDtlPengembalianAdapter = new ViewDtlPengembalianAdapter(listHashPengembalian);
            if (listDtlPengembalian == null) return;
            listDtlPengembalian.setAdapter(viewDtlPengembalianAdapter);
        } catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    public void getKembali() {
        new Handler().postDelayed(new Runnable(){
            public void run() {
                viewKembali();
            }
        }, 3000);
    }

    public void viewKembali() {
//        progressBar.setVisibility(View.VISIBLE);
//        setProgressBarIndeterminateVisibility(true);

        String url = Config.URL_VIEW_KEMBALI_HOME;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
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
                        Toast.makeText(getActivity(), volleyError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
        RequestQueue rq = Volley.newRequestQueue(getActivity());
        rq.add(stringRequest);
    }

    public void showKembali(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            pengembalianList.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                Pengembalian pengembalian = new Pengembalian();
                pengembalian.setKd_Pinjam(jo.getString(Config.TAG_KD_PINJAM));
                pengembalian.setTgl_Pinjam(jo.getString(Config.TAG_TGL_PINJAM));
                pengembalian.setFlowKembali(jo.getString(Config.TAG_FLOW_KEMBALI));
                pengembalianList.add(pengembalian);
            }

            filteredPengembalian.clear();
            filteredPengembalian.addAll(pengembalianList);

            pAdapter.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
        } catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    private void hapusKembali (final Pengembalian pengembalian) {
        String url = Config.URL_DELETE_KEMBALI + pengembalian.getKd_Pinjam() + "'";
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

    private void hapusTgl_Kembali (final Pengembalian pengembalian) {
        String url = Config.URL_DELETE_TGL_KEMBALI + pengembalian.getTgl_Pinjam() + "'";
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

    private void getTimerAddQtyPinjam() {
        /*alertDialog = new SpotsDialog(this);
        alertDialog.show();*/
        new Handler().postDelayed(new Runnable(){
            public void run() {
//                alertDialog.dismiss();
                addQtyPinjam();
            }
        }, 1000);
    }

    private void addQtyPinjam () {
        for (final Map<String, String> entry : listHashPengembalian) {
            String url = Config.URL_ADD_QTY_PINJAM;
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
                    String kdInventaris = entry.get("KdInventaris");
                    String noLaptop = kdInventaris.substring(0, kdInventaris.length() - 5);

                    params.put(Config.KEY_NO_LAPTOP, noLaptop);
                    params.put(Config.KEY_QTY_PINJAM, "1");
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    private void showAddPengembalian(Pengembalian pengembalian, int position) {
        Intent accountsIntent = new Intent(getActivity(), AddPengembalian.class);
        if (pengembalian.getKd_Pinjam() != null) {
            accountsIntent.putExtra("KD_PINJAM", pengembalian.getKd_Pinjam().trim());
            accountsIntent.putExtra("TGL_PINJAM", pengembalian.getTgl_Pinjam().trim());
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
            final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
            searchView.setQueryHint(getString(R.string.search_pengembalian));
            searchView.setIconified(true);
            // To Overcome
            // When you perform filter and then Switch between tabs in TabLayout
            // In such scenario the recyclerview won’t return the original set of data
            MenuItemCompat.setOnActionExpandListener(item,
                    new MenuItemCompat.OnActionExpandListener() {
                        @Override
                        public boolean onMenuItemActionCollapse(MenuItem item) {
                            pAdapter.setFilter(pengembalianList);
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
                    filteredPengembalian = filterPengembalian(pengembalianList, newText);

                    pAdapter.setFilter(filteredPengembalian);
                    return true;
                }
            });
            super.onCreateOptionsMenu(menu, inflater);
//        }
    }

    private List<Pengembalian> filterPengembalian(List<Pengembalian> listKembali, String query) {
        query = query.toLowerCase();
        final List<Pengembalian> filteredPengembalian = new ArrayList<>();

        for (Pengembalian kembali : listKembali) {
            final String kdPinjam = kembali.getKd_Pinjam().toLowerCase();
            String tglPinjam = kembali.getTgl_Pinjam().toLowerCase();
            String tglPinjamNoTime = tglPinjam.substring(0, tglPinjam.length() - 9);
            final String flowKembali = kembali.getFlowKembali().toLowerCase();
            if (kdPinjam.contains(query) ||
                    tglPinjamNoTime.contains(query) ||
                        flowKembali.contains(query)) {
                filteredPengembalian.add(kembali);
            }
        }

        return filteredPengembalian;
    }

    public void multi_selectAll() {
        if (mActionMode != null) {
            if (multiSelectPengembalianList.size() < pengembalianList.size()) {
                multiSelectPengembalianList.clear();
                multiSelectPengembalianList.addAll(pengembalianList);
            } else {
                multiSelectPengembalianList.removeAll(pengembalianList);
            }

            if (multiSelectPengembalianList.size() > 0)
                mActionMode.setTitle(multiSelectPengembalianList.size() + " Selected");
            else
                mActionMode.setTitle("0 Selected");

            refreshAdapter();
        }
    }

    public void refreshAdapter()
    {
        pAdapter.selected_pengembalianList = multiSelectPengembalianList;
        pAdapter.pengembalianList = pengembalianList;
        pAdapter.notifyDataSetChanged();
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiSelectPengembalianList.contains(pengembalianList.get(position)))
                multiSelectPengembalianList.remove(pengembalianList.get(position));
            else
                multiSelectPengembalianList.add(pengembalianList.get(position));

            if (multiSelectPengembalianList.size() > 0)
                mActionMode.setTitle(multiSelectPengembalianList.size() + " Selected");
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
                    alertDialogHelper.showAlertDialog("","Delete Pengembalian","DELETE","CANCEL",1,false);
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
            multiSelectPengembalianList = new ArrayList<Pengembalian>();
            refreshAdapter();
        }
    };

    @Override
    public void onPositiveClick(int from) {
        if(from==1)
        {
            if(multiSelectPengembalianList.size() > 0)
            {
                final List<Pengembalian> deletedItem = new ArrayList<>();
                final List<Integer> deletedIndex = new ArrayList<>();

                final int totalMultiSelect = multiSelectPengembalianList.size();

                // WHY SEPERATE TWO LOOPS?
                // TO EXECUTE DELETE ONLY IF ALL SELECTION HAVEN'T HAD PEMINJAMAN
                for(int i = 0; i < multiSelectPengembalianList.size(); i++) {
                    if (multiSelectPengembalianList.get(i)
                            .getFlowKembali().equals(getContext().getString(R.string.flowNone))) {
                        Toast.makeText(getActivity(), multiSelectPengembalianList.get(i).getKd_Pinjam() +
                                " belum ada data Pengembalian", Toast.LENGTH_LONG).show();
                        return;
                    }
                    deletedItem.add(multiSelectPengembalianList.get(i));
                    deletedIndex.add(pengembalianList.indexOf(multiSelectPengembalianList.get(i)));
                }

                for(int i = 0; i < multiSelectPengembalianList.size(); i++) {
                    final Pengembalian fixDeletedItem = new Pengembalian(deletedItem.get(i));
                    fixDeletedItem.setFlowKembali(getActivity().getString(R.string.flowNone));
                    pAdapter.setItem(fixDeletedItem, deletedIndex.get(i));
                }

                Snackbar snackbar = Snackbar.make(fragmentPengembalian, Html.fromHtml("<font color=\"#ffffff\">DELETED</font>"), Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isUndoClicked = true;
                        for (int i = 0; i < deletedIndex.size(); i++) {
                            pAdapter.setItem(deletedItem.get(i), deletedIndex.get(i));
                        }
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (!isUndoClicked) {
                            for (Pengembalian pengembalian : deletedItem) {
                                viewP_Kembali(pengembalian);
                                getTimerAddQtyPinjam();
                                hapusKembali(pengembalian);
                                hapusTgl_Kembali(pengembalian);
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
            pengembalianList.add(peminjaman);
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
