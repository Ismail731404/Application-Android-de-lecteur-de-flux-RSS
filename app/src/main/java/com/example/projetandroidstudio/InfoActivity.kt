package com.example.projetandroidstudio

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_info.*

class InfoActivity : AppCompatActivity(),InfoListAdapter.OnItemClickListener {

    //Le viewMode && base de donne
    private val newFluxActivityRequestCode = 1
    private val InfoviewModel: ViewModelProjet by viewModels {
        WordViewModelFactory((application as Application).repository)
    }
    //Pour la selection de Info
    private  var sizInfoSelectione: Int =0
    private  var InfoSelectione: List<Info>? =null

    //Selection que nouveau

    private var selectionNouveau:String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        setSupportActionBar(findViewById(R.id.my_toolbar))
        // Autorise button Up  button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = InfoListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val iii=intent
        selectionNouveau=iii.getStringExtra("Nouveau")
        if(selectionNouveau?.equals("true")!!)
        {
            Toast.makeText(
                applicationContext,
                "Vous avez demande que nouveau",
                Toast.LENGTH_LONG
            ).show()
            InfoviewModel.allInfoSelected.observe(owner = this) { Infos ->

                Infos.let {
                    adapter.submitList(it)
                    InfoSelectione=it
                    sizInfoSelectione=it.size
                }}
        }
        else
        {
            Toast.makeText(
                applicationContext,
                "Vous avez demande Tout",
                Toast.LENGTH_LONG
            ).show()
            InfoviewModel.allInfos.observe(owner = this) { Infos ->
                // Mettez à jour la version en cache des mots dans le convertisseur.
                Infos.let {
                    adapter.submitList(it)
                    InfoSelectione=it
                    sizInfoSelectione=it.size
                }
            }
        }


        //Vide la table
        Supprime()


    }

    override fun onItemClick(position: Int, Url: String) {

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(Url))
        InfoviewModel.updateInfo(Url,false)
        startActivity(Intent.createChooser(intent, "Navigateur avec"))

    }

    private  fun Supprime()
    {
        btnSupprime2.setOnClickListener{




            if(sizInfoSelectione==0) {
                Toast.makeText(
                    applicationContext,
                    "Veuillez selection au moins un Flux pour supprime",
                    Toast.LENGTH_LONG
                ).show()
            }else
            {


                AlertDialog.Builder(this)
                    .setMessage("Vous voulez Bien vide")
                    .setTitle("ALERT")
                    .setCancelable(false)
                    .setPositiveButton("Oui") { dialog: DialogInterface, t: Int ->
                        Log.d("Message", "Oui")
                        //finish()
                        dialog.dismiss()

                        for (i in 0 until ( InfoSelectione?.size!!))
                        {

                            val titre= InfoSelectione?.get(i)?.Title.toString()

                            InfoviewModel.deleteUnInfo(titre)
                        }

                    }
                    .setNegativeButton("Non") { _ , _ ->
                        Log.d("Message", "Non")

                        Toast.makeText(
                            applicationContext,
                            "Vous avez annulez",
                            Toast.LENGTH_LONG
                        ).show()

                    }
                    .setNeutralButton("Cancel") { dialogue, _ ->
                        dialogue.cancel()
                        Log.d("Message", "cancel")
                    }
                    .show()


            }

        }
    }


}