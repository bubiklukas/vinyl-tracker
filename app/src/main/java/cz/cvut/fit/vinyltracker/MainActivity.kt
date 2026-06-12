package cz.cvut.fit.vinyltracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cz.cvut.fit.vinyltracker.ui.navigation.Navigation
import cz.cvut.fit.vinyltracker.ui.theme.VinylTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VinylTrackerTheme {
                Navigation()
            }
        }
    }
}
