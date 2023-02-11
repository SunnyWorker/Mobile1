package com.example.labwork1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.labwork1.databinding.FragmentSecondBinding;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Note noteReceive = null;
        int deleteIndex = 0;
        EditText noteTitle = binding.getRoot().findViewById(R.id.note_title);
        EditText noteText = binding.getRoot().findViewById(R.id.note_text);
        Switch switch2 = binding.getRoot().findViewById(R.id.switch2);
        EditText passwordEditText = binding.getRoot().findViewById(R.id.passwordEditText);
        switch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b) {
                    ObjectAnimator animation = ObjectAnimator.ofFloat(compoundButton, "translationX", 300f);
                    animation.setDuration(500);
                    animation.start();
                    animation.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            passwordEditText.setVisibility(View.VISIBLE);
                        }
                    });
                }
                else {
                    passwordEditText.setVisibility(View.GONE);
                    ObjectAnimator animation = ObjectAnimator.ofFloat(compoundButton, "translationX", 0);
                    animation.setDuration(500);
                    animation.start();
                }
            }
        });
        Button buttonSecond = binding.getRoot().findViewById(R.id.button_second);
        if(getArguments()!=null) {
            noteReceive = (Note) getArguments().getSerializable("noteUpdate");
            deleteIndex = getArguments().getInt("noteIndex");
            buttonSecond.setText("Изменить");
            noteTitle.setText(noteReceive.getTitle());
            noteText.setText(noteReceive.getText());
            if(noteReceive.getPasswordHash()!=null) {
                switch2.setChecked(true);
                passwordEditText.setHint("Введите новый пароль");
            }
            getArguments().clear();
        }
        final Note noteUpdate = noteReceive;
        final int finalDeleteIndex = deleteIndex;
        buttonSecond.setOnClickListener(view1 -> {
                if(noteTitle.getText().toString().equals("")) {
                    noteTitle.setHintTextColor(Color.RED);
                    noteTitle.setBackgroundTintList(ColorStateList.valueOf(Color.RED));
                }
                else {
                    Bundle bundle = new Bundle();
                    if(noteUpdate!=null) {
                        noteUpdate.setTitle(noteTitle.getText().toString());
                        noteUpdate.setText(noteText.getText().toString());
                        noteUpdate.setTimeOfModification(Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))));
                        if(switch2.isChecked() && !passwordEditText.getText().toString().equals("")) {
                            noteUpdate.setPasswordHash(passwordEditText.getText().toString());
                        }
                        else if(!switch2.isChecked()) noteUpdate.setPasswordHash(null);
                        bundle.putSerializable("noteUpdate",noteUpdate);
                        bundle.putInt("noteIndex",finalDeleteIndex);
                        NavHostFragment.findNavController(SecondFragment.this)
                                .navigate(R.id.action_SecondFragment_to_FirstFragment,bundle);
                        return;
                    }
                    Note note = null;
                    try {
                        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")));
                        note = new Note(
                                noteTitle.getText().toString(),
                                noteText.getText().toString(),
                                Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))),
                                Timestamp.valueOf(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"))),
                                null,
                                switch2.isChecked() && !passwordEditText.getText().toString().equals("")? MessageDigest.getInstance("SHA-256")
                                        .digest(passwordEditText.getText().toString().getBytes(StandardCharsets.UTF_8))
                                        :null
                        );
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    bundle.putSerializable("noteCreate",note);
                    NavHostFragment.findNavController(SecondFragment.this)
                            .navigate(R.id.action_SecondFragment_to_FirstFragment,bundle);
                }
        });
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}