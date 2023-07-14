package com.github.goldy1992.mp3player.client.models

private const val TO_EMAIL_ADDRESS = "goldy131992@gmail.com"
private const val DEFAULT_BUG_REPORT_SUBJECT = "MP3 Player Bug Report - Please Give a Bug Description"
private const val DEFAULT_FEATURE_REQUEST_SUBJECT = "MP3 Player Feature Request - Please summarise your idea"


data class Email(
    val toAddresses : Array<String> = emptyArray(),
    val ccAddresses : Array<String> = emptyArray(),
    val subject : String = "No subject",
    val type: EmailType = EmailType.BUG_REPORT


) {

    companion object {
        private val DEFAULT_TO_ADDRESSES = arrayOf(TO_EMAIL_ADDRESS)
        private val DEFAULT_CC_ADDRESSES = emptyArray<String>()

        val DEFAULT = Email()
        val BUG_REPORT = Email(
            toAddresses = DEFAULT_TO_ADDRESSES,
            ccAddresses = DEFAULT_CC_ADDRESSES,
            subject = DEFAULT_BUG_REPORT_SUBJECT,
            type = EmailType.BUG_REPORT
        )
        val FEATURE_REQUEST = Email(
            toAddresses = DEFAULT_TO_ADDRESSES,
            ccAddresses = DEFAULT_CC_ADDRESSES,
            subject = DEFAULT_FEATURE_REQUEST_SUBJECT,
            type = EmailType.FEATURE_REQUEST
        )
        val FEEDBACK = Email(
            toAddresses = DEFAULT_TO_ADDRESSES,
            ccAddresses = DEFAULT_CC_ADDRESSES,
            subject = DEFAULT_FEATURE_REQUEST_SUBJECT,
            type = EmailType.FEEDBACK
        )

    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Email

        if (!toAddresses.contentEquals(other.toAddresses)) return false
        if (!ccAddresses.contentEquals(other.ccAddresses)) return false
        if (subject != other.subject) return false

        return true
    }

    override fun hashCode(): Int {
        var result = toAddresses.contentHashCode()
        result = 31 * result + ccAddresses.contentHashCode()
        result = 31 * result + subject.hashCode()
        return result
    }
}

enum class EmailType {
    BUG_REPORT,
    FEATURE_REQUEST,
    FEEDBACK
}