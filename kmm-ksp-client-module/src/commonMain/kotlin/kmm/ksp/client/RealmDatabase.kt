package kmm.ksp.client

import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import kmm.ksp.client.entity.BusinessArea

object RealmDatabase {


    val realm: Realm by lazy {
        val configuration = configurationBuilder()
            .deleteRealmIfMigrationNeeded()
            .build()
        Realm.open(configuration)
    }

    private fun configurationBuilder() = RealmConfiguration.Builder(
        schema = setOf(
            BusinessArea::class,
        )
    )

}