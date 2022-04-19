package com.example.retrofitapinasa

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.retrofitapinasa.databinding.ActivityMainBinding
import com.example.retrofitapinasa.retrofit.ImagenNasa
import com.example.retrofitapinasa.retrofit.NasaAPIService
import com.example.retrofitapinasa.retrofit.RestEngine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Call

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnImagenDelDia.setOnClickListener{
            if(binding.txtFecha.text.toString() != ""){
                binding.progressBar.visibility = View.VISIBLE
            devolverRespuesta(binding.txtFecha.text.toString())
           }else{
                Toast.makeText(applicationContext,
                    "Ingresa una fecha en formato AÑO-MES-DIA",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun devolverRespuesta(date: String) {
        CoroutineScope(Dispatchers.IO).launch{
            val call: NasaAPIService = RestEngine.getRestEngine().create(NasaAPIService::class.java)
            val resultado: Call<ImagenNasa> = call.obtenerImagenAPI(date)
            val i:ImagenNasa? = resultado.execute().body()

            if(i != null){
                runOnUiThread {
                                binding.txtTituloImagen.text = i.title
                                binding.txtDescripcion.text = i.explanation
                                binding.txtAutor.text = i.copyright
                                binding.txtUrl.text = i.hdurl
                                //binding.textImagen.text = "Imagen del Día \n"

                                CoroutineScope(Dispatchers.IO).launch {
                                    val x: Int = async {
                                        construirImagenDia(i)
                                    }.await()
                                }
                                runOnUiThread { binding.progressBar.visibility = View.GONE }

                }
            }else {
                runOnUiThread {
                    Toast.makeText(applicationContext,
                        "No se encontraron resultados...",
                        Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                }
            }

        }

    }

    fun construirImagenDia(i: ImagenNasa): Int{
        runOnUiThread {
            Glide.with(applicationContext)
                .load(i.url)
                .override(1090, 1550)
                .into(binding.imageView)
        }
        return 1
    }


}