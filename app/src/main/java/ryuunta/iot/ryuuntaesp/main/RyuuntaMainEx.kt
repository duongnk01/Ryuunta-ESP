package ryuunta.iot.ryuuntaesp.main

import androidx.viewpager2.widget.ViewPager2
import ryuunta.iot.ryuuntaesp.adapter.MainPagerAdapter
import ryuunta.iot.ryuuntaesp.authentication.UserFragment
import ryuunta.iot.ryuuntaesp.home.HomeFragment
import ryuunta.iot.ryuuntaesp.manage.ManageFragment
import ryuunta.iot.ryuuntaesp.notification.NotificationFragment

fun RyuuntaMainFragment.initViewPager() {
    val fragmentList = listOf(
        HomeFragment.newInstance(),
        NotificationFragment.newInstance(),
        ManageFragment.newInstance(),
        UserFragment.newInstance()
    )

    binding.vpMain.apply {
        orientation = ViewPager2.ORIENTATION_HORIZONTAL
        isUserInputEnabled = false
        offscreenPageLimit = 1
        adapter = MainPagerAdapter(this@initViewPager, fragmentList)
        isSaveEnabled = false
    }
}