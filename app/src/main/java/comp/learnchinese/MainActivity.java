package comp.learnchinese;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity  {
    static final String TAG = "myTag";
    EditText search;
    public static LinkedList<Character> DBCharacters = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        search = findViewById(R.id.etSearch);
        DBCharacters = deserialization();
        if(DBCharacters.size() == 0) {
            try {
                createDB(this);
                DBCharacters = deserialization();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        ShowCharacter.deleteMode = false;
    }

    public void btnSearchClick(View view) {
        Character target = null;
        String text = search.getText().toString();
        if(text.compareTo("") == 0) {
            Toast toast = Toast.makeText(this, R.string.write_something, Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            text = text.toLowerCase();
            String space = " ";
            char lastChar;
            if(text != "") {
                lastChar = text.charAt(text.length() - 1);
                if(space.equals(String.valueOf(lastChar))) {
                    text = text.substring(0, text.length()-1);
                }
            }
            target = findCaracter(text);
            if(target != null) {
                showOneCharacter(target);
            }
            else {
                int [] searchResultId = findSomeCharacters(text);
                switch (searchResultId.length) {
                    case 0: {
                        searchResultId = findCharactersByPinyinWithoutDiakritics(text);
                        switch (searchResultId.length) {
                            case 0: {
                                searchResultId = findCharactersByTranslate(text);
                                switch (searchResultId.length) {
                                    case 0: {
                                        search.setText("");
                                        search.setHintTextColor(Color.RED);
                                        search.setHint(R.string.nothing_found);
                                        break;
                                    }
                                    case 1: {
                                        showOneCharacter(searchResultId[0]);
                                        break;
                                    }
                                    default: {
                                        showMoreCharacters(searchResultId);
                                        break;
                                    }
                                }
                                break;
                            }
                            case 1: {
                                showOneCharacter(searchResultId[0]);
                                break;
                            }
                            default: {
                                showMoreCharacters(searchResultId);
                                break;
                            }
                        }
                        break;
                    }
                    case 1: {
                        showOneCharacter(searchResultId[0]);
                        break;
                    }
                    default: {
                        showMoreCharacters(searchResultId);
                        break;
                    }
                }
            }
        }
    }

    // методы для показа одного или нескольких иероглифов

    void showOneCharacter(Character character) {
        ShowCharacter.deleteMode = false;
        Intent intent = new Intent(this, ShowCharacter.class);
        intent.putExtra("Character to show", character.Character);
        startActivity(intent);
    }

    void showOneCharacter(int id) {
        Character character = null;
        for(int i=0; i<DBCharacters.size(); i++) {
            if(DBCharacters.get(i).getID() == id) {
                character = DBCharacters.get(i);
            }
        }
        ShowCharacter.deleteMode = false;
        Intent intent = new Intent(this, ShowCharacter.class);
        intent.putExtra("Character to show", character.Character);
        startActivity(intent);
    }

    void showMoreCharacters(int[] charactersId) {
        Intent intent = new Intent(getApplicationContext(), ListOfCharakters.class);
        intent.putExtra("characters id to show", charactersId);
        startActivity(intent);
    }


    // методы для разных поисков

    int[] findCharactersByPinyinWithoutDiakritics(String pinyon) {
        LinkedList<Integer> outputRaw = new LinkedList<>();
        Character templ;
        for(int i=0; i<DBCharacters.size(); i++) {
            templ = DBCharacters.get(i);
            if(templ.PinyinWithoutDiakritics.contains(pinyon))
                outputRaw.add(templ.getID());
        }

        int[] output = new int[outputRaw.size()];
        for(int i=0 ; i<output.length; i++) {
            output[i] = outputRaw.get(i);
        }

        return output;
    }

    int[] findCharactersByTranslate(String translate) {
        LinkedList<Integer> outputRaw = new LinkedList<>();
        Character templ;
        for(int i=0; i<DBCharacters.size(); i++) {
            templ = DBCharacters.get(i);
            if(templ.Translation.contains(translate))
                outputRaw.add(templ.getID());
        }

        int[] output = new int[outputRaw.size()];
        for(int i=0 ; i<output.length; i++) {
            output[i] = outputRaw.get(i);
        }

        return output;
    }

    int[] findSomeCharacters(String character) {
        LinkedList<Integer> outputRaw = new LinkedList<>();
        Character templ;
        for(int i=0; i<DBCharacters.size(); i++) {
            templ = DBCharacters.get(i);
            if(templ.Character.contains(character))
                outputRaw.add(templ.getID());
        }

        int[] output = new int[outputRaw.size()];
        for(int i=0 ; i<output.length; i++) {
            output[i] = outputRaw.get(i);
        }

        return output;
    }

    Character findCaracter(String character) {
        Character result = null;
        for(int i=0; i<DBCharacters.size(); i++) {
            if(DBCharacters.get(i).Character.equals(character)) {
                return DBCharacters.get(i);
            }
        }
        return result;
    }

    public void buttonStudyClick(View view) {
        Intent intent = new Intent(this, ChooseLectureActivity.class);
        startActivity(intent);
    }

    public void buttonDBClick(View view) {

        try {
            Intent intent = new Intent(this, DBwithKeyboardActivity.class);
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG);
        }
    }

    public void btnDelateDBClick(View view) {

//        Toast.makeText(this, "Temporarly disabled function", Toast.LENGTH_LONG);
//        LinkedList<Character> templ = new LinkedList<>();
//        try {
//            String path = this.getFilesDir()+"/DB.out";
//            FileOutputStream fileOut = new FileOutputStream(path);
//            ObjectOutputStream out = new ObjectOutputStream(fileOut);
//            out.writeObject(templ);
//            out.close();
//            fileOut.close();
//            Toast toast = Toast.makeText(this, "The DB was successfully deleted.", Toast.LENGTH_SHORT);
//            toast.show();
//            DBCharacters = templ;
//
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        Intent intent = new Intent(this, ListOfCharakters.class);
        startActivity(intent);
    }



    // работа с базой данных

    public static boolean isDBContains(Character templ) {
        boolean isNotUnique = false;
        for(int i=0; i<MainActivity.DBCharacters.size(); i++) {
            if(compareCharacters(templ, MainActivity.DBCharacters.get(i)))
                isNotUnique = true;
        }
        return isNotUnique;
    }

    static boolean compareCharacters(Character input1, Character input2) {
        boolean isNotUnique = false;    //врзвращает true если элементы имеют одинаковый китайский символ
        if(input1.Character.equals(input2.Character))
            isNotUnique = true;
        return  isNotUnique;
    }

    public void createDB(Activity activity) throws UnsupportedEncodingException {
        String input = null;
        boolean lectureFlag = false;
        Resources r = activity.getResources();
        InputStream is = r.openRawResource(R.raw.lectures);
        BufferedReader imBR = new BufferedReader(new InputStreamReader(is,"UTF-16LE"));
        try {
            input = convertStreamToString(imBR);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String [] stringArray = input.split("\n");
        Character templ = new Character();
        char [] lectureNumber ;
        String [] words;
        String trueLectureNumber = "";
        boolean isFirstLectureNumber = true;
        for(int i=0; i<stringArray.length; i++) {
            if(stringArray[i].contains("null")) {
                stringArray[i] = stringArray[i].replace("null","");
            }
            if(stringArray[i].length()<3) {
                lectureNumber = stringArray[i].toCharArray();
                if(isFirstLectureNumber) {
                    trueLectureNumber = String.valueOf(lectureNumber[1]);
                    isFirstLectureNumber = false;
                }
                else {
                    try {
                        if(lectureNumber.length == 2) {
                            trueLectureNumber = String.valueOf(lectureNumber[0]);
                            trueLectureNumber += String.valueOf(lectureNumber[1]);
                        }
                        else {
                            trueLectureNumber = String.valueOf(lectureNumber[0]);
                        }

                    }catch (Exception e) {}
                }
            }
            else {
                words = stringArray[i].split("\\.");
                for(int j=0; j<words.length; j++) {
                    switch (j)  {
                        case 0: {
                            templ.Character = words[j];
                            break;
                        }
                        case 1: {
                            templ.Pinyin = words[j];
                            templ.setPinyinWithoutDiakritics();
                            break;
                        }
                        case 2: {
                            templ.Translation = words[j];
                            break;
                        }
                    }
                }
            }

            if(templ.Translation != null) {
                String ne;
                templ.Lecture = trueLectureNumber;
                Log.d("Character", " with id created"+String.valueOf(templ.getID()));
                if(templ.getID() == 300)
                    ne = "";
                if(!isDBContains(templ))
                    DBCharacters.addLast(templ);
                templ = new Character();
            }
        }
        serializace(activity);
    }

    String convertStreamToString(BufferedReader inBR) throws IOException {

        String endFlag;
        String output = null;
        while((endFlag=inBR.readLine()) !=null) {
            output += endFlag+"\n";
            output += inBR.readLine()+"\n";
        }
        return output;
    }

    public LinkedList<Character> deserialization() {
        LinkedList<Character> output = new LinkedList<>();
        try {
            String path = this.getFilesDir()+"/DB.out";
            FileInputStream fileIn = new FileInputStream(path);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            output = (LinkedList<Character>) in.readObject();
            in.close();
            fileIn.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static void serializace(Activity activity) {
        try {
            String path = activity.getFilesDir()+"/DB.out";
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(MainActivity.DBCharacters);
            out.close();
            fileOut.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteCharacter(int id, Activity activity) {
        for(int i=0; i<DBCharacters.size(); i++) {
            if(id == DBCharacters.get(i).getID()) {
                DBCharacters.remove(i);
//               Resources r = activity.getResources();
                serializace(activity);
            }
        }
    }

    public void deleteDatabase(Activity activity) {
        LinkedList<Character> templ = new LinkedList<>();
        try {
            String path = activity.getFilesDir()+"/DB.out";
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(templ);
            out.close();
            fileOut.close();
//            Toast toast = Toast.makeText(this, "The DB was successfully deleted.", Toast.LENGTH_SHORT);
//            toast.show();
            DBCharacters = templ;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
