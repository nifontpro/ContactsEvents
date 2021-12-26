package ru.nifontbus.contactsevents.presentation.persons

import android.content.ContentResolver
import android.content.ContentValues
import android.provider.ContactsContract


/* Update phone number with raw contact id and phone type.*/
private fun updatePhoneNumber(
    contentResolver: ContentResolver,
    rawContactId: Long,
    phoneType: Int,
    newPhoneNumber: String
) {
    // Create content values object.
    val contentValues = ContentValues()

    // Put new phone number value.
    contentValues.put(ContactsContract.CommonDataKinds.Phone.NUMBER, newPhoneNumber)

    // Create query condition, query with the raw contact id.
    val whereClauseBuf = StringBuffer()

    // Specify the update contact id.
    whereClauseBuf.append(ContactsContract.Data.RAW_CONTACT_ID)
    whereClauseBuf.append("=")
    whereClauseBuf.append(rawContactId)

    // Specify the row data mimetype to phone mimetype( vnd.android.cursor.item/phone_v2 )
    whereClauseBuf.append(" and ")
    whereClauseBuf.append(ContactsContract.Data.MIMETYPE)
    whereClauseBuf.append(" = '")
    val mimetype = ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
    whereClauseBuf.append(mimetype)
    whereClauseBuf.append("'")

    // Specify phone type.
    whereClauseBuf.append(" and ")
    whereClauseBuf.append(ContactsContract.CommonDataKinds.Phone.TYPE)
    whereClauseBuf.append(" = ")
    whereClauseBuf.append(phoneType)

    // Update phone info through Data uri.Otherwise it may throw java.lang.UnsupportedOperationException.
    val dataUri = ContactsContract.Data.CONTENT_URI

    // Get update data count.
    val updateCount =
        contentResolver.update(dataUri, contentValues, whereClauseBuf.toString(), null)
}