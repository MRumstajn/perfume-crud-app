package rumstajn.parfem.parfem.view;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mauricio.parfem.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rumstajn.parfem.parfem.model.Perfume;
import rumstajn.parfem.parfem.viewmodel.PerfumeViewModel;

@SuppressLint("NonConstantResourceId")
public class DetailFragment extends Fragment {
    private final MainActivity mainActivity;
    private final PerfumeViewModel viewModel;

    @BindView(R.id.details_name_field)
    TextView name;
    @BindView(R.id.details_manufacturer_field)
    TextView manufacturer;
    @BindView(R.id.details_gender_field)
    TextView gender;
    @BindView(R.id.details_date_field)
    TextView date;
    @BindView(R.id.details_image)
    ImageView image;

    public DetailFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;

        viewModel = mainActivity.getViewModel();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        View view = inflater.inflate(R.layout.fragment_second, container, false);

        ButterKnife.bind(this, view);

        Perfume selectedPerfume = viewModel.getSelectedPerfume();

        name.setText(selectedPerfume.getName());
        manufacturer.setText(selectedPerfume.getManufacturer());
        gender.setText(selectedPerfume.getGender().toString());
        date.setText(SimpleDateFormat.getDateInstance().format(selectedPerfume.getProductionDate()));

        if (selectedPerfume.getImagePath() != null && !selectedPerfume.getImagePath().isEmpty()) {
            Glide.with(requireContext()).load(selectedPerfume.getImagePath()).into(image);
        }

        return view;
    }

    @OnClick(R.id.details_back_btn)
    public void onClickBackButton() {
        mainActivity.showListFragment();
    }

    @OnClick(R.id.details_edit_button)
    public void onClickEditButton(){
        mainActivity.showEditFragment();
    }

    @OnClick(R.id.details_delete_button)
    public void onDetailsButtonClicked(){
        Perfume selectedPerfume = viewModel.getSelectedPerfume();

        // delete perfume image
        boolean deleted = new File(selectedPerfume.getImagePath()).delete();

        if (!deleted){
            mainActivity.showToast("Failed to delete image for perfume " + selectedPerfume.getName());
        }

        viewModel.deletePerfume(selectedPerfume);
        viewModel.setSelectedPerfume(null);

        mainActivity.showListFragment();
    }
}