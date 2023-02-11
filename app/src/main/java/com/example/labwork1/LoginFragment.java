package com.example.labwork1;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.example.labwork1.databinding.FragmentFirstBinding;
import com.example.labwork1.databinding.FragmentLoginBinding;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Note noteReceive;
        if(getArguments()!=null) {
            if(getArguments().getSerializable("noteDelete")!=null) {
                noteReceive = (Note) getArguments().getSerializable("noteDelete");
                final byte[] passwordHash = noteReceive.getPasswordHash();
                final int deleteIndex = getArguments().getInt("noteIndex");
                binding.passwordInput.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        try {
                            if(Arrays.equals(MessageDigest
                                    .getInstance("SHA-256")
                                    .digest(binding.passwordInput.getText().toString().getBytes(StandardCharsets.UTF_8)),
                                    passwordHash))
                            {
                                FirstFragment.deleteTask(deleteIndex);
                                NavHostFragment.findNavController(LoginFragment.this)
                                        .navigate(R.id.action_LoginFragment_to_FirstFragment);
                                return false;
                            }
                            return false;
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                });
            }
            else if(getArguments().getSerializable("noteUpdate")!=null) {
                noteReceive = (Note) getArguments().getSerializable("noteUpdate");
                final byte[] passwordHash = noteReceive.getPasswordHash();
                final int deleteIndex = getArguments().getInt("noteIndex");
                binding.passwordInput.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View view, int i, KeyEvent keyEvent) {
                        try {
                            if(Arrays.equals(MessageDigest
                                            .getInstance("SHA-256")
                                            .digest(binding.passwordInput.getText().toString().getBytes(StandardCharsets.UTF_8)),
                                    passwordHash))
                            {
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("noteUpdate",noteReceive);
                                bundle.putInt("noteIndex",deleteIndex);
                                NavHostFragment.findNavController(LoginFragment.this)
                                        .navigate(R.id.action_LoginFragment_to_SecondFragment,bundle);
                                return false;
                            }
                            return false;
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                });
            }
            else {
                NavHostFragment.findNavController(LoginFragment.this)
                        .navigate(R.id.action_LoginFragment_to_FirstFragment);
            }
            getArguments().clear();
        }
    }
}