package ru.shprot.chimeraplants.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import ru.shprot.chimeraplants.MainActivity;
import ru.shprot.chimeraplants.R;
import ru.shprot.chimeraplants.adapter.PlantsAdapter;
import ru.shprot.chimeraplants.viewModel.PlantViewModel;

public class MainFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        PlantsAdapter adapter = new PlantsAdapter(new PlantsAdapter.PlantsDiff());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        PlantViewModel viewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        viewModel.getAllPlants().observe(getViewLifecycleOwner(), adapter::submitList);

        FloatingActionButton addButton = view.findViewById(R.id.fab);
        addButton.setOnClickListener(v ->
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_addFragment));
    }


}
