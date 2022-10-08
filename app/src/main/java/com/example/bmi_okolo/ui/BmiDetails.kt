package com.example.bmi_okolo.ui

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.bmi_okolo.BuildConfig
import com.example.bmi_okolo.databinding.FragmentBmiDetailsBinding
import com.example.bmi_okolo.viewmodel.BmiViewModel
import com.google.android.ads.nativetemplates.NativeTemplateStyle
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class BmiDetails : Fragment() {

    private val bmiViewModel: BmiViewModel by activityViewModels()

    private var _binding: FragmentBmiDetailsBinding? = null
    private val binding get() = _binding!!

    private var bmiValue = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentBmiDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initializing the Google Admob SDK
        MobileAds.initialize(requireContext()
        ) { initializationStatus ->
            Toast.makeText(requireContext(),
                "AdMob Sdk Initialize $initializationStatus",
                Toast.LENGTH_LONG).show()
        }

        showNativeAd()

        bmiViewModel.apply {
            bodyMassIndexValue.observe(viewLifecycleOwner) {
                bmiValue = it
                binding.bmiValue.text = createSpannableString(bmiValue)
            }
            ponderalIndex.observe(viewLifecycleOwner) {
                binding.ponderalIndexText.text = it
            }
            bodyMassIndexCategory(bmiValue)
            bodyMassIndexCategory.observe(viewLifecycleOwner) {
                binding.bmiRangeText.text = it
            }
            bodyMassResultText.observe(viewLifecycleOwner) {
                binding.nameAndResult.text = it
            }
        }

        binding.apply {
            rateApp.setOnClickListener {
                rateMe()
            }
            shareScreenshot.setOnClickListener {
                takeScreenShot(activity?.window!!.decorView)
            }
        }
    }

    private fun showNativeAd() {
        val background = ColorDrawable(Color.WHITE)
        val adLoader = AdLoader.Builder(requireContext(), "ca-app-pub-3940256099942544/2247696110")
            .forNativeAd { nativeAd ->
                val style =
                    NativeTemplateStyle.Builder().withMainBackgroundColor(background).build()
                val template = binding.myTemplate
                template.setStyles(style)
                template.setNativeAd(nativeAd)
            }
            .build()

        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun takeScreenShot(view: View) {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        try {
            val mainDir = File(
                requireContext().externalCacheDir, "FilShare")
            if (!mainDir.exists()) {
                val mkdir = mainDir.mkdir()
            }
            val path = "$mainDir/Bmi-$timestamp.jpeg"
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            val imageFile = File(path)
            val fileOutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            shareScreenShot(imageFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //Share ScreenShot
    private fun shareScreenShot(imageFile: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + ".provider",
            imageFile)
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_TEXT, "This is my BMI. Do you want to check yours")
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            startActivity(Intent.createChooser(intent, "Share With"))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(requireContext(), "No App Available", Toast.LENGTH_SHORT).show()
        }
    }

    private fun rateMe() {
        try {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("market://details?id=" + activity?.packageName)))
        } catch (e: ActivityNotFoundException) {
            startActivity(Intent(Intent.ACTION_VIEW,
                Uri.parse("http://play.google.com/store/apps/details?id=" + activity?.packageName)))
        }
    }

    private fun createSpannableString(bmi: Double): SpannableString {
        val formattedString = String.format("%.2f", bmi)
        val spannableString = SpannableString(formattedString)

        if (spannableString.length == 5) {
            spannableString.setSpan(AbsoluteSizeSpan(35, true),
                2,
                5,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(AbsoluteSizeSpan(90, true),
                0,
                2,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        } else {
            spannableString.setSpan(AbsoluteSizeSpan(90, true),
                0,
                3,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(AbsoluteSizeSpan(35, true),
                3,
                6,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        }
        return spannableString
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}