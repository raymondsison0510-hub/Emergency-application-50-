package com.example.myemergencyapplication

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext


class MainActivity : ComponentActivity() {

    private val viewModel: EmergencyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyEmergencyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: EmergencyViewModel) {
    val context = LocalContext.current

    val user by viewModel.user.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val report by viewModel.currentReport.collectAsState()
    val isGpsActive by viewModel.isGpsActive.collectAsState()
    val isOnline by viewModel.isOnline.collectAsState()

    // Dialog Visibility States
    var showLoginSheet by remember { mutableStateOf(false) }
    var showVoiceDialog by remember { mutableStateOf(false) }
    var showPhotoDialog by remember { mutableStateOf(false) }
    var showMessageDialog by remember { mutableStateOf(false) }
    var showAddNoteDialog by remember { mutableStateOf(false) }

    if (!user.isLoggedIn) {
        LoginScreen(
            onLoginDemoClicked = {
                viewModel.loginDemoUser()
                Toast.makeText(context, "Logged in as Raymond A Sison (Demo Resident)", Toast.LENGTH_SHORT).show()
            },
            onLoginSubmitted = { name, phone, address, eName, ePhone ->
                viewModel.loginUser(name, phone, address, eName, ePhone)
                Toast.makeText(context, "Welcome, ${name.ifBlank { "Resident" }}!", Toast.LENGTH_SHORT).show()
            },
            onGuestBypassClicked = {
                viewModel.loginUser("Emergency Guest", "09165975558", "Brgy. Salitran II", "", "")
                Toast.makeText(context, "Logged in as Guest User", Toast.LENGTH_SHORT).show()
            }
        )
    } else {
        Scaffold(
            topBar = {
                val tabSubtitle = when (selectedTab) {
                    0 -> "SOS Hub • Salitran II"
                    1 -> "Report Incident • Salitran II"
                    2 -> "Live Tracking #${report.id}"
                    3 -> "Barangay Emergency Alerts"
                    else -> "Salitran II, Dasmariñas"
                }

                TopHeader(
                    currentTabTitle = "RespondeDasma",
                    currentTabSubtitle = tabSubtitle,
                    user = user,
                    isGpsActive = isGpsActive,
                    isOnline = isOnline,
                    onBackClicked = if (selectedTab != 0) { { viewModel.selectTab(0) } } else null,
                    onProfileClicked = { showLoginSheet = true },
                    onToggleGps = { viewModel.toggleGps() },
                    onToggleOnline = { viewModel.toggleOnline() }
                )
            },
            bottomBar = {
                BottomNavBar(
                    selectedTab = selectedTab,
                    onTabSelected = { index -> viewModel.selectTab(index) }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (selectedTab) {
                    0 -> SosScreen(
                        onSosHoldComplete = {
                            viewModel.triggerSosHoldComplete()
                        },
                        onCategorySelected = { category ->
                            viewModel.selectCategoryAndNavigateToReport(category)
                        }
                    )

                    1 -> ReportScreen(
                        report = report,
                        onDescriptionChange = { viewModel.updateDescription(it) },
                        onSpeakMoreClick = { showVoiceDialog = true },
                        onAttachPhotoClick = { showPhotoDialog = true },
                        onRemovePhotoClick = { viewModel.removeAttachedPhoto() },
                        onSeverityChange = { viewModel.updateSeverity(it) },
                        onSeverityConfirmToggle = { viewModel.setSeverityConfirmed(it) },
                        onLandmarkChange = { viewModel.updateLandmark(it) },
                        onAdjustPinClick = {
                            Toast.makeText(context, "Pin Adjustment Mode Active: Tap map to move pin", Toast.LENGTH_SHORT).show()
                        },
                        onInjuredCountDelta = { viewModel.updateInjuredCount(it) },
                        onSubmitReport = { viewModel.submitIncidentReport() },
                        onNavigateToSos = { viewModel.selectTab(0) },
                        onNavigateToDispatch = { viewModel.selectTab(2) }
                    )

                    2 -> DispatchScreen(
                        report = report,
                        onAdvanceStepClicked = {
                            viewModel.advanceSimulationStep()
                            Toast.makeText(context, "Dispatch step updated!", Toast.LENGTH_SHORT).show()
                        },
                        onCallResponderClicked = {
                            Toast.makeText(context, "Calling Commander ${report.responderName}...", Toast.LENGTH_SHORT).show()
                        },
                        onMessageResponderClicked = { showMessageDialog = true },
                        onAddNoteClicked = { showAddNoteDialog = true }
                    )

                    3 -> AlertsScreen()
                }
            }
        }

        // Active Dialogs
        if (showLoginSheet) {
            QuickLoginSheet(
                currentUser = user,
                onDismissRequest = { showLoginSheet = false },
                onLoginDemo = {
                    viewModel.loginDemoUser()
                    Toast.makeText(context, "Switched to Demo Resident profile", Toast.LENGTH_SHORT).show()
                },
                onLoginUser = { name, phone, address, eName, ePhone ->
                    viewModel.loginUser(name, phone, address, eName, ePhone)
                    Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
                },
                onLogout = {
                    viewModel.logout()
                    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
                }
            )
        }

        if (showVoiceDialog) {
            VoiceRecorderDialog(
                onDismissRequest = { showVoiceDialog = false },
                onTranscribedTextAdded = { transcribedText ->
                    viewModel.appendTranscribedVoiceText(transcribedText)
                    Toast.makeText(context, "Voice transcription added to report", Toast.LENGTH_SHORT).show()
                }
            )
        }

        if (showPhotoDialog) {
            PhotoAttachmentDialog(
                onDismissRequest = { showPhotoDialog = false },
                onPhotoSelected = { photoName ->
                    viewModel.attachPhoto(photoName)
                    Toast.makeText(context, "Photo attached: $photoName", Toast.LENGTH_SHORT).show()
                }
            )
        }

        if (showMessageDialog) {
            ResponderMessageDialog(
                responderName = report.responderName,
                responderPhone = report.responderPhone,
                chatMessages = report.chatMessages,
                onDismissRequest = { showMessageDialog = false },
                onSendMessage = { msgText ->
                    viewModel.sendChatMessage(msgText)
                }
            )
        }

        if (showAddNoteDialog) {
            AddNoteDialog(
                onDismissRequest = { showAddNoteDialog = false },
                onNoteAdded = { noteText ->
                    viewModel.addNoteForResponders(noteText)
                    Toast.makeText(context, "Note sent to dispatch team", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
