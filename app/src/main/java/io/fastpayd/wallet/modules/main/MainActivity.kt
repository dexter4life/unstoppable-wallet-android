package io.fastpayd.wallet.modules.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.fragment.NavHostFragment
import com.walletconnect.web3.wallet.client.Wallet
import io.fastpayd.wallet.R
import io.fastpayd.wallet.core.App
import io.fastpayd.wallet.core.BaseActivity
import io.fastpayd.wallet.core.slideFromBottom
//import io.fastpayd.wallet.modules.authentication.AuthenticationActivity
import io.fastpayd.wallet.modules.intro.IntroActivity
import io.fastpayd.wallet.modules.keystore.KeyStoreActivity
import io.fastpayd.wallet.modules.lockscreen.LockScreenActivity
import io.horizontalsystems.core.hideKeyboard

class MainActivity : BaseActivity() {

    private lateinit var hideContent: View

    private val viewModel by viewModels<MainActivityViewModel> {
        MainActivityViewModel.Factory()
    }

    override fun onResume() {
        super.onResume()
        validate()
        viewModel.onResume()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        viewModel.setIntent(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        hideContent = findViewById(R.id.hideContent)

        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHost.navController

        navController.setGraph(R.navigation.main_graph, intent.extras)
        navController.addOnDestinationChangedListener { _, _, _ ->
            currentFocus?.hideKeyboard(this)
        }

        viewModel.navigateToMainLiveData.observe(this) {
            if (it) {
                navController.popBackStack(navController.graph.startDestinationId, false)
                viewModel.onNavigatedToMain()
            }
        }

        viewModel.wcEvent.observe(this) { wcEvent ->
            if (wcEvent != null) {
                when (wcEvent) {
                    is Wallet.Model.SessionRequest -> {
                        navController.slideFromBottom(R.id.wcRequestFragment)
                    }
                    is Wallet.Model.SessionProposal -> {
                        navController.slideFromBottom(R.id.wcSessionFragment)
                    }
                    else -> {}
                }

                viewModel.onWcEventHandled()
            }
        }

        viewModel.contentHidden.observe(this) { hidden ->
            hideContent.visibility = if (hidden) View.VISIBLE else View.GONE
        }

        viewModel.setIntent(intent)
    }

    private fun validate() = try {
        viewModel.validate()
    } catch (e: MainScreenValidationError.NoSystemLock) {
        KeyStoreActivity.startForNoSystemLock(this)
        finish()
    } catch (e: MainScreenValidationError.KeyInvalidated) {
        KeyStoreActivity.startForInvalidKey(this)
        finish()
    } catch (e: MainScreenValidationError.UserAuthentication) {
        KeyStoreActivity.startForUserAuthentication(this)
        finish()
    } catch (e: MainScreenValidationError.Welcome) {
        IntroActivity.start(this)
        finish()
    } catch (e: MainScreenValidationError.Unlock) {
        LockScreenActivity.start(this)
    } catch (e: MainScreenValidationError.KeystoreRuntimeException) {
        Toast.makeText(App.instance, "Issue with Keystore", Toast.LENGTH_SHORT).show()
        finish()
    }
}
