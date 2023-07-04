package com.kinopio.eatgo.presentation.store


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.kinopio.eatgo.R
import com.kinopio.eatgo.databinding.DialogLoadingBinding
import java.util.zip.Inflater

class LoadingDialog
constructor(context: Context) : Dialog(context){

    lateinit var turnAround : Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var binding = DialogLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        turnAround = AnimationUtils.loadAnimation(context, R.anim.turn_around)

        //loading_img.startAnimation(turnHorizontal)
        binding.loadingImg2.startAnimation(turnAround)
        binding.loadingImg.startAnimation(turnAround)
    }
}