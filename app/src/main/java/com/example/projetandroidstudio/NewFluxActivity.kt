package com.example.projetandroidstudio

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels

class NewFluxActivity : AppCompatActivity() {

    private lateinit var editTag: EditText
    private lateinit var editUrl: EditText
    private lateinit var editSource: EditText

    private val fluxviewModel: ViewModelProjet by viewModels {
        WordViewModelFactory((application as Application).repository)
    }

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_flux)
        setSupportActionBar(findViewById(R.id.my_toolbar))
        // Autorise button Up  button
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        editTag = findViewById(R.id.edit_Tag)
        editUrl = findViewById(R.id.edit_Url)
        editSource = findViewById(R.id.edit_Source)


        val button = findViewById<Button>(R.id.button_save)
        button.setOnClickListener {
            val replyIntent = Intent()
            if (TextUtils.isEmpty(editTag.text) || TextUtils.isEmpty(editUrl.text)  || TextUtils.isEmpty(editSource.text)  ) {
                setResult(Activity.RESULT_CANCELED,replyIntent)

            } else {
                val Tag = editTag.text.toString()
                val Url = editUrl.text.toString()
                val Source = editSource.text.toString()

                val flux = Flux(
                            Source,
                            Tag, Url,false
                        )

                if (flux != null) {
                    fluxviewModel.insertFlux(flux)

                }
                setResult(Activity.RESULT_OK,replyIntent)
            }
            finish()
        }
    }

    companion object {
        const val EXTRA_REPLY = "com.example.projetandroidstudio"
    }
}