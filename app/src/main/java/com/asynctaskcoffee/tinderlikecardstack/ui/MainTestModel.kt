package com.asynctaskcoffee.tinderlikecardstack.ui

class MainTestModel(
    var userName: String,
    var userImage: String,
    var userGender: Int,
    var userAgeLastSeen: String
){
    override fun toString(): String {
        return "userName: $userName userImage: $userImage"
    }
}