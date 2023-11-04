package com.shopio.view.layout;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;


public class MainLayout extends Composite<Div> implements RouterLayout {

    private VerticalLayout content = new VerticalLayout();

    public MainLayout() {
        getContent().add(content);
        getContent().setSizeFull();

        // Add common components to your layout, like headers, navigation menus, etc.
        // For example, you can add a header component:
        content.add(createHeader());

        // You can also define the layout structure for navigation menus or other common elements here.
    }

    public void showView(Component view) {
        content.removeAll();
        content.add(view);
    }

    private Component createHeader() {
        // Define your header component here
        // For example, a navigation menu, logo, and other header elements
        return new Div(); // Replace with your actual header component
    }
}
