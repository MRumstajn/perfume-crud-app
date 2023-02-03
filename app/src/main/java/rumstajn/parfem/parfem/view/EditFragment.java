package rumstajn.parfem.parfem.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mauricio.parfem.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rumstajn.parfem.parfem.model.Perfume;
import rumstajn.parfem.parfem.model.PerfumeGenderType;
import rumstajn.parfem.parfem.viewmodel.PerfumeViewModel;

@SuppressLint("NonConstantResourceId")
public class EditFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private final MainActivity mainActivity;
    @SuppressLint("SimpleDateFormat")
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    private Date productionDate;
    private PerfumeViewModel viewModel;
    private String imagePath;
    private String originalImagePath;
    private ActivityResultLauncher<Uri> launcher;
    private ArrayAdapter<PerfumeGenderType> arrayAdapter;
    private Perfume selectedPerfume;

    @BindView(R.id.edit_perfume_name_field)
    EditText nameField;
    @BindView(R.id.edit_perfume_manufacturer_field)
    EditText manufacturerField;
    @BindView(R.id.edit_perfume_gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.edit_perfume_image)
    ImageView image;
    @BindView(R.id.edit_perfume_date_button)
    Button dateButton;
    @BindString(R.string.calendar_emoji)
    String calendarEmoji;

    public EditFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public void onResume() {
        super.onResume();

        originalImagePath = selectedPerfume.getImagePath();
        nameField.setText(selectedPerfume.getName());
        manufacturerField.setText(selectedPerfume.getManufacturer());
        genderSpinner.setSelection(arrayAdapter.getPosition(selectedPerfume.getGender()));
        productionDate = selectedPerfume.getProductionDate();
        setDateButtonDate(productionDate);
        imagePath = selectedPerfume.getImagePath();

        if (imagePath != null && imagePath.length() > 0) {
            Glide.with(requireContext()).load(imagePath).apply(new RequestOptions().override(300,
                    300)).into(image);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_edit, container, false);

        ButterKnife.bind(this, view);

        arrayAdapter = new ArrayAdapter<>(getContext(),
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        arrayAdapter.add(PerfumeGenderType.MALE);
        arrayAdapter.add(PerfumeGenderType.FEMALE);
        arrayAdapter.add(PerfumeGenderType.ALL);

        genderSpinner.setAdapter(arrayAdapter);

        viewModel = mainActivity.getViewModel();
        selectedPerfume = viewModel.getSelectedPerfume();

        launcher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        if (imagePath != null && imagePath.length() > 0) {
                            // check if image file is valid (not empty)
                            if (ImageFileUtils.isNonEmptyImageFile(imagePath)) {
                                Glide.with(requireContext()).load(imagePath).into(image);
                                ImageFileUtils.addImageToGallery(imagePath, mainActivity);
                                selectedPerfume.setImagePath(imagePath);
                            }
                        }
                    } else {
                        imagePath = null;
                    }
                });

        return view;
    }

    @OnClick(R.id.edit_perfume_date_button)
    public void onDateButtonClicked() {
        LocalDate date = LocalDate.now();

        new DatePickerDialog(requireContext(), this, date.getYear(),
                date.getMonthValue(), date.getDayOfMonth()).show();
    }

    @OnClick(R.id.edit_perfume_save_button)
    public void onSaveButtonClicked() {
        Perfume selectedPerfume = viewModel.getSelectedPerfume();
        selectedPerfume.setName(nameField.getText().toString());
        selectedPerfume.setManufacturer(manufacturerField.getText().toString());
        selectedPerfume.setProductionDate(productionDate);
        selectedPerfume.setGender((PerfumeGenderType) genderSpinner.getSelectedItem());
        selectedPerfume.setImagePath(imagePath);

        viewModel.editPerfume(selectedPerfume);

        mainActivity.showDetailsFragment();
    }

    @OnClick(R.id.edit_perfume_cancel_button)
    public void onCancelButtonClicked() {
        selectedPerfume.setImagePath(originalImagePath);
        mainActivity.showDetailsFragment();
    }

    @OnClick({R.id.edit_perfume_image, R.id.edit_perfume_image_overlay})
    public void onClickOnImage() {
        File tempFile = null;
        try {
            tempFile = ImageFileUtils.createTempFile(mainActivity);
        } catch (IOException e) {
            mainActivity.showToast("Could not create image file");
            e.printStackTrace();
        }
        if (tempFile != null) {
            imagePath = tempFile.getPath();
            Uri tempFileUri = FileProvider.getUriForFile(mainActivity,
                    "rumstajn.parfem.fileprovider", tempFile);

            launcher.launch(tempFileUri);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        productionDate = calendar.getTime();

        setDateButtonDate(productionDate);
    }

    private void setDateButtonDate(Date date) {
        String buttonLabel = calendarEmoji + dateFormat.format(date);
        dateButton.setText(buttonLabel);
    }
}