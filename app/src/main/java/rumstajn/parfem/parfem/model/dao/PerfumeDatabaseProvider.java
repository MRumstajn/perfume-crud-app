package rumstajn.parfem.parfem.model.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import rumstajn.parfem.parfem.model.Perfume;

public abstract class PerfumeDatabaseProvider {
    private static PerfumeDatabase perfumeDatabaseInstance;

    public static PerfumeDatabase getDatabaseInstance(Context context) {
        if (perfumeDatabaseInstance == null) {
            perfumeDatabaseInstance = Room.databaseBuilder(context, PerfumeDatabase.class, "perfume-db")
                    .allowMainThreadQueries().build();
        }

        return perfumeDatabaseInstance;
    }
}
