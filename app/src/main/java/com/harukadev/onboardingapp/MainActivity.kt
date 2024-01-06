package com.harukadev.onboardingapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.harukadev.onboardingapp.adapter.OnBoardingViewPagerAdapter
import com.harukadev.onboardingapp.model.OnBoardingData

class MainActivity : AppCompatActivity() {

    private lateinit var onBoardingViewPagerAdapter: OnBoardingViewPagerAdapter
    private lateinit var tabLayout: TabLayout
    private lateinit var onBoardingViewPager: ViewPager
    private lateinit var btnNextSlide: TextView
	private lateinit var intent: Intent
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
		
		intent = Intent(this, HomeActivity::class.java)

        if (readFirstTimePreference()) {
            startActivity(intent)
			finish()
        }

        initializeViews()

        val onBoardingData = createOnBoardingDataList()

        setOnBoardingViewPagerAdapter(onBoardingData)
        setupListeners(onBoardingData)
    }

    private fun initializeViews() {
        tabLayout = findViewById(R.id.tabLayout)
        onBoardingViewPager = findViewById(R.id.screenPager)
        btnNextSlide = findViewById(R.id.btn_next_slide)
    }

    private fun createOnBoardingDataList(): List<OnBoardingData> {
        return listOf(
            OnBoardingData(getString(R.string.title1), getString(R.string.desc1), R.drawable.ic_gift),
            OnBoardingData(getString(R.string.title2), getString(R.string.desc2), R.drawable.ic_coupons),
            OnBoardingData(getString(R.string.title3), getString(R.string.desc3), R.drawable.ic_airplane)
        )
    }

    private fun setOnBoardingViewPagerAdapter(onBoardingData: List<OnBoardingData>) {
        onBoardingViewPagerAdapter = OnBoardingViewPagerAdapter(this, onBoardingData)
        onBoardingViewPager.adapter = onBoardingViewPagerAdapter
        tabLayout.setupWithViewPager(onBoardingViewPager)
    }

    private fun setupListeners(onBoardingData: List<OnBoardingData>) {
        btnNextSlide.setOnClickListener {
            if (position < onBoardingData.size) {
                position++
                onBoardingViewPager.currentItem = position
            }
			if (position == onBoardingData.size) {
                saveFirstTimePreference()
                startActivity(intent)
				finish()
            }
        }

        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                position = tab!!.position
                btnNextSlide.text = if (tab.position == onBoardingData.size - 1) {
                    "Ir para o app"
                } else {
                    "Próximo"
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Não é necessário implementar neste caso
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Não é necessário implementar neste caso
            }
        })
    }

    private fun saveFirstTimePreference() {
        val preferences: SharedPreferences = applicationContext.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putBoolean("firstTime", true)
        editor.apply()
    }

    private fun readFirstTimePreference(): Boolean {
        val preferences: SharedPreferences = applicationContext.getSharedPreferences("preferences", Context.MODE_PRIVATE)
        return preferences.getBoolean("firstTime", false)
    }
}
