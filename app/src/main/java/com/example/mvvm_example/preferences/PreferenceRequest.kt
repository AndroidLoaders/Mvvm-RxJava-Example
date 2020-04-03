package com.example.mvvm_example.preferences

interface PreferenceRequest {

/*{
    "status_code": "success",
    "user_id": 1,
    "user_name": "ABC3 XYZ",
    "email": "abc3@xyz.com",
    "access_token": "demo.token.from.mock.server",
    "message": "Login Success"
}*/

    fun isLogin(): Boolean

    fun getUserId(): String
    fun setUserId(userId: String)

    fun getUserName(): String
    fun setUserName(userName: String)

    fun getEmailId(): String
    fun setEmailId(emailId: String)

    fun getAccessToken(): String
    fun setAccessToken(accessToken: String)
}