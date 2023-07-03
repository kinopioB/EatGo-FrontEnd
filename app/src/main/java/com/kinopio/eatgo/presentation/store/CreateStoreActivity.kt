package com.kinopio.eatgo.presentation.store

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
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
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.internal.ViewUtils.dpToPx
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.kinopio.eatgo.R
import com.kinopio.eatgo.RetrofitClient
import com.kinopio.eatgo.User
import com.kinopio.eatgo.data.store.StoreService
import com.kinopio.eatgo.databinding.ActivityCreateStoreBinding
import com.kinopio.eatgo.databinding.OpenInfoTimePickerBinding
import com.kinopio.eatgo.domain.store.Menu
import com.kinopio.eatgo.domain.store.MenuRequestDto
import com.kinopio.eatgo.domain.store.OpenInfoRequestDto
import com.kinopio.eatgo.domain.store.PopularStoreResponseDto
import com.kinopio.eatgo.domain.store.StoreDetailResponseDto
import com.kinopio.eatgo.domain.store.StoreRequestDto
import com.kinopio.eatgo.domain.store.TagRequestDto
import com.kinopio.eatgo.domain.store.ui_model.MenuForm
import com.kinopio.eatgo.domain.store.ui_model.OpenInfo
import com.kinopio.eatgo.domain.store.ui_model.Store
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CreateStoreActivity : AppCompatActivity() {
    private lateinit var menuFormAdapter: MenuFormAdapter
    private lateinit var  openInfoAdapter :  OpenInfoAdapter

    private val menuList= mutableListOf<MenuForm>()
    private var isBest = 0
    private val openInfoList = mutableListOf<OpenInfo>()
    private val tagList = mutableListOf<TagRequestDto>()

    private lateinit var binding: ActivityCreateStoreBinding

    private var selectedMenuImageUri: Uri =Uri.EMPTY
    private var selectedStoreImgUri : Uri= Uri.EMPTY

    private var selectedToggleButton: ToggleButton? = null
    private var selectedButtonNumber: Int = 0

    private lateinit var imagePickerLauncher: ActivityResultLauncher<Intent>
    private lateinit var storeImageLauncher: ActivityResultLauncher<Intent>

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    // 레트로핏 전송용 객체
    private lateinit var storeRequestDto : StoreRequestDto
    private  var newMenuList = mutableListOf<MenuRequestDto>()
    private var storeImgUrl = ""

    // 카테고리 선택
    private val toggleButtonChangeListener = CompoundButton.OnCheckedChangeListener { buttonView, isChecked ->
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
            R.id.cate1 -> 1
            R.id.cate2 -> 2
            R.id.cate3 -> 3
            R.id.cate4 -> 4
            R.id.cate5 -> 5
            R.id.cate6 -> 6
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
            binding.root.findViewById<Toolbar>(R.id.toolbar),
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


        // 가게  사진 추가 -> 갤러리 연동
        storeImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
        imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
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
                if(selectedMenuImageUri == null){
                    val menu = MenuForm(menuName, menuAmount, menuPrice, menuInfo, null, 1)
                    menuList.add(menu)
                }else{
                    val menu = MenuForm(menuName, menuAmount, menuPrice, menuInfo, selectedMenuImageUri, isBest)
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
                // Handle input validation failure
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

            if (inputText.isNotEmpty()) {
                val textView = TextView(this)
                textView.text = inputText
                textView.setPadding(20,20,20,20)
                textView.setBackgroundResource(R.drawable.store_create_box_border) // 노란색 태두리 설정
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f) // 텍스트 크기 설정

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

            uploadStoreImg()
            // 메뉴 이미지 전송

        }


    } // onCreate  종료


    private fun uploadStoreImg(){
        Log.d("image", "가게 사진 업로드")

        if(selectedStoreImgUri != null){
            uploadPhoto(
                selectedStoreImgUri,
                successHandler = { imgUrl ->
                    storeImgUrl = imgUrl
                    callMenuImageUpload()
             },
                errorHandler = {
                    Log.d("image","Store Image Uploaded Error")
                   // 토스트 메세지
                }
            )
        }else{
            // callRetrofit()
            // 토스트 띄워야함.
        }

    }

    fun callMenuImageUpload(){
        for (i in 0..menuList.size-1){
            val tmp = menuList.get(i)
            Log.d("image","tmp ${tmp.imageUri}")
            Log.d("menu","tmp ${tmp.amount}")

            tmp.imageUri?.let { uri ->
                // tmp.imageUri가 null이 아닌 경우에 수행할 코드
                uploadPhoto(
                    tmp.imageUri,
                    successHandler = { imgUrl ->
                        Log.d("image", "$imgUrl")
                        newMenuList.add(MenuRequestDto(tmp.name, tmp.info, tmp.price, tmp.amount, imgUrl, tmp.isBest ))
                        checkMenuImgAllUploaded()
                    },
                    errorHandler = {
                        Log.d("image","error")
                        newMenuList.add(MenuRequestDto(tmp.name, tmp.info, tmp.price, tmp.amount,"", tmp.isBest ))
                        checkMenuImgAllUploaded()
                    }
                )
            } ?: run {
                // tmp.imageUri가 null인 경우에 수행할 코드
                newMenuList.add(MenuRequestDto(tmp.name, tmp.info, tmp.price, tmp.amount,"", tmp.isBest ))
                checkMenuImgAllUploaded()
            }
        }

    }
    private fun checkMenuImgAllUploaded() {
        Log.d("image","메뉴 사진 업로드 ${menuList.size }, ${newMenuList.size}")
        if(menuList.size == newMenuList.size){
            callRetrofit()
        }
    }

    fun callRetrofit() {
        Log.d("image","testing_test")

        var newOpenInfoList : MutableList<OpenInfoRequestDto> = mutableListOf()
        var newTagList : MutableList<TagRequestDto> = mutableListOf()

        for(i in 0..openInfoList.size-1){
            var openInfoTmp = openInfoList.get(i)
            newOpenInfoList.add(OpenInfoRequestDto(openInfoTmp.day, openInfoTmp.startTime, openInfoTmp.endTime))
        }


        var storeName =  binding.storeEdittext.text.toString()
        var userId = 1
        var address = "대한민국 서울특별시 종로구 명륜4가 66-2"
        var positionX= 37.58276254809701 // 주소 바꾸기
        var positionY= 127.00055558776944
        var categoryId = selectedButtonNumber
        var createdType = 1


        storeRequestDto = StoreRequestDto(
            storeName = storeName,
            userId =userId,
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

        storeService.createStore(storeRequestDto).enqueue(object : Callback<StoreDetailResponseDto> {
            override fun onFailure(call: Call<StoreDetailResponseDto>, t: Throwable) {
                Log.d("image", "errorororor:) ")
                Log.d("fail", "$t")
                val intent = Intent(applicationContext, ReviewDetailActivity::class.java)
                startActivity(intent)
            }
            override fun onResponse(call: Call<StoreDetailResponseDto>, response: Response<StoreDetailResponseDto>) {
                response.body()?.let {
                  // 응답 성공
                    Log.d("Created", "Store Created Success")
                    Log.d("Created", "${response.body()}")

                    val intent = Intent(this@CreateStoreActivity, ReviewDetailActivity::class.java)
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
        ) // 분리된 클래스의 handleOptionsItemSelected 함수 호출
    }




}
