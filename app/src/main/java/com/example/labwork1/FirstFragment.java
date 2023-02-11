package com.example.labwork1;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.labwork1.dao.NoteDAO;
import com.example.labwork1.dao.file.FileNoteDAO;
import com.example.labwork1.dao.sqllite.SQLLiteNoteDAO;
import com.example.labwork1.databinding.FragmentFirstBinding;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private static List<Note> storeNoteList;
    private static List<Note> viewNoteList;
    private static LinearLayout rl;
    private static NoteDAO noteDAO;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noteDAO = new FileNoteDAO(getContext());
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity)getActivity()).setToolbarButtonsVisibility(View.VISIBLE);
        rl = view.findViewById(R.id.vertical_layout);

        LayoutInflater layoutInflater = this.getLayoutInflater();

        if(getArguments()!=null) {
            if(getArguments().getSerializable("noteCreate")!=null) {
                noteDAO.addNote((Note)getArguments().getSerializable("noteCreate"));
            }
            if(getArguments().getSerializable("noteUpdate")!=null)
                noteDAO.changeNote(getArguments().getInt("noteIndex"),
                        (Note)getArguments().getSerializable("noteUpdate"));
            getArguments().clear();
        }

        storeNoteList = noteDAO.getNoteList();
        for (int i = 0; i < storeNoteList.size(); i++) {
            createNoteView(layoutInflater,storeNoteList.get(i));
        }
        noSearch();


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl.removeAllViews();
                viewNoteList = storeNoteList;
                ((MainActivity)getActivity()).setToolbarButtonsVisibility(View.GONE);
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }


    private void createNoteView(LayoutInflater layoutInflater, Note note) {
        ConstraintLayout constraintLayout = (ConstraintLayout) layoutInflater.inflate(R.layout.note_layout,null,false);
        note.setConstraintLayout(constraintLayout);
        TextView textView = constraintLayout.findViewById(R.id.note_title1);
        textView.setText(note.getShortTitle());
        constraintLayout.setOnClickListener(view1 -> {
            if(requestAccess(note,"noteUpdate")) {
                ((MainActivity)getActivity()).setToolbarButtonsVisibility(View.GONE);
                Bundle bundle = new Bundle();
                bundle.putSerializable("noteUpdate", note);
                bundle.putInt("noteIndex", storeNoteList.indexOf(note));
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment, bundle);
            }
        });
        ImageButton imageButton = constraintLayout.findViewById(R.id.imageButton1);
        imageButton.setOnClickListener(view1 -> {
            if(requestAccess(note,"noteDelete"))
                deleteTask(note);
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void searchForStringInMessage(String s) {
        Stream<Note> streamTitles = storeNoteList.stream().filter(note -> {
            if(note.getTitle().contains(s)) return true;
            return false;
        });
        Stream<Note> streamTexts = storeNoteList.stream().filter(note -> {
            if(note.getText().contains(s) && note.getPasswordHash()==null) return true;
            return false;
        });
        viewNoteList = Stream.concat(streamTitles,streamTexts).distinct().collect(Collectors.toList());
        invalidateList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void sortNotesByTitle(boolean isInversed) {
        if(isInversed) {
            viewNoteList = viewNoteList.stream().sorted(new Comparator<Note>() {
                @Override
                public int compare(Note note, Note t1) {
                    return t1.getTitle().compareTo(note.getTitle());
                }
            }).collect(Collectors.toList());
        }
        else {
            viewNoteList = viewNoteList.stream().sorted(new Comparator<Note>() {
                @Override
                public int compare(Note note, Note t1) {
                    return note.getTitle().compareTo(t1.getTitle());
                }
            }).collect(Collectors.toList());
        }
        invalidateList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void sortNotesByCreationDate(boolean isInversed) {
        if(isInversed) {
            viewNoteList = viewNoteList.stream().sorted(new Comparator<Note>() {
                @Override
                public int compare(Note note, Note t1) {
                    return t1.getTimeOfCreation().compareTo(note.getTimeOfCreation());
                }
            }).collect(Collectors.toList());
        }
        else {
            viewNoteList = viewNoteList.stream().sorted(new Comparator<Note>() {
                @Override
                public int compare(Note note, Note t1) {
                    return note.getTimeOfCreation().compareTo(t1.getTimeOfCreation());
                }
            }).collect(Collectors.toList());
        }
        invalidateList();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void sortNotesByModificationDate(boolean isInversed) {
        if(isInversed) {
            viewNoteList = viewNoteList.stream().sorted(new Comparator<Note>() {
                @Override
                public int compare(Note note, Note t1) {
                    return t1.getTimeOfModification().compareTo(note.getTimeOfModification());
                }
            }).collect(Collectors.toList());
        }
        else {
            viewNoteList = viewNoteList.stream().sorted(new Comparator<Note>() {
                @Override
                public int compare(Note note, Note t1) {
                    return note.getTimeOfModification().compareTo(t1.getTimeOfModification());
                }
            }).collect(Collectors.toList());
        }
        invalidateList();
    }

    public static void noSearch() {
        viewNoteList = storeNoteList;
        invalidateList();
    }

    private static void invalidateList() {
        rl.removeAllViews();
        for (Note note : viewNoteList) {
            rl.addView(note.getConstraintLayout());
        }
    }

    private static void deleteTask(Note note) {
        noteDAO.deleteNote(note);
        viewNoteList.remove(note);
        rl.removeView(note.getConstraintLayout());
    }

    public static void deleteTask(int deleteIndex) {
        deleteTask(storeNoteList.get(deleteIndex));
    }

    private boolean requestAccess(Note note, String bundleName) {
        if(note.getPasswordHash()==null) return true;
        rl.removeAllViews();
        ((MainActivity)getActivity()).setToolbarButtonsVisibility(View.GONE);
        Bundle bundle = new Bundle();
        bundle.putSerializable(bundleName,note);
        bundle.putInt("noteIndex",storeNoteList.indexOf(note));
        NavHostFragment.findNavController(FirstFragment.this)
                .navigate(R.id.action_FirstFragment_to_LoginFragment,bundle);
        return false;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}