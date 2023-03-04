package ru.shprot.chimeraplants;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import ru.shprot.chimeraplants.model.Plant;
import ru.shprot.chimeraplants.viewModel.PlantViewModel;

public class EditAlertDialog extends DialogFragment {

    private final Plant plant;
    private final int fieldId;

    public EditAlertDialog(Plant plant, int fieldId) {
        this.plant = plant;
        this.fieldId = fieldId;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit, null);
        builder.setView(view);
        builder.setBackground(new ColorDrawable(Color.TRANSPARENT));
        AlertDialog dialog = builder.create();

        EditText editText = view.findViewById(R.id.dialogEdit);
        if (fieldId == 1) {
            TextInputLayout l = view.findViewById(R.id.dialogEditLayout);
            l.setErrorEnabled(true);
            l.setCounterEnabled(true);
            l.setCounterMaxLength(32);
            editText.setText(plant.getName());
        }
        else if (fieldId == 2) editText.setText(plant.getLastDate());
        else if (fieldId == 3) editText.setText(plant.getNextDate());
        else if (fieldId == 4) editText.setText(plant.getDescription());
        else if (fieldId == 5) editText.setText(plant.getFood());

        Button cancelButton = view.findViewById(R.id.dialogCancelButton);
        cancelButton.setOnClickListener(v -> dialog.cancel());

        Button saveButton = view.findViewById(R.id.dialogSaveButton);
        saveButton.setOnClickListener(v -> {
            String newText = editText.getText().toString();
            if (fieldId == 1) {
                if (!validateName(view)) return;
                plant.setName(newText + "");
            }
            else if (fieldId == 2) plant.setLastDate(newText + "");
            else if (fieldId == 3) plant.setNextDate(newText + "");
            else if (fieldId == 4) plant.setDescription(newText + "");
            else if (fieldId == 5) plant.setFood(newText + "");
            PlantViewModel viewModel = new ViewModelProvider(this).get(PlantViewModel.class);
            viewModel.updatePlant(plant);
            dialog.cancel();
        });

        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private boolean validateName(View view) {
        TextInputLayout l = view.findViewById(R.id.dialogEditLayout);
        String name = l.getEditText().getText().toString().trim();
        if (name.isEmpty()) {
            l.setError(getString(R.string.error_name));
            return false;
        } else if (name.length() > 32) {
            l.setError(getString(R.string.error_length));
            return false;
        } else {
            l.setError("");
            return true;
        }
    }
}
