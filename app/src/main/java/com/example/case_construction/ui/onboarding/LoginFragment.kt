package com.example.case_construction.ui.onboarding

import android.content.Context.WINDOW_SERVICE
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.case_construction.R
import com.example.case_construction.model.ResultData
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.ui.dialog.NoInternetDialog
import com.example.case_construction.ui.fragment.*
import com.example.case_construction.utility.PreferenceHelper.currentUser
import com.example.case_construction.utility.isInternetAvailable
import com.example.case_construction.utility.printLog
import com.example.case_construction.utility.toast
import com.google.zxing.WriterException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*


@AndroidEntryPoint
class LoginFragment : BaseFragment() {

    private val onboardViewModel by viewModels<OnboardViewModel>()

    var bitmap: Bitmap? = null
    var qrgEncoder: QRGEncoder? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {

        (activity as MainActivity).lockUnlockSideNav(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        tvLogin.setOnClickListener {
            pauseClick(it)
            if (!validation()) {
                requireActivity().toast("Fill all the required data")
                return@setOnClickListener
            }

            if (!requireContext().isInternetAvailable()) {
                (activity as MainActivity).showNoNetworkDialog(object :
                    NoInternetDialog.DialogListener {
                    override fun onOkClick() {

                    }
                })
                return@setOnClickListener
            }
            loginWithUserNameAndPassword(
                edUsername.text.toString().trim(),
                edPassword.text.toString().trim()
            )
        }
    }

    private fun loginWithUserNameAndPassword(username: String, password: String) {
        onboardViewModel.loginWithUsernameAndPassword(username, password)
            .observe(viewLifecycleOwner, Observer {
                when (it) {
                    is ResultData.Loading -> {
                        (requireActivity() as MainActivity).showLoadingDialog()
                    }
                    is ResultData.Success -> {
                        if (it.data == null) return@Observer
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        (activity as MainActivity).defaultPreference.currentUser = it.data.users[0]
                        "Logged in userType is: ${it.data.users[0].userType}".printLog(javaClass.name)
                        replaceFragment(SearchMachineFragment(), R.id.fragmentContainerView)
                        removeAllObservable()
                    }
                    is ResultData.Failed -> {
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        removeAllObservable()
                    }
                }

            })
    }

    private fun validation(): Boolean {
        if (edUsername.text.toString().isBlank()) {
            edUsername.error = "Required"
            return false
        }
        if (edPassword.text.toString().isBlank()) {
            edPassword.error = "Required"
            return false
        }
        return true
    }

    private fun removeAllObservable() {
        onboardViewModel.loginWithUsernameAndPassword("", "").removeObservers(requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initView()
    }

    private fun createQRCode(){
        // below line is for getting
        // the windowmanager service.
        // below line is for getting
        // the windowmanager service.
        val manager = requireContext().getSystemService(WINDOW_SERVICE) as WindowManager?

        // initializing a variable for default display.

        // initializing a variable for default display.
        val display: Display = manager!!.defaultDisplay

        // creating a variable for point which
        // is to be displayed in QR Code.

        // creating a variable for point which
        // is to be displayed in QR Code.
        val point = Point()
        display.getSize(point)

        // getting width and
        // height of a point

        // getting width and
        // height of a point
        val width: Int = point.x
        val height: Int = point.y

        // generating dimension from width and height.

        // generating dimension from width and height.
        var dimen = if (width < height) width else height
        dimen = dimen * 3 / 4

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = QRGEncoder("Case Construction", null, QRGContents.Type.TEXT, dimen)
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder!!.encodeAsBitmap()
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            appLogo.setImageBitmap(bitmap)
            val d: Drawable = BitmapDrawable(resources, bitmap)
            appLogo.setImageDrawable(d)
        } catch (e: WriterException) {
            // this method is called for
            // exception handling.
            e.toString().printLog(javaClass.name)
        }
    }

}