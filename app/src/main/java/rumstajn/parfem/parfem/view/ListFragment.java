package rumstajn.parfem.parfem.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.mauricio.parfem.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import rumstajn.parfem.parfem.model.Perfume;
import rumstajn.parfem.parfem.view.list_adapter.ListAdapter;
import rumstajn.parfem.parfem.viewmodel.PerfumeViewModel;

@SuppressLint("NonConstantResourceId")
public class ListFragment extends Fragment {
    private final MainActivity mainActivity;

    private ListAdapter adapter;
    private PerfumeViewModel viewModel;

    @BindView(R.id.no_perfumes_label) TextView noPerfumesLabel;
    @BindView(R.id.list) ListView list;

    public ListFragment(MainActivity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        ButterKnife.bind(this, view);

        adapter = new ListAdapter();
        list.setAdapter(adapter);

        viewModel = mainActivity.getViewModel();
        reloadData();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        reloadData();
    }

    private void reloadData(){
        viewModel.getAllPerfumes().observe(getViewLifecycleOwner(), perfumes -> {
            if (perfumes.size() > 0) {
                adapter.setPerfumes(perfumes);
                adapter.notifyDataSetChanged();
                noPerfumesLabel.setVisibility(View.INVISIBLE);
            } else {
                noPerfumesLabel.setVisibility(View.VISIBLE);
            }
        });
    }

    @OnItemClick(R.id.list)
    public void onItemClick(int position){
        Perfume selectedPerfume = adapter.getPerfumes().get(position);

        viewModel.setSelectedPerfume(selectedPerfume);

        mainActivity.showDetailsFragment();
    }
}