package ryuunta.iot.ryuuntaesp.authentication

import android.os.Bundle
import com.bumptech.glide.Glide
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentUserBinding
import ryuunta.iot.ryuuntaesp.MainActivity
import ryuunta.iot.ryuuntaesp.MainViewModel
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.setDarkModeTheme
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class UserFragment : BaseFragment<FragmentUserBinding, MainViewModel>(
    FragmentUserBinding::inflate,
    MainViewModel::class.java
) {

    private val TAG = "UserFragment"

    private var isDarkmode = false

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).headerHome(false)

    }

    override fun initViews(savedInstanceState: Bundle?) {
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
            btnSwitchDarkmode.setPreventDoubleClick {
                isDarkmode = !isDarkmode
                setDarkModeTheme(isDarkmode)
                RLog.d(TAG, "darkmode: $isDarkmode")
            }
            btnLogout.setPreventDoubleClick {
                (activity as MainActivity).logout()
            }
        }
    }

    companion object {
        fun newInstance() = UserFragment()
    }

}