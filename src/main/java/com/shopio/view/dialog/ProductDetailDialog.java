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
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductDetailDialog extends Dialog {

    private TextField nameField;
    private TextArea descriptionField;
    private TextField priceField;
    private TextField inventoryField;

    public ProductDetailDialog(Product product){
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setWidth("400px");

        initializeComponents();
        configureComponents(product);

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

    private void configureComponents(Product product) {
        nameField.setValue(product.getName());
        nameField.setReadOnly(true);
        descriptionField.setValue(product.getDescription());
        descriptionField.setReadOnly(true);
        priceField.setValue(String.valueOf(product.getPrice()));
        priceField.setReadOnly(true);
        inventoryField.setValue(String.valueOf(product.getAmount()));
        inventoryField.setReadOnly(true);
    }

    private void initializeComponents() {
        nameField = new TextField("Name");
        descriptionField = new TextArea("Description");
        priceField = new TextField("Price");
        inventoryField = new TextField("In stock");
    }


    private void navigateToReviews(Long productId){
        UI.getCurrent().navigate("product/reviews/" + productId);
        close();
    }
}