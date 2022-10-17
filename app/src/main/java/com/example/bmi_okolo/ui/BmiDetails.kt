package com.example.bmi_okolo.ui

import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.bmi_okolo.BuildConfig
import com.example.bmi_okolo.R
import com.example.bmi_okolo.databinding.FragmentBmiDetailsBinding
import com.example.bmi_okolo.ui.viewmodel.BmiViewModel
import com.google.android.gms.ads.*
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
    private var name = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentBmiDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showNativeAd()
        bmiValue = bmiViewModel.bodyMassIndex
        name = bmiViewModel.userName

        binding.apply {
            shareScreenshot.setOnClickListener {
                takeScreenShot(activity?.window!!.decorView)
            }
            ponderalIndexText.text = bmiViewModel.ponderalIndex
            bmiCategoryDescription.text = bmiCategoryByValue(bmiValue)
            bodyMassIndexValue.text = createSpannableString(bmiValue)
            bmiResults.text = bmiResultText(bmiValue,name)
            rate.setOnClickListener { rateMe() }
        }
    }

    private fun showNativeAd() {
        MobileAds.initialize(requireContext())
        val adLoader = AdLoader.Builder(requireContext(), getString(R.string.native_ad_unit_id))
            .forNativeAd { nativeAd ->
                binding.myTemplate.apply {
                    visibility = View.VISIBLE
                    setNativeAd(nativeAd)
                }
            }.withAdListener(object : AdListener() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.native_ad_not_loaded),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }

    private fun takeScreenShot(view: View)
    {
        val timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        try
        {
            val mainDir = File(
                requireContext().externalCacheDir , getString(R.string.file_share))

            val path = "$mainDir/Bmi-$timestamp.jpeg"
            val bitmap = Bitmap.createBitmap(view.width , view.height , Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.draw(canvas)

            val imageFile = File(path)
            val fileOutputStream = FileOutputStream(imageFile)
            bitmap.compress(Bitmap.CompressFormat.PNG , 100 , fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            shareScreenShot(imageFile)
        } catch (e: IOException)
        {
            e.printStackTrace()
        }
    }

    private fun shareScreenShot(imageFile: File) {
        val uri = FileProvider.getUriForFile(
            requireContext(),
            BuildConfig.APPLICATION_ID + getString(R.string.authority_provider),
            imageFile
        )
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.intent_extra_text))
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        try {
            startActivity(Intent.createChooser(intent, getString(R.string.intent_title)))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_available_app),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun rateMe() = try {
        startActivity(
            Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.uri_string) + activity?.packageName))
        )
    } catch (e: ActivityNotFoundException) {
        startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(getString(R.string.default_uri) + activity?.packageName)
            )
        )
    }

    private fun createSpannableString(bmi: Double): SpannableString {
        val formattedString = String.format("%.2f", bmi)
        val spannableString = SpannableString(formattedString)

        when (spannableString.length) {
            5 -> {
                with(spannableString) {
                    setSpan(AbsoluteSizeSpan(35, true), 2, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    setSpan(AbsoluteSizeSpan(90, true), 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
            else -> {
                with(spannableString) {
                    setSpan(AbsoluteSizeSpan(90, true), 0, 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                    setSpan(AbsoluteSizeSpan(35, true), 3, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
            }
        }
        return spannableString
    }

    private fun bmiCategoryByValue(bmi: Double): String {
        return when (bmi) {
            in 1.0..18.49 -> {
                resources.getString(R.string.underweight_text)
            }
            in 18.5..24.9 -> {
                resources.getString(R.string.normal_weight_text)
            }
            in 25.0..29.9 -> {
                resources.getString(R.string.overweight_text)
            }
            else -> {
                resources.getString(R.string.obesity_text)
            }
        }
    }

    private fun bmiResultText(bmi: Double, name: String): String {
        return when (bmi) {
            in 1.0..18.49 -> {
                resources.getString(R.string.underweight_result, name)
            }
            in 18.5..24.9 -> {
                resources.getString(R.string.normal_result, name)
            }
            in 25.0..29.9 -> {
                resources.getString(R.string.overweight_result, name)
            }
            else -> {
                resources.getString(R.string.obese_result, name)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}