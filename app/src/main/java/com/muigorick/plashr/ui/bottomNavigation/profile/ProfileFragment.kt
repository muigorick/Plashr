package com.muigorick.plashr.ui.bottomNavigation.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import com.muigorick.plashr.BuildConfig
import com.muigorick.plashr.R
import com.muigorick.plashr.account.AccountManager
import com.muigorick.plashr.api.LoginService
import com.muigorick.plashr.dataModels.login.AccessToken
import com.muigorick.plashr.dataModels.users.LoggedUser
import com.muigorick.plashr.dataModels.users.User
import com.muigorick.plashr.databinding.FragmentProfileBinding
import com.muigorick.plashr.network.AppModule
import com.muigorick.plashr.ui.activities.mainActivity.MainActivity
import com.muigorick.plashr.ui.activities.settings.SettingsActivity
import com.muigorick.plashr.utils.Plashr
import dagger.hilt.android.AndroidEntryPoint
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    companion object {
        fun newInstance() = ProfileFragment()
        val AUTH_ENDPOINT: Uri = Uri.parse("https://unsplash.com/oauth/authorize")
        val TOKEN_ENDPOINT: Uri = Uri.parse("https://unsplash.com/oauth/token")
        val SIGN_UP_URL: Uri = Uri.parse("https://unsplash.com/join")
    }

    private val authorizationService = AuthorizationService(Plashr.instance)

    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            // There are no request codes
            val data: Intent? = result.data
            val code: String = data?.data?.getQueryParameter("code")!!
            Log.i("ACCESS CODE TAG", "Code: $code ")
            binding.notSignedInLayout.root.visibility = View.GONE
            binding.loggingInLayout.root.visibility = View.VISIBLE
            requestAccessToken(code)
        } else {

        }
    }

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val profileFragmentViewModel: ProfileFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.profile_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Handle the menu selection
                return when (menuItem.itemId) {
                    R.id.settings -> {
                        val settingsIntent = Intent(requireActivity(), SettingsActivity::class.java)
                        startActivity(settingsIntent)
                        return true
                    }
                    R.id.profile_logout -> {
                        if (AccountManager().isAppAuthorized()) {
                            AccountManager().logoutUser()
                            restartApp()
                        }
                        return true
                    }

                    else -> false
                }
            }

        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        return binding.root
    }

    /**
     * On view created.
     * TODO Re-enable the login button onclick and fix the login pattern.
     *
     * @param view
     * @param savedInstanceState
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (AccountManager().isAppAuthorized()) {
            binding.notSignedInLayout.root.visibility = View.GONE
        } else {
            binding.notSignedInLayout.root.visibility = View.VISIBLE
            /* binding.notSignedInLayout.loginButton.setOnClickListener {
                 openSignInWebPage()
             }*/
            binding.notSignedInLayout.registerNowButton.setOnClickListener {
                openRegisterNowWebPage()
            }
        }

        profileFragmentViewModel.accessTokenFetched.observe(
            viewLifecycleOwner
        ) { fetchStatus ->
            if (fetchStatus) {
                //getLoggedUserProfile()
            }
        }

        profileFragmentViewModel.loggedUserProfileFetched.observe(
            viewLifecycleOwner
        ) { fetchStatus ->
            if (fetchStatus) {
                // getLoggedUserPublicProfile(AccountManager().getUsername()!!)
            }
        }

        profileFragmentViewModel.loggedUserPublicProfileFetched.observe(
            viewLifecycleOwner
        ) { fetchStatus ->
            if (fetchStatus) profileFragmentViewModel.restartApp(true)

        }

        profileFragmentViewModel.restartApp.observe(
            viewLifecycleOwner
        ) { restartStatus ->
            if (restartStatus) {
                restartApp()
            }
        }
        profileFragmentViewModel.message.observe(
            viewLifecycleOwner
        ) { message ->
            binding.loggingInLayout.loginStatusDescription.text = message
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Opens the Join Unsplash URL
     */
    private fun openRegisterNowWebPage() {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(requireContext(), SIGN_UP_URL)
    }

    /**
     * Opens the Sign In Unsplash URL
     */
    private fun openSignInWebPage() {
        val serviceConfiguration = AuthorizationServiceConfiguration(
            AUTH_ENDPOINT /* auth endpoint */, TOKEN_ENDPOINT /* token endpoint */
        )
        val builder = AuthorizationRequest.Builder(
            serviceConfiguration,
            BuildConfig.ACCESS_KEY,
            ResponseTypeValues.CODE,
            Uri.parse(BuildConfig.REDIRECT_URI)
        )
        builder.setScopes(
            "public",
            "read_user",
            "write_user",
            "read_photos",
            "write_photos",
            "write_likes",
            "write_followers",
            "read_collections",
            "write_collections"
        )
        val request = builder.build()

        val intent = authorizationService.getAuthorizationRequestIntent(request)

        resultLauncher.launch(intent)
    }

    /**
     *  Gets the request access token that is used as an authentication bearer in the retrofit interceptor to make calls when user has authorized the app to log them in.
     *  @param codeToGetToken Code user to call the Access Token. This code is returned with the app callback url.
     */
    private fun requestAccessToken(codeToGetToken: String?) {
        profileFragmentViewModel.message(getString(R.string.fetching_access_token_message))

        val loginService: LoginService =
            AppModule.provideRetrofit().create(LoginService::class.java)
        val getAccessToken = loginService.getAccessToken(
            BuildConfig.ACCESS_KEY,
            BuildConfig.SECRET_KEY,
            BuildConfig.REDIRECT_URI,
            codeToGetToken!!,
            "authorization_code"
        )
        getAccessToken.enqueue(object : Callback<AccessToken?> {
            override fun onResponse(call: Call<AccessToken?>, response: Response<AccessToken?>) {
                assert(response.body() != null)
                Log.i("Access Token", "onResponse: ${response.body()!!.accessToken}")
                AccountManager().addAuthorization(
                    isAuthorized = true, response.body()!!.accessToken
                )
                profileFragmentViewModel.accessTokenFetched(fetchStatus = true)
            }

            override fun onFailure(call: Call<AccessToken?>, t: Throwable) {
                profileFragmentViewModel.accessTokenFetched(fetchStatus = false)
            }
        })
    }

    /**
     * Used to restart the app after the user is logged in so that we may be able to
     * make use of the user public functions in unsplash eg. liking pictures,creating collections etc,
     */
    private fun restartApp() {
        profileFragmentViewModel.message(getString(R.string.fetching_user_public_profile_message))

        val i: Intent =
            requireActivity().baseContext.packageManager.getLaunchIntentForPackage(requireActivity().baseContext.packageName)!!
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(
            Intent(Plashr.instance, MainActivity::class.java)
        )
    }

    private fun getLoggedUserProfile() {
        profileFragmentViewModel.message(getString(R.string.fetching_user_profile_message))
        val accessTokenCall =
            AppModule.provideUserDataService(AppModule.provideRetrofit()).getLoggedUserProfile()
        accessTokenCall.enqueue(object : Callback<LoggedUser> {
            override fun onResponse(call: Call<LoggedUser>, response: Response<LoggedUser>) {
                AccountManager().addUserDetails(
                    userID = response.body()?.userId,
                    username = response.body()?.username,
                    firstName = response.body()?.firstName,
                    lastName = response.body()?.lastName,
                    profilePictureURL = null
                )
                profileFragmentViewModel.loggedUserProfileFetched(fetchStatus = true)
            }

            override fun onFailure(call: Call<LoggedUser>, t: Throwable) {
                // TODO("Not yet implemented")
                profileFragmentViewModel.loggedUserProfileFetched(fetchStatus = false)
            }
        })
    }

    private fun getLoggedUserPublicProfile(username: String) {
        profileFragmentViewModel.message(getString(R.string.fetching_user_public_profile_message))
        val accessTokenCall = AppModule.provideUserDataService(AppModule.provideRetrofit())
            .getUserProfile(username = username)
        accessTokenCall.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                AccountManager().addUserDetails(
                    userID = response.body()?.userId,
                    username = response.body()?.username,
                    firstName = response.body()?.firstName,
                    lastName = response.body()?.lastName,
                    profilePictureURL = response.body()?.profileImage?.mediumProfileImage
                )
                profileFragmentViewModel.loggedUserPublicProfileFetched(fetchStatus = true)
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                // TODO("Not yet implemented")
                profileFragmentViewModel.loggedUserPublicProfileFetched(fetchStatus = false)
            }
        })
    }

}