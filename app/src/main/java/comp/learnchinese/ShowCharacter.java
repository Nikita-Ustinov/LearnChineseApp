package comp.learnchinese;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.LinkedList;

public class ShowCharacter extends AppCompatActivity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {
//    LinkedList<Character> DB;
    LinkedList<Character> DBtoShow = new LinkedList<>();
    int count = 0;  //item number in DB
    TextView tvTranslationShow;
    TextView tvPinyinShow;
    TextView tvCharacter;
    TextView tvCountCharacter;
    ConstraintLayout fieldManualInfo;
    boolean[] showLecture;
    String characterString = null;
    int [] charactersIdList;
    public static boolean deleteMode = false;

    //для детектированиея жестов
    private GestureDetectorCompat gestureDetector;
    private static final int SWIPE_DISTANCE_THRESHOLD = 100;
    private static final int SWIPE_VELOCITY_THRESHOLD = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_character);
        tvPinyinShow = (TextView) findViewById(R.id.tvPinyinShow);
        tvTranslationShow = (TextView) findViewById(R.id.tvTranslationShow);
        tvCharacter = (TextView) findViewById(R.id.tvCharacter);
        tvCountCharacter = (TextView) findViewById(R.id.tvCountCharacter);
        fieldManualInfo = findViewById(R.id.fieldManualInfo);

        Intent intent = getIntent();
        showLecture = intent.getBooleanArrayExtra("lectures to show");
        characterString = intent.getStringExtra("Character to show");
        charactersIdList = intent.getIntArrayExtra("characters id to show");

        if(deleteMode) {
            Button deleteButton = findViewById(R.id.btnDelete);
            deleteButton.setVisibility(View.VISIBLE);
        }


        if (characterString != null) {
            Character character = findCaracter(characterString);
            DBtoShow.addLast((Character)character);
        }
        else if (charactersIdList == null) {
            for (int i = 0; i < showLecture.length; i++) {
                if (showLecture[i] == true) {
                    for (int j = 0; j < MainActivity.DBCharacters.size(); j++) {
                        if (Integer.parseInt(MainActivity.DBCharacters.get(j).Lecture) == (i + 1)) {
                            DBtoShow.addLast(MainActivity.DBCharacters.get(j));
                        }
                    }
                }
            }
        }
        else {
            for(int j=0; j<charactersIdList.length; j++) {
                int templID = charactersIdList[j];
                for (int i = 0; i < MainActivity.DBCharacters.size(); i++) {
                    if(MainActivity.DBCharacters.get(i).getID() == templID) {
                        DBtoShow.addLast(MainActivity.DBCharacters.get(i));
                        break;
                    }
                }
            }
        }
        if (DBtoShow.size() != 0 && charactersIdList == null) {
            DBtoShow = shuffleList(DBtoShow);
            tvCharacter.setText(DBtoShow.get(0).Character);
            tvCountCharacter.setText(1 + "/" + DBtoShow.size());
        } else if (charactersIdList == null){
            Toast.makeText(this, R.string.you_have_no_any_characters_to_show, Toast.LENGTH_LONG);
            try {
                wait(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            finish();
        }
        else {
            tvCharacter.setText(DBtoShow.get(0).Character);
            tvCountCharacter.setText(1 + "/" + DBtoShow.size());
        }
        gestureDetector = new GestureDetectorCompat(this, this);
        gestureDetector.setOnDoubleTapListener(this);
    }

    public void nextCharacter() {
        count++;
        if(count > DBtoShow.size()-1) {
            Toast toast = Toast.makeText(this, R.string.end_of_dataset, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0 ,0);
            toast.show();
            finish();
        }
        else {
            tvPinyinShow.setText("");
            tvTranslationShow.setText("");
            tvCharacter.setText(DBtoShow.get(count).Character);
            tvCountCharacter.setText( (count+1) +"/" + DBtoShow.size());
        }
    }

    public void previosCharakter() {
        count--;
        if(count < 0) {
            count++;
            Toast toast = Toast.makeText(this, R.string.left_swipe_to_show_next_character, Toast.LENGTH_SHORT);
            toast.show();
        }
        else {
            tvPinyinShow.setText("");
            tvTranslationShow.setText("");
            tvCharacter.setText(DBtoShow.get(count).Character);
            tvCountCharacter.setText( (count+1) +"/" + DBtoShow.size());
        }
    }

    public void showTranslate() {
        tvPinyinShow.setText(DBtoShow.get(count).Pinyin.toString());
        tvTranslationShow.setText(DBtoShow.get(count).Translation.toString());
    }

    LinkedList<Character> shuffleList(LinkedList<Character> list) {
        int elementNumber;
        Character templ;
        for(int i=0; i<DBtoShow.size()-1; i++) {
            elementNumber = (int)(Math.random()*(DBtoShow.size()-i-1));
            templ = list.get(DBtoShow.size()-i-1);
            list.set(DBtoShow.size()-i-1, list.get(elementNumber));
            list.set(elementNumber, templ);
        }
        return list;
    }

    Character findCaracter(String character) {
        Character result = null;
        for(int i=0; i<MainActivity.DBCharacters.size(); i++) {
            if(MainActivity.DBCharacters.get(i).Character.equals(character)) {
                return MainActivity.DBCharacters.get(i);
            }
        }
        return result;
    }

    public void buttonDBClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.delete_title);
        builder.setMessage(R.string.delete_messege);
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Отпускает диалоговое окно
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void btnDeleteCharacter(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_messege);
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Отпускает диалоговое окно
                MainActivity.deleteCharacter(DBtoShow.get(count).getID(), ShowCharacter.this);
                Intent intent = new Intent(getApplicationContext(), ListOfCharakters.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() { // Кнопка отмена
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // Отпускает диалоговое окно
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void btnManualClick(View view) {
        if(fieldManualInfo.getVisibility() == View.VISIBLE) {
            fieldManualInfo.setVisibility(View.INVISIBLE);
        }
        else {
            fieldManualInfo.setVisibility(View.VISIBLE);
        }
    }

    public void btnBackClick(View view) {
        finish();
    }




    // работа с жестами
    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        showTranslate();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float distanceX = e2.getX() - e1.getX();
        float distanceY = e2.getY() - e1.getY();
        if (Math.abs(distanceX) > Math.abs(distanceY) && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
            if (distanceX > 0)
                onSwipeRight();
            else
                onSwipeLeft();
            return true;
        }
        return false;
    }

    public void onSwipeLeft() {
        nextCharacter();
    }

    public void onSwipeRight() {
        previosCharakter();
    }


}
