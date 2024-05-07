package tech.okcredit.device.remote

import tech.okcredit.device.Device
import tech.okcredit.device.remote.request.ApiDevice

fun Device.toApiModel(): ApiDevice {
    return ApiDevice(
        name = id,
        version_code = versionCode,
        api_level = apiLevel,
        fcm_token = fcmToken,
        android_id = androidId,
        aa_id = aaid,
        make = make ?: "",
        model = model ?: "",
    )
}
