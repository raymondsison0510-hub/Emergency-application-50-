package com.example.myemergencyapplication

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InteractiveMapCanvas(
    modifier: Modifier = Modifier,
    heightDp: Int = 180,
    showResponderUnit: Boolean = false,
    responderEta: String = "~3 mins",
    responderDistance: String = "0.4 km away",
    showAdjustPinButton: Boolean = true,
    onAdjustPinClick: (() -> Unit)? = null,
    onLocationPicked: ((Offset) -> Unit)? = null
) {
    var pinOffset by remember { mutableStateOf(Offset(280f, 180f)) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE2E8F0))
            .border(1.dp, Color(0xFFCBD5E1), RoundedCornerShape(16.dp))
    ) {
        // Compose Map Canvas
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures { offset ->
                        pinOffset = offset
                        onLocationPicked?.invoke(offset)
                    }
                }
        ) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            // Map Background Base
            drawRect(color = Color(0xFFE0E7FF), size = size) // Water/Grass light background

            // Park / Greenery Areas
            drawRoundRect(
                color = Color(0xFFDCFCE7),
                topLeft = Offset(canvasWidth * 0.05f, canvasHeight * 0.1f),
                size = Size(canvasWidth * 0.25f, canvasHeight * 0.35f),
                cornerRadius = CornerRadius(16f, 16f)
            )
            drawRoundRect(
                color = Color(0xFFDCFCE7),
                topLeft = Offset(canvasWidth * 0.65f, canvasHeight * 0.5f),
                size = Size(canvasWidth * 0.3f, canvasHeight * 0.4f),
                cornerRadius = CornerRadius(16f, 16f)
            )

            // Primary Highway: Aguinaldo Highway
            val highwayPath = Path().apply {
                moveTo(0f, canvasHeight * 0.65f)
                cubicTo(
                    canvasWidth * 0.3f, canvasHeight * 0.6f,
                    canvasWidth * 0.6f, canvasHeight * 0.7f,
                    canvasWidth, canvasHeight * 0.4f
                )
            }
            drawPath(
                path = highwayPath,
                color = Color.White,
                style = Stroke(width = 24f)
            )
            drawPath(
                path = highwayPath,
                color = Color(0xFFFDE047), // Yellow center line
                style = Stroke(
                    width = 3f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0f)
                )
            )

            // Secondary Road: Salitran Road
            val salitranRoadPath = Path().apply {
                moveTo(canvasWidth * 0.2f, 0f)
                cubicTo(
                    canvasWidth * 0.3f, canvasHeight * 0.4f,
                    canvasWidth * 0.5f, canvasHeight * 0.55f,
                    canvasWidth * 0.8f, canvasHeight
                )
            }
            drawPath(
                path = salitranRoadPath,
                color = Color.White,
                style = Stroke(width = 16f)
            )

            // Connecting Route Path (if responder is shown)
            if (showResponderUnit) {
                val routePath = Path().apply {
                    moveTo(canvasWidth * 0.2f, canvasHeight * 0.25f)
                    lineTo(canvasWidth * 0.38f, canvasHeight * 0.42f)
                    lineTo(pinOffset.x, pinOffset.y)
                }
                drawPath(
                    path = routePath,
                    color = BlueInfo,
                    style = Stroke(
                        width = 8f,
                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 8f), 0f)
                    )
                )

                // Responder Unit Pin (Unit-02 Patrol)
                val unitX = canvasWidth * 0.2f
                val unitY = canvasHeight * 0.25f
                drawCircle(color = GreenSuccess, radius = 18f, center = Offset(unitX, unitY))
                drawCircle(color = Color.White, radius = 10f, center = Offset(unitX, unitY))
                drawCircle(color = GreenSuccess, radius = 6f, center = Offset(unitX, unitY))
            }

            // Incident Location Pin
            drawCircle(
                color = RedPrimary.copy(alpha = 0.25f),
                radius = 32f,
                center = pinOffset
            )
            drawCircle(
                color = RedPrimary,
                radius = 14f,
                center = pinOffset
            )
            drawCircle(
                color = Color.White,
                radius = 6f,
                center = pinOffset
            )
        }

        // Top Right / Left Map Labels & Overlay Badges
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            // Salitran II Header Badge
            Surface(
                modifier = Modifier.align(Alignment.TopStart),
                shape = RoundedCornerShape(8.dp),
                color = Color.White.copy(alpha = 0.9f),
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RedPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Salitran II, Dasmariñas",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            // Adjust Pin Button if enabled
            if (showAdjustPinButton && onAdjustPinClick != null) {
                Button(
                    onClick = onAdjustPinClick,
                    modifier = Modifier.align(Alignment.TopEnd),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EditLocation,
                        contentDescription = "Adjust Pin",
                        tint = TextPrimary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Adjust Pin",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                }
            }

            // Patrol Unit Speed & Distance Badge overlay
            if (showResponderUnit) {
                Surface(
                    modifier = Modifier.align(Alignment.TopEnd),
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(GreenSuccess)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Unit-02 Patrol • Moving 34 km/h",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                    }
                }

                // Traffic Condition Bottom Banner
                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth(0.95f),
                    shape = RoundedCornerShape(10.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    shadowElevation = 2.dp
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = "Traffic",
                            tint = GreenSuccess,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Traffic condition: Clear along Salitran Road ",
                            fontSize = 11.sp,
                            color = TextPrimary
                        )
                        Text(
                            text = "Fast route",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = GreenSuccess
                        )
                    }
                }
            }
        }
    }
}
