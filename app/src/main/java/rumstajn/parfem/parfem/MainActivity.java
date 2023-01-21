package rumstajn.parfem.parfem;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mauricio.parfem.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new ListFragment())
                .commit();
    }
}