package rumstajn.parfem.parfem.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import rumstajn.parfem.parfem.model.Perfume;

@Dao
public interface PerfumeDAO {
    @Query("select * from perfume")
    LiveData<List<Perfume>> getAllPerfumes();

    @Insert
    void addNewPerfume(Perfume perfume);

    @Update
    void editPerfume(Perfume perfume);

    @Delete
    void deletePerfume(Perfume perfume);
}
