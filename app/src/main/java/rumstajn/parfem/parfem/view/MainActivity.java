package rumstajn.parfem.parfem.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mauricio.parfem.R;

import rumstajn.parfem.parfem.viewmodel.PerfumeViewModel;

public class MainActivity extends AppCompatActivity {
    private PerfumeViewModel viewModel;
    private Fragment listFragment;
    private Fragment detailsFragment;
    private Fragment newPerfumeFragment;
    private Fragment editFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewModel = new ViewModelProvider(this).get(PerfumeViewModel.class);

        listFragment = new ListFragment(this);
        detailsFragment = new DetailFragment(this);
        newPerfumeFragment = new NewPerfumeFragment(this);
        editFragment = new EditFragment(this);

        showListFragment();
    }

    public void showListFragment() {
        showFragment(listFragment);
    }

    public void showDetailsFragment() {
        showFragment(detailsFragment);
    }

    public void showNewPerfumeFragment() {
        showFragment(newPerfumeFragment);
    }

    public void showEditFragment(){
        showFragment(editFragment);
    }

    public void showFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    public PerfumeViewModel getViewModel() {
        return viewModel;
    }

    public void showToast(String msg) {
        runOnUiThread(() -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show());
    }
}