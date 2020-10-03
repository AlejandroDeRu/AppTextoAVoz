package org.unitec.apptextoavoz

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import java.util.*
import kotlin.concurrent.schedule

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    //Este objeto es el intermediario entre neustra app y TextToSpeech
    private var tts:TextToSpeech?= null
    //El siguiiente codigo de peteción que nos va ayudar a garantizar el objeto TextToSpeech se inicio completamente
    private val CODIGO_PETICION=100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Iniciamos ahora si la variable tts paqra que ya no este en null
        tts= TextToSpeech(this,this)

        //KEMOSION!!! VAMOS A ESCUCHAR ESA VOCESITA EN ANDROID, DE BIENVENIDA
        Timer("Bienvenida", false).schedule(1000){
            tts!!.speak(
                    "Hola, bienvenido a mi aplicación, espero y te encante, oprime el botón para que te escuche!!",
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    ""
            )
        }
    }

    override fun onInit(estado: Int) {
        //Este metodo o función sirve para que se inicialice la configuración a la arrancar la app (IDIOMA)
        if(estado==TextToSpeech.SUCCESS){
            //Si el if se cumplio seguira aqui dentro
            var local=Locale("spa", "MEX")
            //La siguiente variable es para que internamente nostros sepamos que la app va bien.
            val resultado=tts!!.setLanguage(local)
            if(resultado==TextToSpeech.LANG_MISSING_DATA){
                Log.i("MALO", "NOOOOOOOO, NO FUNCIONO EL LENGUAJE, ALGO ANDA MAL")
            }
        }
    }
}