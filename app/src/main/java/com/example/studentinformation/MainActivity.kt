package com.example.studentinformation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var editTextMSSV: EditText
    private lateinit var editTextHoTen: EditText
    private lateinit var radioGroupGender: RadioGroup
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var calendarView: CalendarView
    private lateinit var buttonShowCalendar: Button
    private lateinit var spinnerWard: Spinner
    private lateinit var spinnerDistrict: Spinner
    private lateinit var spinnerProvince: Spinner
    private lateinit var checkBoxSport: CheckBox
    private lateinit var checkBoxMovies: CheckBox
    private lateinit var checkBoxMusic: CheckBox
    private lateinit var checkBoxTerms: CheckBox
    private lateinit var buttonSubmit: Button
    private var selectedDate = ""

    private val provinces = listOf("Hà Nội", "TP. Hồ Chí Minh", "Đà Nẵng")
    private val districts = mapOf(
        "Hà Nội" to listOf("Quận Hoàn Kiếm", "Quận Đống Đa", "Quận Hai Bà Trưng"),
        "TP. Hồ Chí Minh" to listOf("Quận 1", "Quận 3", "Quận Tân Bình"),
        "Đà Nẵng" to listOf("Quận Hải Châu", "Quận Thanh Khê", "Quận Sơn Trà")
    )
    private val wards = mapOf(
        "Quận Hoàn Kiếm" to listOf("Phường Hàng Bạc", "Phường Hàng Đào", "Phường Phúc Tân"),
        "Quận Đống Đa" to listOf("Phường Kim Liên", "Phường Phương Mai", "Phường Văn Chương"),
        "Quận Hai Bà Trưng" to listOf("Phường Bạch Mai", "Phường Đồng Tâm", "Phường Phố Huế"),
        "Quận 1" to listOf("Phường Bến Nghé", "Phường Bến Thành", "Phường Phạm Ngũ Lão"),
        "Quận 3" to listOf("Phường 6", "Phường 7", "Phường 14"),
        "Quận Tân Bình" to listOf("Phường 1", "Phường 2", "Phường 3"),
        "Quận Hải Châu" to listOf("Phường Hải Châu 1", "Phường Hải Châu 2", "Phường Nam Dương"),
        "Quận Thanh Khê" to listOf("Phường Thanh Khê Tây", "Phường Thanh Khê Đông", "Phường Xuân Hà"),
        "Quận Sơn Trà" to listOf("Phường An Hải Bắc", "Phường An Hải Tây", "Phường Mân Thái")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Link các thành phần từ XML
        editTextMSSV = findViewById(R.id.editTextMSSV)
        editTextHoTen = findViewById(R.id.editTextHoTen)
        radioGroupGender = findViewById(R.id.radioGroupGender)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPhone = findViewById(R.id.editTextPhone)
        calendarView = findViewById(R.id.calendarView)
        buttonShowCalendar = findViewById(R.id.buttonShowCalendar)
        spinnerWard = findViewById(R.id.spinnerWard)
        spinnerDistrict = findViewById(R.id.spinnerDistrict)
        spinnerProvince = findViewById(R.id.spinnerProvince)
        checkBoxSport = findViewById(R.id.checkBoxSport)
        checkBoxMovies = findViewById(R.id.checkBoxMovies)
        checkBoxMusic = findViewById(R.id.checkBoxMusic)
        checkBoxTerms = findViewById(R.id.checkBoxTerms)
        buttonSubmit = findViewById(R.id.buttonSubmit)

        // Initial hiding of the CalendarView
        calendarView.visibility = View.GONE

        // Xử lý chọn ngày sinh
        buttonShowCalendar.setOnClickListener {
            showDatePickerDialog()
        }

        // Lấy ngày sinh được chọn và ẩn CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
            calendarView.visibility = View.GONE
            buttonShowCalendar.text = "Ngày sinh: $selectedDate"
        }

        // Xử lý khi nhấn Submit
        buttonSubmit.setOnClickListener {
            if (validateInput()) {
                Toast.makeText(this, "Thông tin đã được gửi thành công!", Toast.LENGTH_SHORT).show()
            }
        }

        val provinceAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, provinces)
        provinceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProvince.adapter = provinceAdapter

        // Handle Province selection
        spinnerProvince.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedProvince = provinces[position]
                val districtList = districts[selectedProvince] ?: emptyList()

                val districtAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, districtList)
                districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerDistrict.adapter = districtAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        // Handle District selection
        spinnerDistrict.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedDistrict = spinnerDistrict.selectedItem as? String ?: ""
                val wardList = wards[selectedDistrict] ?: emptyList()

                val wardAdapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_spinner_item, wardList)
                wardAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerWard.adapter = wardAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showDatePickerDialog() {
        // Inflate custom calendar layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_calendar, null)
        val calendarView = dialogView.findViewById<CalendarView>(R.id.dialogCalendarView)

        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .setTitle("Chọn ngày sinh")
            .setPositiveButton("OK") { _, _ ->
                // Show selected date on the button
                buttonShowCalendar.text = "Ngày sinh: $selectedDate"
            }
            .setNegativeButton("Hủy", null)
            .create()

        // Set initial selected date from CalendarView
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate = "$dayOfMonth/${month + 1}/$year"
        }

        dialog.show()
    }

    private fun validateInput(): Boolean {
        if (editTextMSSV.text.isEmpty()) {
            editTextMSSV.error = "Vui lòng nhập MSSV"
            return false
        }
        if (editTextHoTen.text.isEmpty()) {
            editTextHoTen.error = "Vui lòng nhập họ tên"
            return false
        }
        if (radioGroupGender.checkedRadioButtonId == -1) {
            Toast.makeText(this, "Vui lòng chọn giới tính", Toast.LENGTH_SHORT).show()
            return false
        }
        if (editTextEmail.text.isEmpty()) {
            editTextEmail.error = "Vui lòng nhập email"
            return false
        }
        if (editTextPhone.text.isEmpty()) {
            editTextPhone.error = "Vui lòng nhập số điện thoại"
            return false
        }
        if (selectedDate.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn ngày sinh", Toast.LENGTH_SHORT).show()
            return false
        }
        if (!checkBoxTerms.isChecked) {
            Toast.makeText(this, "Vui lòng đồng ý với các điều khoản", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}
