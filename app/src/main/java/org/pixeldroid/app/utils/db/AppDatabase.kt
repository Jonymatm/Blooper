package org.pixeldroid.app.utils.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.pixeldroid.app.utils.db.dao.*
import org.pixeldroid.app.utils.db.dao.feedContent.NotificationDao
import org.pixeldroid.app.utils.db.dao.feedContent.posts.HomePostDao
import org.pixeldroid.app.utils.db.dao.feedContent.posts.PublicPostDao
import org.pixeldroid.app.utils.db.entities.HomeStatusDatabaseEntity
import org.pixeldroid.app.utils.db.entities.InstanceDatabaseEntity
import org.pixeldroid.app.utils.db.entities.PublicFeedStatusDatabaseEntity
import org.pixeldroid.app.utils.db.entities.UserDatabaseEntity
import org.pixeldroid.app.utils.api.objects.Notification

@Database(entities = [
        InstanceDatabaseEntity::class,
        UserDatabaseEntity::class,
        HomeStatusDatabaseEntity::class,
        PublicFeedStatusDatabaseEntity::class,
        Notification::class
    ],
    version = 3
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun instanceDao(): InstanceDao
    abstract fun userDao(): UserDao
    abstract fun homePostDao(): HomePostDao
    abstract fun publicPostDao(): PublicPostDao
    abstract fun notificationDao(): NotificationDao
}