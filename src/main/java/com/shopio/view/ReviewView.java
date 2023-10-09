package com.shopio.view;

import com.shopio.review.entity.ProductReview;
import com.shopio.review.service.ProductReviewService;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.List;

@PageTitle("Product Reviews")
@Route("product/reviews/:productId")
@PermitAll
public class ReviewView extends Div implements BeforeEnterObserver {

    private String productId;
    private final ProductReviewService productReviewService;

    public ReviewView(ProductReviewService productReviewService){
        this.productReviewService = productReviewService;

        H1 title = new H1("Product Reviews");
        Grid<ProductReview> reviewGrid = new Grid<>(ProductReview.class);
        reviewGrid.setSizeFull();

        reviewGrid.setColumns("userId", "comment", "rating");

        add(title, reviewGrid);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event){
        productId = event.getRouteParameters().get("productId").orElse(null);

        if (productId == null) {
            event.rerouteTo("error");
        } else {
            List<ProductReview> reviews = productReviewService.getReviewsByProductId(productId);
            updateReviewGrid(reviews);
        }
    }

    private void updateReviewGrid(List<ProductReview> reviews) {
        Grid<ProductReview> reviewGrid = getReviewGrid();
        if (reviewGrid != null) {
            reviewGrid.setItems(reviews);
        }
    }

    private Grid<ProductReview> getReviewGrid() {
        return this.getChildren()
                .filter(c -> c instanceof Grid)
                .map(c -> (Grid<ProductReview>) c)
                .findFirst()
                .orElse(null);
    }
}
