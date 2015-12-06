package baegmon.sectionedlistadapter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;


public class MainActivity extends ActionBarActivity {

    private ListView listView;

    private SectionListAdapter adapter;

    HashMap<String, ArrayList<String>> dataMap = new HashMap<>();
    ArrayList<String> headerList = new ArrayList<>();
    ArrayList<String> aList =  new ArrayList<>();
    ArrayList<String> bList =  new ArrayList<>();
    ArrayList<String> cList =  new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list_view);

        createData();

        adapter = new SectionListAdapter(this);

        for(int i = 0; i < 3; i++){
            if(adapter != null){
                adapter.addSection(
                        headerList.get(i),
                        dataMap.get(headerList.get(i))
                );
            }
        }

        listView.setAdapter(adapter);

        EditText inputSearch = (EditText) findViewById(R.id.search_text);

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void createData(){
        dataMap = new HashMap<>();
        headerList = new ArrayList<>();
        aList =  new ArrayList<>();
        bList =  new ArrayList<>();
        cList =  new ArrayList<>();

        headerList.add("A");
        headerList.add("B");
        headerList.add("C");

        aList.add("Aeroplane");
        aList.add("Abcd");
        aList.add("Apple");

        bList.add("Broken");
        bList.add("Banana");
        bList.add("Buyer");

        cList.add("Canopy");
        cList.add("Crayon");
        cList.add("Crust");
        for(int i = 0; i < 3; i++){
            if(i == 0){
                dataMap.put(headerList.get(i), aList);
            } else if ( i == 1){
                dataMap.put(headerList.get(i), bList);
            } else {
                dataMap.put(headerList.get(i), cList);
            }
        }

        for(String key : dataMap.keySet()){
            Collections.sort(dataMap.get(key));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu;  this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
