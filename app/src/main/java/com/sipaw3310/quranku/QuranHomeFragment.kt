package com.sipaw3310.quranku

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Interpolator
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Xml
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigationrail.NavigationRailView
import org.xmlpull.v1.XmlPullParser

class QuranHomeFragment(private val readQuran: (name: String, index: Int) -> Unit) : Fragment(), AdapterView.OnItemSelectedListener {
    private lateinit var suraRecyclerView: RecyclerView
    private lateinit var juzRecyclerView: RecyclerView
    var currentMode: Int = 0

    private val suraList = mutableListOf<SuraListClass>()
    private val juzList = mutableListOf<JuzListClass>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_quran_home, container, false)
        suraRecyclerView = view.findViewById(R.id.recyclerview_sura_quran_home_fragment)
        juzRecyclerView = view.findViewById(R.id.recyclerview_juz_quran_home_fragment)

        // Parsing data
        resources.openRawResource(R.raw.quran_data).use {
            val parser: XmlPullParser = Xml.newPullParser()
            parser.setInput(it, null)
            parser.nextTag()
            var endParsing: Boolean = false
            // Parsing sura
            parser.nextTag()
            parser.require(XmlPullParser.START_TAG, null, "suras")
            while (!endParsing) {
                if(parser.name == "sura" && parser.eventType == XmlPullParser.START_TAG) {
                    suraList.add(SuraListClass(
                        parser.getAttributeValue(null, "index").toInt(),
                        parser.getAttributeValue(null, "tname"),
                        parser.getAttributeValue(null, "name")
                    ))
                }

                parser.nextTag()
                if(parser.name == "suras") endParsing = true
            }
            endParsing = false
            // Parsing juz
            parser.nextTag()
            parser.require(XmlPullParser.START_TAG, null, "juzs")
            while (!endParsing) {
                if(parser.name == "juz" && parser.eventType == XmlPullParser.START_TAG) {
                    juzList.add(JuzListClass(
                        parser.getAttributeValue(null, "sura"),
                        parser.getAttributeValue(null, "aya")
                    ))
                }

                parser.nextTag()
                if(parser.name == "juzs") endParsing = true
            }
        }

        val spinner: Spinner = view.findViewById(R.id.spinner_quran_home_fragment)

        // Recyclerview
        suraRecyclerView.layoutManager = LinearLayoutManager(context)
        juzRecyclerView.layoutManager = LinearLayoutManager(context)
        suraRecyclerView.adapter = SuraRecyclerviewAdapter(suraList) { index -> readQuran(suraList[index-1].sura, index) }
        juzRecyclerView.adapter = JuzRecyclerviewAdapter(juzList)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.spinner_list_quran_home_fragment,
            R.layout.support_simple_spinner_dropdown_item
        ).also {
            it.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            spinner.adapter = it
            spinner.onItemSelectedListener = this
        }

        return view
    }

    // Spinner Adapter
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if(position == 0 && currentMode != 0) {
            suraRecyclerView.alpha = 0f
            suraRecyclerView.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(suraRecyclerView, "alpha", 1f).apply {
                duration = 200
                start()
            }
            ObjectAnimator.ofFloat(juzRecyclerView, "alpha", 0f).apply {
                duration = 200
                start()
                doOnEnd {
                    juzRecyclerView.visibility = View.GONE
                }
            }
            currentMode = 0
        } else if(position == 1 && currentMode != 1) {
            juzRecyclerView.alpha = 0f
            juzRecyclerView.visibility = View.VISIBLE
            ObjectAnimator.ofFloat(juzRecyclerView, "alpha", 1f).apply {
                duration = 200
                start()
            }
            ObjectAnimator.ofFloat(suraRecyclerView, "alpha", 0f).apply {
                duration = 200
                start()
                doOnEnd {
                    suraRecyclerView.visibility = View.GONE
                }
            }
            currentMode = 1
        }
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

}