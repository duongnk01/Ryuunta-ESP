package ryuunta.iot.ryuuntaesp.notification

import android.os.Bundle
import ryuunta.iot.ryuuntaesp.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentNotificationBinding
import ryuunta.iot.ryuuntaesp.MainActivity

class NotificationFragment: BaseFragment<FragmentNotificationBinding, NotiViewModel>(FragmentNotificationBinding::inflate, NotiViewModel::class.java) {

    override fun initViews(savedInstanceState: Bundle?) {

    }

    companion object {
        fun newInstance() = NotificationFragment()
    }
}