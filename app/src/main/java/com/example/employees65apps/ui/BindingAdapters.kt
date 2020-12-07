package com.example.employees65apps.ui

import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.employees65apps.R
import com.example.employees65apps.domain.Specialty
import com.github.ybq.android.spinkit.style.Circle
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    if (imgUrl != null) {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()

        val circle = Circle().apply {
            color = ContextCompat.getColor(imgView.context, R.color.primaryColor)
            scale = 0.5f
            start()
        }

        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(circle)
                    .error(R.drawable.ic_broken_image)
                    .dontTransform()
            )
            .into(imgView)
    } else {
        imgView.setImageResource(R.drawable.ic_emloyee_no_photo)
    }
}

/**
 * If the given age is not null and correct, set value to [TextView], otherwise set '-'
 */
@BindingAdapter("setAge")
fun bindAge(textView: TextView, age: Int?) {
    textView.text = if ((age == null) || (age < 0)) "-" else age.toString()
}

/**
 * If the given birthday is not null, convert to "dd.MM.yyyy" and set to [TextView]
 */
@BindingAdapter("setBirthday")
fun bindBirthday(textView: TextView, birthday: LocalDate?) {
    if (birthday != null) {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        textView.text = birthday.format(formatter)
    } else {
        textView.text = "-"
    }
}

/**
 * Join a list of specialties to a [String] with ", " separator, then set to [TextView]
 */
@BindingAdapter("setSpecialties")
fun bindSpecialties(textView: TextView, specialties: List<Specialty>) {
    textView.text = specialties.joinToString(", ") { it.name.toLowerCase(Locale.ROOT) }
}
