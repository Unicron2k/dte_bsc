package com.example.a8_2_room;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvId;
        private final TextView wordItemView;
        private final TextView description;
        private WordViewHolder(View itemView){
            super(itemView);
            tvId = itemView.findViewById(R.id.tvId);
            wordItemView = itemView.findViewById(R.id.textView);
            description = itemView.findViewById(R.id.description);
        }
    }

    private final LayoutInflater mInflater;
    private List<Word> mWords;

    WordListAdapter(Context context){mInflater = LayoutInflater.from(context);}

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position){
        if(mWords!=null){
            Word current = mWords.get(position);
            holder.tvId.setText(current.getId()+"");
            holder.wordItemView.setText(current.getWord());
            holder.description.setText(current.getDescription());
        } else {
            holder.tvId.setText("No id");
            holder.wordItemView.setText("No Word");
            holder.description.setText("No description");
        }
    }

    void setWords(List<Word> words){
        mWords = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount(){
        if(mWords!=null){
            return mWords.size();
        } else {
            return 0;
        }
    }


}
