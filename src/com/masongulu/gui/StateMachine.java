package com.masongulu.gui;

import com.masongulu.utils.Property;

public class StateMachine {
    public final Property<Boolean> supportsCharacters = new Property<>(true);
    public final Property<Boolean> supportsCustomPalette = new Property<>(true);
    public final Property<Boolean> usingCharacters = new Property<>(true);
}
