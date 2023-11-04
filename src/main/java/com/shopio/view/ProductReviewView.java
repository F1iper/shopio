package com.shopio.view;

import com.shopio.review.entity.ProductReview;
import com.shopio.review.service.ProductReviewService;
import com.shopio.view.layout.MainLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@PageTitle("Product Reviews")
@Route(value = "product/reviews/:productId", layout = MainLayout.class)
@PermitAll
public class ProductReviewView extends HorizontalLayout implements HasUrlParameter<Long> {

    // TODO: 11/4/2023 issue with wrong url still exists:
    //  product/reviews/:productId/:___url_parameter(^[+-]?[0-9]{1,19}$) (requires parameter)
    //  this is the return from the browser
    private final ProductReviewService productReviewService;
    private final Grid<ProductReview> reviewGrid;

    @Autowired
    public ProductReviewView(ProductReviewService productReviewService) {
        this.productReviewService = productReviewService;

        H1 title = new H1("Product Reviews");
        reviewGrid = new Grid<>(ProductReview.class);
        reviewGrid.setSizeFull();
        reviewGrid.setColumns("comment", "rating");

        add(title, reviewGrid);
    }

    @Override
    public void setParameter(BeforeEvent event, Long productId) {
        if (productId != null) {
            reviewGrid.setItems(productReviewService.getReviewsByProductId(productId));
        } else {
            event.rerouteTo("error");
        }
    }
}