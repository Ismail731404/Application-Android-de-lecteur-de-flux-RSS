package com.example.projetandroidstudio

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.*
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SimpleAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_main.*
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.SAXException
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserFactory
import java.io.*
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.parsers.ParserConfigurationException


class MainActivity : AppCompatActivity(),FluxListAdapter.OnItemClickListener {



    //Le viewMode && base de donne
    private val newFluxActivityRequestCode = 1
    private val fluxviewModel: ViewModelProjet by viewModels {
        WordViewModelFactory((application as Application).repository)
    }

    //Pour la selection de Flux
    private  var sizFluxSelectione: Int =0
    private  var fluxSelectione: List<Flux>? =null

    //Pour le telechargement d'un Flux
    private lateinit var url :String
    private lateinit var  titre :String
    private val STORAGE_PERMISSION_CODE: Int = 1000
    private var idDownload: Long =0

    //BroadcastReceiver
    private  lateinit var context: Context
    private lateinit var dm :DownloadManager

    //Table Info
   private var titreitem:String=""
   private var linkitem:String=""
   private var description:String =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        // Autorise button Up  button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = FluxListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Observation des different Flux a Affiche
        fluxviewModel.allFlux.observe(owner = this) { fluxs ->
            // Mettez à jour la version en cache des mots dans le convertisseur.
            fluxs.let { adapter.submitList(it) }
        }

