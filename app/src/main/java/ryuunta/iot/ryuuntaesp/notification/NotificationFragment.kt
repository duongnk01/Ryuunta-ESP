package ryuunta.iot.ryuuntaesp.notification

import android.os.Bundle
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentNotificationBinding
import ryuunta.iot.ryuuntaesp.MainActivity

class NotificationFragment: BaseFragment<FragmentNotificationBinding, NotiViewModel>(FragmentNotificationBinding::inflate, NotiViewModel::class.java) {

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).headerHome(true)
    }
    override fun initViews(view: Bundle?) {
//        TODO("Not yet implemented")
    }

    companion object {
        fun newInstance() = NotificationFragment()
    }
}