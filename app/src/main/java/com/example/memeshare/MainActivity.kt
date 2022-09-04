package com.example.memeshare

import android.app.WallpaperManager.getInstance
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.security.identity.IdentityCredentialStore.getInstance
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.memeshare.MySingleton.Companion.getInstance

class MainActivity : AppCompatActivity() {
    var currentimageurl:String?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadmeme()
    }
    private fun loadmeme(){
        findViewById<ProgressBar>(R.id.progressBar).visibility=View.VISIBLE
        currentimageurl = "https://meme-api.herokuapp.com/gimme"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, currentimageurl, null,
            Response.Listener { response ->
                val url=response.getString("url")
                Glide.with(this).load(url).listener(object: RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        findViewById<ProgressBar>(R.id.progressBar).visibility=View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        findViewById<ProgressBar>(R.id.progressBar).visibility=View.GONE
                        return false
                    }
                }).into(findViewById(R.id.imageView))
            },
            Response.ErrorListener {
                Toast.makeText(this,"something went wrong",Toast.LENGTH_LONG).show()
            })
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }
    fun clickOnShare(view: View) {
        val intent=Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey,checkout this cool meme $currentimageurl")
        val chooser=Intent.createChooser(intent,"share this meme using ...")
        startActivity(chooser)
    }
    fun clickOnNext(view: View) {
        loadmeme()
    }
}