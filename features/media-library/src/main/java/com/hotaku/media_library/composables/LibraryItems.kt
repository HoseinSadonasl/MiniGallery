package com.hotaku.media_library.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hotaku.designsystem.theme.MiniGalleryTheme
import com.hotaku.media_library.utils.LibraryFolderItem
import com.hotaku.media_library.utils.libraryFolderItems
import com.hotaku.ui.asString

@Composable
internal fun HorizontalLibraryFolders(item: @Composable (LibraryFolderItem) -> Unit) {
    Row(
        modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
        horizontalArrangement =
            Arrangement.spacedBy(
                space = 8.dp,
                alignment = Alignment.CenterHorizontally,
            ),
    ) {
        libraryFolderItems.forEach { item(it) }
    }
}

@Composable
internal fun VerticalLibraryFolders(item: @Composable (LibraryFolderItem) -> Unit) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp).fillMaxWidth(),
    ) {
        libraryFolderItems.forEach {
            item(it)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
internal fun LibraryFolderItem(
    modifier: Modifier = Modifier,
    isCompact: Boolean,
    item: LibraryFolderItem,
    onItemClick: () -> Unit,
) {
    if (isCompact) {
        Row(
            modifier =
                modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .background(item.color.copy(alpha = .2f))
                    .clickable { onItemClick::invoke }
                    .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = ImageVector.vectorResource(id = item.icon),
                tint = item.color,
                contentDescription = item.label.asString(),
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = item.label.asString(),
                color = item.color,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    } else {
        Column(
            modifier =
                modifier
                    .widthIn(min = 80.dp, max = 120.dp)
                    .aspectRatio(1f)
                    .clip(MaterialTheme.shapes.large)
                    .background(item.color.copy(alpha = .2f))
                    .clickable { onItemClick::invoke }
                    .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Icon(
                modifier = Modifier.size(48.dp),
                imageVector = ImageVector.vectorResource(id = item.icon),
                tint = item.color,
                contentDescription = item.label.asString(),
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = item.label.asString(),
                color = item.color,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LibraryItemCompactPreview() {
    MiniGalleryTheme {
        VerticalLibraryFolders {
        }
    }
}

@Preview(showBackground = true, device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun LibraryItemPreview() {
    MiniGalleryTheme {
        HorizontalLibraryFolders {
        }
    }
}
