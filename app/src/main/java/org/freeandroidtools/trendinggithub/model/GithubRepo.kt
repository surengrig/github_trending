package org.freeandroidtools.trendinggithub.model

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.TypeConverter
import com.google.gson.annotations.SerializedName
import java.util.*


data class SearchResult(
        @SerializedName("items") val repos: List<GithubRepo>
)

@Entity(tableName = "repo")
data class GithubRepo(

        @PrimaryKey
        var id: String = "",
        var name: String = "",
        var fullName: String = "",

        var private: Boolean = false,
        var htmlUrl: String =            "", // "https://github.com/hmkcode/Android",
        var description: String? =       "", // "Android related examples",
        var fork: Boolean =              false,
        var url: String =                "", // "https://api.github.com/repos/hmkcode/Android",
        var forksUrl: String =           "", // "https://api.github.com/repos/hmkcode/Android/forks",
        var keysUrl: String =            "", // "https://api.github.com/repos/hmkcode/Android/keys{/keyId}",
        var collaboratorsUrl: String =   "", // "https://api.github.com/repos/hmkcode/Android/collaborators{/collaborator}",
        var teamsUrl: String =           "", // "https://api.github.com/repos/hmkcode/Android/teams",
        var hooksUrl: String =           "", // "https://api.github.com/repos/hmkcode/Android/hooks",
        var issueEventsUrl: String =     "", // "https://api.github.com/repos/hmkcode/Android/issues/events{/number}",
        var eventsUrl: String =          "", // "https://api.github.com/repos/hmkcode/Android/events",
        var assigneesUrl: String =       "", // "https://api.github.com/repos/hmkcode/Android/assignees{/user}",
        var branchesUrl: String =        "", // "https://api.github.com/repos/hmkcode/Android/branches{/branch}",
        var tagsUrl: String =            "", // "https://api.github.com/repos/hmkcode/Android/tags",
        var blobsUrl: String =           "", // "https://api.github.com/repos/hmkcode/Android/git/blobs{/sha}",
        var gitTagsUrl: String =         "", // "https://api.github.com/repos/hmkcode/Android/git/tags{/sha}",
        var gitRefsUrl: String =         "", // "https://api.github.com/repos/hmkcode/Android/git/refs{/sha}",
        var treesUrl: String =           "", // "https://api.github.com/repos/hmkcode/Android/git/trees{/sha}",
        var statusesUrl: String =        "", // "https://api.github.com/repos/hmkcode/Android/statuses/{sha}",
        var languagesUrl: String? =      "", // "https://api.github.com/repos/hmkcode/Android/languages",
        var stargazersUrl: String =      "", // "https://api.github.com/repos/hmkcode/Android/stargazers",
        var contributorsUrl: String =    "", // "https://api.github.com/repos/hmkcode/Android/contributors",
        var subscribersUrl: String =     "", // "https://api.github.com/repos/hmkcode/Android/subscribers",
        var subscriptionUrl: String =    "", // "https://api.github.com/repos/hmkcode/Android/subscription",
        var commitsUrl: String =         "", // "https://api.github.com/repos/hmkcode/Android/commits{/sha}",
        var gitCommitsUrl: String =      "", // "https://api.github.com/repos/hmkcode/Android/git/commits{/sha}",
        var commentsUrl: String =        "", // "https://api.github.com/repos/hmkcode/Android/comments{/number}",
        var issueCommentUrl: String =    "", // "https://api.github.com/repos/hmkcode/Android/issues/comments{/number}",
        var contentsUrl: String =        "", // "https://api.github.com/repos/hmkcode/Android/contents/{+path}",
        var compareUrl: String =         "", // "https://api.github.com/repos/hmkcode/Android/compare/{base}...{head}",
        var mergesUrl: String =          "", // "https://api.github.com/repos/hmkcode/Android/merges",
        var archiveUrl: String =         "", // "https://api.github.com/repos/hmkcode/Android/{archiveFormat}{/ref}",
        var downloadsUrl: String =       "", // "https://api.github.com/repos/hmkcode/Android/downloads",
        var issuesUrl: String =          "", // "https://api.github.com/repos/hmkcode/Android/issues{/number}",
        var pullsUrl: String =           "", // "https://api.github.com/repos/hmkcode/Android/pulls{/number}",
        var milestonesUrl: String =      "", // "https://api.github.com/repos/hmkcode/Android/milestones{/number}",
        var notificationsUrl: String =   "", // "https://api.github.com/repos/hmkcode/Android/notifications{?since,all,participating}",
        var labelsUrl: String =          "", // "https://api.github.com/repos/hmkcode/Android/labels{/name}",
        var releasesUrl: String =        "", // "https://api.github.com/repos/hmkcode/Android/releases{/id}",
        var deploymentsUrl: String =     "", // "https://api.github.com/repos/hmkcode/Android/deployments",
        var createdAt: Date? = null,            //:"2013-09-02T16:12:28Z",
        var updatedAt: Date? = null,            //:"2018-02-23T10:56:14Z",
        var pushedAt: Date? = null,             //:"2017-12-22T20:53:43Z",
        var gitUrl: String =          "",  //  "git://github.com/hmkcode/Android.git",
        var sshUrl: String =          "",  //  "git@github.com:hmkcode/Android.git",
        var cloneUrl: String =        "",  //  "https://github.com/hmkcode/Android.git",
        var svnUrl: String =          "",  //  "https://github.com/hmkcode/Android",
        var homepage: String? =          null,
        var size: Int =                  0,
        var stargazersCount: Int =       0,
        var watchersCount: Int =         0,
        var language: String? =          "Java",
        var hasIssues: Boolean =         true,
        var hasProjects: Boolean =       true,
        var hasDownloads: Boolean =      true,
        var hasWiki: Boolean =           true,
        var hasPages: Boolean =          false,
        var forksCount: Int =            0,
        var mirrorUrl: String? =          "",
        var archived: Boolean =          false,
        var openIssuesCount: Int =       0,
//        @SerializedName("license")           val license:         String,  //:null,
        var forks: Int = 0,
        var openIssues: Int = 0,
        var watchers: Int = 0,
        var defaultBranch: String = "",
        var score: Double = 0.0,
        @Embedded(prefix = "owner_")
        var owner: Owner = Owner()
)

