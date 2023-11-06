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
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.function.Function;

import static com.shopio.notification.Constants.ENTER_VALID_AMOUNT;
import static com.shopio.notification.Constants.ENTER_VALID_PRICE;
import static com.shopio.notification.Constants.PRODUCT_FAILED_TO_SAVE;
import static com.shopio.notification.Constants.PRODUCT_SAVED;

@PageTitle("Create product")
@Route("product/create")
@PermitAll
class CreateProductView extends FormLayout {

    private final ProductService productService;
    private final LogoView logoView = new LogoView();
    private final TextField nameField = new TextField("Product name");
    private final TextField descriptionField = new TextField("Description");
    private final TextField priceField = new TextField("Price");
    private final TextField amountField = new TextField("Amount");
    private ComboBox<Category> categoryComboBox;
    private Button saveButton;
    private Button cancelButton;
    private final Binder<Product> productBinder = new BeanValidationBinder<>(Product.class);

    CreateProductView(ProductService productService){
        this.productService = productService;
        productBinder.bind(nameField, product -> {
        });

        setWidthFull();
        initializeComponents();

        Component productForm = createProductForm();
        Component buttonLayout = createButtonLayout();
        addSaveAndCancelListeners();

        add(logoView, productForm, buttonLayout);
    }


    private void initializeComponents(){
        categoryComboBox = new ComboBox<>("Category");
        categoryComboBox.setItems(Category.values());
        categoryComboBox.setItemLabelGenerator(Category::name);
        categoryComboBox.setValue(Category.ELECTRONICS);
    }

    private void addSaveAndCancelListeners(){
        saveButton.addClickListener(e -> {
            if (validateAndSaveProduct()) {
                getUI().ifPresent(ui -> ui.navigate(""));
            }
        });
        cancelButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });
    }

    private boolean validateAndSaveProduct(){
        String nameValue = nameField.getValue();
        String descriptionValue = descriptionField.getValue();
        String priceValue = priceField.getValue();
        String amountValue = amountField.getValue();

        Product product = new Product();
        product.setName(nameValue);
        product.setDescription(descriptionValue);

        product.setPrice(parseDouble(priceValue, ENTER_VALID_PRICE));
        product.setAmount(parseInt(amountValue, ENTER_VALID_AMOUNT));

        product.setCategory(categoryComboBox.getValue());

        Set<ConstraintViolation<Product>> violations = validateProduct(product);

        if (! violations.isEmpty()) {
            showValidationErrors(violations);
            return false;
        } else {
            Product savedProduct = productService.createProduct(product);

            if (savedProduct != null) {
                showSuccessMessage(PRODUCT_SAVED);
                return true;
            } else {
                showErrorMessage(PRODUCT_FAILED_TO_SAVE);
                return false;
            }
        }
    }

    private Component createButtonLayout(){
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);

        horizontalLayout.add(saveButton, cancelButton);

        return horizontalLayout;
    }

    private Component createProductForm(){
        FormLayout productForm = new FormLayout();

        productForm.add(nameField, descriptionField, priceField, amountField, categoryComboBox);
        return new VerticalLayout(productForm);
    }


    private double parseDouble(String value, String errorMessage){
        return parseNumber(value, errorMessage, Double::parseDouble, 0.0);
    }

    private int parseInt(String value, String errorMessage){
        return parseNumber(value, errorMessage, Integer::parseInt, 0);
    }

    private <T> T parseNumber(String value, String errorMessage, Function<String, T> parser, T defaultValue){
        try {
            return parser.apply(value);
        } catch (NumberFormatException e) {
            showErrorMessage(errorMessage);
            return defaultValue;
        }
    }

    private Set<ConstraintViolation<Product>> validateProduct(Product product){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        return validator.validate(product);
    }

    private void showValidationErrors(Set<ConstraintViolation<Product>> violations){
        StringBuilder errorMessage = new StringBuilder("Validation errors:\n");
        for (ConstraintViolation<Product> violation : violations) {
            errorMessage.append(violation.getPropertyPath()).append(": ").append(violation.getMessage()).append("\n");
        }
        showErrorMessage(errorMessage.toString());
    }

    private void showSuccessMessage(String message){
        Notification added = Notification.show(message, 3000, Notification.Position.TOP_START);
        added.addThemeVariants(NotificationVariant.LUMO_PRIMARY, NotificationVariant.LUMO_SUCCESS);
    }

    private void showErrorMessage(String message){
        Notification.show(message, 4000, Notification.Position.TOP_START)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}