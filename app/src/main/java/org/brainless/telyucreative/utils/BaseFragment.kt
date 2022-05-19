package org.brainless.telyucreative.utils

import android.app.Dialog
import androidx.fragment.app.Fragment
import org.brainless.telyucreative.databinding.DialogProgressBinding

open class BaseFragment : Fragment() {

    private lateinit var mProgressDialog: Dialog
    private lateinit var binding : DialogProgressBinding

    fun showProgressDialog(text: String) {

        binding = DialogProgressBinding.inflate(layoutInflater)

        mProgressDialog = Dialog(requireContext())
        mProgressDialog.setContentView(binding.root)

        binding.apply {
            tvProgress.text = text
        }

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        //Start the dialog and display it on screen.
        mProgressDialog.show()
    }

    /**
     * This function is used to dismiss the progress dialog if it is visible to user.
     */
    fun hideProgressDialog() {
        mProgressDialog.dismiss()
    }
}