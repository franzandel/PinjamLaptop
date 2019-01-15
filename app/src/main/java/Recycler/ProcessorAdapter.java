package Recycler;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ok.pinjamlaptop.R;

import java.util.ArrayList;
import java.util.List;

import LoginRegister.Processor;

/**
 * Created by ryuze on 6/25/2018.
 */

public class ProcessorAdapter extends RecyclerView.Adapter<ProcessorAdapter.MyViewHolder> {

    Context context;
    public List<Processor> processorList;
    public ArrayList<Processor> selected_processorList = new ArrayList<>();

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView kdProcessor, nmProcessor;
        public RelativeLayout rl_listItemProcessor;

        public MyViewHolder(View view) {
            super(view);
            kdProcessor = (TextView) view.findViewById(R.id.kdProcessor);
            nmProcessor = (TextView) view.findViewById(R.id.nmProcessor);
            rl_listItemProcessor = (RelativeLayout) view.findViewById(R.id.rl_listItemProcessor);
        }
    }

    public ProcessorAdapter(Context context, List<Processor> processorList, ArrayList<Processor> selectedList) {
        this.context = context;
        this.processorList = processorList;
        this.selected_processorList = selectedList;
    }

    @Override
    public ProcessorAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.processor_list_row, parent, false);

        return new ProcessorAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Processor processor = processorList.get(position);

        holder.kdProcessor.setText(processor.getKodeProcessor());
        holder.nmProcessor.setText(processor.getNamaProcessor());

        if(selected_processorList.contains(processorList.get(position)))
            holder.rl_listItemProcessor.setBackgroundColor(ContextCompat.getColor(context, R.color.list_item_selected_state));
        else {
            TypedValue outValue = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            holder.rl_listItemProcessor.setBackgroundResource(outValue.resourceId);
        }

    }

    @Override
    public int getItemCount() {
        return processorList.size();
    }

    public void removeItem(int position) {
        processorList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, processorList.size());
    }

    public void restoreItem(Processor item, int position) {
        processorList.add(position, item);
        notifyItemInserted(position);
    }

    public void setFilter(List<Processor> processor) {
        processorList = new ArrayList<>();
        processorList.addAll(processor);
        notifyDataSetChanged();
    }

}
