package ru.shprot.chimeraplants.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.shprot.chimeraplants.R;
import ru.shprot.chimeraplants.databinding.FragmentAddBinding;
import ru.shprot.chimeraplants.model.Plant;
import ru.shprot.chimeraplants.viewModel.PlantViewModel;

public class AddFragment extends Fragment {

    FragmentAddBinding binding;
    String img;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentAddBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.applyButton.setOnClickListener(v -> {
            if (!validateName()) return;
            saveChanges();
            Navigation.findNavController(v).navigate(R.id.action_addFragment_to_mainFragment);
        });

        binding.cameraIconText.setOnClickListener(v -> {
            addPhoto();
        });

        binding.plantTitle.setOnKeyListener((v, keyCode, event) -> handleKeyEvent(v, keyCode));
        binding.plantLastDate.setOnKeyListener((v, keyCode, event) -> handleKeyEvent(v, keyCode));
        binding.plantNextDate.setOnKeyListener((v, keyCode, event) -> handleKeyEvent(v, keyCode));
        binding.descEditText.setOnKeyListener((v, keyCode, event) -> handleKeyEvent(v, keyCode));
        binding.eatEditText.setOnKeyListener((v, keyCode, event) -> handleKeyEvent(v, keyCode));
    }

    private boolean handleKeyEvent(View view, int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            InputMethodManager inputMethodManager = (InputMethodManager)
                    getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            return true;
        }
        return false;
    }

    private void addPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.choose_picture)),1);
    }

    private void saveChanges() {
        Plant plant = new Plant();
        plant.setName(binding.plantTitle.getText() + "");
        plant.setImage(img + "");
        plant.setLastDate(binding.plantLastDate.getText() + "");
        plant.setNextDate(binding.plantNextDate.getText() + "");
        plant.setDescription(binding.descEditText.getText() + "");
        plant.setFood(binding.eatEditText.getText() + "");
        PlantViewModel viewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        viewModel.insertPlant(plant);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) return;
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }    super.onActivityResult(requestCode, resultCode, data);
        try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_hh_mm_ss");
            String fileName = simpleDateFormat.format(date) + ".JPEG";
            createDirectoryAndSaveFile(selectedImage, fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        File direct = new File(String.valueOf
                (getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)));
        if (!direct.exists()) direct.mkdirs();
        File file = new File(direct, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            img = direct + "/" + fileName;
            File fileOut = new File(img);
            Picasso.get()
                    .load(fileOut)
                    .placeholder(R.drawable.ic_baseline_add_24)
                    .fit()
                    .into(binding.plantImageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateName() {
        String name = binding.plantTitleLayout.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            binding.plantTitleLayout.setError(getString(R.string.error_name));
            return false;
        } else if (name.length() > 32) {
            binding.plantTitleLayout.setError(getString(R.string.error_length));
            return false;
        } else {
            binding.plantTitleLayout.setError("");
            return true;
        }
    }
}
