package com.shopio.view;

import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    private final LogoLayout logoLayout;

    LoginView(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        logoLayout = new LogoLayout();

        LoginForm loginForm = new LoginForm();
        loginForm.setAction("login");
        loginForm.setVisible(true);


        add(logoLayout, loginForm);
    }
}
