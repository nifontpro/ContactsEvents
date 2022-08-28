package ru.nifontbus.templates_domain.model

import android.content.Context
import android.os.Parcelable
import android.provider.ContactsContract
import kotlinx.parcelize.Parcelize
import ru.nifontbus.events_domain.R
import ru.nifontbus.events_domain.model.EventType

@Parcelize
data class Template(
    val id: Long = -1,
    val type: Int = EventType.CUSTOM,
    val label: String = "",
) : Parcelable {

    fun getDescription(context: Context) =
        when (type) {
            EventType.CUSTOM -> {
                label.ifEmpty { context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(type)) }
            }
            EventType.NEW_LIFE_DAY -> context.getString(R.string.sNewLifeDay)
            else -> context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(type))
        }

    fun getDescriptionForSelect(context: Context) =
        when (type) {
            EventType.CUSTOM -> {
                label.ifEmpty { "" }
            }
            EventType.NEW_LIFE_DAY -> context.getString(R.string.sNewLifeDay)
            else -> context.getString(ContactsContract.CommonDataKinds.Event.getTypeResource(type))
        }
}