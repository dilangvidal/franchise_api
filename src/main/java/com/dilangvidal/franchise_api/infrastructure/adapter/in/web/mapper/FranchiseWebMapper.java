package com.dilangvidal.franchise_api.infrastructure.adapter.in.web.mapper;

import com.dilangvidal.franchise_api.domain.model.Branch;
import com.dilangvidal.franchise_api.domain.model.Franchise;
import com.dilangvidal.franchise_api.domain.model.Product;
import com.dilangvidal.franchise_api.domain.model.TopStockProduct;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.response.BranchResponse;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.response.FranchiseResponse;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.response.ProductResponse;
import com.dilangvidal.franchise_api.infrastructure.adapter.in.web.dto.response.TopStockProductResponse;
import org.springframework.stereotype.Component;

@Component
public class FranchiseWebMapper {

    public FranchiseResponse toResponse(Franchise franchise) {
        return FranchiseResponse.builder()
                .id(franchise.getId())
                .name(franchise.getName())
                .branches(franchise.getBranches().stream()
                        .map(this::toResponse)
                        .toList())
                .build();
    }

    public BranchResponse toResponse(Branch branch) {
        return BranchResponse.builder()
                .id(branch.getId())
                .name(branch.getName())
                .products(branch.getProducts().stream()
                        .map(this::toResponse)
                        .toList())
                .build();
    }

    public ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .stock(product.getStock())
                .build();
    }

    public TopStockProductResponse toResponse(TopStockProduct topStockProduct) {
        return TopStockProductResponse.builder()
                .branchId(topStockProduct.getBranchId())
                .branchName(topStockProduct.getBranchName())
                .productId(topStockProduct.getProductId())
                .productName(topStockProduct.getProductName())
                .stock(topStockProduct.getStock())
                .build();
    }
}