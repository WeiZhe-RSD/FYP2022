package com.example.fyp

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.CalendarContract
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.fyp.Entity.EatTogehter
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

class User_InvitationDetail : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_invitation_detail)

        var et = intent.getParcelableExtra<EatTogehter>("invite")

        if(et!=null){

            val tvInviteSender = findViewById<TextView>(R.id.tvInviteSender)
            val tvInviteReveiver = findViewById<TextView>(R.id.tvInviteReveiver)
            val tvInviteDate = findViewById<TextView>(R.id.tvInviteDate)
            val tvInviteVenue = findViewById<TextView>(R.id.tvInviteVenue)
            val tvInviteStatus = findViewById<TextView>(R.id.tvInviteStatus)
            val tvInviteDateCreate = findViewById<TextView>(R.id.tvInviteDateCreate)

            tvInviteSender.text = et.customerID.toString()
            tvInviteReveiver.text = et.invitedID.toString()
            tvInviteDate.text = et.dateMeet.toString()
            tvInviteVenue.text = et.venue.toString()
            tvInviteStatus.text = et.status.toString()
            tvInviteDateCreate.text = et.dateCreated.toString()


            val btnGoogle = findViewById<Button>(R.id.btnGoogleCalender)

            btnGoogle.setOnClickListener(){
                addCalendar(et)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addCalendar(et: EatTogehter) {



        val date2 = et.dateMeet.toString()
        val formatter2 = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss", Locale.ENGLISH)
        val localDate2: LocalDateTime = LocalDateTime.parse(date2, formatter2)
/*
        localDate2.minus(Duration.ofHours(8))
*/
        val timeInMilliseconds2: Long = localDate2.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli()



        val intent2 = Intent(Intent.ACTION_INSERT).apply {
            data = CalendarContract.Events.CONTENT_URI
            putExtra(CalendarContract.Events.TITLE, "Eat Together")
            putExtra(CalendarContract.Events.EVENT_LOCATION, et?.venue.toString())
            putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, timeInMilliseconds2)
            putExtra(CalendarContract.EXTRA_EVENT_END_TIME, timeInMilliseconds2)
        }
        if (intent2.resolveActivity(packageManager) != null) {
            startActivity(intent2)
        }
    }
}