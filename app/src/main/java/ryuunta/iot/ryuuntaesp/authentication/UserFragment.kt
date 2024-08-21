package ryuunta.iot.ryuuntaesp.authentication

import android.content.Intent
import android.view.View
import ryuunta.iot.ryuuntaesp.InitiationActivity
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentUserBinding
import ryuunta.iot.ryuuntaesp.main.MainActivity
import ryuunta.iot.ryuuntaesp.main.MainViewModel
import ryuunta.iot.ryuuntaesp.utils.setPreventDoubleClick

class UserFragment: BaseFragment<FragmentUserBinding, MainViewModel>(FragmentUserBinding::inflate, MainViewModel::class.java) {

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).headerHome(false)
    }
    override fun initViews(view: View) {
//        TODO("Not yet implemented")
    }

    override fun initEvents() {
        super.initEvents()
        binding.apply {
            btnLogout.setPreventDoubleClick {
                AuthenticationHelper.signOut {
                    val intent = Intent(requireActivity(), InitiationActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }
            }
        }
    }

}