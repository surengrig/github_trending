package org.freeandroidtools.trendinggithub.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.freeandroidtools.trendinggithub.BuildConfig
import java.util.Arrays
import java.util.Date

data class Token(
    var id: Int = 0,
    var url: String? = null,
    var token: String? = null,
    var createdAt: Date? = null,
    var updatedAt: Date? = null,
    var scopes: List<String>? = null
)

data class AuthRequest(
    val scopes: List<String>? = null,
    val note: String? = null,
    val noteUrl: String? = null,
    @SerializedName("client_id") var clientId: String? = null,
    @SerializedName("client_secret") var clientSecret: String? = null
) {
    companion object {
        private const val CLIENT_ID = BuildConfig.CLIENT_ID
        private const val CLIENT_SECRET = BuildConfig.CLIENT_SECRET

        fun generate(): AuthRequest {
            return AuthRequest(
                scopes = Arrays.asList("user", "repo", "gist", "notifications"),
                    note = "",
            clientId = CLIENT_ID,
            clientSecret = CLIENT_SECRET,
            noteUrl = "url"
            )
        }
    }
}


@Entity(
    foreignKeys = [ForeignKey(
        entity = User::class,
        parentColumns = arrayOf("dbId"),
        childColumns = arrayOf("userId"),
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("userId")]
)
data class AuthorizedUser(
    var userId: Long
) {
    constructor() : this(0)

    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}

// Part of Json for /user endpoint
@Entity
data class User(
    var token: String,
    var login: String,
    var name: String?,
    var id: Int,
    var node_id: String,
    var avatar_url: String,
    var gravatar_id: String,
    var url: String,
    var html_url: String,
    var followers_url: String,
    var following_url: String,
    var gists_url: String,
    var starred_url: String,
    var subscriptions_url: String,
    var organizations_url: String,
    var repos_url: String,
    var events_url: String,
    var received_events_url: String,
    var type: String
) {
    constructor() : this("", "", "", -1, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "")

    @PrimaryKey(autoGenerate = true)
    var dbId: Long = 0
}

