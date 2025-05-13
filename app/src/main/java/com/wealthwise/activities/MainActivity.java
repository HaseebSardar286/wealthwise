package com.wealthwise.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.wealthwise.R;
import com.wealthwise.fragments.PortfolioFragment;
import com.wealthwise.fragments.TradingPanelFragment;
import com.wealthwise.fragments.TrendingAssetsFragment;
import com.wealthwise.fragments.UserProfileFragment;
import com.wealthwise.fragments.ViewConsultantsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        if (savedInstanceState == null) {
            loadFragment(new TrendingAssetsFragment(), false);
        }

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;
            int itemId = item.getItemId();

            if (itemId == R.id.navigation_trending) {
                selectedFragment = new TrendingAssetsFragment();
            } else if (itemId == R.id.navigation_portfolio) {
                selectedFragment = new PortfolioFragment();
            } else if (itemId == R.id.navigation_trading) {
                selectedFragment = new TradingPanelFragment(); 
            } else if (itemId == R.id.navigation_consultants) {
                selectedFragment = new ViewConsultantsFragment();
            } else if (itemId == R.id.navigation_profile) {
                selectedFragment = new UserProfileFragment();
            }

            if (selectedFragment != null) {
                loadFragment(selectedFragment, false);
                return true;
            }
            return false;
        });

    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        if (addToBackStack) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
}

