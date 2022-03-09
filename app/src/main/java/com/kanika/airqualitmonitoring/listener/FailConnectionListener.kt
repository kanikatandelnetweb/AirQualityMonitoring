package com.kanika.airqualitmonitoring.listener

/**
 * This is on of the requirement of the project, to show socket failure
 */
interface FailConnectionListener {
    fun onSocketFailure()
}