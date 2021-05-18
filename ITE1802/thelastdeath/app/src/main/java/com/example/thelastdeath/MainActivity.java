package com.example.thelastdeath;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import androidx.preference.PreferenceManager;
import com.example.thelastdeath.utils.Utils;
import com.example.thelastdeath.utils.firebase.UtilsFirebase;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashSet;
import java.util.Set;

import static com.example.thelastdeath.utils.Utils.setAppTheme;
import static com.example.thelastdeath.utils.firebase.UtilsFirebase.isUserLoggedIn;
import static com.example.thelastdeath.utils.firebase.UtilsFirebase.redirectIfLoggedOut;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    private SharedPreferences prefs;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Fetches a reference to SharedPreferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        setAppTheme(prefs, this);

        // Lager et AppbarConfiguration-objekt vha. NavController:
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        assert navHostFragment != null;
    navController = navHostFragment.getNavController();
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

        // No need to use try-catch, just check for NULL and we're good2go!
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        if (drawerLayout != null) {
            Set<Integer> topLevelDestinations = new HashSet<>();
            topLevelDestinations.add(R.id.loginFragment);
            appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).setDrawerLayout(drawerLayout).build();
        }

        // Initialiserer actionbar:
        setupActionBar(navController, appBarConfiguration);
        // Initialiserer sidemenyen:
        setupNavigationMenu(navController);
        // Initialiserer bunnmenyen:
        setupBottomNavMenu(navController);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getTitle() == getString(R.string.log_out)){
            if(FirebaseAuth.getInstance().getCurrentUser() != null) {
                FirebaseAuth.getInstance().signOut();
                navController.navigate(R.id.loginFragment);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                return true;
            } else return false;
        }
        else return NavigationUI.onNavDestinationSelected(item, Navigation.findNavController(this, R.id.nav_host_fragment))
                || super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean retValue = super.onCreateOptionsMenu(menu);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // The NavigationView already has these same navigation items, so we only add
        // navigation items to the menu here if there isn't a NavigationView
        if (navigationView != null) {
            getMenuInflater().inflate(R.menu.overflow_menu, menu);
            return true;
        }
        return retValue;
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), appBarConfiguration);
    }

    // Bruker NavigationUI til å initialisere appbaren (denne liggeri alle tre layouts):
    private void setupActionBar(NavController navController, AppBarConfiguration appBarConfig) {
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfig);
    }

    // Bruker NavigationUI til å initialisere sidemenyen (denne ligger i to av tre layouts):
    private void setupNavigationMenu(NavController navController) {
        NavigationView sideNavView = findViewById(R.id.nav_view);
        if (sideNavView != null) {
            NavigationUI.setupWithNavController(sideNavView, navController);
        }
    }

    // Bruker NavigationUI til å initialisere BottomNavigation (denne ligger i en av tre layouts)
    private void setupBottomNavMenu(NavController navController) {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        if (bottomNavigationView != null) {
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
        }
    }


    public Toolbar getNav() {
        return toolbar;
    }

    public void hideBottomNav() {
        if(bottomNavigationView!=null) {
            bottomNavigationView.setVisibility(View.GONE);
        }
    }

    public void showBottomNav() {
        if(bottomNavigationView!=null) {
            bottomNavigationView.setVisibility(View.VISIBLE);
        }
    }

}
