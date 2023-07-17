package com.pm1.g2.md3.ui.home;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.pm1.g2.md3.databinding.FragmentHomeBinding;

import java.util.Objects;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Uri uri= null;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.bAbrirCamara.setOnClickListener(v ->{
            ContentValues values =new ContentValues();
            values.put(MediaStore.Images.Media.TITLE,"Titulo");
            values.put(MediaStore.Images.Media.DESCRIPTION,"Descripcion");

            uri= Objects.requireNonNull(getContext()).getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT,uri);
            camaraURL.launch(intent);
        });

        binding.bAbrirGaleria.setOnClickListener(v ->{
            Intent intent= new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            galeriaURL.launch(intent);
        });

        return root;
    }

    private ActivityResultLauncher<Intent> galeriaURL=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        Intent data= result.getData();
                        uri= data.getData();
                        binding.image.setImageURI(uri);
                        //Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getContext(), "No se ha seleccionado ninguna imagen", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> camaraURL=registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        //Intent data= result.getData();
                        //uri= data.getData();
                        binding.image.setImageURI(uri);
                        //Toast.makeText(getContext(), uri.toString(), Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getContext(), "No se ha tomado ninguna fotografia", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}