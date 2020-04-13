package com.example.mydictionary;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    SearchView search;
    static DatabaseHelper myDbHelper;
    static boolean databaseOpened = false;
    SimpleCursorAdapter simpleCursorAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final SearchView search = (SearchView) findViewById(R.id.search_view);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search.setIconified(false);

            }
        });

        myDbHelper = new DatabaseHelper(this);
        if (myDbHelper.checkDataBase()){
            openDatabase();

        } else {
            LoadDatabaseAsync task = new LoadDatabaseAsync(MainActivity.this);
            task.execute();

        }

        final String[] from = new String[]{"en_word"};
        final int[] to = new int[] {R.id.suggestion_text};

        final SimpleCursorAdapter suggestionAdapter = new SimpleCursorAdapter(MainActivity.this,
                R.layout.suggestion_row, null, from, to, 0) {

            @Override
            public void changeCursor(Cursor cursor) {
                super.swapCursor(cursor);

            }
        };
        search.setSuggestionsAdapter(suggestionAdapter);

        search.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionClick(int position) {
                CursorAdapter ca = search.getSuggestionsAdapter();
                Cursor cursor = ca.getCursor();
                cursor.moveToPosition(position);
                String clicked_word = cursor.getString(cursor.getColumnIndex("en_word"));
                search.setQuery(clicked_word,false);
                //search.setQuery("",false);
                search.clearFocus();
                search.setFocusable(false);

                Intent intent = new Intent(MainActivity.this, WordMeaningActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("en_word", clicked_word);
                intent.putExtras(bundle);
                startActivity(intent);

                return true;
            }

            @Override
            public boolean onSuggestionSelect(int position) {
                return true;
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String text = search.getQuery().toString();
                Cursor c = myDbHelper.getMeaning(text);

                if (c.getCount()==0){
                    search.setQuery("",false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialogTheme);
                    builder.setTitle("Word Not Found");
                    builder.setMessage("Please Search Again");

                    String positiveText = getString(android.R.string.ok);
                    builder.setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    String negativeText = getString(android.R.string.cancel);
                    builder.setPositiveButton(negativeText, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            search.clearFocus();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }else{

                    search.clearFocus();
                    search.setFocusable(false);

                    Intent intent = new Intent(MainActivity.this,WordMeaningActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("en_word",text);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String s) {
                search.setIconifiedByDefault(false);
                Cursor cursorSuggestion = myDbHelper.getSuggestions(s);
                suggestionAdapter.changeCursor(cursorSuggestion);
                return false;
            }
        });
    }

    protected static void openDatabase(){
        try{
            myDbHelper.openDataBase();
            databaseOpened=true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu_main; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Handle action bar item clicks here. the action bar will
        //automatically handle clicks on the home/up button
        int id = item.getItemId();
        if (id == R.id.action_settings){
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        if (id == R.id.action_exit){
            System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
