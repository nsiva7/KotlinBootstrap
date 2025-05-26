package com.stevdza.san.kotlinbs.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import com.stevdza.san.kotlinbs.models.button.ButtonVariant
import com.stevdza.san.kotlinbs.models.ModalSize
import com.varabyte.kobweb.compose.ui.Modifier
import com.varabyte.kobweb.compose.ui.attrsModifier
import com.varabyte.kobweb.compose.ui.modifiers.classNames
import com.varabyte.kobweb.compose.ui.modifiers.id
import com.varabyte.kobweb.compose.ui.thenIf
import com.varabyte.kobweb.compose.ui.toAttrs
import kotlinx.browser.document
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.H2
import org.jetbrains.compose.web.dom.Text
import org.w3c.dom.HTMLElement

/**
 * Powerful UI element used to display content, messages, or interactive forms in a popup
 * window that temporarily overlays the main content of a webpage. Modals are commonly used
 * to grab the user's attention and prompt them for an action, display additional information,
 * or confirm a choice.
 * This component comes with a [showModalOnClick] util function, that is used to trigger/show
 * this component. And [hideModalOnClick] that is used to dismiss the same component.
 * @param title Modal title.
 * @param body Modal body.
 * @param negativeButtonText Text of the negative button.
 * @param positiveButtonText Text of the positive button.
 * @param closableOutside Whether we can close a Modal when clicking somewhere outside.
 * @param centered Whether to center the content of the Modal.
 * @param size The size of the Modal itself.
 * @param onNegativeButtonClick Lambda which is triggered when a negative button is clicked.
 * @param onPositiveButtonClick Lambda which is triggered when a positive button is clicked.
 * */
@Composable
fun BSModal(
    modifier: Modifier = Modifier,
    id: String,
    title: String? = null,
    body: @Composable () -> Unit,
    negativeButtonText: String? = null,
    positiveButtonText: String? = null,
    closableOutside: Boolean = false,
    centered: Boolean = true,
    size: ModalSize = ModalSize.None,
    onClose: (() -> Unit)? = null,
    onNegativeButtonClick: (() -> Unit)? = null,
    onPositiveButtonClick: (() -> Unit)? = null,
) {
    Div(attrs = modifier
        .id(id)
        .classNames("modal", "fade")
        .thenIf(
            condition = !closableOutside,
            other = Modifier.attrsModifier {
                attr("data-bs-backdrop", "static")
            }
        )
        .toAttrs {
            attr("tabindex", "-1")
        }
    ) {
        Div(
            attrs = Modifier
                .classNames("modal-dialog")
                .thenIf(
                    condition = size != ModalSize.None,
                    other = Modifier.classNames(size.value)
                )
                .thenIf(
                    condition = centered,
                    other = Modifier.classNames("modal-dialog-centered")
                )
                .toAttrs()
        ) {
            Div(
                attrs = Modifier
                    .classNames("modal-content")
                    .toAttrs()
            ) {
                title?.let {
                    Div(
                        attrs = Modifier
                            .classNames("modal-header")
                            .toAttrs()
                    ) {
                        H2(
                            attrs = Modifier
                                .classNames("modal-title")
                                .toAttrs()
                        ) {
                            Text(value = it)
                        }
                        BSCloseButton(modifier = Modifier.attrsModifier {
                            attr("data-bs-dismiss", "modal")
                        })
                    }
                }
                Div(
                    attrs = Modifier
                        .classNames("modal-body")
                        .toAttrs()
                ) {
                    body()
                }

                if (negativeButtonText == null && positiveButtonText == null) return@Div
                Div(
                    attrs = Modifier
                        .classNames("modal-footer")
                        .toAttrs()
                ) {
                    negativeButtonText?.takeIf { it.isEmpty().not() }?.let {
                        BSButton(
                            modifier = Modifier.attrsModifier {
                                attr("data-bs-dismiss", "modal")
                            },
                            text = it,
                            variant = ButtonVariant.Secondary,
                            onClick = { onNegativeButtonClick?.invoke() }
                        )
                    }
                    positiveButtonText?.takeIf { it.isEmpty().not() }?.let {
                        BSButton(
                            modifier = Modifier.attrsModifier {
                                attr("data-bs-dismiss", "modal")
                            },
                            text = it,
                            variant = ButtonVariant.Primary,
                            onClick = { onPositiveButtonClick?.invoke() }
                        )
                    }
                }
            }
        }
    }

    DisposableEffect(id) {
        val modalEl = document.getElementById(id) as? HTMLElement
        val listener: (dynamic) -> Unit = {
            onClose?.invoke()
        }

        modalEl?.addEventListener("hidden.bs.modal", listener)

        onDispose {
            modalEl?.removeEventListener("hidden.bs.modal", listener)
        }
    }
}

/**
 * Util function which is used to trigger/show [BSModal] component.
 * */
fun Modifier.showModalOnClick(id: String): Modifier = attrsModifier {
    attr("data-bs-toggle", "modal")
    attr("data-bs-target", "#$id")
}

/**
 * Util function which is used to hide [BSModal] component.
 * */
fun Modifier.hideModalOnClick(): Modifier = attrsModifier {
    attr("data-bs-dismiss", "modal")
}