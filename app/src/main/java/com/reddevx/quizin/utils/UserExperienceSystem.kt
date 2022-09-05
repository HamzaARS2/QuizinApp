package com.reddevx.quizin.utils

import com.reddevx.quizin.data.models.User
import kotlin.random.Random

class UserExperienceSystem(
    private val user: User
) {

    var levelUp = false


    fun getUpdatedUserExperience(trophies: Int): Pair<User, Int> {
        val collectedExp = getCollectedExp(trophies)
        increaseExp(collectedExp)
        return Pair(user, collectedExp)
    }

    private fun increaseExp(collectedExp: Int) {
        val updatedExp = user.exp + collectedExp
        if (levelUp(updatedExp)) {
            // Level Up! .. Updating user level
            levelUp = true
            user.apply {
                exp = getRemainingLevelUpExp(updatedExp)
                level += 1
                badge = getUserBadge(user.level)
            }
            return
        }
        // Increasing user experience
        user.exp = updatedExp
    }

    private fun levelUp(updatedExp: Int): Boolean {
        if (updatedExp >= user.level.times(1000)) {
            // User Level Up!
            return true
        }
        return false
    }

    private fun getRemainingLevelUpExp(updatedExp: Int)
            : Int = updatedExp - (user.level.times(1000))

    private fun getCollectedExp(trophies: Int): Int {
        val randomBonusExp = Random.nextInt(0, 10)
        val levelBonusExp = user.level.times(2)
        return trophies + levelBonusExp + randomBonusExp
    }

    private fun getUserBadge(level: Int): String {
        return when {
            level >= 40 -> PROFESSIONAL_BADGE
            level >= 20 -> INTERMEDIATE_BADGE
            else -> BEGINNER_BADGE
        }
    }
}