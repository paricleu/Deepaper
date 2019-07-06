package com.hackathon.deepaper.content

import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.hackathon.deepaper.Constants
import com.hackathon.deepaper.R
import com.hackathon.deepaper.model.Message
import com.hackathon.deepaper.model.User
import kotlinx.android.synthetic.main.activity_content.*
import java.util.*

class ContentActivity : AppCompatActivity() {

    lateinit var mTTS: TextToSpeech

    companion object {
        const val CONTENT_KEY = "content_key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        mTTS = TextToSpeech(applicationContext, TextToSpeech.OnInitListener { status ->
            if (status != TextToSpeech.ERROR) {
                mTTS.language = Locale.GERMANY
            }
        })

        if (intent.hasExtra(CONTENT_KEY)) {
            val content = intent.getStringExtra(CONTENT_KEY)
            if (content == "video") {
                val uri = "android.resource://" + packageName + "/" + R.raw.karneval

                videoView.setVideoPath(uri)
                videoView.start()
            } else if (content == "text") {
                Handler().postDelayed({
                    mTTS.speak(
                        getString(R.string.newspaper_text),
                        TextToSpeech.QUEUE_FLUSH,
                        null,
                        "random"
                    )
                }, 1000)
            }
        }



        itemRv.layoutManager = LinearLayoutManager(this)
        val user1 = User(1, "Simone Lammbach", "")
        val user2 = User(1, "Thorsten Macher", "")
        itemRv.adapter = MessageAdapter(
            listOf(
                Message("Tolles Video! Das Karneval war super!!!!!", user1, "11.10"),
                Message(
                    "Ich war leider nicht da, an dem Wochenende war Hackathon :(",
                    user2,
                    "13:37"
                ),
                Message("Läuft bei euch!!!!", Constants.ME, "18:01")
            )
        )
    }

    override fun onStop() {
        super.onStop()

        mTTS.stop()
    }
}