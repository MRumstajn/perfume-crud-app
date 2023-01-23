package rumstajn.parfem.parfem.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mauricio.parfem.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import rumstajn.parfem.parfem.view.list_adapter.ListAdapter;
import rumstajn.parfem.parfem.viewmodel.PerfumeViewModel;

@SuppressLint("NonConstantResourceId")
public class ListFragment extends Fragment {
    @BindView(R.id.list) ListView list;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_first, container, false);

        ButterKnife.bind(this, view);

        ListAdapter adapter = new ListAdapter();
        list.setAdapter(adapter);

        PerfumeViewModel model = new ViewModelProvider(this).get(PerfumeViewModel.class);
        model.getAllPerfumes().observe(getViewLifecycleOwner(), perfumes -> {
            System.out.println(perfumes);
            adapter.setPerfumes(perfumes);
            adapter.notifyDataSetChanged();
        });

        return view;
    }
}