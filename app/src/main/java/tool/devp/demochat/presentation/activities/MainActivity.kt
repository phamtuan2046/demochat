package tool.devp.demochat.presentation.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.act_main.*
import tool.devp.demochat.R
import tool.devp.demochat.common.DemoChatApp
import tool.devp.demochat.presentation.fragments.TopFragment


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    var TAG = MainActivity::class.java.name
    lateinit var topFragment: TopFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_main)
        initView()
        setUpToolbar()
        navigationView.setNavigationItemSelectedListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    Log.d(TAG, p0)
                    return false
                }

                override fun onQueryTextChange(s: String?): Boolean {
                    s?.let {
                        topFragment.onFilter(it)
                    }
                    return true
                }
            })
            setOnSearchClickListener { v ->
                Log.d(TAG, "setOnSearchClickListener")
            }
            setOnCloseListener {

                false
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.nav_about -> {
                startActivity(Intent(this,AboutActivity::class.java))
            }
            R.id.nav_logout -> {
                logout()
            }
        }
        drawerLayout.closeDrawers()
        return true
    }

    private fun initView(){
        topFragment = TopFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container,topFragment).commit()
    }
    private fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "DemoChat"
            setDisplayHomeAsUpEnabled(true)
            setHomeButtonEnabled(true);
        }
        drawerLayout.run {
            val toggle = ActionBarDrawerToggle(
                    this@MainActivity,
                    drawerLayout,
                    toolbar,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
            )
            toggle.isDrawerIndicatorEnabled = true
            addDrawerListener(toggle)
            toggle.syncState()
        }
        toolbar.setOnClickListener { v ->
            Log.d("PhamDinhTuan: ", "toolbar setOnClickListener")
        }
    }

    private fun logout() {
        DemoChatApp.INTANCE.store.delete()
        startActivity(Intent(this, SignInSignUpActivity::class.java))
        finish()
    }
}
