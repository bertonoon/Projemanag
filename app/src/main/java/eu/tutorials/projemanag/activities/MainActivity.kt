package eu.tutorials.projemanag.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import eu.tutorials.projemanag.R
import eu.tutorials.projemanag.adapters.BoardItemsAdapter
import eu.tutorials.projemanag.databinding.ActivityMainBinding
import eu.tutorials.projemanag.firebase.FirestoreClass
import eu.tutorials.projemanag.models.Board
import eu.tutorials.projemanag.models.User
import eu.tutorials.projemanag.utils.Constants

class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var binding : ActivityMainBinding? = null
    private lateinit var mUserName : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding?.root)
        setupActionBar()
        binding?.navView?.setNavigationItemSelectedListener(this)
        FirestoreClass().loadUserData(this@MainActivity,true)

        val fabCreateBoard : FloatingActionButton = findViewById(R.id.fab_create_board)
        fabCreateBoard.setOnClickListener{
            val intent = Intent(this,CreateBoardActivity::class.java)
            intent.putExtra(Constants.NAME,mUserName)
            startActivityForResult(intent, CREATE_BOARD_REQUEST_CODE)
        }

    }

    override fun onResume() {
        super.onResume()

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!){
                    binding?.drawerLayout?.closeDrawer(GravityCompat.START)
                } else{
                    doubleBackToExit()
                }
            }
        })

    }

    private fun setupActionBar(){
        val toolbar : Toolbar = findViewById(R.id.toolbar_main_activity)
        setSupportActionBar(toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_action_navigation_menu)

        toolbar.setNavigationOnClickListener {
            toggleDrawer()
        }

    }

    private fun toggleDrawer(){
        if(binding?.drawerLayout?.isDrawerOpen(GravityCompat.START)!!){
            binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        } else{
            binding?.drawerLayout?.openDrawer(GravityCompat.START)
        }
    }


    private fun signOut(){
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this@MainActivity,IntroActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_sign_out -> {
                signOut()
            }
            R.id.nav_my_profile -> {
                startActivityForResult(Intent(this,MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE)
            }
        }
        binding?.drawerLayout?.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateNavigationUserDetails(user: User, readBoardsList: Boolean) {
        mUserName=user.name
        Glide
            .with(this)
            .load(user.image)
            .fitCenter()
            .placeholder(R.drawable.ic_user_place_holder)
            .into(findViewById(R.id.nav_user_image))

        val tvUsername : TextView = findViewById(R.id.tv_username)
        tvUsername.text = user.name

        if (readBoardsList){
            showProgressDialog(resources.getString(R.string.please_wait))
            FirestoreClass().getBoardsList(this)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == MY_PROFILE_REQUEST_CODE){
            FirestoreClass().loadUserData(this)
        } else if(resultCode == Activity.RESULT_OK && requestCode == CREATE_BOARD_REQUEST_CODE){
            FirestoreClass().getBoardsList(this)
        }
        else {
            Log.e("Cancelled","Cancelled")
        }
    }

    fun populateBoardsListToUI (boardsList: ArrayList<Board>){
        val rvBoardsList : RecyclerView? = findViewById(R.id.rv_board_list)
        val tvNoBoardsAvailable : TextView? = findViewById(R.id.tv_no_boards_available)
        if(boardsList.size > 0){
            rvBoardsList?.visibility = View.VISIBLE
            tvNoBoardsAvailable?.visibility = View.GONE
            rvBoardsList?.layoutManager = LinearLayoutManager(this)
            rvBoardsList?.setHasFixedSize(true)

            val adapter = BoardItemsAdapter(this,boardsList)
            rvBoardsList?.adapter = adapter
        } else {
            rvBoardsList?.visibility = View.GONE
            tvNoBoardsAvailable?.visibility = View.VISIBLE
        }
    }


    companion object{
        const val MY_PROFILE_REQUEST_CODE : Int = 11
        const val CREATE_BOARD_REQUEST_CODE : Int = 12
    }

}
