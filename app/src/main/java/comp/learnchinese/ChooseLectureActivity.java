package comp.learnchinese;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChooseLectureActivity extends AppCompatActivity {

//    CheckBox cbLecture1; CheckBox cbLecture2;  CheckBox cbLecture3;  CheckBox cbLecture4;
//    CheckBox cbLecture5; CheckBox cbLecture6;  CheckBox cbLecture7;  CheckBox cbLecture8;
//    CheckBox cbLecture9; CheckBox cbLecture10; CheckBox cbLecture11; CheckBox cbLecture12;
//    CheckBox cbLecture13; CheckBox cbLecture14; CheckBox cbLecture15; CheckBox cbLecture16;
//    CheckBox cbLecture17; CheckBox cbLecture18;  CheckBox cbLecture19;  CheckBox cbLecture20;
//    CheckBox cbLecture21; CheckBox cbLecture22;  CheckBox cbLecture23;  CheckBox cbLecture24;
//    CheckBox cbLecture25; CheckBox cbLecture26; CheckBox cbLecture27; CheckBox cbLecture28;
//    CheckBox cbLecture29; CheckBox cbLecture30;
    CheckBox[] lectureArray = new CheckBox[30];
    boolean[] isNeedToShowLecture = new boolean[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_lecture);
        ShowCharacter.deleteMode = false;

        lectureArray[0] = (CheckBox)findViewById(R.id.checkBox1); lectureArray[1] = (CheckBox)findViewById(R.id.checkBox2);
        lectureArray[2] = (CheckBox)findViewById(R.id.checkBox3); lectureArray[3] = (CheckBox)findViewById(R.id.checkBox4);
        lectureArray[4] = (CheckBox)findViewById(R.id.checkBox5); lectureArray[5] = (CheckBox)findViewById(R.id.checkBox6);
        lectureArray[6] = (CheckBox)findViewById(R.id.checkBox7); lectureArray[7] = (CheckBox)findViewById(R.id.checkBox8);
        lectureArray[8] = (CheckBox)findViewById(R.id.checkBox9); lectureArray[9] = (CheckBox)findViewById(R.id.checkBox10);
        lectureArray[10] = (CheckBox)findViewById(R.id.checkBox11); lectureArray[11] = (CheckBox)findViewById(R.id.checkBox12);
        lectureArray[12] = (CheckBox)findViewById(R.id.checkBox13); lectureArray[13] = (CheckBox)findViewById(R.id.checkBox14);
        lectureArray[14] = (CheckBox)findViewById(R.id.checkBox15); lectureArray[15] = (CheckBox)findViewById(R.id.checkBox16);

        lectureArray[16] = (CheckBox)findViewById(R.id.checkBox17); lectureArray[17] = (CheckBox)findViewById(R.id.checkBox18);
        lectureArray[18] = (CheckBox)findViewById(R.id.checkBox29); lectureArray[19] = (CheckBox)findViewById(R.id.checkBox20);
        lectureArray[20] = (CheckBox)findViewById(R.id.checkBox21); lectureArray[21] = (CheckBox)findViewById(R.id.checkBox22);
        lectureArray[22] = (CheckBox)findViewById(R.id.checkBox23); lectureArray[23] = (CheckBox)findViewById(R.id.checkBox24);
        lectureArray[24] = (CheckBox)findViewById(R.id.checkBox25); lectureArray[25] = (CheckBox)findViewById(R.id.checkBox26);
        lectureArray[26] = (CheckBox)findViewById(R.id.checkBox27); lectureArray[27] = (CheckBox)findViewById(R.id.checkBox28);
        lectureArray[28] = (CheckBox)findViewById(R.id.checkBox29); lectureArray[29] = (CheckBox)findViewById(R.id.checkBox30);
    }

    public void btnBeginClick(View view) {

        boolean choosedFlag = false;
        for (int i = 0; i < lectureArray.length; i++) {
            if (lectureArray[i].isChecked() == true) {
                isNeedToShowLecture[i] = true;
                choosedFlag = true;
            } else
                isNeedToShowLecture[i] = false;
        }
        if(!choosedFlag){
            Toast toast = Toast.makeText(this, R.string.choose_at_least_one_lecture, Toast.LENGTH_LONG);
            toast.show();
        }
        else {
            Intent intent = new Intent(this, ShowCharacter.class);
            intent.putExtra("lectures to show", isNeedToShowLecture);
            startActivity(intent);
        }


    }

    public void btnBackClick(View view) {
        finish();
    }


}
