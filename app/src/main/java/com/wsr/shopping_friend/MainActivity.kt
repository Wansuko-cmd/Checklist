package com.wsr.shopping_friend

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration.TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE
import com.wsr.shopping_friend.databinding.ActivityMainBinding
import com.wsr.shopping_friend.preference.checkVersion

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //設定のバージョンの確認
        checkVersion(this)

        //広告
        val requestConfiguration = MobileAds.getRequestConfiguration()
            .toBuilder()
            .setTagForChildDirectedTreatment(TAG_FOR_CHILD_DIRECTED_TREATMENT_TRUE)
            .build()

        MobileAds.setRequestConfiguration(requestConfiguration)

        val adRequest: AdRequest = AdRequest.Builder().build()
        ActivityMainBinding.inflate(layoutInflater).adView.loadAd(adRequest)
    }
}