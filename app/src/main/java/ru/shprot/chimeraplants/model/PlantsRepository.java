package ru.shprot.chimeraplants.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;
import java.util.concurrent.Callable;

public class PlantsRepository {

    private PlantDao mPlantDao;
    private LiveData<List<Plant>> mAllPlants;

    public PlantsRepository(Application application) {
        PlantDatabase db = PlantDatabase.getDatabase(application);
        mPlantDao = db.plantDao();
        mAllPlants = mPlantDao.getAllPlants();
    }

    public LiveData<List<Plant>> getAllPlants() {
        return mAllPlants;
    }

    public void insertPlant(Plant plant) {
        PlantDatabase.databaseExecutor.execute(() -> {
            mPlantDao.insertPlant(plant);
        });
    }

    public LiveData<Plant> getPlant(int id) {
        return mPlantDao.getPlant(id);
    }

    public void updatePlant(Plant plant) {
        PlantDatabase.databaseExecutor.execute(() -> {
            mPlantDao.updatePlant(plant);
        });
    }

    public void deletePlant(Plant plant) {
        PlantDatabase.databaseExecutor.execute(() -> {
            mPlantDao.deletePlant(plant);
        });
    }
}

