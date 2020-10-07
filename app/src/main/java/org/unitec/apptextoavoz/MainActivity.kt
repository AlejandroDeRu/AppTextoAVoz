package org.unitec.apptextoavoz

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
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

        hablar.setOnClickListener {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            try {

            }catch(e:Exception){
                startActivityForResult(intent,CODIGO_PETICION)
            }
        }

        //Programamos el clicqueo del boton para que interprete lo escrito
        interpretar.setOnClickListener {
            if(fraseescrita.text.isEmpty()){
                Toast.makeText(this,"Debes escribir algo para que lo hable", Toast.LENGTH_LONG)
            }else{
                //Este metodo ahorita lo vamos a impplementar
                hablarTexto(fraseescrita.text)
            }
        }

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

    //Esta funcion es la que nos ayuda a interpretar o que escriba en este texto de la frase
    fun hablarTexto(textoHablar: Editable){
        tts!!.speak(textoHablar, TextToSpeech.QUEUE_FLUSH,null,"")
    }

    override fun onDestroy() {
        super.onDestroy()
        if(tts!=null){
            //En caso de la aplicaciones de espionaje estos dos renglones NUNCA SE APAGAN
            tts!!.stop()
            tts!!.shutdown()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CODIGO_PETICION->{
                if(resultCode== RESULT_OK && null!=data){
                    val result=data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
                    //Finalmente le vamos a decir a nuestro texto estatico que aqui nos muestre lo que dijimos pero en texto
                    interpretar.setText(result!![0])
                }
            }
        }
    }
}