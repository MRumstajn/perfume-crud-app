package rumstajn.parfem.parfem.model.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import rumstajn.parfem.parfem.model.Perfume;

@Database(entities = {Perfume.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class PerfumeDatabase extends RoomDatabase {
    public abstract PerfumeDAO getPerfumeDAO();
}
