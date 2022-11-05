package com.example.guesscolor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper db;
    int count = 0, totalColors = 0, editTextCount = 0;
    ArrayList<ColorModal> arrColors;
    String colorName;
    EditText et[];
    LinearLayout editTextLL;
    GridLayout keyboardGridLayout;
    Button hintButton;
    Drawable background, background2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        db = DatabaseHelper.getDB(getApplicationContext());
        if (isFirstTime()) {
            addColors();
        }
        background = getDrawable(R.drawable.edit_text);
        background2 = getDrawable(R.drawable.edit_text2);

        arrColors = (ArrayList<ColorModal>) db.colorDao().getAllColors();
        setLayoutColorAndColorName();

        hintButton = findViewById(R.id.hintButton);
        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Character> hint = new ArrayList<>();
                for (int i = 0; i < colorName.length(); i++) {
                    hint.add(colorName.charAt(i));
                }
                Collections.shuffle(hint);
                String hintstr = "";
                for (int i = 0; i < hint.size(); i++) {
                    hintstr += hint.get(i);
                }

                Collections.shuffle(hint);
                hintstr += " | ";
                for (int i = 0; i < hint.size(); i++) {
                    hintstr += hint.get(i);
                }

                showToast("Your Hint is : " + hintstr);
            }
        });


    }

    private boolean isFirstTime() {

        SharedPreferences sp = getSharedPreferences("Guess_Color", MODE_PRIVATE);
        SharedPreferences.Editor speditor = sp.edit();

        if (sp.getBoolean("IS_FIRST_TIME", true)) {
            speditor.putBoolean("IS_FIRST_TIME", false);
            speditor.commit();
            return true;
        } else {
            return false;
        }

    }

    //==============================================================================
    //==========================Add Colors in Room Database=========================
    //==============================================================================
    private void addColors() {
        db.colorDao().insert(new ColorModal("black", 0, 0, 0));
        db.colorDao().insert(new ColorModal("white", 255, 255, 255));
        db.colorDao().insert(new ColorModal("red", 255, 0, 0));
        db.colorDao().insert(new ColorModal("lime", 0, 255, 0));
        db.colorDao().insert(new ColorModal("blue", 0, 0, 255));
        db.colorDao().insert(new ColorModal("yellow", 255, 255, 0));
        totalColors = 6;
    }

    //==============================================================================
    //==========Sets Color in display and sets Current color name===================
    //==============================================================================
    private void setLayoutColorAndColorName() {

        if (count == arrColors.size()) {
            count = 0;
        }
        TextView levelTextView = findViewById(R.id.levelTextView);
        levelTextView.setText("Level-" + count);

        colorName = arrColors.get(count).getColorName();
        int red = arrColors.get(count).getRed();
        int green = arrColors.get(count).getGreen();
        int blue = arrColors.get(count).getBlue();

        LinearLayout colorLL = findViewById(R.id.colorLinearLayout);
        int color = Color.argb(255, red, green, blue);

        count++;
        colorLL.setBackgroundColor(color);

        setEditTexts(colorName);
        setKeyBoard(colorName);
    }

    //==============================================================================
    //=================Sets Appropriate Custom EditTexts============================
    //==============================================================================
    private void setEditTexts(String colorName) {

        et = new EditText[colorName.length()];

        for (int i = 0; i < colorName.length(); i++) {
            editTextLL = findViewById(R.id.editTextLinearLayout);

            // add edittext
            et[i] = new EditText(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMarginStart(10);
            et[i].setLayoutParams(lp);
            et[i].setEms(1);


            //setting maxlength to 1
            int maxLength = 1;
            InputFilter[] fArray = new InputFilter[1];
            fArray[0] = new InputFilter.LengthFilter(maxLength);
            et[i].setFilters(fArray);

            et[i].setBackground(background);

            editTextLL.addView(et[i]);
        }
    }


    //==============================================================================
    //=========================Sets random keyboard=================================
    //==============================================================================
    private void setKeyBoard(String colorName) {
        ArrayList<Character> keyboardCharacters = new ArrayList<>();
        ArrayList<Character> alphabets = new ArrayList<>();
        int colorLength = colorName.length();
        boolean flag = true;

        for (int i = 0; i < colorLength; i++) {
            keyboardCharacters.add(colorName.charAt(i));
        }
        for (Character ch = 'a'; ch <= 'z'; ch++) {
            alphabets.add(ch);
        }
        Collections.shuffle(alphabets);

        for (int i = 0; i < 13 - colorLength; i++) {

            for (int j = 0; j < keyboardCharacters.size(); j++) {
                if (alphabets.get(i) == keyboardCharacters.get(j)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                keyboardCharacters.add(alphabets.get(i));
            } else {
                flag = true;
            }
        }

        // ==========================Seting Keyboard============================
        Collections.shuffle(keyboardCharacters);
        keyboardGridLayout = findViewById(R.id.keyboardGridLayout);

        Button btn[] = new Button[keyboardCharacters.size()];
        for (int i = 0; i < keyboardCharacters.size(); i++) {
            Character currentChar = keyboardCharacters.get(i);
            btn[i] = new Button(this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(5, 5, 5, 5);
            btn[i].setLayoutParams(lp);
            btn[i].setText(currentChar + "");
            int color = Color.argb(255, 255, 87, 51);
            btn[i].setBackgroundColor(color);

            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (editTextCount < colorLength) {

                        et[editTextCount].setText(((Button) v).getText().toString());
                        et[editTextCount].setBackground(background2);
                        editTextCount++;

                    }
                    if (editTextCount == colorLength) {
                        calculateResult();

                    }
                }
            });
            keyboardGridLayout.addView(btn[i]);
        }
    }

    private void calculateResult() {
        String writtenColor = "";
        int tempCount = 0;
        while (tempCount < colorName.length()) {
            writtenColor += et[tempCount].getText().toString();
            tempCount++;
        }
        if (writtenColor.equals(colorName)) {
            showToast("Hurray Right Answer Try Next..");
            editTextLL.removeAllViews();
            keyboardGridLayout.removeAllViews();
            editTextCount = 0;
            setLayoutColorAndColorName();
        } else {
            showToast("Wrong Answer Try Again!");
            tempCount = 0;
            editTextCount = 0;
            while (tempCount < colorName.length()) {
                et[tempCount].setText("");
                et[tempCount].setBackground(background);
                tempCount++;
            }
        }
    }

    private void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }


}