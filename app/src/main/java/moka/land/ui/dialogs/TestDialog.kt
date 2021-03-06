package moka.land.ui.dialogs

import android.view.View
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moka.land.databinding.DialogTestBinding
import moka.land.dialog._BaseDialog

class TestDialog : _BaseDialog() {

    private val _view by lazy { DialogTestBinding.inflate(layoutInflater) }

    override fun getPositiveText() = "닫기"

    override fun getWidthRatio(): Float? = 0.9f

    override fun getContentView(): View = _view.root

    override fun init() {
        lifecycleScope.launch {
            setPositiveEnabled(false)
            delay(4000)
            setPositiveEnabled(true)
        }
    }

}
