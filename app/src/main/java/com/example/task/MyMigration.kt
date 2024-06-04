import io.realm.DynamicRealm
import io.realm.RealmMigration

class MyMigration : RealmMigration {
    override fun migrate(realm: DynamicRealm, oldVersion: Long, newVersion: Long) {
        val schema = realm.schema
        var version = oldVersion
        if (version == 0L) {
            schema.create("LocationData")
                .addField("latitude", Double::class.java)
                .addField("longitude", Double::class.java)
            version++
        }
    }
}
