package views

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.luuk.common.databinding.ProgressBarBinding

class CustomProgressBar : DialogFragment() {
    lateinit var binding: ProgressBarBinding
    companion object {
        const val TAG = "ProgressDialog"
        fun newInstance() = CustomProgressBar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        dialog?.setCanceledOnTouchOutside(false)
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =  ProgressBarBinding.inflate(inflater, container, false)
        return binding.root;
    }

    fun setProgress(progress: Int){
        binding.pbProgress.progress = progress
    }
}