package com.luzkan.photer.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.luzkan.photer.R
import com.luzkan.photer.adapter.Image
import com.luzkan.photer.data.local.PhotoListDatabase.Companion.photoListDatabase
import com.luzkan.photer.helper.GlideApp
import com.luzkan.photer.helper.ZoomOutPageTransformer
import kotlinx.android.synthetic.main.image_fullscreen.view.*

class GalleryFullscreenFragment: DialogFragment() {

    private var imageList = ArrayList<Image>()
    private var selectedPosition: Int = 0

    lateinit var fViewPager: ViewPager
    lateinit var fPictureDescription: TextView
    lateinit var fRating: RatingBar
    lateinit var fId: TextView
    lateinit var fGalleryPagerAdapter: GalleryPagerAdapter

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_gallery_fullscreen, container, false)

        // ViewPager allows user to swipe through pages
        fViewPager = view.findViewById(R.id.viewPager)
        fPictureDescription = view.findViewById(R.id.pictureDescription)
        fRating = view.findViewById(R.id.ratingBar)
        fId = view.findViewById(R.id.cheatingIdEasyWayImTiredImSorry)
        fGalleryPagerAdapter = GalleryPagerAdapter()

        imageList = arguments?.getSerializable("images") as ArrayList<Image>
        selectedPosition = arguments!!.getInt("position")

        fViewPager.adapter = fGalleryPagerAdapter
        fViewPager.addOnPageChangeListener(viewPagerPageChangeListener)
        fViewPager.setPageTransformer(true, ZoomOutPageTransformer())

        fRating.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_UP -> {
                        val photo = photoListDatabase!!.getPhoto().getPhotoItem(fId.text.toString().toInt())
                        photo.rating = fRating.rating.toInt()
                        photoListDatabase!!.getPhoto().updatePhoto(photo)

                        // Sorts automatically after rating was made
                        imageList.clear()
                        for(Photo in photoListDatabase!!.getPhoto().getPhotoListRatingSorted()){
                            imageList.add(Image(photoListDatabase!!.getPhoto().getPhotoItem(Photo.tId).url, photoListDatabase!!.getPhoto().getPhotoItem(Photo.tId).description, photoListDatabase!!.getPhoto().getPhotoItem(Photo.tId).rating, photoListDatabase!!.getPhoto().getPhotoItem(Photo.tId).tId))
                        }

                        // TODO: CALL ADAPTER IN MAINACTIVITY.
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })

        setCurrentItem(selectedPosition)

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar_Fullscreen)
    }

    private fun setCurrentItem(position: Int) {
        fViewPager.setCurrentItem(position, false)
    }

    // ViewPager page change listener
    private var viewPagerPageChangeListener: ViewPager.OnPageChangeListener =
            object : ViewPager.OnPageChangeListener {

        // Sets the stuff visible for user in the UI
        override fun onPageSelected(position: Int) {
            fPictureDescription.text = imageList[position].description
            fRating.rating = imageList[position].rating.toFloat()
            fId.text = imageList[position].id.toString()
        }

        override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {
        }

        override fun onPageScrollStateChanged(arg0: Int) {
        }
    }

    // Gallery Adapter
    inner class GalleryPagerAdapter : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup, position: Int): Any {

            val layoutInflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = layoutInflater.inflate(R.layout.image_fullscreen, container, false)

            val image = imageList[position]

            // Load Image
            GlideApp.with(context!!)
                    .load(image.imageUrl)
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(view.ivFullscreenImage)

            container.addView(view)

            return view
        }

        override fun getCount(): Int {
            return imageList.size
        }

        override fun isViewFromObject(view: View, obj: Any): Boolean {
            return view === obj as View
        }

        override fun destroyItem(container: ViewGroup, position: Int, obj: Any) {
            container.removeView(obj as View)
        }
    }
}