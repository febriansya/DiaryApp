package com.bulleh.diaryapp.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bulleh.diaryapp.model.Diary
import com.bulleh.diaryapp.model.Mood
import com.bulleh.diaryapp.ui.theme.Elevation
import com.bulleh.diaryapp.util.toInstant
import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun DiaryHolder(
    diary: Diary, onClick: (String) -> Unit
) {

    /*
    * local densitiy to mark height  as current
    * */

    val localDensity = LocalDensity.current
    var componentHeight by remember { mutableStateOf(0.dp) }
    var galleryOpened by remember { mutableStateOf(false) }

    Row(modifier = Modifier.clickable(indication = null, interactionSource = remember {
        MutableInteractionSource()
    }) { onClick(diary._id.toString()) }) {

        Spacer(modifier = Modifier.width(14.dp))
        Surface(
            modifier = Modifier
                .width(2.dp)
                .height(componentHeight + 14.dp),
            tonalElevation = Elevation.Level1
        ) {}
        Spacer(modifier = Modifier.width(20.dp))
        Surface(
            modifier = Modifier
                .clip(shape = Shapes().medium)

                /*
                * make height surface based on local and update the componentHeight
                * */
                .onGloballyPositioned {
                    componentHeight = with(localDensity) {
                        it.size.height.toDp()
                    }
                },

            tonalElevation = Elevation.Level1
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                DiaryHeader(moodName = diary.mood, time = diary.date.toInstant())
                Text(
                    modifier = Modifier.padding(all = 14.dp),
                    text = diary.description,
                    style = TextStyle(fontSize = MaterialTheme.typography.bodyLarge.fontSize),
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                if (diary.images.isNotEmpty()) {
                    ShowGalleryButton(galleryOpened = galleryOpened, onClick = {
                        galleryOpened = !galleryOpened
                    })
                }
                AnimatedVisibility(visible = galleryOpened) {
                    Column(modifier = Modifier.padding(all = 14.dp)) {
                        Gallery(images = diary.images)
                    }
                }
            }
        }
    }
}

@Composable
fun DiaryHeader(moodName: String, time: Instant) {

    /*
    *
    * header body
    * */

    val mood by remember {
        mutableStateOf(Mood.valueOf(moodName))
    }
    val formatter = remember {
        DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault()).withZone(ZoneId.systemDefault())
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Mood.valueOf(moodName).containerColor)
            .padding(horizontal = 14.dp, vertical = 7.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.size(18.dp),
                painter = painterResource(id = Mood.valueOf(moodName).icon),
                contentDescription = "Mood Icon"
            )
            Spacer(modifier = Modifier.width(7.dp))
            Text(
                text = Mood.valueOf(moodName).name,
                color = Mood.valueOf(moodName).contentColor,
                style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
            )
        }

        Text(
            text = formatter.format(time),
            color = mood.contentColor,
            style = TextStyle(fontSize = MaterialTheme.typography.bodyMedium.fontSize)
        )
    }
}


@Composable
fun ShowGalleryButton(
    galleryOpened: Boolean, onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = if (galleryOpened) "Hide Gallery " else "Show Gallery", style = TextStyle(
                fontSize = MaterialTheme.typography.bodySmall.fontSize
            )
        )
    }
}


@Preview
@Composable
fun DiaryHolderPreviewItem() {
    DiaryHolder(diary = Diary().apply {
        title = "Dear Recruiter"
        description = "lorem ipsum bla bla aefaeaef aefaefaefvarbbav arafaeflaekfajef aefpajefohae"
        mood = Mood.Angry.name
        images = realmListOf("", "")
    }, onClick = {})
}