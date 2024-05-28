package app.okcredit.merchant.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import app.okcredit.merchant.MerchantApplication

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MerchantApplication(appScreenModelFactory = applicationComponent.appScreenModelFactory)
        }
    }
}
