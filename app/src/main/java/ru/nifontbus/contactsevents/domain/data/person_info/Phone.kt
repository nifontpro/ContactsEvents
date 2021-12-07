package ru.nifontbus.contactsevents.domain.data.person_info

import android.content.Context
import android.provider.ContactsContract

data class Phone(
    val number: String,
    val type: Int
) {
    fun stringType(context: Context) =
        context.getString(ContactsContract.CommonDataKinds.Phone.getTypeLabelResource(type))
}
