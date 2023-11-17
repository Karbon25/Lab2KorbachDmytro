package com.example.lab2korbachdmytro.ShowResult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab2korbachdmytro.R;

import java.util.List;

public class GameResultAdapter extends RecyclerView.Adapter<GameResultAdapter.ReactionViewHolder> {

    private List<GameResult> gameResults;
    private Context context;

    public GameResultAdapter(Context context, List<GameResult> gameResults) {
        this.context = context;
        this.gameResults = gameResults;
    }

    @NonNull
    @Override
    public ReactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_game_result, parent, false);
        return new ReactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReactionViewHolder holder, int position) {
        GameResult result = gameResults.get(position);
        holder.bind(result);
    }

    @Override
    public int getItemCount() {
        return gameResults.size();
    }

    public class ReactionViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView timeTextView;
        private TextView scoreTextView;
        public ReactionViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.item_result_game_name);
            timeTextView = itemView.findViewById(R.id.item_result_game_time);
            scoreTextView = itemView.findViewById(R.id.item_result_game_score);
        }

        public void bind(GameResult result) {
            nameTextView.setText("Гравець: " + result.getName());
            timeTextView.setText("Час гри: " + result.getTime());
            scoreTextView.setText("Рахунок: " + result.getScore());
        }
    }
}