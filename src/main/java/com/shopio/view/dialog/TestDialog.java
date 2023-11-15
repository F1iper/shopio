package com.shopio.view.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class TestDialog extends Dialog {

    private boolean isEnabled;
    private final Button state = new Button(new Icon(VaadinIcon.CHECK_CIRCLE_O), event -> {
        changeState();
        showState();
    });

    private final MenuBar menuBar = new MenuBar();
    MenuItem share = createIconItem(menuBar, VaadinIcon.SHARE, "Share");
    SubMenu shareSubMenu = share.getSubMenu();
    MenuItem mainMenu = shareSubMenu.addItem("Main menu");

    SubMenu subMenu = mainMenu.getSubMenu();

    MenuItem menuItem = subMenu.addItem("Option a", event -> Notification.show("Option A chosen", 3000, Notification.Position.TOP_CENTER));
    MenuItem menuItem1 = subMenu.addItem("Option b", event -> Notification.show("Option B chosen", 3000, Notification.Position.TOP_CENTER));

    MenuItem menuItem2 = subMenu.addItem("Option c", event -> Notification.show("Option C chosen", 3000, Notification.Position.TOP_CENTER));


    public TestDialog() {

        setupMenuBar();

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(state, menuBar);
        this.add(horizontalLayout);
    }

    private void setupMenuBar() {
        menuBar.addThemeVariants(MenuBarVariant.LUMO_PRIMARY);


        subMenu.addItem(new Hr());


    }

    private MenuItem createIconItem(MenuBar menu, VaadinIcon iconName, String ariaLabel) {
        Icon icon = new Icon(iconName);
        MenuItem item = menu.addItem(icon);
        item.getElement().setAttribute("aria-label", ariaLabel);

        return item;
    }

    private void changeState() {
        isEnabled ^= true;
        menuItem.setEnabled(!isEnabled);
        menuItem1.setEnabled(isEnabled);
        menuItem2.setEnabled(!isEnabled);
    }

    private void showState() {
        Notification.show("current state of bool: " + isEnabled, 3000, Notification.Position.TOP_CENTER);
    }

}
