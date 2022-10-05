package kmm.ksp.client.entity

import io.realm.kotlin.types.RealmUUID
import kmm.annotation.MyAnnotation
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey

@MyAnnotation()
class BusinessArea(
    /** Global identifier in local/remote DB */
    @field:PrimaryKey
    var id: RealmUUID = RealmUUID.random(),
    /** Name of business Area */
    var title: String = "",
    var scope: Int = 1,
    /** description of business Area */
    var description: String = "",
) : RealmObject {
    /** Default no-arg constructor for Realm */
    constructor() : this(
        id = RealmUUID.random()
    )
}
