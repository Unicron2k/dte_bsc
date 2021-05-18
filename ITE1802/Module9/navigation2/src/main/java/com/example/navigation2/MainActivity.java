package com.example.navigation2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Lager et AppbarConfiguration-objekt vha. NavController:
        NavHostFragment myNavHosFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.my_nav_host_fragment);
        NavController navController = myNavHosFragment.getNavController();

        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        try {
            // Mer bruk av try/catch. MainActivity har tre ulike layouter. Kun en av dem har en DrawerLayout.
            //
            // res/layout/activity_main.xml        : Denne brukes dersom skjermhøyde er mindre enn 470dp. F.eks. mobil i landscape.
            //                                       Her brukes NavigationView (sidemenyen) sammen med DrawerLayout.
            // res/layout-h470dp/activity_main.xml : Denne brukes dersom skjermhøyde er STØRRE enn 470dp. F.eks. mobil i portrait.
            //                                       Her brukes IKKE DrawerLayout men i stedet BottomNavigationView.
            //                                       Bruke derfor try/catch siden vi prøver å hente en referanse til noe som ikke finnes i layouten.
            // res/layout-w960dp/activity_main.xml : Denne brukes dersom skjermBREDDE er STØRRE enn 960dp. F.eks. nettbrett.
            //                                       Her brukes IKKE DrawerLayout men i stedet NavigationView alene, som alltid vises.
            //                                       Bruke derfor try/catch siden vi prøver å hente en referanse til noe som ikke finnes i layouten.
            // Se: https://developer.android.com/training/multiscreen/screensizes
            DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
            if (null != drawerLayout) {
                Set<Integer> topLevelDestinations = new HashSet<>();
                topLevelDestinations.add(R.id.mainFragment);
                appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).setDrawerLayout(drawerLayout).build();
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        // Initialiserer actionbar:
        setupActionBar(navController, appBarConfiguration);

        // Initialiserer sidemenyen:
        setupNavigationMenu(navController);

        setupBottomNavMenu(navController);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.my_nav_host_fragment))
                        || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retValue = super.onCreateOptionsMenu(menu);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        if (navigationView == null) {
            getMenuInflater().inflate(R.menu.overflow_menu, menu);
            return true;
        }
        return retValue;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.my_nav_host_fragment), appBarConfiguration);
    }

    // Bruker NavigationUI til å initialisere appbaren (denne liggeri alle tre layouts):
    private void setupActionBar(NavController navController, AppBarConfiguration appBarConfig) {
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
    }

    // Bruker NavigationUI til å initialisere sidemenyen (denne ligger i to av tre layouts):
    private void setupNavigationMenu(NavController navController) {
        NavigationView sideNavView = findViewById(R.id.nav_view);
        if (sideNavView != null) {
            NavigationUI.setupWithNavController(sideNavView,navController);
        }
    }

    // Bruker NavigationUI til å initialisere BottomNavigation (denne ligger i en av tre layouts)
    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        if (bottomNav != null) {
            NavigationUI.setupWithNavController(bottomNav,navController);
        }
    }
}
