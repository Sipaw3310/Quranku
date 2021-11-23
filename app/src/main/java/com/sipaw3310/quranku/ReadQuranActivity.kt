package com.sipaw3310.quranku

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Xml
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.animation.doOnEnd
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.*
import org.xmlpull.v1.XmlPullParser
import java.util.*


class ReadQuranActivity : AppCompatActivity() {

    lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_quran)

        //tabLayout = findViewById(R.id.tab_layout_ReadQuran)

        /*ObjectAnimator.ofFloat(tabLayout, "alpha", 1f).apply {
            duration = 500
            start()
            doOnEnd {
                val suraList = intent.getStringArrayListExtra("suraList")
                suraList?.forEach {
                    val newTab = tabLayout.newTab()
                    newTab.text = it
                    tabLayout.addTab(newTab)
                }
            }
        }*/

        /*val suraList = intent.getStringArrayListExtra("suraList")
        suraList?.forEach {
            val newTab = tabLayout.newTab()
            newTab.text = it
            tabLayout.addTab(newTab)
        }*/

        /*val model: QuranViewModel by viewModels()
            //val firstSecond = Calendar.MILLISECOND
            resources.openRawResource(R.raw.quran_data).use {
                val parser: XmlPullParser = Xml.newPullParser()
                parser.setInput(it, null)
                parser.nextTag()
                var endParsing: Boolean = false
                // Parsing sura
                parser.nextTag()
                parser.require(XmlPullParser.START_TAG, null, "suras")
                while (!endParsing) {
                    if (parser.name == "sura" && parser.eventType == XmlPullParser.START_TAG) {
                        model.sura.value!!.add(parser.getAttributeValue(null, "tname"))
                    }

                    parser.nextTag()
                    if (parser.name == "suras") endParsing = true
                }
            }

        val observer = Observer<MutableList<String>> { sura ->
            Toast.makeText(this, "abc", Toast.LENGTH_SHORT).show()
            sura.forEach {
                val newTab = tabLayout.newTab()
                newTab.text = it
                tabLayout.addTab(newTab)
            }
        }

        model.sura.observe(this, observer)*/

    }
}

/*class QuranViewModel : ViewModel() {
    val sura: MutableLiveData<MutableList<String>> by lazy {
        MutableLiveData<MutableList<String>>().also {
            it.value = mutableListOf("abc")
        }
    }
}*/