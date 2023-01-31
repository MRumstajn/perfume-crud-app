package rumstajn.parfem.parfem.view;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.mauricio.parfem.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rumstajn.parfem.parfem.model.Perfume;
import rumstajn.parfem.parfem.model.PerfumeGenderType;

@SuppressLint("NonConstantResourceId")
public class NewPerfumeFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private final MainActivity mainActivity;

    @BindView(R.id.new_perfume_name_field)
    EditText nameField;
    @BindView(R.id.new_perfume_manufacturer_field)
    EditText manufacturerField;
    @BindView(R.id.new_perfume_gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.new_perfume_image)
    ImageView image;

    private Date productionDate;
    private ActivityResultLauncher<Object> launcher;
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
                new ArrayAdapter<PerfumeGenderType>(getContext(),
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

        launcher = registerForActivityResult(new ActivityResultContract<Object, Object>() {
            @Override
            public Object parseResult(int i, @Nullable Intent intent) {
                return intent != null ? intent.getExtras().get("data") : null;
            }

            @NonNull
            @Override
            public Intent createIntent(@NonNull Context context, Object o) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                File tempFile = null;
                try {
                    tempFile = createTempFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (tempFile != null) {
                    imagePath = tempFile.getPath();
                    Uri tempFileUri = FileProvider.getUriForFile(mainActivity,
                            "rumstajn.parfem.fileprovider", tempFile);

                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
                }

                return cameraIntent;
            }
        }, ignored -> {
            Glide.with(requireContext()).load(imagePath).into(image);

            addImageToGallery();
        });
    }

    @OnClick(R.id.new_perfume_date_button)
    public void onDateButtonClicked() {
        new DatePickerDialog(requireContext(), this, 2023, 1, 1).show();
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

        mainActivity.getViewModel().addNewPerfume(new Perfume(name, manufacturer, genderType,
                productionDate, imagePath));

        addImageToGallery();

        mainActivity.showListFragment();
    }

    @OnClick(R.id.new_perfume_cancel_button)
    public void onCancelButtonClicked() {
        mainActivity.showListFragment();
    }

    @OnClick(R.id.new_perfume_image)
    public void onClickOnImage() {
        launcher.launch(null);
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
    }

    @SuppressLint("SimpleDateFormat")
    private File createTempFile() throws IOException {
        File imagesDir = mainActivity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        return File.createTempFile(timeStamp, ".jpg", imagesDir);
    }

    private void addImageToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imagePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        mainActivity.sendBroadcast(mediaScanIntent);
    }
}