package ryuunta.iot.ryuuntaesp.notification

import android.os.Bundle
import ryuunta.iot.ryuuntaesp.core.base.BaseFragment
import ryuunta.iot.ryuuntaesp.databinding.FragmentNotificationBinding

class NotificationFragment: BaseFragment<FragmentNotificationBinding, NotiViewModel>(FragmentNotificationBinding::inflate, NotiViewModel::class.java) {

    override fun initViews(savedInstanceState: Bundle?) {

    }

    companion object {
        fun newInstance() = NotificationFragment()
    }
}