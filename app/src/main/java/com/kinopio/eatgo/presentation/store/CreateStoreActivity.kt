package com.kinopio.eatgo.presentation.store

import ToolbarUtils
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.data.store.StoreService
import com.kinopio.eatgo.databinding.ActivityCreateStoreBinding
import com.kinopio.eatgo.databinding.OpenInfoTimePickerBinding
import com.kinopio.eatgo.domain.store.CreateStoreResponseDto
import com.kinopio.eatgo.domain.store.MenuRequestDto
import com.kinopio.eatgo.domain.store.OpenInfoRequestDto
import com.kinopio.eatgo.domain.store.StoreRequestDto
import com.kinopio.eatgo.domain.store.TagRequestDto
import com.kinopio.eatgo.domain.store.ui_model.MenuForm
import com.kinopio.eatgo.domain.store.ui_model.OpenInfo
import com.kinopio.eatgo.presentation.map.NaverMapFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class CreateStoreActivity : AppCompatActivity() {
    private lateinit var menuFormAdapter: MenuFormAdapter
    private lateinit var openInfoAdapter: OpenInfoAdapter

    private val menuList = mutableListOf<MenuForm>()
    private var isBest = 0
    private val openInfoList = mutableListOf<OpenInfo>()
    private val tagList = mutableListOf<TagRequestDto>()
    private var selectedPositionX = 0.0
    private var selectedPositionY = 0.0

    private lateinit var binding: ActivityCreateStoreBinding


    private var selectedMenuImageUri: Uri = Uri.EMPTY
    private var selectedStoreImgUri: Uri = Uri.EMPTY

    private var selectedToggleButton: ToggleButton? = null
    private var selectedButtonNumber: Int = 0

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var storeImageLauncher: ActivityResultLauncher<Intent>

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    // 레트로핏 전송용 객체
    private lateinit var storeRequestDto: StoreRequestDto
    private var newMenuList = mutableListOf<MenuRequestDto>()
    private var storeImgUrl = ""

    // 카테고리 선택
    private val toggleButtonChangeListener =
        CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // 하나의 토글 버튼이 선택되면 이전에 선택되었던 토글 버튼의 선택 상태를 해제합니다.
                if (selectedToggleButton != null && selectedToggleButton != buttonView) {
                    selectedToggleButton?.isChecked = false
                }
                selectedToggleButton = buttonView as ToggleButton
                selectedButtonNumber = getButtonNumber(buttonView)

            } else {
                // 선택이 해제되면 selectedToggleButton을 null로 설정합니다.
                if (selectedToggleButton == buttonView) {
                    selectedToggleButton = null
                    selectedButtonNumber = 0
                }
            }
        }

    private fun getButtonNumber(button: ToggleButton): Int {
        return when (button.id) {
            com.kinopio.eatgo.R.id.cate1 -> 1
            com.kinopio.eatgo.R.id.cate2 -> 2
            com.kinopio.eatgo.R.id.cate3 -> 3
            com.kinopio.eatgo.R.id.cate4 -> 4
            com.kinopio.eatgo.R.id.cate5 -> 5
            com.kinopio.eatgo.R.id.cate6 -> 6
            else -> -1
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoreBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupToggleButtons()

        // 툴바 세팅
        ToolbarUtils.setupToolbar(
            this,
            binding.root.findViewById<Toolbar>(com.kinopio.eatgo.R.id.toolbar),
            "가게 등록",
            null
        )


        menuFormAdapter = MenuFormAdapter(menuList)
        openInfoAdapter = OpenInfoAdapter(openInfoList)

        binding.mRecyclerView.apply {
            adapter = menuFormAdapter
            layoutManager = LinearLayoutManager(this@CreateStoreActivity)
        }

        binding.openRecyclerView.apply {
            adapter = openInfoAdapter
            layoutManager = LinearLayoutManager(this@CreateStoreActivity)
        }

        // 메뉴 추가 창 보이게
        binding.menuAddBtn.setOnClickListener {
            val isInputVisible = binding.menuInputLayout.visibility == View.VISIBLE
            if (isInputVisible) {
                binding.menuInputLayout.visibility = View.GONE
            } else {
                binding.menuInputLayout.visibility = View.VISIBLE
            }
        }

        // 가게 사진 추가
        binding.storeImgAddBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            storeImageLauncher.launch(intent)
        }


        // 가게 위치 추가
        binding.positionBtn.setOnClickListener {
            binding.createStoreContainer.visibility = View.INVISIBLE
            binding.createMapContainer.visibility = View.VISIBLE
            val fm = supportFragmentManager
            val transaction = fm.beginTransaction()
            var mapFragment : NaverMapFragment = NaverMapFragment()
            transaction.add(com.kinopio.eatgo.R.id.createMapContainer, mapFragment)
            transaction.commit()

        }


        // 가게  사진 추가 -> 갤러리 연동
        storeImageLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val imageUri: Uri? = data.data
                        if (imageUri != null) {
                            selectedStoreImgUri = imageUri
                            binding.storeImgLayout.visibility = View.VISIBLE
                            binding.storeImg.setImageURI(selectedStoreImgUri)
                        }
                    }
                }
            }

        // 대표 메뉴 선택 체크박스
        // 체크 박스의 상태 변경 리스너 설정
        binding.isBestCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                isBest = 1;
            } else {
                isBest = 0;
                // isChecked가 false인 경우에 해당하는 코드를 작성하세요.
            }
        }

        // 메뉴 사진 추가
        binding.buttonAddPhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            imagePickerLauncher.launch(intent)
        }


        // 메뉴 사진 추가 -> 갤러리 연동
        imagePickerLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    if (data != null) {
                        val imageUri: Uri? = data.data
                        if (imageUri != null) {
                            selectedMenuImageUri = imageUri
                            binding.menuImg.setImageURI(selectedMenuImageUri)
                        }
                    }
                }
            }

        // 메뉴 추가
        binding.buttonAdd.setOnClickListener {
            val menuName = binding.editTextMenuName.text.toString()
            val menuAmount = binding.editTextMenuAmount.text.toString().toIntOrNull()
            Log.d("amount", "${menuAmount}")
            val menuPrice = binding.editTextMenuPrice.text.toString().toIntOrNull()
            val menuInfo = binding.menuInfo.text.toString() //칸 추가 필요

            if (menuName.isNotEmpty() && menuAmount != null && menuPrice != null) {
                if (selectedMenuImageUri == null) {
                    val menu = MenuForm(menuName, menuAmount, menuPrice, menuInfo, null, 1)
                    menuList.add(menu)
                } else {
                    val menu = MenuForm(
                        menuName,
                        menuAmount,
                        menuPrice,
                        menuInfo,
                        selectedMenuImageUri,
                        isBest
                    )
                    menuList.add(menu)

                }
                menuFormAdapter.notifyDataSetChanged()

                // 메뉴 확정한 뒤 리셋
                binding.editTextMenuName.text.clear()
                binding.editTextMenuAmount.text.clear()
                binding.editTextMenuPrice.text.clear()
                isBest = 0

                // Hide the input layout
                binding.menuInputLayout.visibility = View.GONE

                binding.menuImg.setImageURI(Uri.EMPTY)
                selectedMenuImageUri = Uri.EMPTY
            } else {
                val myToast: Toast =
                    Toast.makeText(this.applicationContext, com.kinopio.eatgo.R.string.NO_MENU_REQUIRED_INPUT_ERROR, Toast.LENGTH_SHORT)
                myToast.show()
            }
        }

        // 카테고리 버튼 리스너
        binding.cate1.setOnCheckedChangeListener(toggleButtonChangeListener)
        binding.cate2.setOnCheckedChangeListener(toggleButtonChangeListener)
        binding.cate3.setOnCheckedChangeListener(toggleButtonChangeListener)
        binding.cate4.setOnCheckedChangeListener(toggleButtonChangeListener)
        binding.cate5.setOnCheckedChangeListener(toggleButtonChangeListener)
        binding.cate6.setOnCheckedChangeListener(toggleButtonChangeListener)


        // 태그 추가
        binding.tagAddBtn.setOnClickListener {
            val inputText = binding.tagEt.text.toString().trim()

            binding.tagEt.text = null

            if (inputText.isNotEmpty() || inputText != null) {
                val textView = TextView(this)
                textView.text = inputText
                textView.setPadding(20, 20, 20, 20)
                textView.setBackgroundResource(com.kinopio.eatgo.R.drawable.store_create_box_border) // 노란색 태두리 설정
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f) // 텍스트 크기 설정
                textView.elevation= 10f

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    dpToPx(40)

                )
                layoutParams.setMargins(0, 0, 20, 0) // TextView 간격 설정
                textView.layoutParams = layoutParams

                binding.tagInputed.addView(textView)
                tagList.add(TagRequestDto(inputText));
            }
        }
        //  등록 버튼
        binding.submitBtn.setOnClickListener {

            if(checkIsAbleToUpload()){
                uploadStoreImg()
            }
            // 메뉴 이미지 전송
        }


    } // onCreate  종료

    private fun checkIsAbleToUpload():Boolean{

        var checkStoreName = binding.storeEdittext.text.toString()
        var checkStoreAddress = binding.resultAddress.text.toString()
        if (checkStoreName ==" " || checkStoreName.isNullOrBlank() || checkStoreName.isBlank()) {

            val myToast: Toast =
                Toast.makeText(this.applicationContext, com.kinopio.eatgo.R.string.NO_STORE_NAME_ERROR, Toast.LENGTH_SHORT)
            myToast.show()
            return false
        }
        // 주소 입력되었는 지 확인
        if(checkStoreAddress == " " || checkStoreAddress.isNullOrBlank() || checkStoreAddress.isBlank()){
            val myToast: Toast =
                Toast.makeText(this.applicationContext, com.kinopio.eatgo.R.string.NO_STORE_ADDRESS_ERROR, Toast.LENGTH_SHORT)
            myToast.show()
            return false
        }
        if(selectedButtonNumber <= 0 || selectedButtonNumber > 7 || selectedButtonNumber == null){
            val myToast: Toast =
                Toast.makeText(this.applicationContext, com.kinopio.eatgo.R.string.NO_STORE_CATEGORY_ERROR, Toast.LENGTH_SHORT)
            myToast.show()
            return false
        }
        return true
    }
    private fun uploadStoreImg() {

        if (selectedStoreImgUri != Uri.EMPTY) {

            val loadingAnimDialog = LoadingDialog(this, getString(R.string.CREATE_STORE_LOADING_MESSAGE))
            loadingAnimDialog.show()
            Handler().postDelayed({
                loadingAnimDialog.dismiss()
            }, 10000)

            uploadPhoto(
                selectedStoreImgUri,
                successHandler = { imgUrl ->
                    storeImgUrl = imgUrl
                    callMenuImageUpload()
                },
                errorHandler = {
                    Log.d("image", "Store Image Uploaded Error")
                    val myToast: Toast =
                        Toast.makeText(this.applicationContext, com.kinopio.eatgo.R.string.STORE_IMAGE_UPLOAD_ERROR, Toast.LENGTH_SHORT)
                    myToast.show()
                }
            )
        } else {
            // 가게 사진 있는지 확인
            val myToast: Toast =
                Toast.makeText(this.applicationContext, com.kinopio.eatgo.R.string.NO_STORE_IMAGE_ERROR, Toast.LENGTH_SHORT)
            myToast.show()
        }

    }

    fun callMenuImageUpload() {
        if(menuList.size ==0){
            callRetrofit()
        }
        for (i in 0..menuList.size - 1) {
            val tmp = menuList.get(i)
            Log.d("image", "tmp ${tmp.imageUri}")
            Log.d("menu", "tmp ${tmp.amount}")

            tmp.imageUri?.let { uri ->
                // tmp.imageUri가 null이 아닌 경우에 수행할 코드
                uploadPhoto(
                    tmp.imageUri,
                    successHandler = { imgUrl ->
                        Log.d("image", "$imgUrl")
                        newMenuList.add(
                            MenuRequestDto(
                                tmp.name,
                                tmp.info,
                                tmp.price,
                                tmp.amount,
                                imgUrl,
                                tmp.isBest
                            )
                        )
                        checkMenuImgAllUploaded()
                    },
                    errorHandler = {
                        Log.d("image", "error")
                        newMenuList.add(
                            MenuRequestDto(
                                tmp.name,
                                tmp.info,
                                tmp.price,
                                tmp.amount,
                                "",
                                tmp.isBest
                            )
                        )
                        checkMenuImgAllUploaded()
                    }
                )
            } ?: run {
                // tmp.imageUri가 null인 경우에 수행할 코드
                newMenuList.add(
                    MenuRequestDto(
                        tmp.name,
                        tmp.info,
                        tmp.price,
                        tmp.amount,
                        "",
                        tmp.isBest
                    )
                )
                checkMenuImgAllUploaded()
            }
        }

    }

    private fun checkMenuImgAllUploaded() {
        Log.d("image", "메뉴 사진 업로드 ${menuList.size}, ${newMenuList.size}")
        if (menuList.size == newMenuList.size) {
            callRetrofit()
        }
    }

    fun callRetrofit() {
        Log.d("image", "testing_test")

        var newOpenInfoList: MutableList<OpenInfoRequestDto> = mutableListOf()
//        var newTagList: MutableList<TagRequestDto> = mutableListOf()

        for (i in 0..openInfoList.size - 1) {
            var openInfoTmp = openInfoList.get(i)
            newOpenInfoList.add(
                OpenInfoRequestDto(
                    openInfoTmp.day,
                    openInfoTmp.startTime,
                    openInfoTmp.endTime
                )
            )
        }


        var storeName = binding.storeEdittext.text.toString()
        var userId = 1
        var address = binding.resultAddress.text.toString().trim()
        getPosition(address)
        var positionX = selectedPositionX
        var positionY = selectedPositionY
        var categoryId = selectedButtonNumber
        var createdType = 1



        storeRequestDto = StoreRequestDto(
            storeName = storeName,
            userId = userId,
            address = address,
            positionX = positionX,
            positionY = positionY,
            categoryId = categoryId,
            thumbnail = storeImgUrl,
            createdType = createdType,
            menus = newMenuList,
            tags = tagList,
            openInfos = newOpenInfoList
        )

        Log.d("image", "ended with ${storeRequestDto}")

        // retrofit 연결
        val retrofit = RetrofitClient.getRetrofit2()
        val storeService = retrofit.create(StoreService::class.java)

        storeService.createStore(storeRequestDto)
            .enqueue(object : Callback<CreateStoreResponseDto> {
                override fun onFailure(call: Call<CreateStoreResponseDto>, t: Throwable) {
                    Log.d("image", "errorororor:) ")
                    Log.d("fail", "$t")
                }

                override fun onResponse(
                    call: Call<CreateStoreResponseDto>,
                    response: Response<CreateStoreResponseDto>
                ) {
                    response.body()?.let {
                        // 응답 성공
                        Log.d("Created", "Store Created Success")
                        Log.d("Created", "${response.body()}")
                        val intent = Intent(this@CreateStoreActivity, StoreDetailActivity::class.java)
                        intent.putExtra("storeId", response.body()!!.storeId)
                        LoadingDialog(this@CreateStoreActivity, getString(R.string.CREATE_STORE_LOADING_MESSAGE) ).hide()
                        startActivity(intent)
                    }
                }
            })

    }

    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit) {
        val fileName = "${System.currentTimeMillis()}.png"
        storage.reference.child("images").child("1").child(fileName)
            .putFile(uri)
            .addOnSuccessListener { uploadTask ->
                uploadTask.storage.downloadUrl
                    .addOnSuccessListener { uri ->
                        successHandler(uri.toString())
                    }
                    .addOnFailureListener {
                        errorHandler()
                    }
            }
            .addOnFailureListener {
                errorHandler()
            }

    }

    private fun dpToPx(dp: Int): Int {
        return (dp * resources.displayMetrics.density).toInt()
    }

    // 영업일 정보 토글 버튼 리스너 세팅
    private fun setupToggleButtons() {
        val toggleButtons = listOf(
            binding.tbMon,
            binding.tbTue,
            binding.tbWed,
            binding.tbThu,
            binding.tbFri,
            binding.tbSat,
            binding.tbSun
        )

        toggleButtons.forEach { toggleButton ->
            toggleButton.setOnCheckedChangeListener { _, isChecked ->
                val day = toggleButton.text.toString()
                if (isChecked) {
                    showTimePicker(day)
                } else {
                    removeOpenInfo(day)
                }
            }
        }
    }

    // 영업 시작, 종료 시간 선택 time picker open
    private fun showTimePicker(day: String) {
        val dialogBinding = OpenInfoTimePickerBinding.inflate(layoutInflater)
        val dialog = AlertDialog.Builder(this)
            .setView(dialogBinding.root)
            .setPositiveButton("Done") { _, _ ->
                val startTimeHour = dialogBinding.timePicker.hour
                val startTimeMinute = dialogBinding.timePicker.minute
                val endTimeHour = dialogBinding.timePicker2.hour
                val endTimeMinute = dialogBinding.timePicker2.minute

                val startTime = String.format("%02d:%02d", startTimeHour, startTimeMinute)
                val endTime = String.format("%02d:%02d", endTimeHour, endTimeMinute)

                if (startTime.isNotEmpty() && endTime.isNotEmpty()) {
                    val openInfo = OpenInfo(day, startTime, endTime)
                    addOpenInfo(openInfo)
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.setOnShowListener {
            val window = dialog.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
            )
            val positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
            val cancelButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
            val parentView = positiveButton.parent as ViewGroup

            // LinearLayout으로 버튼들을 감싸고 중앙 정렬
            val buttonContainer = LinearLayout(this)
            buttonContainer.orientation = LinearLayout.HORIZONTAL


            // 버튼들을 LinearLayout에 추가
            parentView.removeView(positiveButton)
            parentView.removeView(cancelButton)
            positiveButton.gravity = Gravity.CENTER
            cancelButton.gravity = Gravity.CENTER
            buttonContainer.addView(cancelButton)
            buttonContainer.addView(positiveButton)

            // LinearLayout을 다이얼로그에 추가
            parentView.addView(buttonContainer)

        }

        dialog.show()
    }

    // recycler view에 open Info 추가
    private fun addOpenInfo(openInfo: OpenInfo) {
        openInfoList.add(openInfo)
        openInfoAdapter.notifyDataSetChanged()
    }

    // recycler view에 open Info 삭제
    private fun removeOpenInfo(day: String) {
        val removedStores = openInfoList.filter { it.day == day }
        openInfoList.removeAll(removedStores)
        openInfoAdapter.notifyDataSetChanged()
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return ToolbarUtils.handleOptionsItemSelected(
            this,
            item
        )
    }

    private fun getPosition(address : String) {
        Geocoder(applicationContext, Locale.KOREA).getFromLocationName(address, 1)?.let {
            Location("").apply {
                Log.d("geo", "${it[0].latitude}, ${it[0].longitude}")
                selectedPositionX = it[0].latitude.toDouble()
                selectedPositionY = it[0].longitude.toDouble()

            }
        }
    }


}