package com.example.seknet.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.example.seknet.fragments.FragmentAnalisis
import com.example.seknet.fragments.FragmentHome
import com.example.seknet.fragments.FragmentInfo
import com.example.seknet.fragments.FragmentPing
import com.example.seknet.fragments.FragmentPortscan
import com.example.seknet.fragments.FragmentSpeedtest
import com.example.seknet.R
import com.example.seknet.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        drawerLayout = binding.lateralMenu
        val toggle = ActionBarDrawerToggle(
            this, binding.lateralMenu, binding.appBarMain.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                setReorderingAllowed(true)
                add<FragmentHome>(R.id.fragment_content_main)
            }
        }

        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_menu_home -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentHome>(R.id.fragment_content_main)
                        drawerLayout.closeDrawers()
                    }
                }

                R.id.item_menu_info -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentInfo>(R.id.fragment_content_main)
                        drawerLayout.closeDrawers()
                    }
                }

                R.id.item_menu_portscan -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentPortscan>(R.id.fragment_content_main)
                        drawerLayout.closeDrawers()
                    }
                }

                R.id.item_menu_overview -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentAnalisis>(R.id.fragment_content_main)
                        drawerLayout.closeDrawers()
                    }
                }

                R.id.item_menu_speedtest -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentSpeedtest>(R.id.fragment_content_main)
                        drawerLayout.closeDrawers()
                    }
                }

                R.id.item_menu_ping -> {
                    supportFragmentManager.commit {
                        setReorderingAllowed(true)
                        replace<FragmentPing>(R.id.fragment_content_main)
                        drawerLayout.closeDrawers()
                    }
                }
            }
            true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}