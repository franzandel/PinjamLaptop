package BottomNavigation;

import android.app.AlertDialog;
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
import android.widget.ArrayAdapter;
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

import LoginRegister.Laptop;
import Recycler.LaptopAdapter;
import Recycler.MyDividerItemDecoration;
import Recycler.RecyclerTouchListener;
import dmax.dialog.SpotsDialog;

/**
 * Created by ryuze on 6/26/2018.
 */

public class LaptopFragment extends Fragment implements AlertDialogHelper.AlertDialogListener {

    private View fragmentLaptop;
    public static List<Laptop> laptopList = new ArrayList<>();
    public RecyclerView recyclerView;
    public static LaptopAdapter lAdapter;
    private FloatingActionButton faAdd;
    private Laptop laptop;
    ArrayList<HashMap<String, String>> dataList;
    SwipeRefreshLayout swipeToRefresh;
    private Paint p = new Paint();
    private boolean isUndoClicked = false;
    List<Laptop> filteredLaptop = new ArrayList<>();
    private boolean isMultiSelect = false;
    ArrayList<Laptop> multiSelectLaptopList = new ArrayList<>();
    ActionMode mActionMode;
    Menu context_menu;
    AlertDialogHelper alertDialogHelper;
    private AlertDialog alertDialog;
    private Spinner spKdMerek, spKdProcessor, spKdVGA;
    private TextInputEditText tietModel, tietHDD;
    private ArrayList<String> eachLaptopList= new ArrayList<String>();
    private ArrayList<String> processorList = new ArrayList<String>();
    private ArrayList<String> vgaList = new ArrayList<String>();
    ArrayAdapter<String> adapterSpKdMerek, adapterSpKdProcessor, adapterSpKdVGA;
    final ArrayList<HashMap<String,String>> listHashPeminjaman = new ArrayList<HashMap<String,String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_laptop, container, false);

        fragmentLaptop = (View)view.findViewById(R.id.fragmentLaptop);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        faAdd = (FloatingActionButton)view.findViewById(R.id.fabAdd);
        swipeToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);
        dataList = new ArrayList<HashMap<String, String>>();
        laptop = new Laptop();
        alertDialogHelper = new AlertDialogHelper(this, getContext());

        swipeToRefresh.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), android.R.color.holo_blue_bright),
                ContextCompat.getColor(getActivity(), android.R.color.holo_green_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_orange_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));

        lAdapter = new LaptopAdapter(getActivity(), laptopList, multiSelectLaptopList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new MyDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL, 16));
        recyclerView.setAdapter(lAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (isMultiSelect)
                    multi_select(position);
                else {
                    Laptop laptop = laptopList.get(position);
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View rootView = inflater.inflate(R.layout.view_laptop, null);
                    TextInputEditText tietNoLaptop = (TextInputEditText)rootView.findViewById(R.id.tietNoLaptop);
                    TextInputEditText tietSatuan = (TextInputEditText)rootView.findViewById(R.id.tietSatuan);
                    TextInputEditText tietQtyTotal = (TextInputEditText)rootView.findViewById(R.id.tietQtyTotal);
                    spKdProcessor = (Spinner)rootView.findViewById(R.id.spKdProcessor);
                    TextInputEditText tietRAM = (TextInputEditText)rootView.findViewById(R.id.tietRAM);

                    spKdMerek = (Spinner)rootView.findViewById(R.id.spKdMerek);
                    spKdVGA = (Spinner)rootView.findViewById(R.id.spKdVGA);
                    tietModel = (TextInputEditText)rootView.findViewById(R.id.tietModel);
                    tietHDD = (TextInputEditText)rootView.findViewById(R.id.tietHDD);

                    getSpKdMerek();
                    getSpKdProcessor();
                    getSpKdVGA();
                    getTimerEachLaptop(laptop);

                    tietNoLaptop.setText(laptop.getNo_Laptop());
                    tietSatuan.setText(laptop.getSatuan());
                    tietQtyTotal.setText(String.valueOf(laptop.getQty_total()));
                    tietRAM.setText(laptop.getRam());

                    tietNoLaptop.setEnabled(false);
                    tietSatuan.setEnabled(false);
                    tietQtyTotal.setEnabled(false);
                    spKdProcessor.setEnabled(false);
                    tietRAM.setEnabled(false);

                    spKdMerek.setEnabled(false);
                    spKdVGA.setEnabled(false);
                    tietModel.setEnabled(false);
                    tietHDD.setEnabled(false);

                    builder.setView(rootView);
                    builder.setCancelable(true);

                    builder.setTitle("Detail " + laptop.getNo_Laptop());
                    android.support.v7.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setLayout(1000, 1600);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiSelectLaptopList = new ArrayList<Laptop>();
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
                showAddLaptop(laptop, 0);
            }
        });

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTimerLaptop();
            }
        });

        initSwipe();
        getPeminjaman();
        getTimerLaptop();

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
                    final Laptop deletedItem = laptopList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    String url = Config.URL_VALIDASI_LAPTOP + deletedItem.getNo_Laptop() + "'";
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.length() == 13) {
                                        lAdapter.removeItem(position);

                                        Snackbar snackbar = Snackbar.make(fragmentLaptop, Html.fromHtml("<font color=\"#ffffff\">DELETED</font>"), Snackbar.LENGTH_LONG);
                                        snackbar.setAction("UNDO", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                isUndoClicked = true;
                                                lAdapter.restoreItem(deletedItem, deletedIndex);
                                            }
                                        });
                                        snackbar.setActionTextColor(Color.YELLOW);
                                        snackbar.show();
                                        snackbar.addCallback(new Snackbar.Callback() {
                                            @Override
                                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                                if (!isUndoClicked) {
                                                    hapusLaptop(deletedItem);
                                                    lAdapter.notifyDataSetChanged();
                                                    Toast.makeText(getActivity(), "Laptop berhasil dihapus", Toast.LENGTH_LONG).show();
                                                }
                                                isUndoClicked = false;
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), deletedItem.getNo_Laptop()
                                                + " sudah ada data Peminjaman", Toast.LENGTH_LONG).show();
                                        lAdapter.notifyDataSetChanged();
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
                    Laptop laptop = laptopList.get(position);
                    showAddLaptop(laptop, position);
                    lAdapter.notifyDataSetChanged();
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

    private void showAddLaptop(Laptop laptop, int position) {
        Intent accountsIntent = new Intent(getActivity(), AddLaptop.class);
        if (laptop.getNo_Laptop() != null) {
            accountsIntent.putExtra("NO_LAPTOP", laptop.getNo_Laptop().trim());
            accountsIntent.putExtra("SATUAN", laptop.getSatuan());
            accountsIntent.putExtra("QTY_TOTAL", laptop.getQty_total());
            accountsIntent.putExtra("KD_PROCESSOR", laptop.getKd_processor().trim());
            accountsIntent.putExtra("RAM", laptop.getRam().trim());
            accountsIntent.putExtra("POSITION", position);
        }
        startActivity(accountsIntent);
    }

    protected void getTimerLaptop() {
        alertDialog = new SpotsDialog(getActivity());
        alertDialog.show();
        new Handler().postDelayed(new Runnable(){
            public void run() {
                viewDataLaptop();
            }
        }, 3000);
    }

    public void viewDataLaptop() {
        String url = Config.URL_VIEW_ALL_LAPTOP;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showDataLaptop(response);
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

    public void showDataLaptop(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            laptopList.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                Laptop laptop = new Laptop();
                laptop.setNo_Laptop(jo.getString(Config.TAG_NO_LAPTOP));
                laptop.setQty_total(Integer.parseInt(jo.getString(Config.TAG_QTY_TOTAL)));
                laptop.setSatuan(jo.getString(Config.TAG_SATUAN));
                laptop.setKd_merek(jo.getString(Config.TAG_KD_MEREK));
                laptop.setKd_processor(jo.getString(Config.TAG_KD_PROCESSOR));
                laptop.setKd_vga(jo.getString(Config.TAG_KD_VGA));
                laptop.setModel(jo.getString(Config.TAG_MODEL));
                laptop.setRam(jo.getString(Config.TAG_RAM));
                laptop.setHdd(jo.getString(Config.TAG_HDD));
                laptopList.add(laptop);
            }

            filteredLaptop.clear();
            filteredLaptop.addAll(laptopList);

            lAdapter.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
            alertDialog.dismiss();
        } catch (JSONException jse){
            jse.printStackTrace();
        }

    }

    private void hapusLaptop (final Laptop laptop) {
        String url = Config.URL_DELETE_M_LAPTOP + laptop.getNo_Laptop() + "'";
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
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint(getString(R.string.search_laptop));
        searchView.setIconified(true);
        // To Overcome
        // When you perform filter and then Switch between tabs in TabLayout
        // In such scenario the recyclerview won’t return the original set of data
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        lAdapter.setFilter(laptopList);
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
                filteredLaptop = filterLaptop(laptopList, newText);

                lAdapter.setFilter(filteredLaptop);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private List<Laptop> filterLaptop(List<Laptop> listLaptop, String query) {
        query = query.toLowerCase();
        final List<Laptop> filteredLaptop = new ArrayList<>();

        for (Laptop laptop : listLaptop) {
            final String noLaptop = laptop.getNo_Laptop().toLowerCase();
//            final int qtyTotal = laptop.getQty_total();
            final String satuan = laptop.getSatuan().toLowerCase();
            if (noLaptop.contains(query) ||
//                    qtyTotal.equals(query) ||
                        satuan.contains(query)) {
                filteredLaptop.add(laptop);
            }
        }

        return filteredLaptop;
    }

    private boolean getPeminjaman() {
        String url = Config.URL_VIEW_ALL_P_PINJAM;
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
                temp.put("NoLaptop", jo.getString(Config.TAG_NO_LAPTOP));
                temp.put("QtyPinjam", jo.getString(Config.TAG_QTY_PINJAM));
                temp.put("Keperluan", jo.getString(Config.TAG_KEPERLUAN));
                listHashPeminjaman.add(temp);
            }
        } catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    public boolean hasPeminjaman(Laptop deletedItem) {
        for (final Map<String, String> data : listHashPeminjaman) {
            if (deletedItem.getNo_Laptop().equals(data.get("NoLaptop"))) {
                return true;
            }
        }
        return false;
    }

    public void multi_selectAll() {
        if (mActionMode != null) {
            if (multiSelectLaptopList.size() < laptopList.size()) {
                multiSelectLaptopList.clear();
                multiSelectLaptopList.addAll(laptopList);
            } else {
                multiSelectLaptopList.removeAll(laptopList);
            }

            if (multiSelectLaptopList.size() > 0)
                mActionMode.setTitle(multiSelectLaptopList.size() + " Selected");
            else
                mActionMode.setTitle("0 Selected");

            refreshAdapter();
        }
    }

    public void refreshAdapter()
    {
        lAdapter.selected_laptopList = multiSelectLaptopList;
        lAdapter.laptopList = laptopList;
        lAdapter.notifyDataSetChanged();
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiSelectLaptopList.contains(laptopList.get(position)))
                multiSelectLaptopList.remove(laptopList.get(position));
            else
                multiSelectLaptopList.add(laptopList.get(position));

            if (multiSelectLaptopList.size() > 0)
                mActionMode.setTitle(multiSelectLaptopList.size() + " Selected");
            else
                mActionMode.setTitle("0 Selected");

            refreshAdapter();
        }
    }

    private void getTimerEachLaptop(final Laptop laptop) {
        new Handler().postDelayed(new Runnable(){
            public void run() {
                viewEachLaptop(laptop);
            }
        }, 2500);
    }

    public void viewEachLaptop(Laptop laptop) {
        String url = Config.URL_VIEW_EACH_P_LAPTOP  + laptop.getNo_Laptop() + "'";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        showEachLaptop(response);
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

    public void showEachLaptop(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);
            JSONObject jo = data.getJSONObject(0);

            spKdMerek.setSelection(getSpinnerIndex(spKdMerek, jo.getString(Config.TAG_KD_MEREK)));
            spKdProcessor.setSelection(getSpinnerIndex(spKdProcessor, jo.getString(Config.TAG_KD_PROCESSOR)));
            spKdVGA.setSelection(getSpinnerIndex(spKdVGA, jo.getString(Config.TAG_KD_VGA)));
            tietModel.setText(jo.getString(Config.TAG_MODEL));
            tietHDD.setText(jo.getString(Config.TAG_HDD));
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

    private void getSpKdMerek() {
        alertDialog = new SpotsDialog(getActivity());
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
    }

    private void getKdMerek(JSONArray j) {
        for (int i = 0; i < j.length(); i++) {
            try {
                JSONObject json = j.getJSONObject(i);
                eachLaptopList.add(json.getString(Config.TAG_KD_MEREK));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapterSpKdMerek =
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, eachLaptopList);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, processorList);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
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
                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, vgaList);
        spKdVGA.setAdapter(adapterSpKdVGA);
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
                    alertDialogHelper.showAlertDialog("","Delete Laptop?","DELETE","CANCEL",1,false);
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
            multiSelectLaptopList = new ArrayList<Laptop>();
            refreshAdapter();
        }
    };

    @Override
    public void onPositiveClick(int from) {
        if(from==1)
        {
            if(multiSelectLaptopList.size() > 0)
            {
                final List<Laptop> deletedItem = new ArrayList<>();
                final List<Integer> deletedIndex = new ArrayList<>();

                final int totalMultiSelect = multiSelectLaptopList.size();

                // WHY SEPERATE TWO LOOPS?
                // TO EXECUTE DELETE ONLY IF ALL SELECTION HAVEN'T HAD PEMINJAMAN
                for(int i = 0; i < multiSelectLaptopList.size(); i++) {
                    if (hasPeminjaman(multiSelectLaptopList.get(i))) {
                        Toast.makeText(getActivity(), multiSelectLaptopList.get(i).getNo_Laptop() +
                                " sudah ada data Peminjaman", Toast.LENGTH_LONG).show();
                        return;
                    }
                    deletedItem.add(multiSelectLaptopList.get(i));
                    deletedIndex.add(laptopList.indexOf(multiSelectLaptopList.get(i)));
                }

                for(int i = 0; i < multiSelectLaptopList.size(); i++) {
                    laptopList.remove(multiSelectLaptopList.get(i));
                }

                Snackbar snackbar = Snackbar.make(fragmentLaptop, Html.fromHtml("<font color=\"#ffffff\">DELETED</font>"), Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        isUndoClicked = true;
                        for (int i = 0; i < deletedIndex.size(); i++) {
                            lAdapter.restoreItem(deletedItem.get(i), deletedIndex.get(i));
                        }
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
                snackbar.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (!isUndoClicked) {
                            for (Laptop laptop : deletedItem) {
                                hapusLaptop(laptop);
                            }
                            Toast.makeText(getActivity(), totalMultiSelect +
                                    " item(s) deleted", Toast.LENGTH_LONG).show();
                        }
                        isUndoClicked = false;
                    }
                });

                lAdapter.notifyDataSetChanged();

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
            lAdapter.notifyDataSetChanged();*/

        }
    }


    @Override
    public void onNegativeClick(int from) {

    }

    @Override
    public void onNeutralClick(int from) {

    }
}
