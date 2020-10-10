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
    var edad:Int?=null
    var x:Float?=null
    var pi=3.1416

    //El siguiiente codigo de peteción que nos va ayudar a garantizar el objeto TextToSpeech se inicio completamente
    private val CODIGO_PETICION=100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Iniciamos ahora si la variable tts paqra que ya no este en null
        tts= TextToSpeech(this,this)

        //Invocamos la clase Log
        Log.i("YXZ", "Se acaba de iniciar el metodo OnCreate")
        //Java
        //Log.i("XYZ", "Tu edad en dias es: "+tuEdadEnDias(21))
        //Kotlin (El signo de pesos en kotlin se conoce como interpolación de String) junto con llave
        Log.i("XYZ", "Tu edad en días es: ${tuEdadEnDias(21)} ya solo bien")
        //En kotlin las funciones TAMBIEN SON VARIABLES y su ambito se puede definir solo con llaves
        Log.i("XYZ","La siguiente es otro ejemplo ${4+5} te dara una suma de 9")
        //En kotlin, ademas de ser orientado a objetos:TAMBIEN ES FUNCIONAL
        //es decir las funciones son tratadas como una VARIABLE
        var x=2
        //En kotlin una funcion puede ser declara DENTRO DE OTRO PORQUE SON TRATADAS COMO VARIABLES
        fun funcioncita()={
            print("Una funcioncita ya con notacion funcional!!")
        }
        //Otro ejemplo con argumentos
        fun otraFuncion(x:Int, y:Int)={
            print("Esta funcion hace de dos argumentos que les pases ${x+y}")
        }

        Log.i("XYZ","Mi primer funcion con notacion funcional ${funcioncita()} listooo")
        //Se invoca directamente abajo
        otraFuncion(5,4)

        //Funciones de orden superior y operador lambda

        //Para este ejecicio necesitamos crear una nueva clase
        class Ejemplito:(Int)->Int{
            override fun invoke(p1: Int): Int {
                TODO("Not yet implemented")
            }

        }

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
    //Implementamos un metodo o funcion que es lo mismo

    fun saludar(mensaje:String){
        Log.i("HOLA", "Un mensaje dentro de kotlon")
    }

    fun saludar2(mensaje:String):String{
        return "Mi mensaje de bienbenida"
    }

    fun tuEdadEnDias(edad:Int):Int{
        val diasAnio=365

        return diasAnio*edad
    }
}