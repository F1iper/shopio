package com.shopio.view;

import com.shopio.product.entity.Category;
import com.shopio.product.entity.Product;
import com.shopio.product.service.ProductService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

@PageTitle("Create product")
@Route("product/create")
@PermitAll
class CreateProductView extends VerticalLayout {

    private final ProductService productService;
    private LogoLayout logoLayout;
    private TextField nameField;
    private TextField descriptionField;
    private TextField priceField;
    private TextField inventoryField;
    private ComboBox<Category> categoryComboBox;
    private Button saveButton;
    private Button cancelButton;

    CreateProductView(ProductService productService){
        this.productService = productService;
        setupMainLayout();

        initializeComponents();

        Component productForm = createProductForm();
        Component buttonLayout = createButtonLayout();
        addSaveAndCancelListeners();

        add(logoLayout, productForm, buttonLayout);
    }

    private void setupMainLayout(){
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }

    private void initializeComponents(){
        this.logoLayout = new LogoLayout();

        nameField = new TextField("Product name");
        descriptionField = new TextField("Description");
        priceField = new TextField("Price");
        inventoryField = new TextField("Amount");

        categoryComboBox = new ComboBox<>("Category");
        categoryComboBox.setItems(Category.values());
        categoryComboBox.setItemLabelGenerator(Category::name);
        categoryComboBox.setValue(Category.ELECTRONICS);
    }

    private void addSaveAndCancelListeners(){
        saveButton.addClickListener(e -> {
            saveProduct();
            getUI().ifPresent(ui -> ui.navigate(""));
        });
        cancelButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });
    }

    private Component createButtonLayout(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        horizontalLayout.setJustifyContentMode(JustifyContentMode.START);
        horizontalLayout.add(saveButton, cancelButton);

        return horizontalLayout;
    }

    private Component createProductForm(){
        FormLayout productForm = new FormLayout();

        productForm.add(nameField, descriptionField, priceField, inventoryField, categoryComboBox);
        return new VerticalLayout(productForm);
    }

    private void saveProduct() {
        // Create a Product instance
        Product product = new Product();

        // Get field values
        String nameValue = nameField.getValue();
        String descriptionValue = descriptionField.getValue();
        String priceValue = priceField.getValue();
        String inventoryValue = inventoryField.getValue();

        // Set values in the Product instance
        product.setName(nameValue);
        product.setDescription(descriptionValue);

        try {
            // Validate and set the price
            double price = Double.parseDouble(priceValue);
            product.setPrice(price);
        } catch (NumberFormatException e) {
            Notification.show("Invalid price format. Please enter a valid number.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        try {
            // Validate and set the inventory
            int inventory = Integer.parseInt(inventoryValue);
            product.setInventory(inventory);
        } catch (NumberFormatException e) {
            Notification.show("Invalid amount format. Please enter a valid integer.", 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        // Set the category
        product.setCategory(categoryComboBox.getValue());

        // Perform validation using Bean Validation
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Product>> violations = validator.validate(product);

        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder("Validation errors:\n");
            for (ConstraintViolation<Product> violation : violations) {
                errorMessage.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
            }
            Notification.show(errorMessage.toString(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        } else {
            // Save the product if validation succeeds
            Product savedProduct = productService.createProduct(product);

            if (savedProduct != null) {
                Notification added = Notification.show("Product saved successfully!", 3000, Notification.Position.TOP_CENTER);
                added.addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_SUCCESS);
            } else {
                Notification.show("Failed to save product!", 3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }
}