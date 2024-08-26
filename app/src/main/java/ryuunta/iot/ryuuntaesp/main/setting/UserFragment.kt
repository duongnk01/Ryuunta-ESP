package ryuunta.iot.ryuuntaesp.main.setting

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ryuunta.iot.ryuuntaesp.MainViewModel
import ryuunta.iot.ryuuntaesp.RMainActivity
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.core.base.Config
import ryuunta.iot.ryuuntaesp.databinding.FragmentUserBinding
import ryuunta.iot.ryuuntaesp.helper.AuthenticationHelper
import ryuunta.iot.ryuuntaesp.preference.ThemePreference
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.setDarkModeTheme
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick
import ryuunta.iot.ryuuntaesp.widget.SwitchView

class UserFragment : BaseFragment<FragmentUserBinding, MainViewModel>(
    FragmentUserBinding::inflate,
    MainViewModel::class.java
) {

    private val TAG = "UserFragment"

    override fun initViews(savedInstanceState: Bundle?) {
        AuthenticationHelper.getInfoUser()?.let {
            if (!it.displayName.isNullOrEmpty())
                binding.txtUsername.text = it.displayName
            binding.txtEmail.text = it.email

            if (it.photoUrl != null) {
                Glide.with(requireContext()).load(it.photoUrl).into(binding.imgUserAvt)
            }
        }
        binding.swTheme.setEnable(Config.isDarkMode)
    }

    override fun initEvents() {
        super.initEvents()
        binding.apply {
            btnLogout.setPreventDoubleClick {
                (activity as RMainActivity).logout()
            }

            swTheme.switchViewListener = object : SwitchView.SwitchViewListener {
                override fun onSwitchChange(enable: Boolean) {
                    if (enable != Config.isDarkMode) {
                        Config.isDarkMode = enable
                        setDarkModeTheme(Config.isDarkMode)
                        RLog.d(TAG, "isDarkMode change: $enable")
                        CoroutineScope(Dispatchers.Default).launch {
                            ThemePreference.setDarkMode(requireContext(), Config.isDarkMode)
                            withContext(Dispatchers.Main) {
                                requireActivity().startActivity(Intent(requireActivity(), RMainActivity::class.java))
                                requireActivity().finish()
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance() = UserFragment()
    }

}