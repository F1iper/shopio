package com.shopio.view;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

final class LogoLayout extends HorizontalLayout {

    private final Image image;

    LogoLayout(){
        image = new Image("/images/logo.png", "logo");

        setJustifyContentMode(JustifyContentMode.CENTER);
        add(image);
    }
}
