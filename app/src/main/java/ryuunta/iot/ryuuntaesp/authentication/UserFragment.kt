package ryuunta.iot.ryuuntaesp.authentication

import android.content.Intent
import android.view.View
import com.bumptech.glide.Glide
import ryuunta.iot.ryuuntaesp.InitiationActivity
import ryuunta.iot.ryuuntaesp.R
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentUserBinding
import ryuunta.iot.ryuuntaesp.main.MainActivity
import ryuunta.iot.ryuuntaesp.main.MainViewModel
import ryuunta.iot.ryuuntaesp.utils.RLog
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class UserFragment : BaseFragment<FragmentUserBinding, MainViewModel>(
    FragmentUserBinding::inflate,
    MainViewModel::class.java
) {

    private val TAG = "UserFragment"

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).headerHome(false)

    }

    override fun initViews(view: View) {
        AuthenticationHelper.getInfoUser()?.let {
//            RLog.d(TAG, "User name: ${it.displayName}")
//            RLog.d(TAG, "mail: ${it.email}")
//            RLog.d(TAG, "avatar url: ${it.photoUrl}")
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
                (activity as MainActivity).logout()
            }
        }
    }

}