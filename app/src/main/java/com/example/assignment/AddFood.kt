package com.example.assignment



import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.assignment.databinding.ActivityAddFoodBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


/**
 * A simple [Fragment] subclass.
 */
class AddFood : AppCompatActivity() {
    lateinit var editName: EditText
    lateinit var editDesc: EditText
    lateinit var editDate: EditText
    lateinit var editLocate : EditText
    lateinit var btnSubmit: Button
    lateinit var imgview: ImageView
    lateinit var btnBrowse: Button

    lateinit var FilePathUri:Uri
    lateinit var storageReference: StorageReference
    lateinit var databaseReference: DatabaseReference
    var Image_Request_Code: Int = 7
    lateinit var progressDialog:ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.setContentView<ActivityAddFoodBinding>(this,R.layout.activity_add_food)
        editName = binding.edittxtName
        editDesc = binding.edittxtDesc
        editDate = binding.edittxtdate
        editLocate = binding.edittxtLocate
        btnSubmit = binding.btnSubmit
        btnBrowse = binding.btnbrowse
        imgview = binding.imageView

        storageReference = FirebaseStorage.getInstance().getReference("Images")
        databaseReference = FirebaseDatabase.getInstance().getReference("Foods")
        progressDialog = ProgressDialog(this)
        btnBrowse.setOnClickListener { onClick() }
        btnSubmit.setOnClickListener {
                UploadImage()
        }


    }

    fun onClick() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Image"), Image_Request_Code)
    }
    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Image_Request_Code && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            FilePathUri = data.data!!
            try {
                val bitmap =
                    MediaStore.Images.Media.getBitmap(contentResolver, FilePathUri)
                imgview.setImageBitmap(bitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }


    fun GetFileExtension(uri: Uri?): String? {
        val contentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri!!))
    }


    fun UploadImage() {
        if(FilePathUri != null) {
            val sdf = SimpleDateFormat("dd/MM/yyyy")
            val currentTimeDate = sdf.format(Date())
            val name: String = editName.getText().toString().trim()
            val desc: String = editDesc.getText().toString().trim()
            val date: String = editDate.getText().toString().trim()
            val locate: String = editLocate.getText().toString().trim()
            val foodId:String = databaseReference.push().key.toString()
            val userid: String = FirebaseAuth.getInstance().currentUser!!.uid
            val status: String = "Available"
            if(name.isEmpty()){
                editName.error = "Please enter a name."
                return
            }
            if(desc.isEmpty()){
                editDesc.error = "Please enter description."
                return
            }
            if(date.isEmpty()){
                editDate.error = "Please enter date."
                return
            }
            if(locate.isEmpty()){
                editLocate.error = "Please enter your location."
                return
            }
            progressDialog.setTitle("Image is Uploading...")
            progressDialog.show()
            val storageReference2 = storageReference.child(
                System.currentTimeMillis().toString() + "." + GetFileExtension(FilePathUri)
            )
            storageReference2.putFile(FilePathUri)
                .addOnSuccessListener { taskSnapshot ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Image Uploaded Successfully ",
                        Toast.LENGTH_LONG
                    ).show()
                    taskSnapshot.storage.downloadUrl.addOnCompleteListener {taskSnapshot2->
                        val imageUploadInfo =
                            FoodEntity(userid,taskSnapshot2.result.toString(),foodId,name,desc,date,locate,currentTimeDate,status)
                        databaseReference.child(foodId).setValue(imageUploadInfo)
                    }
                    editName.setText("")
                    editDate.setText("")
                    editDesc.setText("")
                    editLocate.setText("")
                    imgview.setImageDrawable(resources.getDrawable(R.drawable.image))
                }
        }else{
            Toast.makeText(this, "Please Select Image", Toast.LENGTH_LONG).show()
        }
    }

}
