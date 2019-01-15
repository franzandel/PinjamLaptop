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

import LoginRegister.Processor;
import Recycler.ProcessorAdapter;
import Recycler.MyDividerItemDecoration;
import Recycler.RecyclerTouchListener;

/**
 * Created by ryuze on 6/25/2018.
 */

public class ProcessorFragment extends Fragment implements AlertDialogHelper.AlertDialogListener {

    private View fragmentProcessor;
    public static List<Processor> processorList = new ArrayList<>();
    public RecyclerView recyclerView;
    public static ProcessorAdapter pAdapter;
    private FloatingActionButton faAdd;
    private Processor processor;
    ArrayList<HashMap<String, String>> dataList;
    SwipeRefreshLayout swipeToRefresh;
    private Paint p = new Paint();
    private boolean isUndoClicked = false;
    List<Processor> filteredProcessor = new ArrayList<>();
    private boolean isMultiSelect = false;
    ArrayList<Processor> multiSelectProcessorList = new ArrayList<>();
    ActionMode mActionMode;
    Menu context_menu;
    AlertDialogHelper alertDialogHelper;
    final ArrayList<HashMap<String,String>> listHashLaptop = new ArrayList<HashMap<String,String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_processor, container, false);

        fragmentProcessor = (View)view.findViewById(R.id.fragmentProcessor);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        faAdd = (FloatingActionButton)view.findViewById(R.id.fabAdd);
        swipeToRefresh = (SwipeRefreshLayout)view.findViewById(R.id.swipeToRefresh);
        dataList = new ArrayList<HashMap<String, String>>();
        processor = new Processor();
        alertDialogHelper = new AlertDialogHelper(this, getContext());

        swipeToRefresh.setColorSchemeColors(
                ContextCompat.getColor(getActivity(), android.R.color.holo_blue_bright),
                ContextCompat.getColor(getActivity(), android.R.color.holo_green_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_orange_light),
                ContextCompat.getColor(getActivity(), android.R.color.holo_red_light));

        pAdapter = new ProcessorAdapter(getActivity(), processorList, multiSelectProcessorList);
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
                    Processor processor = processorList.get(position);
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getContext());

                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View rootView = inflater.inflate(R.layout.view_processor, null);
                    TextInputEditText tietKdProcessor = (TextInputEditText)rootView.findViewById(R.id.tietKdProcessor);
                    TextInputEditText tietNmProcessor = (TextInputEditText)rootView.findViewById(R.id.tietNmProcessor);

                    tietKdProcessor.setText(processor.getKodeProcessor());
                    tietNmProcessor.setText(processor.getNamaProcessor());

                    tietKdProcessor.setEnabled(false);
                    tietNmProcessor.setEnabled(false);

                    builder.setView(rootView);
                    builder.setCancelable(true);

                    builder.setTitle("Detail " + processor.getKodeProcessor());
                    android.support.v7.app.AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    alertDialog.getWindow().setLayout(1000, 800);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
                if (!isMultiSelect) {
                    multiSelectProcessorList = new ArrayList<Processor>();
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
                showAddProcessor(processor, 0);
            }
        });

        swipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getTimerProcessor();
            }
        });

        initSwipe();
        getLaptop();
        getTimerProcessor();

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
                    final Processor deletedItem = processorList.get(viewHolder.getAdapterPosition());
                    final int deletedIndex = viewHolder.getAdapterPosition();

                    String url = Config.URL_VALIDASI_PROCESSOR + deletedItem.getKodeProcessor() + "'";
                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    if (response.length() == 13) {
                                        pAdapter.removeItem(position);

                                        Snackbar snackbar = Snackbar.make(fragmentProcessor, Html.fromHtml("<font color=\"#ffffff\">DELETED</font>"), Snackbar.LENGTH_LONG);
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
                                                    hapusProcessor(deletedItem);
                                                    Toast.makeText(getActivity(), "Processor berhasil dihapus", Toast.LENGTH_LONG).show();
                                                }
                                                isUndoClicked = false;
                                            }
                                        });
                                    } else {
                                        Toast.makeText(getActivity(), deletedItem.getKodeProcessor()
                                                + " sudah ada data Laptop", Toast.LENGTH_LONG).show();
                                        pAdapter.notifyDataSetChanged();
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
                    Processor processor = processorList.get(position);
                    showAddProcessor(processor, position);
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

    private void showAddProcessor(Processor processor, int position) {
        Intent accountsIntent = new Intent(getActivity(), AddProcessor.class);
        if (processor.getKodeProcessor() != null) {
            accountsIntent.putExtra("KD_PROCESSOR", processor.getKodeProcessor().trim());
            accountsIntent.putExtra("NM_PROCESSOR", processor.getNamaProcessor().trim());
            accountsIntent.putExtra("POSITION", position);
        }
        startActivity(accountsIntent);
    }

    protected void getTimerProcessor() {
        new Handler().postDelayed(new Runnable(){
            public void run() {
                viewDataProcessor();
            }
        }, 3000);
    }

    public void viewDataProcessor() {
        String url = Config.URL_VIEW_ALL_PROCESSOR;
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showDataProcessor(response);
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

    public void showDataProcessor(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            processorList.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                Processor processor = new Processor();
                processor.setKodeProcessor(jo.getString(Config.TAG_KD_PROCESSOR));
                processor.setNamaProcessor(jo.getString(Config.TAG_NM_PROCESSOR));
                processorList.add(processor);
            }

            filteredProcessor.clear();
            filteredProcessor.addAll(processorList);

            pAdapter.notifyDataSetChanged();
            swipeToRefresh.setRefreshing(false);
        } catch (JSONException jse){
            jse.printStackTrace();
        }

    }

    private void hapusProcessor (final Processor processor) {
        String url = Config.URL_DELETE_PROCESSOR + processor.getKodeProcessor() + "'";
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
        searchView.setQueryHint(getString(R.string.search_processor));
        searchView.setIconified(true);
        // To Overcome
        // When you perform filter and then Switch between tabs in TabLayout
        // In such scenario the recyclerview won’t return the original set of data
        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        pAdapter.setFilter(processorList);
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
                filteredProcessor = filterProcessor(processorList, newText);

                pAdapter.setFilter(filteredProcessor);
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    private List<Processor> filterProcessor(List<Processor> listProcessor, String query) {
        query = query.toLowerCase();
        final List<Processor> filteredProcessor = new ArrayList<>();

        for (Processor processor : listProcessor) {
            final String kdProcessor = processor.getKodeProcessor().toLowerCase();
            final String nmProcessor = processor.getNamaProcessor().toLowerCase();
            if (kdProcessor.contains(query) ||
                    nmProcessor.contains(query)) {
                filteredProcessor.add(processor);
            }
        }

        return filteredProcessor;
    }

    private boolean getLaptop() {
        String url = Config.URL_VIEW_ALL_LAPTOP;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        getDataLaptop(response);
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

    public void getDataLaptop(String response) {
        JSONArray data = null;
        JSONObject jso = null;

        try {
            jso = new JSONObject(response);
            data = jso.getJSONArray(Config.TAG_JSON_ARRAY);

            listHashLaptop.clear();
            for (int i = 0; i < data.length(); i++) {
                JSONObject jo = data.getJSONObject(i);

                HashMap<String,String> temp = new HashMap<String,String>();
                temp.put("NoLaptop", jo.getString(Config.TAG_NO_LAPTOP));
                temp.put("KdMerek", jo.getString(Config.TAG_KD_MEREK));
                temp.put("KdProcessor", jo.getString(Config.TAG_KD_PROCESSOR));
                temp.put("KdVGA", jo.getString(Config.TAG_KD_VGA));
                temp.put("Model", jo.getString(Config.TAG_MODEL));
                temp.put("RAM", jo.getString(Config.TAG_RAM));
                temp.put("HDD", jo.getString(Config.TAG_HDD));
                listHashLaptop.add(temp);
            }
        } catch (JSONException jse){
            jse.printStackTrace();
        }
    }

    public boolean hasLaptop(Processor deletedItem) {
        for (final Map<String, String> data : listHashLaptop) {
            if (deletedItem.getKodeProcessor().equals(data.get("KdProcessor"))) {
                return true;
            }
        }
        return false;
    }

    public void multi_selectAll() {
        if (mActionMode != null) {
            if (multiSelectProcessorList.size() < processorList.size()) {
                multiSelectProcessorList.clear();
                multiSelectProcessorList.addAll(processorList);
            } else {
                multiSelectProcessorList.removeAll(processorList);
            }

            if (multiSelectProcessorList.size() > 0)
                mActionMode.setTitle(multiSelectProcessorList.size() + " Selected");
            else
                mActionMode.setTitle("0 Selected");

            refreshAdapter();
        }
    }

    public void refreshAdapter()
    {
        pAdapter.selected_processorList = multiSelectProcessorList;
        pAdapter.processorList = processorList;
        pAdapter.notifyDataSetChanged();
    }

    public void multi_select(int position) {
        if (mActionMode != null) {
            if (multiSelectProcessorList.contains(processorList.get(position)))
                multiSelectProcessorList.remove(processorList.get(position));
            else
                multiSelectProcessorList.add(processorList.get(position));

            if (multiSelectProcessorList.size() > 0)
                mActionMode.setTitle(multiSelectProcessorList.size() + " Selected");
            else
                mActionMode.setTitle("0 Selected");

            refreshAdapter();
        }
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
                    alertDialogHelper.showAlertDialog("","Delete Processor?","DELETE","CANCEL",1,false);
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
            multiSelectProcessorList = new ArrayList<Processor>();
            refreshAdapter();
        }
    };

    @Override
    public void onPositiveClick(int from) {
        if(from==1)
        {
            if(multiSelectProcessorList.size() > 0)
            {
                final List<Processor> deletedItem = new ArrayList<>();
                final List<Integer> deletedIndex = new ArrayList<>();

                final int totalMultiSelect = multiSelectProcessorList.size();

                // WHY SEPERATE TWO LOOPS?
                // TO EXECUTE DELETE ONLY IF ALL SELECTION HAVEN'T HAD PEMINJAMAN
                for(int i = 0; i < multiSelectProcessorList.size(); i++) {
                    if (hasLaptop(multiSelectProcessorList.get(i))) {
                        Toast.makeText(getActivity(), multiSelectProcessorList.get(i).getKodeProcessor() +
                                " sudah ada data Laptop", Toast.LENGTH_LONG).show();
                        return;
                    }
                    deletedItem.add(multiSelectProcessorList.get(i));
                    deletedIndex.add(processorList.indexOf(multiSelectProcessorList.get(i)));
                }

                for(int i = 0; i < multiSelectProcessorList.size(); i++) {
                    processorList.remove(multiSelectProcessorList.get(i));
                }

                Snackbar snackbar = Snackbar.make(fragmentProcessor, Html.fromHtml("<font color=\"#ffffff\">DELETED</font>"), Snackbar.LENGTH_LONG);
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
                            for (Processor processor : deletedItem) {
                                hapusProcessor(processor);
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
