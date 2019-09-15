package com.example.simplestylinglibraryproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.simplestyling.SimpleStyling;

public class MainActivity extends AppCompatActivity{
    EditText TestEditText ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         TestEditText = findViewById(R.id.testEdiText);

        Button increaseSize=findViewById(R.id.increaseSize);
        Button decreaseSize=findViewById(R.id.decreaseSize);
        Button bold=findViewById(R.id.bold);
        Button itallic=findViewById(R.id.itallic);
        Button underLine=findViewById(R.id.underline);
        Button highLight=findViewById(R.id.highlight);


//Welcome to the sample project of SimpleStyling

        //    PARAMETERS

    /*
    TypeOrSize: required operation to be performed on the selected text
     -1 makes text bold
    -2 Highlights text provided the color is in integer 'HighLightColor'
    -3 underline
    -4 italic
    0 Incremental increase/decrease of text size by amount of 'TextSizeIncrement'
    (any size) changes text to a specific size

SelectionStart : start of selection

SelectionEnd : end of selection

EditTextName : name of the editText View

TextSizeIncrement : required incremental increase/decrease of text size +ve for increase and -ve for decrease

HighLightColor : required highlightColor of the text set to anything if the operation isn't highLight
    */



//set type to 2
//        Increment set to 6
        increaseSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleStyling.autoselection(TestEditText);
                SimpleStyling.Format(0, SimpleStyling.autoselection(TestEditText)[0], SimpleStyling.autoselection(TestEditText)[1],TestEditText,6,0);
            }
        });
//set type to 1
//        Increment set to -6
        decreaseSize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleStyling.autoselection(TestEditText);
                SimpleStyling.Format(0, SimpleStyling.autoselection(TestEditText)[0], SimpleStyling.autoselection(TestEditText)[1],TestEditText,-6,0);
            }
        });

/*YOU CAN SET THE TEXT TO A SPECIFIC SIZE JUST BY ADDING ANY NUMBER TO
TyprOrSize WITHOUT WORRING ABOUT THE INCREMENT*/


        //set type to -1
        bold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleStyling.autoselection(TestEditText);
                SimpleStyling.Format(-1, SimpleStyling.autoselection(TestEditText)[0], SimpleStyling.autoselection(TestEditText)[1],TestEditText,6,0);
            }
        });

//set type to -2

        highLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleStyling.autoselection(TestEditText);
                SimpleStyling.Format(-2, SimpleStyling.autoselection(TestEditText)[0], SimpleStyling.autoselection(TestEditText)[1],TestEditText,6,getResources().getColor(R.color.HighlightColor));
            }
        });
//set type to -3
        underLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleStyling.autoselection(TestEditText);
                SimpleStyling.Format(-3, SimpleStyling.autoselection(TestEditText)[0], SimpleStyling.autoselection(TestEditText)[1],TestEditText,6,0);
            }
        });

//set type to -4
        itallic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleStyling.autoselection(TestEditText);
                SimpleStyling.Format(-4, SimpleStyling.autoselection(TestEditText)[0], SimpleStyling.autoselection(TestEditText)[1],TestEditText,6,0);
            }
        });



    }


}
