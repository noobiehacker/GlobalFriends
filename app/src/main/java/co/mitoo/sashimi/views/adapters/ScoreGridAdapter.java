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

    public ScoreGridAdapter(List<String> myDataset , int rowCount) {
        this.scoreData = myDataset;
        this.rowCount = rowCount;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

    @Override
    public ScoreGridAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {

        View v;
        if(viewType == this.headerViewType){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.standing_head_text_view, parent, false);
        }else{
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.standing_score_text_view, parent, false);
        }

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        final String name = this.scoreData.get(position);
        holder.txtHeader.setText(this.scoreData.get(position));

    }

    @Override
    public int getItemCount() {
        return this.scoreData.size();
    }

    private boolean isHeaderView(int position){
        return position< this.rowCount;
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