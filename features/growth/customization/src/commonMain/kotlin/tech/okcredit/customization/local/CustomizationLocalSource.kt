package tech.okcredit.customization.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.db.SqlDriver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import okcredit.base.appDispatchers
import tech.okcredit.customization.models.Component
import tech.okcredit.customization.models.TargetComponent

typealias CustomizationSqlDriver = SqlDriver

@Inject
class CustomizationLocalSource(
    sqlDriver: CustomizationSqlDriver,
) {

    private val database by lazy {
        CustomizationDatabase(driver = sqlDriver)
    }

    private val queries by lazy {
        database.customizationEntityV2Queries
    }

    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    suspend fun insertComponents(businessId: String, list: List<TargetComponent>) {
        withContext(appDispatchers.io) {
            database.transaction {
                list.forEach {
                    queries.insert(
                        businessId = businessId,
                        component = json.encodeToString(it.component),
                        target = it.target,
                    )
                }
            }
        }
    }

    fun listComponentsForTarget(target: String): Flow<List<Component>> {
        return queries.listComponentsForTarget(target = target)
            .asFlow()
            .mapToList(appDispatchers.io)
            .map { list ->
                list.map { component ->
                    json.decodeFromString<Component>(component)
                }
            }
    }
}
