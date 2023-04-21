package com.example.seknet
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.findFragment
import androidx.navigation.Navigation
import com.example.seknet.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationBarMenu
import com.google.android.material.navigation.NavigationBarView

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout : DrawerLayout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = ActivityMainBinding.inflate(layoutInflater)
         setContentView(binding.root)
            setSupportActionBar(binding.toolbar)

            drawerLayout = binding.lateralMenu
            val toggle = ActionBarDrawerToggle(this,binding.lateralMenu,binding.toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close)
            drawerLayout.addDrawerListener(toggle);
            toggle.syncState();

            if (savedInstanceState == null) {
                supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    add<FragmentHome>(R.id.fragmentContainer)
                }
            }


     }
}