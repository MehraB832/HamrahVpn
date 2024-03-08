package sp.inetvpn.ui.split

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.chad.library.adapter4.BaseQuickAdapter
import com.xray.lite.ui.BaseActivity
import sp.inetvpn.R
import sp.inetvpn.databinding.ActivitySplitBinding
import sp.inetvpn.model.SplitList

/**
 * by Mehrab on 2024,
 * Java && Kotlin,
 * Easy
 */
class SplitActivity : BaseActivity() {
    private var binding: ActivitySplitBinding? = null
    private val returnIntent = Intent()

    private val mAnimationAdapter: AnimationAdapter = AnimationAdapter().apply {
        // 打开 Adapter 的动画
        animationEnable = true
        // 是否是首次显示时候加载动画
        isAnimationFirstOnly = false
    }

    override fun onResume() {
        super.onResume()
        val thread = Thread {
            runOnUiThread {
                SaveData().loadData(packageManager)

                binding!!.splitRecyclerView.adapter = mAnimationAdapter
                if (splitLists.isNotEmpty()) {
                    mAnimationAdapter.setItemAnimation(BaseQuickAdapter.AnimationType.SlideInBottom)
                }
                binding!!.animationLayout.llLayout.visibility = View.GONE
            }
        }
        thread.start()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplitBinding.inflate(layoutInflater)
        val view: View = binding!!.getRoot()
        setContentView(view)
        setSupportActionBar(null)
        binding!!.animationLayout.animationView.setAnimation(R.raw.loading_circle)
        binding!!.animationLayout.animationView.playAnimation()

        binding!!.headerLayout.llBack.setOnClickListener {
//            returnIntent.putExtra("restart", true)
            onBackPressed()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        setResult(RESULT_OK, returnIntent)
        finish()
        overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right)
    }

    companion object {
        val splitLists: MutableList<SplitList> = ArrayList()
    }
}