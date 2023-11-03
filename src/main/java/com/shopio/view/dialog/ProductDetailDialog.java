package com.shopio.view.dialog;

import com.shopio.product.entity.Product;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;

public class ProductDetailDialog extends Dialog {

    public ProductDetailDialog(Product product){
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setWidth("400px");

        TextField nameField = new TextField("Name");
        nameField.setValue(product.getName());
        nameField.setReadOnly(true);

        TextArea descriptionField = new TextArea("Description");
        descriptionField.setValue(product.getDescription());
        descriptionField.setReadOnly(true);

        TextField priceField = new TextField("Price");
        priceField.setValue(String.valueOf(product.getPrice()));
        priceField.setReadOnly(true);

        TextField inventoryField = new TextField("In stock");
        inventoryField.setValue(String.valueOf(product.getAmount()));
        inventoryField.setReadOnly(true);

        Button closeButton = new Button(new Icon(VaadinIcon.CLOSE), e -> close());

        HorizontalLayout exitLayout = new HorizontalLayout();
        exitLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        exitLayout.add(closeButton);

        Button viewReviewsButton = new Button("View Reviews", e -> navigateToReviews(product.getId()));
        viewReviewsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        add(exitLayout, nameField, descriptionField, priceField, inventoryField);

        HorizontalLayout reviewsLayout = new HorizontalLayout();
        reviewsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        reviewsLayout.add(viewReviewsButton);

        add(reviewsLayout);
    }


    private void navigateToReviews(Long path){
        UI.getCurrent().navigate("product/reviews/" + path);
    }
}