package ru.nifontbus.contactsevents.domain.data

import android.provider.ContactsContract

object EventType {
    // Template type
    const val BIRTHDAY = ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY
    const val ANNIVERSARY = ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY
    const val OTHER = ContactsContract.CommonDataKinds.Event.TYPE_OTHER
    const val CUSTOM = ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM
    const val NEW_LIFE_DAY = 888
}