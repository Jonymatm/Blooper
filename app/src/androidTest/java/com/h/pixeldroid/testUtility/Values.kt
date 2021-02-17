package com.h.pixeldroid.testUtility

import com.h.pixeldroid.BuildConfig.*
import com.h.pixeldroid.utils.db.entities.InstanceDatabaseEntity
import com.h.pixeldroid.utils.db.entities.UserDatabaseEntity

val testiTestoInstance = InstanceDatabaseEntity(
        uri = INSTANCE_URI,
        title = "PixelDroid CI instance",
        maxStatusChars = 150,
        maxPhotoSize = 64000,
        maxVideoSize = 64000,
        albumLimit = 4
)
val testiTesto = UserDatabaseEntity(

        user_id = USER_ID,
        instance_uri = INSTANCE_URI,
        username = "testitesto",
        display_name = "testi testo",
        avatar_static = "$INSTANCE_URI/storage/avatars/default.jpg?v=0",
        isActive = true,
        accessToken = ACCESS_TOKEN,
        refreshToken = REFRESH_TOKEN,
        clientId = CLIENT_ID,
        clientSecret = CLIENT_SECRET
)

