package com.educards.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.educards.R
import com.educards.adapter.RecyclerCardAdapter
import com.educards.model.Deck
import com.educards.model.entities.Card
import com.educards.service.FirebaseConnection
import com.educards.service.SCard
import com.educards.service.SDeck
import com.educards.util.UListeners
import com.educards.util.UTextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.educards.util.UAlertGenericDialog.createDialogAlert
import com.educards.util.UIndexDeckOrCard.getItemsCardInDeckSelected
import com.educards.util.UIndexDeckOrCard.getLastKeyCardDeckSelected
import com.educards.util.UIndexDeckOrCard.realTimeIndexCardInSelectedDeck
import com.educards.util.UIndexDeckOrCard.setSelectedDeckKey
import com.educards.util.UListeners.setCardListener

class DeckActivity : AppCompatActivity(), View.OnClickListener,
    NavigationView.OnNavigationItemSelectedListener {
    private lateinit var recyclerCard: RecyclerView
    private lateinit var activity: DeckActivity
    private lateinit var drawer: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var btReverse: ImageButton

    private lateinit var fabStudy: FloatingActionButton
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView

    var cardsData = ArrayList<Card>()
    private lateinit var bundle: Bundle
    lateinit var cardListener:ValueEventListener
    var isCardAdd = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deck)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerCard = findViewById(R.id.recycler_cards)
        fabStudy = findViewById(R.id.fab_study)
        fabAdd = findViewById(R.id.fab_add)
        tvDescription = findViewById(R.id.card_description)
        btReverse = findViewById(R.id.fab_revert)

        activity = this
        bundle = this.intent.extras!!


        //agregando titulo y descripcion del mazo seleccionado
        tvDescription.text = bundle?.getString("description")
        title = bundle?.getString("title")

        var horizontalLayoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
        val adapter = RecyclerCardAdapter(cardsData, btReverse, this, activity,recyclerCard)

        cardListener = object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    cardsData.clear()
                    snapshot.children.forEach{
                        if (it != null){
                            cardsData.add(it.getValue<Card>() as Card)
                        }
                    }
                    recyclerCard.adapter = adapter
                    if (isCardAdd){
                        recyclerCard.scrollToPosition(cardsData.size-1)
                    }
                    setCardListener(cardListener)
                }
                override fun onCancelled(error: DatabaseError) {
                    createDialogAlert(this@DeckActivity,"Cards","Error showing the cards of the selected deck.\nDetails: $error")
                }

            }
        FirebaseConnection.refGlobal.child("/cards").child(bundle?.getString("idDeck").toString()).addValueEventListener(cardListener)
        recyclerCard.setHasFixedSize(true)
        recyclerCard.layoutManager = horizontalLayoutManager
        recyclerCard.adapter = adapter

        initView()
    }

    private lateinit var dialog: AlertDialog
    private lateinit var etTitle: EditText
    private lateinit var etHeader: TextView
    private lateinit var btUpdate: MaterialButton
    private lateinit var etHover: TextInputLayout
    private lateinit var tiDescription: TextInputLayout

    private fun initView() {
        toolbar = findViewById(R.id.toolbar_deck)
        toolbar.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_input, null)
            btUpdate = view.findViewById(R.id.bt_create)
            etTitle = view.findViewById(R.id.et_title)
            etHeader = view.findViewById(R.id.tv_header)

            //personalizar el dialog para el titulo
            etHeader.text = "Rename deck"
            btUpdate.text = "Update"
            //contenido del input
            etTitle.setText(title.toString())
            builder.setView(view)

            dialog = builder.create()
            dialog.show()

            btUpdate.setOnClickListener {
                if (UTextView.verifyContentInTextViews(this,etTitle,"Null or empty deck title field")){
                    //los datos del mazo vienen del bundle y solo se actualiza el titulo
                    SDeck.updateDeck(this,Deck(
                        bundle.getString("idDeck").toString(),
                        etTitle.text.toString().replaceFirstChar { it.uppercase() },
                        tvDescription.text.toString(),
                        bundle.getString("isFavorite").toString().toBoolean(),
                        bundle.getInt("count")
                    ),"title")
                    title = etTitle.text.toString().replaceFirstChar { it.uppercase() }
                    dialog.dismiss()
                }
            }
        }
        tvDescription.setOnClickListener{
            val builder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.dialog_input, null)
            btUpdate = view.findViewById(R.id.bt_create)
            etTitle = view.findViewById(R.id.et_title)
            etHeader = view.findViewById(R.id.tv_header)
            etHover = view.findViewById(R.id.titl_title)

            //personalizar el dialog para la descripcion
            etHeader.text = "Update description"
            btUpdate.text = "Update"
            etHover.hint = "Description"
            //contenido del input
            etTitle.setText(tvDescription.text)
            builder.setView(view)

            dialog = builder.create()
            dialog.show()

            realTimeIndexCardInSelectedDeck()
            btUpdate.setOnClickListener {
                if (UTextView.verifyContentInTextViews(this,etTitle,"Null or empty deck description field")){
                    //los datos del mazo vienen del bundle y la descripcion y el titulo de las edittext
                    SDeck.updateDeck(this,Deck(
                        bundle.getString("idDeck").toString(),
                        title.toString(),
                        etTitle.text.toString(),
                        bundle.getString("isFavorite").toString().toBoolean(),
                        bundle.getInt("count")
                    ),"description")
                    tvDescription.setText(etTitle.text.toString())
                    dialog.dismiss()
                }
            }
        }
        setSupportActionBar(toolbar)

        drawer = findViewById(R.id.drawer_deck)
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        var navigationView: NavigationView = findViewById(R.id.nav_view_deck)
        navigationView.setNavigationItemSelectedListener(this)
        navigationView.setItemIconTintList(null)


        val headerView: View = navigationView.getHeaderView(0)
        val navHeader = headerView.findViewById<LinearLayout>(R.id.nav_header)
        navHeader.setOnClickListener(this)

        fabStudy.setOnClickListener(this)
        fabAdd.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.deck_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_deck_delete -> {
                Toast.makeText(this, "Delete deck", Toast.LENGTH_SHORT).show()
//                TODO: Eliminar el mazo
            }

            R.id.menu_deck_close -> {
                Toast.makeText(this, "Save and exit", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onClick(view: View) {
        when(view.id) {
            R.id.nav_header -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                val drawer: DrawerLayout =
                    findViewById(R.id.drawer_main)
                drawer.closeDrawer(GravityCompat.START)
                finish()
            }

            R.id.fab_revert -> Toast.makeText(this, "Reverse", Toast.LENGTH_SHORT).show()

            R.id.fab_study -> {
              if (cardsData.size > 0){
                  val studyIntent = Intent(this, EstudyActivity::class.java)
                  studyIntent.putExtra("title", this.intent.extras?.getString("title"))
                  startActivity(studyIntent)
              }
            }
            R.id.fab_add -> {
                val cardIndex = getItemsCardInDeckSelected().plus(1)
                val newCardKey = getLastKeyCardDeckSelected()?.toInt()?.plus(1)
                isCardAdd = true
                SCard.saveCard(this,Card("$newCardKey", "Click to edit question $cardIndex", "Click to edit answer $cardIndex"))
                CoroutineScope(Dispatchers.IO).launch {
                    Thread.sleep(1500)
                    isCardAdd = false
                }
            }
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_all_decks -> {
                val mainIntent = Intent(this, MainActivity::class.java)
                startActivity(mainIntent)
                this.finish()
            }
            R.id.nav_all_favorites -> {
            }
            R.id.nav_about -> {
                val aboutIntent = Intent(this, AboutActivity::class.java)
                startActivity(aboutIntent)
            }
            R.id.nav_donate -> {
                val donateIntent = Intent(this, DonateActivity::class.java)
                startActivity(donateIntent)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
