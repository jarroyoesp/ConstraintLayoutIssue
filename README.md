# ConstraintLayout Visibility Issue

Sample project to show the issue when we have a ConstraintLayout with several ComposeViews inside, and we want to change their visibility to show one or another:

ConstraintLayout version: constraintlayout-compose:1.1.0-alpha12

Jetpack Compose version: 1.6.0-alpha04

Jetpack Compose component used: ConstraintLayout

Android Studio Build: Android Studio Giraffe | 2022.3.1 Build #AI-223.8836.35.2231.10406996, built on June 29, 2023 Runtime version: 17.0.6+0-17.0.6b829.9-10027231 aarch64 VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.

Kotlin version: 1.9.10

Material Version: material3:1.2.0-alpha06

This is a regression issue, latest version of `constraintlayout-compose` which worked well: `1.1.0-alpha05`

Steps to Reproduce or Code Sample to Reproduce:

1) Create a `ConstraintLayout` with 2 ComposeViews inside
2) Show one ComposeView or another depending on a flag
3) Change visibility of these views depending on this flag (`visibility = if (!detailVisible) Visibility.Gone else Visibility.Visible`)
4) The ComposeView not visible the first time with the `width = Dimension.fillToConstraints property causes the other ComposeView not to be shown on the screen

## Sample Code

```kotlin
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
```

And this issue just happens in case we use `Visibility.Gone`. If we use `Visibility.Invisible` it works well.

#### Temporary Fix

If we change `width` property just to be `Dimension.fillToConstraints` only when is shown, makes the other ComposeView is shown:

```kotlin
Text(
    text = "Text 1 - Detail",
    modifier = Modifier.constrainAs(detailRef) {
        start.linkTo(parent.start)
        width = if (!detailVisible) Dimension.wrapContent else Dimension.fillToConstraints
        visibility = if (!detailVisible) Visibility.Gone else Visibility.Visible
    },
)
```