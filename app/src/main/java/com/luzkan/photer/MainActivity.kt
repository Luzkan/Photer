package com.luzkan.photer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import com.luzkan.photer.adapter.GalleryImageAdapter
import com.luzkan.photer.adapter.GalleryImageClickListener
import com.luzkan.photer.adapter.Image
import com.luzkan.photer.data.local.PhotoListDatabase
import com.luzkan.photer.data.local.models.Photo
import com.luzkan.photer.fragment.GalleryFullscreenFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), GalleryImageClickListener {

    private var columns = 2
    private val imageList = ArrayList<Image>()
    lateinit var galleryAdapter: GalleryImageAdapter
    private var photoDatabase: PhotoListDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Database
        photoDatabase = PhotoListDatabase.getInstance(this)

        // Initialize Adapter
        galleryAdapter = GalleryImageAdapter(imageList)
        galleryAdapter.listener = this
    }

    fun reloadRecyclerView(){
        loadImages()
        recyclerView.layoutManager = GridLayoutManager(this, columns)
        recyclerView.adapter = galleryAdapter
    }

    override fun onResume() {
        super.onResume()
        reloadRecyclerView()
    }

    private fun loadImages() {

        imageList.clear()
        for(Photo in photoDatabase!!.getPhoto().getPhotoListRatingSorted()){
            imageList.add(Image(photoDatabase!!.getPhoto().getPhotoItem(Photo.tId).url, photoDatabase!!.getPhoto().getPhotoItem(Photo.tId).description, photoDatabase!!.getPhoto().getPhotoItem(Photo.tId).rating, photoDatabase!!.getPhoto().getPhotoItem(Photo.tId).tId))
        }
        galleryAdapter.notifyDataSetChanged()
    }

    // Tapping shows fullscreen of the image with description and rating module
    override fun onClick(position: Int) {

        val bundle = Bundle()
        bundle.putSerializable("images", imageList)
        bundle.putInt("position", position)

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val galleryFragment = GalleryFullscreenFragment()
        galleryFragment.arguments = bundle
        galleryFragment.show(fragmentTransaction, "gallery")
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when(item.itemId) {
            R.id.action_increase -> {
                columns++
                onResume()}
            R.id.action_decrease -> {
                columns--
                onResume()}
            R.id.action_add -> showCreateCategoryDialog()
        }
        return true
    }

    // Adding new Image to Database
    private fun showCreateCategoryDialog() {
        val context = this
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Add New Image")

        // https://stackoverflow.com/questions/10695103/creating-custom-alertdialog-what-is-the-root-view
        val view = layoutInflater.inflate(R.layout.dialog__new_image, null)
        val description = view.findViewById(R.id.descriptionInput) as EditText
        val url = view.findViewById(R.id.urlInput) as EditText
        builder.setView(view)

        builder.setPositiveButton(android.R.string.ok) { dialog, _ ->
            val titleNew = description.text.toString()
            val urlNew = url.text.toString()
            var isValid = true
            if (titleNew.isBlank() || urlNew.isBlank()) {
                isValid = false
                dialog.dismiss()
            }
            if (isValid) {
                val photo = Photo(urlNew, titleNew, 0)
                photoDatabase!!.getPhoto().savePhoto(photo)
                onResume()
                dialog.dismiss()
            }
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }
}
