package ru.nifontbus.events_domain.model

import android.provider.ContactsContract

object EventType {

    const val CUSTOM = ContactsContract.CommonDataKinds.Event.TYPE_CUSTOM
    const val ANNIVERSARY = ContactsContract.CommonDataKinds.Event.TYPE_ANNIVERSARY
    const val OTHER = ContactsContract.CommonDataKinds.Event.TYPE_OTHER
    const val BIRTHDAY = ContactsContract.CommonDataKinds.Event.TYPE_BIRTHDAY

    const val NEW_LIFE_DAY = 888
}