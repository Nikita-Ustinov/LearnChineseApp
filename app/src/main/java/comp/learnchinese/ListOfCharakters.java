package comp.learnchinese;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ListOfCharakters extends ListActivity {

    List<Character> charakterList = new LinkedList<>();
    int [] charactersIdList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        ShowCharacter.deleteMode = false;
        charactersIdList = intent.getIntArrayExtra("characters id to show");
        if(charactersIdList != null) {
            for(int j=0; j<charactersIdList.length; j++) {
                int templID = charactersIdList[j];
                for (int i = 0; i < MainActivity.DBCharacters.size(); i++) {
                    if (MainActivity.DBCharacters.get(i).getID() == templID) {
                        charakterList.add(MainActivity.DBCharacters.get(i));
                        break;
                    }
                }
            }
        }
        else {
            charakterList = copyList(MainActivity.DBCharacters);
        }

        setListAdapter(getListAdapter(charakterList));
        ListView listView = getListView();
        listView.setTextFilterEnabled(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShowCharacter.deleteMode = false;
                Character templ = charakterList.get(position);
                charakterList.remove(templ);
                int [] idList = createList(templ.getID());
                charakterList.add(templ);
                Intent intent = new Intent(getApplicationContext(), ShowCharacter.class);
                intent.putExtra("characters id to show", idList);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                ShowCharacter.deleteMode = true;
                Character templ = charakterList.get(position);
                charakterList.remove(templ);
                int [] idList = createList(templ.getID());
                charakterList.add(templ);
                Intent intent = new Intent(getApplicationContext(), ShowCharacter.class);
                intent.putExtra("characters id to show", idList);
                startActivity(intent);
                return true;
            }
        });
    }



// методы для отображения списка на экране
    private ListAdapter getListAdapter(List<Character> charakterList) {
        String[] atributsNames = {"Character", "Pinyin", "Translate"};
        int[] idAtributu = {R.id.listItemCharakter, R.id.listItemPinyin, R.id.listItemCTranslate};
        SimpleAdapter adapter = new SimpleAdapter(this, getListAdapterData(charakterList), R.layout.list_item, atributsNames, idAtributu);
        return adapter;
    }

    private List<Map<String, ?>> getListAdapterData(List<Character> charakterList) {
        List<Map<String, ?>> list = new ArrayList<>(charakterList.size());
        for(Character character : charakterList) {
            Map<String, String> MapItems = new HashMap<>();
            MapItems.put("Character", character.getCharacter());
            MapItems.put("Pinyin", character.getPinyin());
            MapItems.put("Translate", character.getTranslait());
            list.add(MapItems);
        }
        return list;
    }

    private int[] createList(int id) {
        int [] output = new int[charakterList.size()+1];
        output[0] = id;
        for(int i=1; i<charakterList.size(); i++) {
                output[i] = charakterList.get(i-1).getID();
        }
        return  output;
    }

    private LinkedList<Character> copyList(LinkedList<Character> input) {
        LinkedList<Character> output = new LinkedList<>();
        for(int i=0; i<input.size(); i++) {
            output.add(input.get(i));
        }
        return output;
    }
}
