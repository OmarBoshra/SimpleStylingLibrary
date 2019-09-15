package com.example.simplestyling;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.widget.EditText;

import java.util.Arrays;
import java.util.Comparator;

public class SimpleStyling {

    private static int CurrentSize = 0;
    private static SpannedString a;


// All types of formatting

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

    public static void Format (int TypeOrSize, int SelectionStart, int SelectionEnd, final EditText EditTextName ,int TextSizeIncrement ,int HighLightColor) {// formatting

        int st = EditTextName.getSelectionStart();
        int end = EditTextName.getSelectionEnd();
        int ind = -1;
        int oldei = -1;

        EditTextName.clearComposingText();
        int maindiff = 0;
        int colororig = HighLightColor;

        int colorchang = 0;
        String separation = "0";

        SpannableString selection = new SpannableString(EditTextName.getText().subSequence(SelectionStart, SelectionEnd));//selected part
        SpannableString selectionl = new SpannableString("");
        SpannableString selectionr = new SpannableString("");
        SpannedString newparts = new SpannedString("");



        AbsoluteSizeSpan[] spannedsize = EditTextName.getText().getSpans(SelectionStart, SelectionEnd, AbsoluteSizeSpan.class);

        StyleSpan[] spannedsize2 = EditTextName.getText().getSpans(SelectionStart, SelectionEnd, StyleSpan.class);
        BackgroundColorSpan[] spannedsize3 = EditTextName.getText().getSpans(SelectionStart, SelectionEnd, BackgroundColorSpan.class);
        UnderlineSpan[] spannedsize4 = EditTextName.getText().getSpans(SelectionStart, SelectionEnd, UnderlineSpan.class);
        //abs Type spans selection
        int v = -1;

        if (TypeOrSize < 0) {
            for (int xc = 0; TypeOrSize == -2 ? xc < spannedsize3.length : TypeOrSize == -3 ? xc < spannedsize4.length : xc < spannedsize2.length; xc++) {//check removal
                int sps = EditTextName.getText().getSpanStart(TypeOrSize == -2 ? spannedsize3[xc] : TypeOrSize == -3 ? spannedsize4[xc] : spannedsize2[xc]);
                int spe = EditTextName.getText().getSpanEnd(TypeOrSize == -2 ? spannedsize3[xc] : TypeOrSize == -3 ? spannedsize4[xc] : spannedsize2[xc]);//endinf index
                if (TypeOrSize == -2) {
                    colororig = spannedsize3[xc].getBackgroundColor();
                    if (colororig != HighLightColor) {
                        colorchang = 1;
                    }
                } else if (TypeOrSize == -4 || TypeOrSize == -1) {
                    int vcheck = spannedsize2[xc].getStyle();
                    if ((TypeOrSize == -4 && vcheck == Typeface.BOLD || TypeOrSize == -1 && vcheck == Typeface.ITALIC)) {

                        if ((SelectionStart == sps && SelectionEnd == spe)) {
                            ind = 1;
                        } else
                            ind = -2;// difference exists

                    }
                }
                if (!(SelectionStart == sps && SelectionEnd == spe)) {
                    if (SelectionStart < spe && sps < SelectionStart && SelectionEnd>spe ) {// left spanned part within selection
                        maindiff = maindiff + (spe - SelectionStart);
                    } else if (SelectionEnd < spe && sps < SelectionEnd && SelectionStart<sps ) {//right //
                        maindiff = maindiff + (SelectionEnd - sps);
                    } else if(SelectionEnd-SelectionStart>spe-sps){
                        maindiff = maindiff + (spe - sps);// in selection
                    }else
                        maindiff = maindiff + (SelectionEnd - SelectionStart);// outside selection
                } else {
                    maindiff = (SelectionEnd - SelectionStart);
                    break;
                }

            }
        }
        int sstyle = 0, shigh = 0, sunder = 0;
        int style = 0, high = 0, under = 0, abs = 0;

        int totallength = spannedsize.length + spannedsize2.length + spannedsize3.length + spannedsize4.length;
        int actualtotallength = totallength;

        if(totallength>0){// to sort the span arrays

            if(spannedsize.length>0) {
                Arrays.sort(spannedsize, new Comparator<AbsoluteSizeSpan>() {
                    @Override
                    public int compare(AbsoluteSizeSpan o1, AbsoluteSizeSpan o2) {
                        return EditTextName.getText().getSpanStart(o1) - EditTextName.getText().getSpanStart(o2);
                    }
                });
            }
            if(spannedsize2.length>0) {
                Arrays.sort(spannedsize2, new Comparator<StyleSpan>() {
                    @Override
                    public int compare(StyleSpan o1, StyleSpan o2) {
                        return EditTextName.getText().getSpanStart(o1) - EditTextName.getText().getSpanStart(o2);
                    }
                });
            }
            if(spannedsize3.length>0) {
                Arrays.sort(spannedsize3, new Comparator<BackgroundColorSpan>() {
                    @Override
                    public int compare(BackgroundColorSpan o1, BackgroundColorSpan o2) {
                        return EditTextName.getText().getSpanStart(o1) - EditTextName.getText().getSpanStart(o2);
                    }
                });
            }
            if(spannedsize4.length>0) {
                Arrays.sort(spannedsize4, new Comparator<UnderlineSpan>() {
                    @Override
                    public int compare(UnderlineSpan o1, UnderlineSpan o2) {
                        return EditTextName.getText().getSpanStart(o1) - EditTextName.getText().getSpanStart(o2);
                    }
                });
            }

        }

        for (int spanCounter = 0; spanCounter < totallength; spanCounter++) {//for inside word
            int pst = -1;
            int pend = -1;
            int subvar = -1;
            int si = -1, ei = -1, si1 = -1, ei1 = -1, si2 = -1, ei2 = -1, si3 = -1, ei3 = -1;

            if (spannedsize.length > (spanCounter + (high + style + under))) {



                si = EditTextName.getText().getSpanStart(spannedsize[spanCounter + (high + style + under)]);
                ei = EditTextName.getText().getSpanEnd(spannedsize[spanCounter + (high + style + under)]);//endinf index


                CurrentSize = spannedsize[spanCounter + (high + style + under)].getSize();


                pst = si;
                pend = ei;


            }
            if (spannedsize4.length > sunder + (spanCounter + (high + style + abs))) {

                si3 = EditTextName.getText().getSpanStart(spannedsize4[sunder + (spanCounter + (high + style + abs))]);
                ei3 = EditTextName.getText().getSpanEnd(spannedsize4[sunder + (spanCounter + (high + style + abs))]);//endinf index

                if (ei3 < pend) {
                    pend = ei3;
                    pst = si3;
                } else if (ei3 == pend) {
                    subvar = switchspans(TypeOrSize, sstyle, shigh, sunder, spanCounter, high, under, abs, style);//for getting the right index of the span

                    totallength = totallength - 1;
                } else if (pend == -1) {
                    pst = si3;
                    pend = ei3;
                }
            }

            if (spannedsize2.length > sstyle + (spanCounter + (high + under + abs))) {


                si1 = EditTextName.getText().getSpanStart(spannedsize2[sstyle + (spanCounter + (high + under + abs))]);
                ei1 = EditTextName.getText().getSpanEnd(spannedsize2[sstyle + (spanCounter + (high + under + abs))]);//endinf index
                v = spannedsize2[sstyle + (spanCounter + (high + under + abs))].getStyle();

                if (ei1 < pend) {
                    pend = ei1;
                    pst = si1;

                } else if (ei1 == pend) {
                    subvar = switchspans(TypeOrSize, sstyle, shigh, sunder, spanCounter, high, under, abs, style);
                    totallength = totallength - 1;

                } else if (pend == -1) {
                    pst = si1;
                    pend = ei1;
                }
            }
            if (spannedsize3.length > shigh + (spanCounter + (style + under + abs))) {


                si2 = EditTextName.getText().getSpanStart(spannedsize3[shigh + (spanCounter + (style + under + abs))]);
                ei2 = EditTextName.getText().getSpanEnd(spannedsize3[shigh + (spanCounter + (style + under + abs))]);//endinf index
                colororig = spannedsize3[shigh + (spanCounter + (style + under + abs))].getBackgroundColor();


                if (ei2 < pend) {
                    pend = ei2;
                    pst = si2;
                } else if (ei2 == pend) {
                    subvar = switchspans(TypeOrSize, sstyle, shigh, sunder, spanCounter, high, under, abs, style);
                    totallength = totallength - 1;
                } else if (pend == -1) {
                    pst = si2;
                    pend = ei2;
                }
            }
            // for selecting the main index, shifting and  shifting similarities

            if (pend == ei) {
                if (subvar == -1) {
                    subvar = spanCounter + (high + style + under);
                }
                abs--;
            }
            if (pend == ei1) {
                if (pend != ei) {
                    if (subvar == -1) {
                        subvar = (sstyle) + (spanCounter + (high + under + abs));
                    }
                    style--;
                } else {//if there is a similarity
                    sstyle++;
                }
            }
            if (pend == ei2) {
                if (pend != ei && pend != ei1) {
                    if (subvar == -1) {
                        subvar = (shigh) + (spanCounter + (style + under + abs));
                    }
                    high--;
                } else {
                    shigh++;
                }
            }
            if (pend == ei3) {
                if (pend != ei && pend != ei1 && pend != ei2) {
                    if (subvar == -1) {
                        subvar = (sunder) + (spanCounter + (high + style + abs));
                    }
                    under--;
                } else {
                    sunder++;
                }
            }





            if (pst < SelectionStart && SelectionStart < pend && pend < SelectionEnd) {


                if (SelectionStart < ei && si < SelectionStart && SelectionEnd > ei) {//abs left


                    selectionl = new SpannableString(EditTextName.getText().subSequence(si, SelectionStart));
                    selectionl.removeSpan(spannedsize[subvar]);
                    separation = "l";
                    selectionl.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);




                    pst = SelectionStart;
                    si = pst;

                }
                if (SelectionStart < ei1 && si1 < SelectionStart && SelectionEnd > ei1) {//STYLE left
                    if (!separation.contains("l")) {
                        selectionl = new SpannableString(EditTextName.getText().subSequence(si1, SelectionStart));
                        selectionl.removeSpan(spannedsize2[subvar]);
                    }
                    separation = "l";
                    if (v == Typeface.ITALIC) {//fot itallic
                        selectionl.setSpan(new StyleSpan(Typeface.ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (v == Typeface.BOLD_ITALIC) {
                        selectionl.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else
                        selectionl.setSpan(new StyleSpan(Typeface.BOLD), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pst = SelectionStart;
                    si1 = pst;
                }
                if (SelectionStart < ei2 && si2 < SelectionStart && SelectionEnd > ei2) {// highlight left

                    if (!separation.contains("l")) {
                        selectionl = new SpannableString(EditTextName.getText().subSequence(si2, SelectionStart));
                        selectionl.removeSpan(spannedsize3[subvar]);
                    }
                    separation = "l";

                    selectionl.setSpan((new BackgroundColorSpan(colororig)), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    pst = SelectionStart;
                    si2 = pst;
                }
                if (SelectionStart < ei3 && si3 < SelectionStart && SelectionEnd > ei3) {// underline left

                    if (!separation.contains("l")) {
                        selectionl = new SpannableString(EditTextName.getText().subSequence(si3, SelectionStart));
                        selectionl.removeSpan(spannedsize4[subvar]);
                    }
                    separation = "l";

                    selectionl.setSpan((new UnderlineSpan()), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pst = SelectionStart;
                    si3 = pst;
                }
            } else if (SelectionEnd < pend && pst < SelectionEnd && SelectionStart < pst) {

                if (SelectionEnd < ei && si < SelectionEnd && SelectionStart < si) {//abs right
                    selectionr = new SpannableString(EditTextName.getText().subSequence(si, SelectionEnd));
                    selectionr.removeSpan(spannedsize[subvar]);
                    separation = "r";
                    selectionr.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);



                    pend = SelectionEnd;
                    ei = pend;
                }
                if (SelectionEnd < ei1 && si1 < SelectionEnd && SelectionStart < si1) {//STYLE right
                    if (!separation.contains("r")) {
                        selectionr = new SpannableString(EditTextName.getText().subSequence(si1, SelectionEnd));
                        selectionr.removeSpan(spannedsize2[subvar]);
                    }
                    separation = "r";
                    if (v == Typeface.ITALIC) {//fot itallic
                        selectionr.setSpan(new StyleSpan(Typeface.ITALIC), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else if (v == Typeface.BOLD_ITALIC) {
                        selectionr.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else
                        selectionr.setSpan(new StyleSpan(Typeface.BOLD), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pend = SelectionEnd;
                    ei1 = pend;
                }
                if (SelectionEnd < ei2 && si2 < SelectionEnd && SelectionStart < si2) {// highlight right
                    if (!separation.contains("r")) {
                        selectionr = new SpannableString(EditTextName.getText().subSequence(si2, SelectionEnd));
                        selectionr.removeSpan(spannedsize3[subvar]);
                    }
                    separation = "r";

                    selectionr.setSpan((new BackgroundColorSpan(colororig)), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pend = SelectionEnd;
                    ei2 = pend;

                }
                if (SelectionEnd < ei3 && si3 < SelectionEnd && SelectionStart < si3) {// underline right

                    if (!separation.contains("r")) {
                        selectionr = new SpannableString(EditTextName.getText().subSequence(si3, SelectionEnd));
                        selectionr.removeSpan(spannedsize4[subvar]);
                    }
                    separation = "r";

                    selectionr.setSpan((new UnderlineSpan()), 0, selectionr.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                    pend = SelectionEnd;
                    ei3 = pend;

                }

            }
            //separation implementation

            if (separation.equals("l") && spanCounter == 0 || separation.equals("r")) {

                a = (SpannedString) TextUtils.concat(a.subSequence(0, SelectionStart - selectionl.length()), selectionl, a.subSequence(SelectionStart, selectionr.toString().isEmpty() ? EditTextName.length() : SelectionEnd - selectionr.length()), selectionr, selectionr.toString().isEmpty() ? "" : a.subSequence(SelectionEnd, EditTextName.length()));

                EditTextName.setText(a);
                //separated so they can only come in right



            }


            if ((SelectionStart > si && SelectionEnd <= ei || SelectionStart >= si && SelectionEnd < ei) || (SelectionStart > si3 && SelectionEnd <= ei3 || SelectionStart >= si3 && SelectionEnd < ei3) || ((SelectionStart > si1 && SelectionEnd <= ei1 || SelectionStart >= si1 && SelectionEnd < ei1)) || ((SelectionStart > si2 && SelectionEnd <= ei2 || SelectionStart >= si2 && SelectionEnd < ei2))) {//inword


                separation = "w";

                if ((SelectionStart > si && SelectionEnd <= ei || SelectionStart >= si && SelectionEnd < ei)) {

                    selectionl = new SpannableString(EditTextName.getText().subSequence(si, SelectionStart));
                    selectionl.removeSpan(spannedsize[subvar]);

                    if (si != SelectionStart) {

                        selectionl.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (ei != SelectionEnd) {
                        selection.removeSpan(spannedsize[subvar]);
                        selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);//to be done anyways
                    }
                    a = (SpannedString) TextUtils.concat(EditTextName.getText().subSequence(0, si), selectionl, selection, EditTextName.getText().subSequence(SelectionEnd, EditTextName.length()));

                    if (SelectionStart > si) {//shifting indexis
                        pst = SelectionStart;
                    }
                    if (SelectionEnd < ei) {
                        pend = SelectionEnd;
                        ei = pend;
                    }

                }
                if ((SelectionStart > si1 && SelectionEnd <= ei1 || SelectionStart >= si1 && SelectionEnd < ei1)) {//styling


                    if (selectionl.toString().isEmpty()) {
                        selectionl = new SpannableString(EditTextName.getText().subSequence(si1, SelectionStart));
                    } else
                        selectionl.removeSpan(spannedsize2[subvar]);

                    if (si1 != SelectionStart) {
                        if (v == Typeface.ITALIC) {
                            selectionl.setSpan(new StyleSpan(Typeface.ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else if (v == Typeface.BOLD) {
                            selectionl.setSpan(new StyleSpan(Typeface.BOLD), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        } else
                            selectionl.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (ei1 != SelectionEnd) {
                        selection.removeSpan(spannedsize2[subvar]);
                        if (v == Typeface.BOLD) {

                            selection.setSpan(new StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (v == Typeface.ITALIC) {

                            selection.setSpan(new StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                        if (v == Typeface.BOLD_ITALIC) {

                            selection.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }
                    a = (SpannedString) TextUtils.concat(EditTextName.getText().subSequence(0, si1), selectionl, selection, EditTextName.getText().subSequence(SelectionEnd, EditTextName.length()));

                    if (SelectionStart > si1) {
                        pst = SelectionStart;
                    }
                    if (SelectionEnd < ei1) {
                        pend = SelectionEnd;
                        ei1 = pend;
                    }

                }
                if ((SelectionStart > si2 && SelectionEnd <= ei2 || SelectionStart >= si2 && SelectionEnd < ei2)) {//highlight


                    if (selectionl.toString().isEmpty()) {
                        selectionl = new SpannableString(EditTextName.getText().subSequence(si2, SelectionStart));
                    } else
                        selectionl.removeSpan(spannedsize3[subvar]);

                    if (si2 != SelectionStart) {
                        selectionl.setSpan((new BackgroundColorSpan(colororig)), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (ei2 != SelectionEnd) {
                        selection.removeSpan(spannedsize3[subvar]);
                        selection.setSpan((new BackgroundColorSpan(colororig)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    a = (SpannedString) TextUtils.concat(EditTextName.getText().subSequence(0, si2), selectionl, selection, EditTextName.getText().subSequence(SelectionEnd, EditTextName.length()));

                    if (SelectionStart > si2) {
                        pst = SelectionStart;
                    }
                    if (SelectionEnd < ei2) {
                        pend = SelectionEnd;
                        ei2 = pend;
                    }
                }
                if ((SelectionStart > si3 && SelectionEnd <= ei3 || SelectionStart >= si3 && SelectionEnd < ei3)) {//underline


                    if (selectionl.toString().isEmpty()) {
                        selectionl = new SpannableString(EditTextName.getText().subSequence(si3, SelectionStart));
                    } else
                        selectionl.removeSpan(spannedsize4[subvar]);

                    if (si3 != SelectionStart) {
                        selectionl.setSpan((new UnderlineSpan()), 0, selectionl.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }

                    if (ei3 != SelectionEnd) {
                        selection.removeSpan(spannedsize4[subvar]);
                        selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    a = (SpannedString) TextUtils.concat(EditTextName.getText().subSequence(0, si3), selectionl, selection, EditTextName.getText().subSequence(SelectionEnd, EditTextName.length()));

                    if (SelectionStart > si3) {
                        pst = SelectionStart;
                    }
                    if (SelectionEnd < ei3) {
                        pend = SelectionEnd;
                        ei3 = pend;
                    }
                }


                EditTextName.setText(a);


            }

            if (separation.equals("r") || separation.equals("w")) {//resettinf for word and right part
                spannedsize4 = EditTextName.getText().getSpans(SelectionStart, SelectionEnd, UnderlineSpan.class);//abs Type spans selection
                spannedsize3 = EditTextName.getText().getSpans(SelectionStart, SelectionEnd, BackgroundColorSpan.class);//abs Type spans selection
                spannedsize2 = EditTextName.getText().getSpans(SelectionStart, SelectionEnd, StyleSpan.class);//abs Type spans selection
                spannedsize = EditTextName.getText().getSpans(SelectionStart, SelectionEnd, AbsoluteSizeSpan.class);//abs Type spans selection
                if (separation.equals("r")) {//for addingg +ve or -ve
                    int newtotallength = spannedsize.length + spannedsize2.length + spannedsize3.length + spannedsize4.length;
                    if (actualtotallength != newtotallength) {
                        subvar = subvar - (actualtotallength - newtotallength);
                    }
                }
            }
            if ((TypeOrSize == 0 || TypeOrSize > 0) && pend == ei || (TypeOrSize == -4 || TypeOrSize == -1) && pend == ei1 || TypeOrSize == -2 && pend == ei2 || TypeOrSize == -3 && pend == ei3) {// if span is same type

                EditTextName.getText().removeSpan(TypeOrSize == -2 && ((maindiff == (SelectionEnd - SelectionStart) || colorchang == 1)) ? spannedsize3[subvar] : TypeOrSize == -2 ? null : TypeOrSize == -3 && maindiff == (SelectionEnd - SelectionStart) ? spannedsize4[subvar] : TypeOrSize < 0 && (maindiff == (SelectionEnd - SelectionStart) || ind == -2) ? spannedsize2[subvar] : TypeOrSize < 0 ? null : spannedsize[subvar]);//for all possabilities plus inf whole span is selected for style spans it gets removed



            }

            if (oldei >= 0 && pst - oldei > 1) {//get middle//
                if (TypeOrSize == 0) {// FOR middle empty
                    CurrentSize = (int) (EditTextName.getTextSize() + TextSizeIncrement);// input Type
                }
                selection = new SpannableString(EditTextName.getText().subSequence(oldei + 1, pst));
                selection = empty(TypeOrSize, selection,HighLightColor);
                newparts = (SpannedString) TextUtils.concat(newparts, selection);
            }
            oldei = pend - 1;
            selection = new SpannableString(EditTextName.getText().subSequence(pst, pend));

            AbsoluteSizeSpan[] spandfnedsize = EditTextName.getText().getSpans(0, EditTextName.length(), AbsoluteSizeSpan.class);

            if ((ind > -1 || (TypeOrSize == -3 ? pend != ei3 : TypeOrSize == -2 ? pend != ei2 : TypeOrSize < 0 && pend != ei1))) {//if no spans in same type


                if (ind > -1) {
                    selection.removeSpan(spannedsize2[subvar]);//remove the prev abs Type span
                    EditTextName.getText().removeSpan(spannedsize2[subvar]);
                    selection.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (TypeOrSize == -1) {

                    selection.setSpan(new StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);


                } else if (TypeOrSize == -4) {


                    selection.setSpan(new StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (TypeOrSize == -3) {//underlinspan
                    selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (TypeOrSize == -2) {//underlinspan
                    selection.setSpan((new BackgroundColorSpan(HighLightColor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }


            } else if ((TypeOrSize == -4 || TypeOrSize == -1) && (ind == -2 || maindiff == (SelectionEnd - SelectionStart) && spannedsize2[subvar].getStyle() == Typeface.BOLD_ITALIC)) {
                if (TypeOrSize == -1 && spannedsize2[subvar].getStyle() == Typeface.BOLD_ITALIC) {//bold
                    if (ind == -2) {

                        selection.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else
                        selection.setSpan(new StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (TypeOrSize == -4 && spannedsize2[subvar].getStyle() == Typeface.BOLD_ITALIC) {//bold
                    if (ind == -2) {
                        selection.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else
                        selection.setSpan(new StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if ((TypeOrSize == -1 && spannedsize2[subvar].getStyle() == Typeface.ITALIC) || (TypeOrSize == -4 && spannedsize2[subvar].getStyle() == Typeface.BOLD)) {//bold

                    selection.setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if ((TypeOrSize == -1 && spannedsize2[subvar].getStyle() == Typeface.BOLD && ind == -2)) {
                    selection.setSpan(new StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if ((TypeOrSize == -4 && spannedsize2[subvar].getStyle() == Typeface.ITALIC && ind == -2)) {
                    selection.setSpan(new StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            } else if (TypeOrSize == -2 && colorchang == 1) {//SPANS HIGHLIGHT AND NON HIGHLIGHT SPANS
                selection.setSpan((new BackgroundColorSpan(HighLightColor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (TypeOrSize == 0 || TypeOrSize > 0) {//SPANS ABS SIZE,TOTAL SIZE AND NO SIZE SPANS
                switch (TypeOrSize) {
                    case 0:
                        if (pend == ei) {
                            if (spannedsize[subvar].getSize() + TextSizeIncrement < 0) {
                                CurrentSize = 1;
                            } else
                                CurrentSize = spannedsize[subvar].getSize() + TextSizeIncrement;//adding or sub
                        } else if (EditTextName.getTextSize() + TextSizeIncrement < 0) {
                            CurrentSize = 1;
                        } else
                            CurrentSize = (int) (EditTextName.getTextSize() + TextSizeIncrement);// input Type
                        break;
                    default:
                        CurrentSize = TypeOrSize;

                        break;
                }
                selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            if (TypeOrSize == 0) {// FOR middle empty
                CurrentSize = (int) (EditTextName.getTextSize() + TextSizeIncrement);// input Type
            }


            newparts = (SpannedString) TextUtils.concat(newparts, selection);


            if (spanCounter == 0 && pst > 0 && SelectionStart < pst) {// at start of sentence
                selection = new SpannableString(EditTextName.getText().subSequence(SelectionStart, pst));

                selection = empty(TypeOrSize, selection,HighLightColor);
                newparts = (SpannedString) TextUtils.concat(selection, newparts);
            }
            if (spanCounter == totallength - 1 && SelectionEnd > pend) {//at end of sentence

                selection = new SpannableString(EditTextName.getText().subSequence(pend, SelectionEnd));
                pend = SelectionEnd;//to catch last char
                selection = empty(TypeOrSize, selection,HighLightColor);
                newparts = (SpannedString) TextUtils.concat(newparts, selection);
            }


            a = (SpannedString) TextUtils.concat(EditTextName.getText().subSequence(0, SelectionStart), newparts, EditTextName.getText().subSequence(pend, EditTextName.length())
            );


        }
        if (totallength == 0) {




            if (TypeOrSize == -1) {

                selection.setSpan(new StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (TypeOrSize == -4) {

                selection.setSpan(new StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (TypeOrSize == -3) {//underlinspan
                selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else if (TypeOrSize == -2) {
                selection.setSpan((new BackgroundColorSpan(HighLightColor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                if (TypeOrSize == 0) {
                    if (EditTextName.getTextSize() + TextSizeIncrement < 0) {
                        CurrentSize = 1;
                    } else
                        CurrentSize = (int) (EditTextName.getTextSize() + TextSizeIncrement);
                } else
                    CurrentSize = TypeOrSize;


                selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            a = (SpannedString) TextUtils.concat(EditTextName.getText().subSequence(0, SelectionStart), selection, EditTextName.getText().subSequence(SelectionEnd, EditTextName.length()));
        }

        EditTextName.setText(a);

        EditTextName.setSelection(st, end);


    }
    private static SpannableString empty(int size, SpannableString selection, int hicolor ) {//for empty parts
        switch (size) {
            case -1:
                selection.setSpan(new StyleSpan(Typeface.BOLD), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case -3:
                selection.setSpan((new UnderlineSpan()), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case -2:
                selection.setSpan((new BackgroundColorSpan(hicolor)), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            case -4:
                selection.setSpan(new StyleSpan(Typeface.ITALIC), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                break;
            default:
                selection.setSpan(new AbsoluteSizeSpan(CurrentSize), 0, selection.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        return selection;
    }

    private static int switchspans(int size, int sstyle, int shigh, int sunder, int sp, int high, int under, int abs, int style) {

        int subvar;
        switch (size) {
            case -1:
            case -4:
                subvar = (sstyle) + (sp + (high + under + abs));
                break;
            case -3:
                subvar = (sunder) + (sp + (high + style + abs));
                break;
            case -2:
                subvar = (shigh) + (sp + (style + under + abs));
                break;

            default:
                subvar = sp + (high + style + under);
                break;
        }

        return subvar;
    }


    // returns the starting and ending indexes of a word at the edge or within a cursor
    public static  int[] autoselection(EditText EditTextName) {


        int selectionStart = EditTextName.getSelectionStart();
        int selectionEnd = EditTextName.getSelectionEnd();


        int lastIndex;
        int indexOf;
        int[] sel = new int[2];
        if (selectionStart < selectionEnd) {//choice is if there is a selection but I want that of the word
            sel[0] = selectionStart;
            sel[1] = selectionEnd;
            return sel;
        }
        lastIndex = EditTextName.getText().toString().lastIndexOf(" ", selectionStart - 1);
        int lastIndex2 = EditTextName.getText().toString().lastIndexOf('\n', selectionStart - 1);
        if (lastIndex == -1 || lastIndex2 > lastIndex) {
            lastIndex = lastIndex2;

        }
        indexOf = EditTextName.getText().toString().indexOf(" ", lastIndex + 1);
        int indexOf2 = EditTextName.getText().toString().indexOf('\n', lastIndex + 1);
        if (indexOf == -1 || indexOf2 < indexOf && indexOf2 != -1) {
            indexOf = indexOf2;
        }


        sel[0] = lastIndex + 1;
        sel[1] = indexOf == -1 ? EditTextName.getText().toString().length() : indexOf;
        return sel;

    }



}
