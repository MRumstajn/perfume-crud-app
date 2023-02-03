package rumstajn.parfem.parfem.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rumstajn.parfem.parfem.model.Perfume;
import rumstajn.parfem.parfem.model.PerfumeGenderType;

@SuppressLint("NonConstantResourceId")
public class NewPerfumeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private final MainActivity mainActivity;
    @SuppressLint("SimpleDateFormat")
    private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @BindView(R.id.new_perfume_name_field)
    EditText nameField;
    @BindView(R.id.new_perfume_manufacturer_field)
    EditText manufacturerField;
    @BindView(R.id.new_perfume_gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.new_perfume_image)
    ImageView image;
    @BindView(R.id.new_perfume_date_button)
    Button dateButton;
    @BindString(R.string.calendar_emoji)
    String calendarEmoji;

    private Date productionDate;
    private ActivityResultLauncher<Uri> launcher;
    private String imagePath;

    public NewPerfumeFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_new_perfume, container, false);

        ButterKnife.bind(this, view);

        ArrayAdapter<PerfumeGenderType> arrayAdapter =
                new ArrayAdapter<>(getContext(),
                        androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);

        arrayAdapter.add(PerfumeGenderType.MALE);
        arrayAdapter.add(PerfumeGenderType.FEMALE);
        arrayAdapter.add(PerfumeGenderType.ALL);

        genderSpinner.setAdapter(arrayAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        launcher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) {
                        if (imagePath != null && imagePath.length() > 0) {
                            // check if image file is valid (not empty)
                            if (ImageFileUtils.isNonEmptyImageFile(imagePath)) {
                                Glide.with(requireContext()).load(imagePath).apply(new RequestOptions().override(300, 300)).into(image);
                                ImageFileUtils.addImageToGallery(imagePath, mainActivity);
                            }
                        }
                    } else {
                        imagePath = null;
                    }
                });
    }

    @OnClick(R.id.new_perfume_date_button)
    public void onDateButtonClicked() {
        LocalDate date = LocalDate.now();

        new DatePickerDialog(requireContext(), this, date.getYear(),
                date.getMonthValue(), date.getDayOfMonth()).show();
    }

    @OnClick(R.id.new_perfume_create_button)
    public void onCreateButtonClicked() {
        String name = nameField.getText().toString();
        String manufacturer = manufacturerField.getText().toString();

        if (checkStringsEmpty(name, manufacturer)) {
            mainActivity.showToast("All fields are required");
            return;
        }

        if (productionDate == null) {
            mainActivity.showToast("Select a date");
            return;
        }

        Object selectedSpinnerObj = genderSpinner.getSelectedItem();
        if (selectedSpinnerObj == null) {
            mainActivity.showToast("Select a gender first");
            return;
        }

        PerfumeGenderType genderType = (PerfumeGenderType) selectedSpinnerObj;

        if (imagePath == null || imagePath.length() == 0 || !ImageFileUtils.isNonEmptyImageFile(imagePath)){
            mainActivity.showToast("Image is required");
            return;
        }

        mainActivity.getViewModel().addNewPerfume(new Perfume(name, manufacturer, genderType,
                productionDate, imagePath));

        ImageFileUtils.addImageToGallery(imagePath, mainActivity);

        resetFields();

        mainActivity.showListFragment();
    }

    @OnClick(R.id.new_perfume_cancel_button)
    public void onCancelButtonClicked() {
        resetFields();

        mainActivity.showListFragment();
    }

    @OnClick({R.id.new_perfume_image, R.id.new_perfume_image_overlay})
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

    private boolean checkStringsEmpty(String... args) {
        return Arrays.stream(args).anyMatch(String::isEmpty);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        productionDate = calendar.getTime();

        String buttonLabel = calendarEmoji + dateFormat.format(productionDate);
        dateButton.setText(buttonLabel);
    }

    private void resetFields(){
        nameField.setText(null);
        manufacturerField.setText(null);
        genderSpinner.setSelection(0);
        productionDate = null;
        imagePath = null;
    }
}