        //Observation de selection d'un Flux
        fluxviewModel.allFluxTelecharge.observe(owner = this){
            sizFluxSelectione =it.size
            fluxSelectione=it;
        }

        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent = Intent(this@MainActivity, NewFluxActivity::class.java)
            startActivityForResult(intent, newFluxActivityRequestCode)
        }

        // Bouton de Telecharge

            telecharge()

        //Bouton de Supprime

             Supprime()





    }


    private fun telecharge()
    {
        btnDownload.setOnClickListener{

            if(sizFluxSelectione==0) {
                Toast.makeText(
                    applicationContext,
                    "Veuillez selection au moins un Flux ",
                    Toast.LENGTH_LONG
                ).show()
            }else
            {

                for (i in 0 until ( fluxSelectione?.size!!))
                {
                    Toast.makeText(
                        applicationContext,
                        fluxSelectione?.get(i)?.Source,
                        Toast.LENGTH_LONG
                    ).show()
                    titre= fluxSelectione?.get(i)?.Source.toString()
                    url =fluxSelectione?.get(i)?.URL.toString()
                    //BroadcastReceiver
                    /* preparer un filtre pour BroadcastReceiver */
                    broadCastReciver(titre)
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                    {
                        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)==
                            PackageManager.PERMISSION_DENIED){
                            //autorisation refusée, demandez-la

                            //afficher popus pour l'autorisation d'exécution
                            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),STORAGE_PERMISSION_CODE)
                        }
                        else {
                            //permission déjà accordée, effectuez le téléchargement
                            startDownloading(titre,url);
                        }
                    }
                    else
                    {
                        //système, os od moins que guimauve, autorisation d'exécution non requise, effectuer le téléchargement
                        startDownloading(titre,url);
                    }
                    //mettre a jour le case
                    fluxviewModel.updateFlux(titre,false)


                }


            }

        }
    }



    private fun startDownloading(titre:String, url:String) {
        //get text/url from recylview

        //demande de téléchargement
        val request = DownloadManager.Request(Uri.parse(url))

        //autoriser le type de réseaux à télécharger des fichiers par défaut, les deux sont autorisés
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle(titre+".xml")
        request.setDescription("The file is downloading...")

        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$titre")

        //obtenir le service de téléchargement et mettre le fichier en file d'attente
        val manager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        idDownload= manager.enqueue(request)


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

       when(requestCode)
       {
          STORAGE_PERMISSION_CODE->{
              if(grantResults.isNotEmpty() && grantResults[0] ==
                      PackageManager.PERMISSION_GRANTED){
                  //PERMISSION DE POPUP A ÉTÉ accordée, effectuez le téléchargement
                  startDownloading(titre,url)
              }
              else
              {
                  //l'autorisation du popup a été refusée, afficher le message d'erreur
                  Toast.makeText(this,"Permision denied",Toast.LENGTH_LONG).show()
              }

          }
       }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        if (requestCode == newFluxActivityRequestCode && resultCode == Activity.RESULT_OK) {
            Toast.makeText(
                applicationContext,
                "Flux est bien enregistre",
                Toast.LENGTH_LONG
            ).show()

        }
        else
        {
            Toast.makeText(
                applicationContext,
                R.string.empty_not_saved,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.activity_main_manu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when( item.itemId ){
            R.id.action_Info -> {
                AlertDialog.Builder(this)
                    .setMessage("Vous voulez affiche que nouveau ou Tout")
                    .setTitle("ALERT")
                    .setCancelable(false)
                    .setPositiveButton("Tout") { dialog: DialogInterface, t: Int ->
                        Log.d("Message", "Tout")
                        //finish()
                        dialog.dismiss()
                        //startActivity(Intent(this, InfoActivity::class.java))
                        val iii=Intent(this, InfoActivity::class.java)
                        iii.putExtra("Nouveau","false")
                        startActivity(iii)
                    }
                    .setNegativeButton("Nouveau") { _ , _ ->
                        Log.d("Message", "Nouveau")
                        val iii=Intent(this, InfoActivity::class.java)
                        iii.putExtra("Nouveau","true")
                        startActivity(iii)
                    }
                    .setNeutralButton("Cancel") { dialogue, _ ->
                        dialogue.cancel()
                        Log.d("Message", "cancel")
                    }
                    .show()

            }
            R.id.action_ajouter -> {

                startActivityForResult(Intent(this, NewFluxActivity::class.java), newFluxActivityRequestCode)
            }
            android.R.id.home -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onItemClick(position: Int,Source: String,coche:Boolean) {
        Toast.makeText(this,"Item $position clicked de Source : $Source  et coche: $coche",Toast.LENGTH_SHORT).show()

        fluxviewModel.updateFlux(Source,coche)

    }


    private fun broadCastReciver(titre:String)
    {
        context=this
        //ref dm de  DownloadManager
        dm = this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val receiver= object : BroadcastReceiver() {
            override fun onReceive(cont: Context?, intent: Intent?) {
                // /* récupérer l'identifiant de l'action download */
                val reference: Long? = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
                /* vérifier si l'identifiant identique à notre action download */
                if(reference!=idDownload){
                    return
                }

                //récupérer ParcelFileDescriptor pour le fichier téléchargé
                val pdesc : ParcelFileDescriptor = dm.openDownloadedFile(reference)
                val desc: FileDescriptor = pdesc.fileDescriptor

                val fileInputStream = FileInputStream( desc )
                // et lire le fichier


                Toast.makeText(
                    baseContext,
                    "$titre ::telecharge complete ",
                    Toast.LENGTH_LONG
                ).show()

                //Un foix le telecharge est complet parser le fichier


                Thread{


                    var item: HashMap<String?, String?>? = HashMap()
                    val parserFactory: XmlPullParserFactory = XmlPullParserFactory.newInstance()
                    val parser: XmlPullParser = parserFactory.newPullParser()
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true)
                    parser.setInput(fileInputStream, null)
                    var tag: String?
                    var text = ""
                    var event = parser.eventType
                    while (event != XmlPullParser.END_DOCUMENT) {
                        tag = parser.name
                        when (event) {
                            XmlPullParser.START_TAG -> if (tag == "item") item = HashMap()
                            XmlPullParser.TEXT -> text = parser.text
                            XmlPullParser.END_TAG -> when (tag) {
                            "title" -> item!!["title"] = text
                            "link" -> item!!["link"] = text
                            "description" -> item!!["description"] = text
                            "item" -> if (item != null)
                            {
                                //userList.add(item)
                                titreitem= item["title"].toString()
                                description= item["description"].toString()
                                linkitem= item["link"].toString()
                                val info=Info(titreitem,description,linkitem)
                                fluxviewModel.insertInfo(info);
                            }
                        }
                        }
                        event = parser.next()
                    }

               }.start()



            }


        }

        /* preparer un filtre pour BroadcastReceiver */
        val filter = IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        context.registerReceiver(receiver, filter)



    }



    private  fun Supprime()
    {
        btnSupprime.setOnClickListener{


            if(sizFluxSelectione==0) {
                Toast.makeText(
                    applicationContext,
                    "Veuillez selection au moins un Flux pour supprime",
                    Toast.LENGTH_LONG
                ).show()
            }else
            {
                AlertDialog.Builder(this)
                    .setMessage("Vous voulez Bien supprime le(s) Flux(s) selectionner ")
                    .setTitle("ALERT")
                    .setCancelable(false)
                    .setPositiveButton("Oui") { dialog: DialogInterface, t: Int ->
                        Log.d("Message", "Oui")
                        //finish()
                        dialog.dismiss()

                        for (i in 0 until ( fluxSelectione?.size!!))
                        {

                            titre= fluxSelectione?.get(i)?.Source.toString()

                            fluxviewModel.deleteUnFlux(titre)
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