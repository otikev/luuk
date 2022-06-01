package views

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.luuk.common.R

class LuukDialog : DialogFragment() {
    val TAG = LuukDialog.javaClass.canonicalName
    var message: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.app_name)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok)) { _, _ -> }
            .create()
    }

    companion object {
        const val TAG = "LuukDialog"

        fun newInstance(message: String): LuukDialog {
            val dialog = LuukDialog()
            dialog.message = message
            return dialog
        }
    }
}