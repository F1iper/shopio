package com.shopio.view;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("login")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    public LoginView(){
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        final LoginForm loginForm = new LoginForm();
        loginForm.setAction("login");

        add(new H1("welcome to shopio"), loginForm);
    }
}
