package com.example.myemergencyapplication

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true, name = "Report Incident Screen")
@Composable
fun PreviewReportScreen() {
    MyEmergencyApplicationTheme {
        ReportScreen(
            report = IncidentReport(),
            onDescriptionChange = {},
            onSpeakMoreClick = {},
            onAttachPhotoClick = {},
            onRemovePhotoClick = {},
            onSeverityChange = {},
            onSeverityConfirmToggle = {},
            onLandmarkChange = {},
            onAdjustPinClick = {},
            onInjuredCountDelta = {},
            onSubmitReport = {},
            onNavigateToSos = {},
            onNavigateToDispatch = {}
        )
    }
}

@Preview(showBackground = true, name = "SOS Hub Screen")
@Composable
fun PreviewSosScreen() {
    MyEmergencyApplicationTheme {
        SosScreen(
            onSosHoldComplete = {},
            onCategorySelected = {}
        )
    }
}

@Preview(showBackground = true, name = "Dispatch Live Tracking Screen")
@Composable
fun PreviewDispatchScreen() {
    MyEmergencyApplicationTheme {
        DispatchScreen(
            report = IncidentReport(),
            onAdvanceStepClicked = {},
            onCallResponderClicked = {},
            onMessageResponderClicked = {},
            onAddNoteClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Quick Login Screen")
@Composable
fun PreviewLoginScreen() {
    MyEmergencyApplicationTheme {
        LoginScreen(
            onLoginDemoClicked = {},
            onLoginSubmitted = { _, _, _, _, _ -> },
            onGuestBypassClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "Community Alerts Screen")
@Composable
fun PreviewAlertsScreen() {
    MyEmergencyApplicationTheme {
        AlertsScreen()
    }
}
