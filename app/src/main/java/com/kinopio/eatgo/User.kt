package com.kinopio.eatgo

object User {
    private var userId : Int? = null
    private var userName : String? = null
    private var role : Int? = null
    private var token : String? = null

    fun setUserId(userId:Int) {
        this.userId = userId;
    }

    fun getUserId() : Int?{
        return this.userId
    }

    fun setUserName(userName:String) {
        this.userName = userName
    }

    fun getUserName() : String? {
        return this.userName
    }

    fun setRole(role:Int) {
        this.role = role
    }

    fun getRole() : Int? {
        return this.role
    }

    fun setToken(token:String) {
        this.token = token
    }

    fun getToken() :String? {
        return this.token
    }
}