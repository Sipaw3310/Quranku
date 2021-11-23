package com.sipaw3310.quranku

import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Xml
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.animation.doOnCancel
import androidx.core.animation.doOnEnd
import androidx.core.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigationrail.NavigationRailView
import kotlinx.coroutines.*
import org.xmlpull.v1.XmlPullParser

class MainActivity : AppCompatActivity() {

    lateinit var toolbar: MaterialToolbar
    lateinit var viewPager: ViewPager2
    lateinit var navRail: NavigationRailView
    lateinit var sheet: ConstraintLayout
    lateinit var sheetToolbar1: MaterialToolbar
    lateinit var sheetToolbar2: MaterialToolbar
    lateinit var readQuranRecyclerview: RecyclerView
    var sheetAnimation: ObjectAnimator? = null

    //var suraCache = mutableListOf<SuraCache>()
    var statusBarHeight = 0
    var actionBarSize = 0
    var currentSura = -1
    var isSheetOpened = false
    var isSheetHasBeenOpened = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.home_toolbar)
        viewPager = findViewById(R.id.home_viewPager)
        navRail = findViewById(R.id.home_navigationRail)
        sheet = findViewById(R.id.home_readQuranLayout)
        sheetToolbar1 = findViewById(R.id.home_readQuranToolbar1)
        sheetToolbar2 = findViewById(R.id.home_readQuranToolbar2)
        readQuranRecyclerview = findViewById(R.id.home_readQuranRecyclerview)

        val attrActionBarSize = obtainStyledAttributes(intArrayOf(R.attr.actionBarSize))
        actionBarSize = attrActionBarSize.getDimension(0, 0f).toInt()
        attrActionBarSize.recycle()

        viewPager.adapter = ViewPagerAdapter(this)
        viewPager.isUserInputEnabled = false
        readQuranRecyclerview.layoutManager = LinearLayoutManager(this)

        sheetToolbar1.setOnMenuItemClickListener {
            if (it.itemId == R.id.readQuranMenu_minimize) {
                minimizeQuranSheet()
            }
            return@setOnMenuItemClickListener true
        }
        sheetToolbar2.setOnMenuItemClickListener {
            if (it.itemId == R.id.readQuranMenu_open) {
                openQuranSheet(null, null)
            }
            return@setOnMenuItemClickListener true
        }

        sheet.translationY = resources.displayMetrics.heightPixels.toFloat()

        // Window inset logic
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById<CoordinatorLayout>(R.id.home_parent)) { v, insets ->
                statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top

                toolbar.layoutParams.height = actionBarSize + statusBarHeight
                toolbar.setPadding(0, statusBarHeight, 0, 0)
                toolbar.requestLayout()

                sheetToolbar1.layoutParams.height = actionBarSize + statusBarHeight
                sheetToolbar1.setPadding(0, statusBarHeight, 0, 0)
                sheetToolbar1.requestLayout()

                //Toast.makeText(this, "${appBarLayout.layoutParams.height}", Toast.LENGTH_SHORT).show()
                return@setOnApplyWindowInsetsListener WindowInsetsCompat.CONSUMED
            }

        }

        navRail.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.quran_menu -> {
                    viewPager.currentItem = 0
                    true
                }
                R.id.bookmark_menu -> {
                    viewPager.currentItem = 1
                    true
                }
                else -> false
            }
        }
    }

    inner class ViewPagerAdapter(fActivity: FragmentActivity): FragmentStateAdapter(fActivity) {
        override fun getItemCount(): Int = 2

        override fun createFragment(pos: Int): Fragment {
            lateinit var f: Fragment
            when(pos) {
                0 -> f = QuranHomeFragment { name, index -> openQuranSheet(name, index) }
                1 -> f = BookmarkHomeFragment()
            }
            return f
        }
    }

    override fun onBackPressed() {
        if(isSheetOpened) {
            minimizeQuranSheet()
        } else super.onBackPressed()
    }

    private fun openQuranSheet(suraName: String?, suraIndex: Int?) {
        isSheetOpened = true

        if (suraName != null && suraIndex != null && suraIndex != currentSura) {
            readQuranRecyclerview.adapter = ReadQuranRecyclerviewAdapter(getSura(suraIndex))
            sheetToolbar1.title = suraName
            sheetToolbar2.title = suraName
            currentSura = suraIndex
        }
        if (!isSheetHasBeenOpened) {
            val spacer: View = findViewById(R.id.home_spacer)
            spacer.layoutParams.height = actionBarSize
            spacer.requestLayout()
        }
        //sheetToolbar.layoutParams.height = actionBarSize + statusBarHeight
        //sheetToolbar1.setPadding(0, statusBarHeight, 0, 0)

        sheetToolbar1.visibility = View.VISIBLE
        sheetToolbar2.visibility = View.GONE

        sheetAnimation?.cancel()
        sheetAnimation = ObjectAnimator.ofFloat(sheet, "translationY", 0f).apply {
            var canceled = false
            duration = 300
            interpolator = DecelerateInterpolator()
            sheet.visibility = View.VISIBLE
            doOnCancel { canceled = true }
            doOnEnd {
                if (!canceled) {
                    viewPager.visibility = View.GONE
                    navRail.visibility = View.GONE
                }
            }
            start()
        }
    }

    private fun minimizeQuranSheet() {
        isSheetOpened = false

        sheetToolbar1.visibility = View.GONE
        sheetToolbar2.visibility = View.VISIBLE

        sheetAnimation?.cancel()
        sheetAnimation = ObjectAnimator.ofFloat(sheet, "translationY", resources.displayMetrics.heightPixels.toFloat() - actionBarSize).apply {
            duration = 300
            interpolator = DecelerateInterpolator()
            viewPager.visibility = View.VISIBLE
            navRail.visibility = View.VISIBLE
            start()
        }
        //sheetToolbar.layoutParams.height = actionBarSize
        //sheetToolbar1.setPadding(0, 0, 0, statusBarHeight)
    }

    private fun getSura(index: Int): List<AyaClass> {
        /*suraCache.forEach {
            if (it.index == index) {
                //Toast.makeText(this, "found in cache", Toast.LENGTH_LONG).show()
                return it.ayaList
            }
        }*/
        val ayaList = mutableListOf<AyaClass>()
        val resource = resources.openRawResource(R.raw.quran_simple)
        val parser: XmlPullParser = Xml.newPullParser()
        parser.setInput(resource, null)
        parser.nextTag()
        var endParsing1 = false
        var endParsing2 = false
        // Find the sura
        parser.require(XmlPullParser.START_TAG, null, "quran")
        while (!endParsing1) {
            if (parser.name == "sura"
                && parser.eventType == XmlPullParser.START_TAG
                && parser.getAttributeValue(null, "index").toInt() == index) {
                // Parse all aya
                parser.require(XmlPullParser.START_TAG, null, "sura")
                parser.nextTag()
                while (!endParsing2) {
                    if (parser.name == "aya" && parser.eventType == XmlPullParser.START_TAG) {
                        ayaList.add(AyaClass(parser.getAttributeValue(null, "text")))
                    }
                    // Go to the next aya
                    parser.nextTag()
                    // if all of the aya has been parsed, end the parsing operation
                    if(parser.name == "sura") {
                        endParsing2 = true
                    }
                }
                // End parsing sura
                endParsing1 = true
            }
            // Go to the next tag if the sura hasn't been found
            parser.nextTag()
            // End parsing if the sura is not founded
            if(parser.name == "quran") {
                endParsing1 = true
            }
        }
        // Cache all of the parsed aya in a variable
        //suraCache.add(SuraCache(index, ayaList))
        // Return all of the parsed aya
        return ayaList
    }
}

//class SuraCache(val index: Int, val ayaList: List<AyaClass>)