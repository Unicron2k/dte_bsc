package com.shadyshrimp.lab9_2;

import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert host != null;
        NavController navController = host.getNavController();

        // Use of try-catch is unnecessary here, as no exception will be thrown(at least in api29+)
        // if the element cannot be found: findViewById() return NULL if the element cannot be found.
        // Do the appropriate checks for NULL, and act accordingly
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout == null) {
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        } else {
            Set<Integer> topLevelDestinations = new HashSet<>();
            topLevelDestinations.add(R.id.loginFragment);
            topLevelDestinations.add(R.id.programsFragment);
            topLevelDestinations.add(R.id.settingsFragment);
            appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).setOpenableLayout(drawerLayout).build();
        }

        setupActionBar(navController, appBarConfiguration);
        setupNavigationMenu(navController);
        setupBottomNavMenu(navController);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retValue = super.onCreateOptionsMenu(menu);
        NavigationView navigationView = findViewById(R.id.nav_view);
        if (navigationView != null) {
            getMenuInflater().inflate(R.menu.overflow_menu, menu);
            return true;
        }
        return retValue;
    }

    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), appBarConfiguration);
    }

    private void setupActionBar(NavController navController, AppBarConfiguration appBarConfig) {
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
    }

    private void setupNavigationMenu(NavController navController) {
        NavigationView sideNavView = findViewById(R.id.nav_view);
        if (sideNavView != null) {
            NavigationUI.setupWithNavController(sideNavView,navController);
        }
    }

    private void setupBottomNavMenu(NavController navController) {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_view);
        if (bottomNav != null) {
            NavigationUI.setupWithNavController(bottomNav,navController);
        }
    }
}
