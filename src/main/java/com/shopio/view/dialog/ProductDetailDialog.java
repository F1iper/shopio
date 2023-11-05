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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
        VerticalLayout mainLayout = new VerticalLayout();
        setCloseOnEsc(true);
        setCloseOnOutsideClick(true);
        setWidth("400px");

        initializeComponents();
        configureComponents(product);

        Button closeButton = new Button("Close", e -> close());
        Button viewReviewsButton = new Button("View Reviews", e -> navigateToReviews(product.getId()));
        viewReviewsButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        HorizontalLayout reviewsLayout = new HorizontalLayout();
        reviewsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        reviewsLayout.add(viewReviewsButton, closeButton);

        mainLayout.add(nameField, descriptionField, priceField, inventoryField, reviewsLayout);
        add(mainLayout);
    }

    private void configureComponents(Product product) {
        nameField.setValue(product.getName());
        nameField.setReadOnly(true);
        nameField.setWidthFull();
        descriptionField.setValue(product.getDescription());
        descriptionField.setReadOnly(true);
        descriptionField.setWidthFull();
        priceField.setValue(String.valueOf(product.getPrice()));
        priceField.setReadOnly(true);
        priceField.setWidthFull();
        inventoryField.setValue(String.valueOf(product.getAmount()));
        inventoryField.setReadOnly(true);
        inventoryField.setWidthFull();
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