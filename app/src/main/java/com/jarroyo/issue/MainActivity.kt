package com.jarroyo.issue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.constraintlayout.compose.Visibility

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.background
            ) {
                MainContent()
            }
        }
    }
}

@Composable
fun MainContent() {
    var detailVisible by rememberSaveable { mutableStateOf(false) }
    ConstraintLayout(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .clickable { detailVisible = !detailVisible },
    ) {
        val (titleRef, detailRef) = createRefs()
        Text(
            text = "Text 1 - Detail",
            modifier = Modifier.constrainAs(detailRef) {
                start.linkTo(parent.start)
                width = Dimension.fillToConstraints
                // TEMPORAL FIX width = if (!detailVisible) Dimension.wrapContent else Dimension.fillToConstraints
                visibility = if (!detailVisible) Visibility.Gone else Visibility.Visible
            },
        )
        Text(
            text = "Text 2",
            modifier = Modifier
                .constrainAs(titleRef) {
                    width = Dimension.fillToConstraints
                    visibility = if (detailVisible) Visibility.Gone else Visibility.Visible
                },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MainContent()
}