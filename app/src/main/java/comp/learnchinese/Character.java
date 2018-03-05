package comp.learnchinese;


import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.Normalizer;
import java.util.regex.Pattern;

public class Character implements Serializable {
    public String Pinyin;
    public String PinyinWithoutDiakritics;
    public String Character;
    public String Translation;
    public String Lecture;
    private int ID;
    private static int MaxId = 0;
    public static final Pattern DIACRITICS_AND_FRIENDS = Pattern.compile("[\\p{InCombiningDiacriticalMarks}]+");

    public Character() {
        if(MaxId == 0){
            MaxId = 1;
            ID = 1;
        }
        else {
            ID = MaxId + 1;
            MaxId = ID;
        }
    }

    public Character(String pinyin, String character, String translation, String lecture) {
        if(lecture == "") {
            lecture = "0";
        }
        if(character == "") {
            Character = "";
        }
        if(pinyin == "") {
            pinyin = "";
        }
        Pinyin = pinyin;
        PinyinWithoutDiakritics = stripDiacritics(pinyin);
        Character = character;
        Translation = translation;
        Lecture = lecture;
        if(MaxId == 0){
            MaxId = 1;
            ID = 1;
        }
        else {
            ID = MaxId + 1;
            MaxId = ID;
        }
    }

    public String getPinyin() {
        return Pinyin;
    }

    public String getCharacter() {
        return Character;
    }

    public String getTranslait() {
        return Translation;
    }

    public int getID() {
        return ID;
    }


    private static String stripDiacritics(String str) {
        str = Normalizer.normalize(str, Normalizer.Form.NFD);
        str = DIACRITICS_AND_FRIENDS.matcher(str).replaceAll("");
        return str;
    }

    public void setPinyinWithoutDiakritics() {
        PinyinWithoutDiakritics = stripDiacritics(Pinyin);
    }


}
