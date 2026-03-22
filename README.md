# SimpleStylingLibrary
Simple Library for styling text in a textEditor (EditText) in android

[![](https://jitpack.io/v/OmarBoshra/SimpleStylingLibrary.svg)](https://jitpack.io/#OmarBoshra/SimpleStylingLibrary)


[![enter image description here][1]][1]


  [1]: https://i.stack.imgur.com/dEvqD.gif

Ever wondered if there was just simple lightweight and efficent font styling library for a quick text editor .Well you've come to the right place with just [BOLD, ITALIC, UNDERLINE, HIGHLIGHT, INCREMENTAL_SIZE, FIXED_SIZE] this library has whats neccessary for a basic clean text formatting



# **Prerequisites**
Add this in your root build.gradle file (not your module build.gradle file):
```java
    allprojects {
    	repositories {
    		...
    		maven { url "https://jitpack.io" }
    	}
    }
```
# **Dependency**
Add this to your module's build.gradle file (make sure the version matches the JitPack badge above):

```java
dependencies {
	...
	        implementation 'com.github.OmarBoshra:SimpleStylingLibrary:2.03'
}
```


# **Usage**

Just this one method 

```java
  SimpleStyling.format(FormattingType, SelectionStart, SelectionEnd, EditTextName, sizeValue, HighLightColor);
```

PARAMETERS

**FormattingType**: required operation to be performed on the selected text. It uses the `SimpleStyling.FormattingType` enum:
    
    - BOLD: makes text bold
    - ITALIC: makes text italic
    - UNDERLINE: underlines text
    - HIGHLIGHT: highlights text provided the color is in integer 'HighLightColor'
    - INCREMENTAL_SIZE: Incremental increase/decrease of text size by amount of 'sizeValue'
    - FIXED_SIZE: changes text to a specific size specified in 'sizeValue'

**SelectionStart** : start of selection

**SelectionEnd** : end of selection

**EditTextName** : name of the editText View

**sizeValue** : required value for size change (increment for INCREMENTAL_SIZE, absolute for FIXED_SIZE). +ve for increase and -ve for decrease in incremental mode.

**HighLightColor** : required highlightColor of the text set to anything if the operation isn't HIGHLIGHT


The function 

SimpleStyling.autoselection(TestEditText);

returns an array with the starting and ending indexies so a button click can be used this way:-
```java
            SimpleStyling.format(SimpleStyling.FormattingType.INCREMENTAL_SIZE, SimpleStyling.autoselection(TestEditText)[0], SimpleStyling.autoselection(TestEditText)[1], TestEditText, 6, 0);
```            
#If you want to be sure you can see the underline, disable the textAutoCorrection this way:-
```java
            m.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_FILTER | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_CLASS_TEXT);
```        

This is an app with a full implementation here :-

https://github.com/OmarBoshra/Simple-text-styling-Android/blob/master/README.md

Good luck
