package com.vitalsense.app.core.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "VitalSenseNetwork"

/**
 * Connectivity state classifications tailored for rural low-connectivity environments (SIH26133).
 */
enum class ConnectivityState {
    /** High-bandwidth, low-latency connection (Wi-Fi, 4G/5G, validated internet). */
    ONLINE,

    /** Constrained, low-bandwidth, or degraded network (2G/3G, metered, high packet loss). */
    SLOW_NETWORK,

    /** Completely offline / zero internet capability. */
    OFFLINE
}

/**
 * Synchronization status lifecycle for the durable outbox queue.
 */
enum class SyncState {
    IDLE,
    SYNCING,
    ALL_SYNCED,
    PENDING_CHANGES,
    FAILED
}

@Singleton
class NetworkMonitor @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    private val _connectivityState = MutableStateFlow(determineInitialState())
    val connectivityState: StateFlow<ConnectivityState> = _connectivityState.asStateFlow()

    private val _isManualOfflineForced = MutableStateFlow(false)
    val isManualOfflineForced: StateFlow<Boolean> = _isManualOfflineForced.asStateFlow()

    private val _lastSyncTimestamp = MutableStateFlow(System.currentTimeMillis())
    val lastSyncTimestamp: StateFlow<Long> = _lastSyncTimestamp.asStateFlow()

    private val _activeNetworkType = MutableStateFlow("UNKNOWN")
    val activeNetworkType: StateFlow<String> = _activeNetworkType.asStateFlow()

    private var onNetworkRestoredCallback: (() -> Unit)? = null

    init {
        registerNetworkCallback()
    }

    fun setOnNetworkRestoredCallback(callback: () -> Unit) {
        onNetworkRestoredCallback = callback
    }

    fun updateLastSyncTimestamp(timestamp: Long = System.currentTimeMillis()) {
        _lastSyncTimestamp.value = timestamp
    }

    /**
     * Toggles manual offline simulation mode for UI testing and hackathon demo verification.
     */
    fun toggleManualOffline() {
        _isManualOfflineForced.value = !_isManualOfflineForced.value
        recomputeState()
    }

    fun setManualOffline(offline: Boolean) {
        _isManualOfflineForced.value = offline
        recomputeState()
    }

    fun isOnline(): Boolean {
        return !_isManualOfflineForced.value && _connectivityState.value != ConnectivityState.OFFLINE
    }

    fun isSlowNetwork(): Boolean {
        return !_isManualOfflineForced.value && _connectivityState.value == ConnectivityState.SLOW_NETWORK
    }

    private fun registerNetworkCallback() {
        if (connectivityManager == null) {
            _connectivityState.value = ConnectivityState.OFFLINE
            return
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()

        try {
            connectivityManager.registerNetworkCallback(
                request,
                object : ConnectivityManager.NetworkCallback() {
                    override fun onAvailable(network: Network) {
                        Log.d(TAG, "Network available: $network")
                        val wasOffline = _connectivityState.value == ConnectivityState.OFFLINE
                        recomputeState()
                        if (wasOffline && !_isManualOfflineForced.value) {
                            Log.d(TAG, "Internet connectivity restored! Triggering outbox flush.")
                            onNetworkRestoredCallback?.invoke()
                        }
                    }

                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities
                    ) {
                        Log.d(TAG, "Network capabilities changed: $networkCapabilities")
                        val state = classifyCapabilities(networkCapabilities)
                        if (!_isManualOfflineForced.value) {
                            val previous = _connectivityState.value
                            _connectivityState.value = state
                            if (previous == ConnectivityState.OFFLINE && state != ConnectivityState.OFFLINE) {
                                onNetworkRestoredCallback?.invoke()
                            }
                        }
                    }

                    override fun onLost(network: Network) {
                        Log.w(TAG, "Network lost: $network")
                        recomputeState()
                    }

                    override fun onUnavailable() {
                        Log.w(TAG, "Network unavailable")
                        recomputeState()
                    }
                }
            )
        } catch (e: Exception) {
            Log.e(TAG, "Failed to register network callback: ${e.message}", e)
        }
    }

    private fun recomputeState() {
        if (_isManualOfflineForced.value) {
            _connectivityState.value = ConnectivityState.OFFLINE
            return
        }

        val cm = connectivityManager ?: run {
            _connectivityState.value = ConnectivityState.OFFLINE
            return
        }

        val activeNetwork = cm.activeNetwork
        if (activeNetwork == null) {
            _connectivityState.value = ConnectivityState.OFFLINE
            _activeNetworkType.value = "NONE"
            return
        }

        val capabilities = cm.getNetworkCapabilities(activeNetwork)
        if (capabilities == null || !capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)) {
            _connectivityState.value = ConnectivityState.OFFLINE
            _activeNetworkType.value = "NONE"
            return
        }

        _connectivityState.value = classifyCapabilities(capabilities)
    }

    private fun classifyCapabilities(capabilities: NetworkCapabilities): ConnectivityState {
        val hasInternet = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        val isValidated = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)

        if (!hasInternet) {
            _activeNetworkType.value = "DISCONNECTED"
            return ConnectivityState.OFFLINE
        }

        // Detect transport type
        val isWifi = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
        val isCellular = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        val isEthernet = capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)

        _activeNetworkType.value = when {
            isWifi -> "WIFI"
            isCellular -> "CELLULAR"
            isEthernet -> "ETHERNET"
            else -> "OTHER"
        }

        // Detect slow/weak network conditions
        val downKbps = capabilities.linkDownstreamBandwidthKbps

        // If downstream bandwidth is lower than 400 Kbps (typical 2G/EDGE) or unvalidated
        if ((downKbps in 1..400) || (!isValidated && hasInternet)) {
            return ConnectivityState.SLOW_NETWORK
        }

        return ConnectivityState.ONLINE
    }

    private fun determineInitialState(): ConnectivityState {
        val cm = connectivityManager ?: return ConnectivityState.OFFLINE
        val activeNetwork = cm.activeNetwork ?: return ConnectivityState.OFFLINE
        val capabilities = cm.getNetworkCapabilities(activeNetwork) ?: return ConnectivityState.OFFLINE
        return classifyCapabilities(capabilities)
    }
}