data class Owner(
        var login: String             = "",   //"hmkcode",
        var id: String                = "",   //3790597,
        var avatarUrl: String         = "",   //"https://avatars3.githubusercontent.com/u/3790597?v=4",
        var gravatarId: String        = "",   //"",
        var url: String               = "",   //"https://api.github.com/users/hmkcode",
        var htmlUrl: String           = "",   //"https://github.com/hmkcode",
        var followersUrl: String      = "",   //"https://api.github.com/users/hmkcode/followers",
        var followingUrl: String      = "",   //"https://api.github.com/users/hmkcode/following{/otherUser}",
        var gistsUrl: String          = "",   //"https://api.github.com/users/hmkcode/gists{/gistId}",
        var starredUrl: String        = "",   //"https://api.github.com/users/hmkcode/starred{/owner}{/repo}",
        var subscriptionsUrl: String  = "",   //"https://api.github.com/users/hmkcode/subscriptions",
        var organizationsUrl: String  = "",   //"https://api.github.com/users/hmkcode/orgs",
        var reposUrl: String          = "",   //"https://api.github.com/users/hmkcode/repos",
        var eventsUrl: String         = "",   //"https://api.github.com/users/hmkcode/events{/privacy}",
        var receivedEventsUrl: String = ""   //"https://api.github.com/users/hmkcode/receivedEvents",
//val type :                String                  //"User","siteAdmin":false
)


class DateConverter {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return if (timestamp == null) null else Date(timestamp)
    }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? {
        return date?.time
    }
}


