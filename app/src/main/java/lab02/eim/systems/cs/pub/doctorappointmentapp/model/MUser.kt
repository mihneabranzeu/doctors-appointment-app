package lab02.eim.systems.cs.pub.doctorappointmentapp.model

data class MUser(val id: String?,
                val userId: String,
                val displayName: String,
                val email: String,
                val firstName: String,
                val lastName: String,
                val birthDate: String) {
  fun toMap(): Map<String, Any> {
    val result = mutableMapOf<String, Any>()
    result["user_id"] = userId
    result["display_name"] = displayName
    result["email"] = email
    result["first_name"] = firstName
    result["last_name"] = lastName
    result["birth_date"] = birthDate
    return result
  }
}
