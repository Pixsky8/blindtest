package tools

import java.nio.charset.StandardCharsets
import java.security.MessageDigest

private fun bytesToHex(hash: ByteArray): String {
    val hexString = StringBuilder(2 * hash.size)
    for (i in hash.indices) {
        val hex = Integer.toHexString(0xff and hash[i].toInt())
        if (hex.length == 1) {
            hexString.append('0')
        }
        hexString.append(hex)
    }
    return hexString.toString()
}

fun HashPassword(password: String): String? {
    val digest = MessageDigest.getInstance("SHA3-256")
    val passwd_sha3: ByteArray? = digest.digest(
        password.toByteArray(StandardCharsets.UTF_8)
    )

    if (passwd_sha3 == null)
        return null

    return bytesToHex(passwd_sha3)
}
