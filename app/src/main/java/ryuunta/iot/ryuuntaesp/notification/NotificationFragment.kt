package ryuunta.iot.ryuuntaesp.notification

import android.view.View
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentNotificationBinding
import ryuunta.iot.ryuuntaesp.main.MainActivity

class NotificationFragment: BaseFragment<FragmentNotificationBinding, NotiViewModel>(FragmentNotificationBinding::inflate, NotiViewModel::class.java) {

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).headerHome(true)
    }
    override fun initViews(view: View) {
//        TODO("Not yet implemented")
    }
}