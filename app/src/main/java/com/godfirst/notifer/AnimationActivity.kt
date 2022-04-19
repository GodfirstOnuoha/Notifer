package com.godfirst.notifer

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.godfirst.notifer.databinding.ActivityAnimationBinding

class AnimationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnimationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnimationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            buttonRotate.setOnClickListener { beginRotation(imageStar) }
            buttonTranslate.setOnClickListener { beginTranslation(imageStar) }
            buttonScale.setOnClickListener { beginScaling(imageStar) }
            buttonFade.setOnClickListener { beginFade(imageStar) }
            buttonColor.setOnClickListener { changeColor(imageStar) }
            buttonShower.setOnClickListener { beginShower(imageStar) }
        }
    }

    private fun beginRotation(imageStar: ImageView) {
        imageStar.animate().rotation(360.0f).duration = 1000
    }

    private fun beginTranslation(imageStar: ImageView) {
        imageStar.animate().translationX(50.0f).duration = 1000
    }

    private fun beginScaling(imageStar: ImageView) {
        imageStar.animate().scaleX(5.0f).duration = 1000
        imageStar.animate().scaleY(5.0f).duration = 1000
    }

    private fun beginFade(imageStar: ImageView) {
        imageStar.animate().alpha(0.0f).duration = 2000
    }

    private fun changeColor(imageStar: ImageView) {
        imageStar.setColorFilter(ContextCompat.getColor(this, R.color.teal_700))
    }

    private fun beginShower(imageStar: ImageView) {
        val container = imageStar.parent as ViewGroup
        val containerW = container.width
        val containerH = container.height
        var starW: Float = imageStar.width.toFloat()
        var starH: Float = imageStar.height.toFloat()

        val newStar = AppCompatImageView(this)
        newStar.setImageResource(R.drawable.ic_star)
        newStar.layoutParams = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        container.addView(newStar)
        newStar.scaleX = Math.random().toFloat() * 1.5f + .1f
        newStar.scaleY = newStar.scaleX
        starW *= newStar.scaleX
        starH *= newStar.scaleY
        newStar.translationX = Math.random().toFloat() * containerW - starW / 2

        val mover = ObjectAnimator.ofFloat(newStar, View.TRANSLATION_Y, -starH, containerH + starH)
        mover.interpolator = AccelerateInterpolator(1f)
        val rotator =
            ObjectAnimator.ofFloat(newStar, View.ROTATION, (Math.random() * 1080).toFloat())
        rotator.interpolator = LinearInterpolator()

        val set = AnimatorSet()
        set.playTogether(mover, rotator)
        set.duration = (Math.random() * 1500 + 500).toLong()

        set.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                container.removeView(newStar)
            }
        })
        set.start()
    }
}