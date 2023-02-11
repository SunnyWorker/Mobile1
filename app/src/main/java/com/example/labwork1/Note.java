package com.example.labwork1;

import android.os.Parcelable;
import android.provider.BaseColumns;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Note implements Serializable, BaseColumns {
    public static final String TABLE_NAME = "note";
    public static final int shortNameMaxLettersCount = 18;
    private int id;
    private String title;
    private String text;
    private Timestamp timeOfCreation;
    private Timestamp timeOfModification;
    private transient ConstraintLayout constraintLayout;
    private byte[] passwordHash;

    public Note() {
    }

    public Note(String title, String text, Timestamp timeOfCreation, Timestamp timeOfModification, ConstraintLayout constraintLayout, byte[] passwordHash) {
        this.title = title;
        this.text = text;
        this.timeOfCreation = timeOfCreation;
        this.timeOfModification = timeOfModification;
        this.constraintLayout = constraintLayout;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String encodingString) {
        try {
            this.passwordHash = MessageDigest.getInstance("SHA-256")
                    .digest(encodingString.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        catch (NullPointerException nullPointerException) {
            this.passwordHash = null;
        }
    }

    public ConstraintLayout getConstraintLayout() {
        return constraintLayout;
    }

    public void setConstraintLayout(ConstraintLayout constraintLayout) {
        this.constraintLayout = constraintLayout;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Timestamp getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(Timestamp timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public Timestamp getTimeOfModification() {
        return timeOfModification;
    }

    public void setTimeOfModification(Timestamp timeOfModification) {
        this.timeOfModification = timeOfModification;
    }

    public String getShortTitle() {
        if(title.length()-2<shortNameMaxLettersCount) return title;
        return title.substring(0,shortNameMaxLettersCount)+"...";
    }


}
