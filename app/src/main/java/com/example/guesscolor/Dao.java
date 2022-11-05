package com.example.guesscolor;


import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@androidx.room.Dao
public interface Dao {

    @Insert
    void insert(ColorModal model);

    @Query("SELECT * FROM color_table ORDER BY colorName ASC")
    List<ColorModal> getAllColors();

}
