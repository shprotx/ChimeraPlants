package ru.shprot.chimeraplants.adapter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavAction;
import androidx.navigation.NavDirections;
import androidx.navigation.NavOptions;
import androidx.navigation.NavOptionsBuilder;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;

import ru.shprot.chimeraplants.R;
import ru.shprot.chimeraplants.fragments.MainFragment;
import ru.shprot.chimeraplants.model.Plant;

public class PlantsAdapter extends ListAdapter<Plant, PlantsAdapter.PlantViewHolder> {

    public PlantsAdapter(@NonNull DiffUtil.ItemCallback<Plant> diffCallback) {
        super(diffCallback);
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.plant_item, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant currentPlant = getItem(position);
        holder.cardTitle.setText(currentPlant.getName());
        holder.lastDate.setText(currentPlant.getLastDate());
        holder.nextDate.setText(currentPlant.getNextDate());
        holder.plantId = currentPlant.getId();
        Picasso.get()
                .load(new File(currentPlant.getImage()))
                .fit()
                .placeholder(R.drawable.example)
                .into(holder.cardImage);
    }



    class PlantViewHolder extends RecyclerView.ViewHolder {

        private final TextView cardTitle;
        private final ImageView cardImage;
        private final TextView lastDate;
        private final TextView nextDate;
        private int plantId;

        private PlantViewHolder(View itemView) {
            super(itemView);
            cardTitle = itemView.findViewById(R.id.cardTitle);
            cardImage = itemView.findViewById(R.id.cardImageView);
            lastDate = itemView.findViewById(R.id.cardLastDate);
            nextDate = itemView.findViewById(R.id.cardNextDate);

            itemView.setOnClickListener( v -> {
                NavAction action = new NavAction(R.id.action_mainFragment_to_plantFragment);
                Bundle bundle = new Bundle();
                bundle.putInt("plant_id", plantId);
                action.setDefaultArguments(bundle);
                Navigation.findNavController(itemView).navigate(R.id.action_mainFragment_to_plantFragment, bundle);
            });
        }
    }

    public static class PlantsDiff extends DiffUtil.ItemCallback<Plant> {

        @Override
        public boolean areItemsTheSame(@NonNull Plant oldItem, @NonNull Plant newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull Plant oldItem, @NonNull Plant newItem) {
            return oldItem.getImage().equals(newItem.getImage()) &&
                    oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getLastDate().equals(newItem.getLastDate()) &&
                    oldItem.getNextDate().equals(newItem.getNextDate());
        }
    }
}
