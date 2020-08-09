package `in`.vrkhedkr.wardrobe.view

import FileHelper
import `in`.vrkhedkr.wardrobe.R
import `in`.vrkhedkr.wardrobe.constant.WareType
import `in`.vrkhedkr.wardrobe.constant.WareType.BOTTOM
import `in`.vrkhedkr.wardrobe.constant.WareType.TOP
import `in`.vrkhedkr.wardrobe.databinding.ActivityHomeBinding
import `in`.vrkhedkr.wardrobe.model.Ware
import `in`.vrkhedkr.wardrobe.viewmodel.HomeViewModel
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import java.io.File
import java.io.FileOutputStream
import java.security.AccessController.getContext

const val TOP_WARE_CAMERA = 101
const val BOTTOM_WARE_CAMERA = 102
const val TOP_WARE_GALLERY = 103
const val BOTTOM_WARE_GALLERY = 104

class Home : AppCompatActivity() {

    private var _binder: ActivityHomeBinding? = null
    private val binder: ActivityHomeBinding get() = _binder!!

    private lateinit var model: HomeViewModel
    private var chooser:AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binder = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binder.root)
        model = ViewModelProvider(this).get(HomeViewModel::class.java)
        binder.addTopWare.setOnClickListener { selectWare(TOP) }
        binder.addBottomWare.setOnClickListener { selectWare(BOTTOM) }
        binder.topWare.adapter = PagerAdapter()
        binder.bottomWare.adapter = PagerAdapter()
        model.topWare.observe(this, Observer {
            (binder.topWare.adapter as PagerAdapter).setData(it as ArrayList<Ware>)
        })

        model.bottomWare.observe(this, Observer {
            (binder.bottomWare.adapter as PagerAdapter).setData(it as ArrayList<Ware>)
        })

        model.outfit.observe(this, Observer {
            checkCurrentPair()
        })

        binder.addFavourite.setOnClickListener {
            model.saveOutfitCombination(binder.topWare.currentItem,binder.bottomWare.currentItem)
        }

        binder.bottomWare.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                checkCurrentPair()
            }
        })

        binder.topWare.registerOnPageChangeCallback(object:ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                checkCurrentPair()
            }
        })

    }

    fun checkCurrentPair(){
        if(model.checkCurrentOutFitPresent(binder.topWare.currentItem,binder.bottomWare.currentItem)){
            binder.addFavourite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_like_active));
        }else{
            binder.addFavourite.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_like_inactive));
        }
    }

    private fun selectWare(type:WareType){
        val builder = AlertDialog.Builder(this)
        val view = layoutInflater.inflate(R.layout.img_option_chosser, null,false)
        val takePhotoOption = view.findViewById<TextView>(R.id.option_take_photo)
        val choosePhotoOption = view.findViewById<TextView>(R.id.option_choose_photo)
        takePhotoOption.setOnClickListener {
            chooser?.dismiss()
            chooser = null
            captureWareUsingCamera(type)
        }
        choosePhotoOption.setOnClickListener {
            chooser?.dismiss()
            chooser = null
            getWareFromGallery(type)
        }
        builder.setView(view)
        chooser = builder.create()
        chooser?.show()
    }

    private fun captureWareUsingCamera(type:WareType){
        val code = if(type == TOP) TOP_WARE_CAMERA else BOTTOM_WARE_CAMERA
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent,code)
    }

    private fun getWareFromGallery(type:WareType){
        val code = if(type == TOP) TOP_WARE_GALLERY else BOTTOM_WARE_GALLERY
        val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent,code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            when(requestCode){
                TOP_WARE_CAMERA,BOTTOM_WARE_CAMERA->{
                    val bitMap:Bitmap? = data?.extras?.get("data") as Bitmap?
                    bitMap?.let {
                        val file:File = FileHelper.getFile(this)
                        val out = FileOutputStream(file)
                        it.compress(Bitmap.CompressFormat.PNG,100,out)
                        model.saveWare(Ware(imgUrl = file.absolutePath,type = if(requestCode == TOP_WARE_CAMERA) TOP else BOTTOM))
                    }
                }
                TOP_WARE_GALLERY,BOTTOM_WARE_GALLERY->{
                    val uri = data?.data ?: Uri.EMPTY
                    var bitMap:Bitmap? = null
                    bitMap = if (android.os.Build.VERSION.SDK_INT >= 29){
                        ImageDecoder.decodeBitmap(ImageDecoder.createSource(contentResolver,uri))
                    } else{
                        MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    }
                    bitMap?.let {
                        val file:File = FileHelper.getFile(this)
                        val out = FileOutputStream(file)
                        it.compress(Bitmap.CompressFormat.PNG,100,out)
                        model.saveWare(Ware(imgUrl = file.absolutePath,type = if(requestCode == TOP_WARE_GALLERY) TOP else BOTTOM))
                    }
                }
            }
        }else{
            Toast.makeText(this,"Operation Canceled",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binder = null
    }

}