package co.mitoo.sashimi.views.adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

import co.mitoo.sashimi.R;

/**
 * Created by david on 15-05-29.
 */

public class ScoreGridAdapter  extends RecyclerView.Adapter<ScoreGridAdapter.ViewHolder> {
    private List<String> scoreData;
    private int rowCount;

    private final int headerViewType = 0;
    private final int rowViewType = 1;
    // Provide a suitable constructor (depends on the kind of dataset)
    public ScoreGridAdapter(List<String> myDataset , int rowCount) {
        this.scoreData = myDataset;
        this.rowCount = rowCount;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;

        public ViewHolder(View v) {
            super(v);
            txtHeader = (TextView) v.findViewById(R.id.score_text_view);
        }
    }

    public void add(int position, String item) {
        this.scoreData.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(String item) {
        int position = this.scoreData.indexOf(item);
        this.scoreData.remove(position);
        notifyItemRemoved(position);
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ScoreGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v;
        if(viewType == this.headerViewType){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.standing_head_text_view, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.standing_score_text_view, parent, false);
        }
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = this.scoreData.get(position);
        holder.txtHeader.setText(this.scoreData.get(position));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return this.scoreData.size();
    }

    private boolean isHeaderView(int position){
        return position% this.rowCount == 0 ;
    }

    @Override
    public int getItemViewType (int position) {

        if(isHeaderView(position)){
            return this.headerViewType;
        }else{
            return this.rowViewType;
        }
    }

}