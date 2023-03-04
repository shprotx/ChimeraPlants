package ru.shprot.chimeraplants.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import java.util.List;
import ru.shprot.chimeraplants.model.Plant;
import ru.shprot.chimeraplants.model.PlantsRepository;

public class PlantViewModel extends AndroidViewModel {

    private PlantsRepository mRepository;
    private LiveData<List<Plant>> mAllPlants;

    public PlantViewModel(@NonNull Application application) {
        super(application);
        mRepository = new PlantsRepository(application);
        mAllPlants = mRepository.getAllPlants();
    }

    public LiveData<List<Plant>> getAllPlants() {
        return mAllPlants;
    }

    public LiveData<Plant> getPlant(int id) {
        return mRepository.getPlant(id);
    }

    public void insertPlant(Plant plant) {
        mRepository.insertPlant(plant);
    }

    public void updatePlant(Plant plant) { mRepository.updatePlant(plant);}

    public void deletePlant(Plant plant) { mRepository.deletePlant(plant);}
}
