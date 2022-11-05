package com.example.guesscolor;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "color_table")
public class ColorModal {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "colorName")
    private String colorName;

    @ColumnInfo(name = "red")
    private int red;

    @ColumnInfo(name = "green")
    private int green;

    @ColumnInfo(name = "blue")
    private int blue;

    public int getId() {
        return id;
    }


    public ColorModal(int id, String colorName, int red, int green, int blue) {
        this.id = id;
        this.colorName = colorName;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    @Ignore
    public ColorModal(String colorName, int red, int green, int blue) {
        this.colorName = colorName;
        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public int getRed() {
        return red;
    }

    public void setRed(int red) {
        this.red = red;
    }

    public int getGreen() {
        return green;
    }

    public void setGreen(int green) {
        this.green = green;
    }

    public int getBlue() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue = blue;
    }
}
