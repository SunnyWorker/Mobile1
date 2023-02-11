package com.example.labwork1;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.labwork1.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;
    private MenuItem search;
    private MenuItem sort1;
    private MenuItem sort2;
    private MenuItem sort3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        search = menu.findItem(R.id.app_bar_search);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        sort1 = menu.findItem(R.id.menu_item_sort1);
        sort2 = menu.findItem(R.id.menu_item_sort2);
        sort3 = menu.findItem(R.id.menu_item_sort3);
        sort1.getIcon().setAlpha(100);
        sort2.getIcon().setAlpha(100);
        sort3.getIcon().setAlpha(100);
        sort1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getIcon().getAlpha()==100) {
                    sort1.getIcon().setAlpha(254);
                    sort2.getIcon().setAlpha(100);
                    sort3.getIcon().setAlpha(100);
                    FirstFragment.sortNotesByTitle(false);
                    return true;
                }
                if(menuItem.getIcon().getAlpha()==254) {
                    sort1.getIcon().setAlpha(255);
                    sort1.setIcon(R.drawable.sort1inv);
                    FirstFragment.sortNotesByTitle(true);
                    return false;
                }
                if(menuItem.getIcon().getAlpha()==255) {
                    sort1.getIcon().setAlpha(254);
                    sort1.setIcon(R.drawable.sort1);
                    FirstFragment.sortNotesByTitle(false);
                    return false;
                }
                return false;
            }
        });

        sort2.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getIcon().getAlpha()==100) {
                    sort2.getIcon().setAlpha(254);
                    sort1.getIcon().setAlpha(100);
                    sort3.getIcon().setAlpha(100);
                    FirstFragment.sortNotesByCreationDate(false);
                    return true;
                }
                if(menuItem.getIcon().getAlpha()==254) {
                    sort2.getIcon().setAlpha(255);
                    sort2.setIcon(R.drawable.sort2inv);
                    FirstFragment.sortNotesByCreationDate(true);
                    return false;
                }
                if(menuItem.getIcon().getAlpha()==255) {
                    sort2.getIcon().setAlpha(254);
                    sort2.setIcon(R.drawable.sort2);
                    FirstFragment.sortNotesByCreationDate(false);
                    return false;
                }
                return false;
            }
        });

        sort3.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getIcon().getAlpha()==100) {
                    sort3.getIcon().setAlpha(254);
                    sort1.getIcon().setAlpha(100);
                    sort2.getIcon().setAlpha(100);
                    FirstFragment.sortNotesByModificationDate(false);
                    return true;
                }
                if(menuItem.getIcon().getAlpha()==254) {
                    sort3.getIcon().setAlpha(255);
                    sort3.setIcon(R.drawable.sort3inv);
                    FirstFragment.sortNotesByModificationDate(true);
                    return true;
                }
                if(menuItem.getIcon().getAlpha()==255) {
                    sort3.getIcon().setAlpha(254);
                    sort3.setIcon(R.drawable.sort3);
                    FirstFragment.sortNotesByModificationDate(false);
                    return true;
                }
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String s) {
                FirstFragment.searchForStringInMessage(s);
                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                FirstFragment.noSearch();
                sort1.setIcon(R.drawable.sort1);
                sort2.setIcon(R.drawable.sort2);
                sort3.setIcon(R.drawable.sort3);
                sort1.getIcon().setAlpha(100);
                sort2.getIcon().setAlpha(100);
                sort3.getIcon().setAlpha(100);
                return false;
            }
        });
        return true;
    }

    public void setToolbarButtonsVisibility(int visibility) {
        boolean visible;
        if(search!=null) {
            if(visibility == View.GONE) visible = false;
            else visible = true;
            search.setVisible(visible);
            sort1.setVisible(visible);
            sort2.setVisible(visible);
            sort3.setVisible(visible);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_bar_search || id == R.id.menu_item_sort1 || id == R.id.menu_item_sort2 || id == R.id.menu_item_sort3) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}