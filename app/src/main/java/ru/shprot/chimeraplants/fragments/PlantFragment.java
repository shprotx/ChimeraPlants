package ru.shprot.chimeraplants.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.shprot.chimeraplants.EditAlertDialog;
import ru.shprot.chimeraplants.R;
import ru.shprot.chimeraplants.databinding.FragmentPlantBinding;
import ru.shprot.chimeraplants.model.Plant;
import ru.shprot.chimeraplants.viewModel.PlantViewModel;

public class PlantFragment extends Fragment {

    private FragmentPlantBinding binding;
    private int id = 0;
    private Plant currentPlant;
    private PlantViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPlantBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null) id = bundle.getInt("plant_id");

        viewModel = new ViewModelProvider(this).get(PlantViewModel.class);
        viewModel.getPlant(id).observe(getViewLifecycleOwner(), plant -> {
            binding.plantTitle.setText(plant.getName());
            binding.plantLastDate.setText(plant.getLastDate());
            binding.plantNextDate.setText(plant.getNextDate());
            binding.descEditText.setText(plant.getDescription());
            binding.eatEditText.setText(plant.getFood());
            Picasso.get()
                    .load(new File(plant.getImage()))
                    .placeholder(R.drawable.example)
                    .fit()
                    .into(binding.plantImageView);
            currentPlant = plant;
        });

        setOnClick(binding.editName, 1);
        setOnClick(binding.editLastData, 2);
        setOnClick(binding.editNextData, 3);
        setOnClick(binding.editDescription, 4);
        setOnClick(binding.editFood, 5);
        binding.plantImageView.setOnClickListener(v -> changePhoto());

        setHasOptionsMenu(true);
    }

    private void changePhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),1);
    }

    private void setOnClick(ImageView button, int fieldId) {
        button.setOnClickListener( v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            EditAlertDialog dialog = new EditAlertDialog(currentPlant, fieldId);
            dialog.show(fragmentManager, "edit_dialog");
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_header, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deletePlant) {
            Navigation.findNavController(getView()).navigate(R.id.action_plantFragment_to_mainFragment);
            viewModel.deletePlant(currentPlant);
        }
        return super.onOptionsItemSelected(item);
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
            String img = direct + "/" + fileName;
            currentPlant.setImage(img);
            viewModel.updatePlant(currentPlant);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
