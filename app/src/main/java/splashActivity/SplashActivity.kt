package splashActivity

import InicioSesion.MainActivity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ies.luiscarrillo.proyectofinaldamjlbsva.databinding.ActivitySplashBinding
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen


class SplashActivity : AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val screenSplash = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        screenSplash.setKeepOnScreenCondition{true}
        Thread.sleep(5000)
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}