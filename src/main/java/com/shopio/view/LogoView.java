package com.shopio.view;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("images/logo.png")
final class LogoView extends VerticalLayout {


    LogoView(){
        Image image = new Image("/images/logo.png", "logo");

        setJustifyContentMode(JustifyContentMode.CENTER);
        add(image);
    }
}
