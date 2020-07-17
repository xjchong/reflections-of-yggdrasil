package extensions

import org.hexworks.cobalt.datatypes.Maybe

val <T> Maybe<T>.optional: T?
    get() = {
        if (isPresent) {
            get()
        } else {
            null
        }
    }()