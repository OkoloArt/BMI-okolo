package com.example.bmi_okolo.ui

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.bmi_okolo.R
import com.example.bmi_okolo.databinding.FragmentAddBmiDetailsBinding
import com.example.bmi_okolo.viewmodel.BmiViewModel
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
const val AD_UNIT_ID = "ca-app-pub-3940256099942544/1033173712"

class AddBmiDetails : Fragment() {

    private val bmiViewModel: BmiViewModel by activityViewModels()

    private var mInterstitialAd: InterstitialAd? = null

    private var _binding: FragmentAddBmiDetailsBinding? = null
    private val binding get() = _binding!!

    private var personHeight = 0
    private var personWeight = 0
    private var gender = arrayOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        _binding = FragmentAddBmiDetailsBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MobileAds.initialize(requireContext())
        loadIntAd()

        gender = resources.getStringArray(R.array.gender_array)
        binding.apply {
            weightNumberPicker.apply {
                minValue = 30
                maxValue = 100
            }
            heightNumberPicker.apply {
                minValue = 100
                maxValue = 250
            }
            genderPicker.apply {
                value = 0
                minValue = 0
                maxValue = 1
                displayedValues = gender
            }
            calculateBmi.setOnClickListener {
                calculateBmi()
            }
        }
    }

    private fun calculateBmi() {
        personHeight = binding.heightNumberPicker.value
        personWeight = binding.heightNumberPicker.value
        val name = binding.nameTextField.editText?.text.toString()
        var value = 0.0

        if (name.isBlank()) {
            binding.nameTextField.editText?.error = " name cannot be blank"
        } else {
            bmiViewModel.apply {
                calculateBodyMassIndex(personHeight, personWeight)
                calculatePonderalIndex(personHeight, personWeight)
                bodyMassIndexValue.observe(viewLifecycleOwner) {
                    value = it
                }
                bodyMassResultText(value, name)
            }
            showInterstitialAds()
        }
    }

    private fun showInterstitialAds() {
        // First we ensure the Interstitial ad is not nullable

        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    super.onAdFailedToShowFullScreenContent(p0)
                }

                override fun onAdShowedFullScreenContent() {
                    //Input your code here
                    super.onAdShowedFullScreenContent()
                }

                // When you exit the ad using the cancel button, the next activity is displayed.

                override fun onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent()
                    val action = AddBmiDetailsDirections.actionFirstFragmentToSecondFragment()
                    findNavController().navigate(action)
                }

                // What will happen when the ad is clicked
                override fun onAdClicked() {
                    //Input your code here
                    super.onAdClicked()
                }
            }
            mInterstitialAd?.show(requireActivity())
        } else {
            // If the Ad is not loaded, a toast will be displayed and then navigate to the second activity
            Toast.makeText(requireContext(), "Ad was not loaded", Toast.LENGTH_SHORT).show()
            val action = AddBmiDetailsDirections.actionFirstFragmentToSecondFragment()
            findNavController().navigate(action)
        }
    }

    private fun loadIntAd() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(requireContext(), AD_UNIT_ID, adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    mInterstitialAd = interstitialAd
                }
            })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}