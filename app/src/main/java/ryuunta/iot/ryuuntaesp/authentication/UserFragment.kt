package ryuunta.iot.ryuuntaesp.authentication

import android.os.Bundle
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentUserBinding
import ryuunta.iot.ryuuntaesp.RMainActivity
import ryuunta.iot.ryuuntaesp.MainViewModel
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.helper.AuthenticationHelper
import ryuunta.iot.ryuuntaesp.preference.ThemePreference
import ryuunta.iot.ryuuntaesp.utils.setDarkModeTheme
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class UserFragment : BaseFragment<FragmentUserBinding, MainViewModel>(
    FragmentUserBinding::inflate,
    MainViewModel::class.java
) {

    private val TAG = "UserFragment"
    var isDarkMode = false

    override fun initViews(savedInstanceState: Bundle?) {
        ThemePreference.getThemePreferenceData(requireContext(), onError = {

        }, onComplete = {
            isDarkMode = it.isDarkMode
        })
        AuthenticationHelper.getInfoUser()?.let {
            if (!it.displayName.isNullOrEmpty())
                binding.txtUsername.text = it.displayName
            binding.txtEmail.text = it.email

            if (it.photoUrl != null) {
                Glide.with(requireContext()).load(it.photoUrl).into(binding.imgUserAvt)
            }
        }
    }

    override fun initEvents() {
        super.initEvents()
        binding.apply {
            btnLogout.setPreventDoubleClick {
                (activity as RMainActivity).logout()
            }

            btnSwitchDarkmode.setPreventDoubleClick {
                isDarkMode = !isDarkMode
                setDarkModeTheme(isDarkMode)
                CoroutineScope(Dispatchers.Default).launch {
                    ThemePreference.setDarkMode(requireContext(), isDarkMode)
                }
//                if (isDarkMode) {
//                    requireActivity().setTheme(R.style.Theme_RyuuntaESP)
//                } else {
//                    requireActivity().setTheme(R.style.Theme_RyuuntaESP_Dark)
//                }

            }
        }
    }

    companion object {
        fun newInstance() = UserFragment()
    }

}