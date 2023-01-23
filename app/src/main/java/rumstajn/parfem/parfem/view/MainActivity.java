package rumstajn.parfem.parfem.view;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mauricio.parfem.R;

import java.util.Date;

import rumstajn.parfem.parfem.model.Perfume;
import rumstajn.parfem.parfem.model.PerfumeGenderType;
import rumstajn.parfem.parfem.model.dao.PerfumeDatabaseProvider;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Perfume perfume = new Perfume("debug", "shanel", PerfumeGenderType.MALE, new Date(), "");
        PerfumeDatabaseProvider.getDatabaseInstance(getApplicationContext()).getPerfumeDAO().addNewPerfume(perfume);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ListFragment())
                .commit();
    }
}