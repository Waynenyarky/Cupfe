package com.tamayo_aaron_b.cupfe_expresso

import android.app.Dialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.journeyapps.barcodescanner.BarcodeEncoder
import retrofit2.Call
import android.util.Base64

class Receipts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_receipts)

        val payCash = findViewById<Button>(R.id.payCash)
        val payOnline = findViewById<Button>(R.id.payOnline)
        val cancelBtn = findViewById<ImageView>(R.id.cancelBtn)
        val tvOrderType = findViewById<TextView>(R.id.tvOrderType)
        val tvCoffeeName = findViewById<TextView>(R.id.tvCoffeeName)
        val tvCoffeeQuantity = findViewById<TextView>(R.id.tvCoffeeQuantity)
        val tvSubTotal = findViewById<TextView>(R.id.tvSubTotal)
        val total_amount = findViewById<TextView>(R.id.tvTotal)
        val etComment1 = findViewById<TextView>(R.id.etComment1)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvTime = findViewById<TextView>(R.id.tvTime)
        val reference_number = findViewById<TextView>(R.id.tvTransactionId)
        val tvSize = findViewById<TextView>(R.id.tvSize)
        val tvPrize = findViewById<TextView>(R.id.tvPrize)
        val tvEstTime = findViewById<TextView>(R.id.tvEstTime)

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val email = sharedPreferences.getString("user_email", null) ?: ""
        val username = sharedPreferences.getString("USERNAME", null) ?: "Hello User!"
        Log.d("DEBUG", "Email Saved in Receipts: $email")
        Log.d("DEBUG", "Name Saved in Receipts: $username")


        // Retrieve data from intent
        val transactionId = intent.getStringExtra("transactionId")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")
        val orderType = intent.getStringExtra("orderType")
        val coffeeName = intent.getStringExtra("coffeeName")
        val size = intent.getStringExtra("size")
        val quantity = intent.getIntExtra("quantity", 0)
        val price = intent.getDoubleExtra("price", 0.0)
        val comment = intent.getStringExtra("comment")
        val subTotal = intent.getDoubleExtra("subTotal", 0.0)
        val total = intent.getDoubleExtra("total", 0.0)
        val estimatedTime = intent.getStringExtra("estimatedTime")

        // Update UI
        reference_number.text = "$transactionId"
        tvDate.text = "$date"
        tvTime.text = "$time"
        tvOrderType.text = "$orderType"
        tvCoffeeName.text = "$coffeeName"
        tvSize.text = "Size: $size"
        tvCoffeeQuantity.text = "x$quantity"
        tvPrize.text = "₱${String.format("%.2f", price)}"
        etComment1.text = "``$comment"
        tvSubTotal.text = "₱${String.format("%.2f", subTotal)}"
        total_amount.text = "₱${String.format("%.2f", total)}"
        tvEstTime.text = "$estimatedTime"




        payCash.setOnClickListener { showSuccessDialog(transactionId, orderType,coffeeName,size,quantity,price,comment,total,estimatedTime) }
        payOnline.setOnClickListener { showSuccessDialog1(transactionId, orderType,coffeeName,size,quantity,price,comment,total,estimatedTime) }

        cancelBtn.setOnClickListener{
            finish()
            overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.fade_out)
        }


    }

    //CASH
    private fun showSuccessDialog(
        transactionId: String?,
        orderType: String?,
        coffeeName: String?,
        size: String?,
        quantity: Int,
        price: Double,
        comment: String?,
        total: Double,
        estimatedTime: String?
    ) {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.confirmation_receipt_dialog)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", null) ?: "Hello User!"
        val email = sharedPreferences.getString("user_email", null) ?: ""
        val coffeeId = intent?.getIntExtra("coffeeId", -1) ?: -1// Default value -1 if not found


        val okBtn = dialog.findViewById<Button>(R.id.okBtn)
        val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)
        val btnCopy = dialog.findViewById<ImageView>(R.id.btnCopy)
        val txtTransactionIdCash = dialog.findViewById<TextView>(R.id.txtTransactionIdCash)

        txtTransactionIdCash.text = "Ref No. $transactionId"


        val underlineSpan = SpannableString(txtTransactionIdCash.text.toString()).apply {
            setSpan(UnderlineSpan(), 0, length, 0)
        }
        txtTransactionIdCash.text = underlineSpan

        var isCopied = false
        btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Transaction ID", transactionId ?: "N/A")
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Transaction ID copied", Toast.LENGTH_SHORT).show()
            isCopied = true
        }

        txtTransactionIdCash.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Transaction ID", transactionId ?: "N/A")
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Transaction ID copied", Toast.LENGTH_SHORT).show()
            isCopied = true
        }

        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        okBtn.setOnClickListener {
            if (!isCopied) {
                Toast.makeText(this, "Please copy the reference number before proceeding!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // Prevent further execution
            }
            val orderRequest = OrderRequest(
                reference_number = transactionId ?: "N/A",
                username = username,
                email = email,
                total_amount = total.toString(),
                status = "pending",
                promo_code = "none",
                order_type = orderType ?: "Unknown",
                payment_method = "Cash",
                payment_status = "Unpaid",
                est_time = estimatedTime.toString(),
                reason = "exceed limit",
                order_items = listOf(
                    OrderItem(
                        item_id = coffeeId.toString(),
                        item_name = coffeeName ?: "Unknown Coffee",
                        quantity = quantity,
                        price = String.format("%.2f", price),
                        size = size ?: "Unknown",
                        special_instructions = comment ?: "N/A",
                        username = username,
                        email = email
                    )
                )
            )

            RetrofitClient.instance.createOrder(orderRequest).enqueue(object : retrofit2.Callback<OrderResponse> {
                override fun onResponse(call: Call<OrderResponse>, response: retrofit2.Response<OrderResponse>) {
                    if (response.isSuccessful) {
                        Log.d("ORDER", "Order created successfully")
                        dialog.dismiss()
                        startActivity(Intent(this@Receipts, Main_Home_Page::class.java))
                        Toast.makeText(this@Receipts, "Proceed to the Counter", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Log.d("ORDER", "Response Code: ${response.code()}")
                        Log.d("ORDER", "Response Body: ${response.body()}")
                        Log.e("ORDER", "Failed to create order")
                    }
                }

                override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                    Log.e("ORDER", "Error: ${t.message}")
                }
            })
        }

        dialog.show()
    }


    //ONLINE
    private fun showSuccessDialog1(
        transactionId: String?,
        orderType: String?,
        coffeeName: String?,
        size: String?,
        quantity: Int,
        price: Double,
        comment: String?,
        total: Double,
        estimatedTime: String?
        ) {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.confirmation_receipt_dialog2)
            dialog.setCancelable(false)
            dialog.setCanceledOnTouchOutside(false)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

            val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
            val username = sharedPreferences.getString("USERNAME", null) ?: "Hello User!"
            val email = sharedPreferences.getString("user_email", null) ?: ""
            val coffeeId = intent?.getIntExtra("coffeeId", -1) ?: -1 // Default value -1 if not found

            val okBtn = dialog.findViewById<Button>(R.id.okBtn)
            val cancelBtn = dialog.findViewById<Button>(R.id.cancelBtn)
            val btnCopy = dialog.findViewById<ImageView>(R.id.btnCopy)
            val txtTransactionId = dialog.findViewById<TextView>(R.id.txtTransactionId)
            val btnGenerateQR = dialog.findViewById<Button>(R.id.btnGenerateQR) // Add a button for QR generation
            val imgQRCode = dialog.findViewById<ImageView>(R.id.imgQRCode)
            val txtQrFallback = dialog.findViewById<TextView>(R.id.txtQrFallback)
            val separator = dialog.findViewById<LinearLayout>(R.id.separator)
            val returnBtn = dialog.findViewById<Button>(R.id.returnBtn)


            var isCopied = false

            // Set transaction ID in the TextView
            txtTransactionId.text = "Ref No. $transactionId"

            val underlineSpan = SpannableString(txtTransactionId.text.toString()).apply {
                setSpan(UnderlineSpan(), 0, length, 0)
            }
            txtTransactionId.text = underlineSpan

            val underlineSpans = SpannableString(txtQrFallback.text.toString()).apply {
                setSpan(UnderlineSpan(), 0, length, 0)
            }
            txtQrFallback.text = underlineSpans



            // Generate QR Code when button is clicked
            btnGenerateQR.setOnClickListener {
                if (!isCopied) {
                    Toast.makeText(this, "Please copy the reference number before generating QR code!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Prevent further execution
                }

                val orderRequest = OrderRequest(
                    reference_number = transactionId ?: "N/A",
                    username = username,
                    email = email,
                    total_amount = total.toString(),
                    status = "pending",
                    promo_code = "none",
                    order_type = orderType ?: "Unknown",
                    payment_method = "Cashless",
                    payment_status = "Unpaid",
                    est_time = estimatedTime.toString(),
                    reason = "exceed limit",
                    order_items = listOf(
                        OrderItem(
                            item_id = coffeeId.toString(),
                            item_name = coffeeName ?: "Unknown Coffee",
                            quantity = quantity,
                            price = String.format("%.2f", price),
                            size = size ?: "Unknown",
                            special_instructions = comment ?: "N/A",
                            username = username,
                            email = email
                        )
                    )
                )

                RetrofitClient.instance.createOrder(orderRequest).enqueue(object : retrofit2.Callback<OrderResponse> {
                    override fun onResponse(call: Call<OrderResponse>, response: retrofit2.Response<OrderResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            Log.d("ORDER", "Order created successfully")

                                Toast.makeText(this@Receipts, "QR Generated", Toast.LENGTH_SHORT).show()
                                val secretToken = "ABC123SECRET"
                                val paymentUrl = "http://192.168.1.20/expresso-cafe/api/stripePayment/payment_form_order.php?token=$secretToken&appSpecificId=com.enrique_john_wayne_m.cupfe_scanner"
                                val encodedUrl = Base64.encodeToString(paymentUrl.toByteArray(), Base64.NO_WRAP)
                                val qrCodeBitmap = generateQRCode(encodedUrl)

                                imgQRCode.setImageBitmap(qrCodeBitmap) // Display the generated QR code
                                imgQRCode.visibility = View.VISIBLE
                                txtQrFallback.visibility = View.VISIBLE // Show fallback link
                                returnBtn.visibility = View.VISIBLE

                                // Hide separator and OK button
                                separator.visibility = View.GONE
                                okBtn.visibility = View.GONE
                                btnGenerateQR.visibility = View.GONE
                                cancelBtn.visibility = View.GONE


                                txtQrFallback.setOnClickListener {
                                    val homeIntent = Intent(this@Receipts, Main_Home_Page::class.java)
                                    startActivity(homeIntent)


                                    Handler(Looper.getMainLooper()).postDelayed({
                                        Toast.makeText(this@Receipts, "Redirecting to payment...", Toast.LENGTH_SHORT).show()
                                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl))
                                        startActivity(browserIntent)
                                    }, 1000)
                                }

                        } else {
                            Log.d("ORDER", "Response Code: ${response.code()}")
                            Log.d("ORDER", "Response Body: ${response.body()}")
                            Log.e("ORDER", "Failed to create order")
                        }
                    }

                    override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                        Log.e("ORDER", "Error: ${t.message}")
                    }
                })
            }

            returnBtn.setOnClickListener {
                // Check if transactionId is not null
                if (transactionId != null) {
                    // Copy transactionId to clipboard
                    val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Transaction ID", transactionId)
                    clipboard.setPrimaryClip(clip)

                    Toast.makeText(this, "Transaction ID copied", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Transaction ID not found", Toast.LENGTH_SHORT).show()
                }

                // Proceed to Main_Home_Page
                val homeIntent = Intent(this@Receipts, Main_Home_Page::class.java)
                startActivity(homeIntent)
                overridePendingTransition(R.anim.nav_fade_in_heart, R.anim.nav_fade_out_heart)
            }


            btnCopy.setOnClickListener {
                val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("Transaction ID", transactionId ?: "N/A")
                clipboard.setPrimaryClip(clip)

                Toast.makeText(this, "Transaction ID copied", Toast.LENGTH_SHORT).show()
                isCopied = true
            }

        txtTransactionId.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("Transaction ID", transactionId ?: "N/A")
            clipboard.setPrimaryClip(clip)

            Toast.makeText(this, "Transaction ID copied", Toast.LENGTH_SHORT).show()
            isCopied = true
        }

            cancelBtn.setOnClickListener {
                dialog.dismiss()
            }

            okBtn.setOnClickListener {
                if (!isCopied) {
                    Toast.makeText(this, "Please copy the reference number before proceeding!", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener // Prevent further execution
                }

                val orderRequest = OrderRequest(
                    reference_number = transactionId ?: "N/A",
                    username = username,
                    email = email,
                    total_amount = total.toString(),
                    status = "pending",
                    promo_code = "none",
                    order_type = orderType ?: "Unknown",
                    payment_method = "Cashless",
                    payment_status = "Unpaid",
                    est_time = estimatedTime.toString(),
                    reason = "exceed limit",
                    order_items = listOf(
                        OrderItem(
                            item_id = coffeeId.toString(),
                            item_name = coffeeName ?: "Unknown Coffee",
                            quantity = quantity,
                            price = String.format("%.2f", price),
                            size = size ?: "Unknown",
                            special_instructions = comment ?: "N/A",
                            username = username,
                            email = email
                        )
                    )
                )

                RetrofitClient.instance.createOrder(orderRequest).enqueue(object : retrofit2.Callback<OrderResponse> {
                    override fun onResponse(call: Call<OrderResponse>, response: retrofit2.Response<OrderResponse>) {
                        if (response.isSuccessful && response.body() != null) {
                            Log.d("ORDER", "Order created successfully")
                            dialog.dismiss()

                            // Navigate to Main_Home_Page first
                            val homeIntent = Intent(this@Receipts, Main_Home_Page::class.java)
                            startActivity(homeIntent)

                            // Delay before launching payment page to ensure navigation occurs smoothly
                            Handler(Looper.getMainLooper()).postDelayed({
                                Toast.makeText(this@Receipts, "Redirecting to payment...", Toast.LENGTH_SHORT).show()
                                val paymentIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://192.168.1.20/expresso-cafe/api/stripePayment/payment_form_order.php"))
                                startActivity(paymentIntent)
                                finish()
                            }, 1000) // 1-second delay before proceeding to payment

                        } else {
                            Log.d("ORDER", "Response Code: ${response.code()}")
                            Log.d("ORDER", "Response Body: ${response.body()}")
                            Log.e("ORDER", "Failed to create order")
                        }
                    }

                    override fun onFailure(call: Call<OrderResponse>, t: Throwable) {
                        Log.e("ORDER", "Error: ${t.message}")
                    }
                })
            }

            dialog.show()
        }

    private fun generateQRCode(text: String): Bitmap {
        val width = 500 // QR code width
        val height = 500 // QR code height
        val bitMatrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height)
        val barcodeEncoder = BarcodeEncoder()
        return barcodeEncoder.createBitmap(bitMatrix)
    }
}