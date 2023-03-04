package ru.shprot.chimeraplants.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PlantDao {

    @Insert
    void insertPlant(Plant plant);

    @Delete
    void deletePlant(Plant plant);

    @Update
    void updatePlant(Plant plant);

    @Query("SELECT * FROM plants_table WHERE id ==:id")
    LiveData<Plant> getPlant(int id);

    @Query("SELECT * FROM plants_table")
    LiveData<List<Plant>> getAllPlants();
}

