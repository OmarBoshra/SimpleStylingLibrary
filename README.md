# SimpleStylingLibrary
Simple Library for styling text in a textEditor (EditText) in android

[![](https://jitpack.io/v/OmarBoshra/SimpleStylingLibrary.svg)](https://jitpack.io/#OmarBoshra/SimpleStylingLibrary)


[![enter image description here][1]][1]


  [1]: https://i.stack.imgur.com/dEvqD.gif

Ever wondered if there was just simple lightweight and efficent font styling library for a quick text editor .Well you've come to the right place with just [BOLD ,ITALLIC ,UNDERLINE ,HIGHLIGHTING AND TEXTSIZE] this library has whats neccessary for a basic clean text formatting



**Prerequisites**
Add this in your root build.gradle file (not your module build.gradle file):

allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}

**Dependency**
Add this to your module's build.gradle file (make sure the version matches the JitPack badge above):

**dependencies** {
	...
	        implementation 'com.github.OmarBoshra:SimpleStylingLibrary:-SNAPSHOT'
}


**Usage**

Just this one method 

SimpleStyling.Format(TypeOrSize,SelectionStart,SelectionEnd, EditTextName ,TextSizeIncrement ,HighLightColor);

    PARAMETERS

   
    **TypeOrSize**: required operation to be performed on the selected text
    -1 makes text bold
    -2 Highlights text provided the color is in integer 'HighLightColor'
    -3 underline
    -4 italic
    0 Incremental increase/decrease of text size by amount of 'TextSizeIncrement'
    (any size) changes text to a specific size

    **SelectionStart** : start of selection

    **SelectionEnd** : end of selection

    **EditTextName** : name of the editText View

    **TextSizeIncrement** : required incremental increase/decrease of text size +ve for increase and -ve for decrease

    **HighLightColor** : required highlightColor of the text set to anything if the operation isn't highLight

The library also includes an auto selection method so that the user doesn't have to highlight the text in order to format it:-

The function 

SimpleStyling.autoselection(TestEditText);

returns an array with the starting and ending indexies so a button click can be used this way:-

            SimpleStyling.Format(0, SimpleStyling.autoselection(TestEditText)[0], SimpleStyling.autoselection(TestEditText)[1],TestEditText,6,0);
            
#If you want to be sure you can see the underline, disable the textAutoCorrection this way:-

            m.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_FILTER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
            
Good luck


