package com.example.productapproval.service;

import com.example.productapproval.dto.DashboardStats;
import com.example.productapproval.dto.ProductApplyForm;
import com.example.productapproval.dto.ProductApplyQuery;
import com.example.productapproval.entity.ApprovalStatus;
import com.example.productapproval.entity.Merchant;
import com.example.productapproval.entity.ProductApply;
import com.example.productapproval.entity.ProductCategory;
import com.example.productapproval.repository.MerchantRepository;
import com.example.productapproval.repository.ProductApplyRepository;
import com.example.productapproval.repository.ProductCategoryRepository;
import com.example.productapproval.service.rule.PriceWarningRule;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ProductApplyService {

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "png");

    private final ProductApplyRepository productApplyRepository;
    private final MerchantRepository merchantRepository;
    private final ProductCategoryRepository productCategoryRepository;
    private final PriceWarningRule priceWarningRule;
    private final Path uploadRoot;

    public ProductApplyService(ProductApplyRepository productApplyRepository,
                               MerchantRepository merchantRepository,
                               ProductCategoryRepository productCategoryRepository,
                               PriceWarningRule priceWarningRule,
                               @Value("${app.upload.dir:uploads}") String uploadDir) {
        this.productApplyRepository = productApplyRepository;
        this.merchantRepository = merchantRepository;
        this.productCategoryRepository = productCategoryRepository;
        this.priceWarningRule = priceWarningRule;
        this.uploadRoot = Paths.get(uploadDir).toAbsolutePath().normalize();
    }

    @Transactional(readOnly = true)
    public List<ProductApply> search(ProductApplyQuery query) {
        return productApplyRepository.findAll(toSpecification(query), Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    @Transactional(readOnly = true)
    public List<ProductApply> findRecentForMerchant(Long merchantId) {
        return productApplyRepository.findTop5ByMerchantIdOrderByCreatedAtDesc(merchantId);
    }

    @Transactional(readOnly = true)
    public List<ProductApply> findRecentPending() {
        return productApplyRepository.findTop5ByStatusOrderByCreatedAtDesc(ApprovalStatus.PENDING);
    }

    @Transactional(readOnly = true)
    public List<ProductApply> findRecentWarnings() {
        return productApplyRepository.findTop5ByWarningOrderByCreatedAtDesc(true);
    }

    @Transactional(readOnly = true)
    public DashboardStats getMerchantStats(Long merchantId) {
        return new DashboardStats(
                productApplyRepository.countByMerchantId(merchantId),
                productApplyRepository.countByMerchantIdAndStatus(merchantId, ApprovalStatus.PENDING),
                productApplyRepository.countByMerchantIdAndStatus(merchantId, ApprovalStatus.APPROVED),
                productApplyRepository.countByMerchantIdAndStatus(merchantId, ApprovalStatus.REJECTED),
                productApplyRepository.countByMerchantIdAndWarning(merchantId, true)
        );
    }

    @Transactional(readOnly = true)
    public DashboardStats getAdminStats() {
        return new DashboardStats(
                productApplyRepository.count(),
                productApplyRepository.count(toSpecification(statusQuery(ApprovalStatus.PENDING))),
                productApplyRepository.count(toSpecification(statusQuery(ApprovalStatus.APPROVED))),
                productApplyRepository.count(toSpecification(statusQuery(ApprovalStatus.REJECTED))),
                productApplyRepository.count(toSpecification(warningQuery(true)))
        );
    }

    @Transactional
    public ProductApply create(Long merchantId, ProductApplyForm form) {
        Merchant merchant = merchantRepository.findById(merchantId)
                .filter(item -> Boolean.TRUE.equals(item.getEnabled()))
                .orElseThrow(() -> new IllegalArgumentException("商家不存在或已停用"));
        ProductCategory category = productCategoryRepository.findById(form.getCategoryId())
                .filter(item -> Boolean.TRUE.equals(item.getEnabled()))
                .orElseThrow(() -> new IllegalArgumentException("商品分类不存在或已停用"));

        LocalDateTime now = LocalDateTime.now();
        ProductApply productApply = new ProductApply();
        productApply.setMerchantId(merchant.getId());
        productApply.setMerchantName(merchant.getName());
        productApply.setCategoryId(category.getId());
        productApply.setCategoryName(category.getName());
        productApply.setProductName(form.getProductName());
        productApply.setPrice(form.getPrice());
        productApply.setImageUrl(saveImage(form.getImage()));
        productApply.setIntro(form.getIntro());
        productApply.setRemark(form.getRemark());
        productApply.setStatus(ApprovalStatus.PENDING);
        productApply.setCreatedAt(now);
        productApply.setUpdatedAt(now);
        applyWarning(productApply);

        return productApplyRepository.save(productApply);
    }

    @Transactional
    public void approve(Long id, String approvalRemark) {
        ProductApply productApply = getPendingApply(id);
        productApply.setStatus(ApprovalStatus.APPROVED);
        productApply.setApprovalRemark(hasText(approvalRemark) ? approvalRemark : "审批通过");
        productApply.setApprovedAt(LocalDateTime.now());
        productApply.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void reject(Long id, String approvalRemark) {
        if (!hasText(approvalRemark)) {
            throw new IllegalArgumentException("驳回原因不能为空");
        }
        ProductApply productApply = getPendingApply(id);
        productApply.setStatus(ApprovalStatus.REJECTED);
        productApply.setApprovalRemark(approvalRemark.trim());
        productApply.setApprovedAt(LocalDateTime.now());
        productApply.setUpdatedAt(LocalDateTime.now());
    }

    private Specification<ProductApply> toSpecification(ProductApplyQuery query) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (query == null) {
                return criteriaBuilder.conjunction();
            }
            if (query.getMerchantId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("merchantId"), query.getMerchantId()));
            }
            if (query.getCategoryId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("categoryId"), query.getCategoryId()));
            }
            if (hasText(query.getKeyword())) {
                predicates.add(criteriaBuilder.like(root.get("productName"), "%" + query.getKeyword().trim() + "%"));
            }
            if (query.getStatus() != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), query.getStatus()));
            }
            if (query.getWarning() != null) {
                predicates.add(criteriaBuilder.equal(root.get("warning"), query.getWarning()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    private ProductApplyQuery statusQuery(ApprovalStatus status) {
        ProductApplyQuery query = new ProductApplyQuery();
        query.setStatus(status);
        return query;
    }

    private ProductApplyQuery warningQuery(Boolean warning) {
        ProductApplyQuery query = new ProductApplyQuery();
        query.setWarning(warning);
        return query;
    }

    private ProductApply getPendingApply(Long id) {
        ProductApply productApply = productApplyRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("商品申请不存在"));
        if (productApply.getStatus() != ApprovalStatus.PENDING) {
            throw new IllegalStateException("该申请已经审批，不能重复操作");
        }
        return productApply;
    }

    private String saveImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            return null;
        }

        String originalFilename = StringUtils.cleanPath(
                image.getOriginalFilename() == null ? "" : image.getOriginalFilename());
        String extension = getExtension(originalFilename);
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("商品图片只支持 jpg、jpeg、png 格式");
        }

        try {
            Files.createDirectories(uploadRoot);
            String savedFilename = UUID.randomUUID() + "." + extension;
            Path target = uploadRoot.resolve(savedFilename).normalize();
            Files.copy(image.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            return "/uploads/" + savedFilename;
        } catch (IOException ex) {
            throw new IllegalStateException("商品图片保存失败", ex);
        }
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            return "";
        }
        return filename.substring(dotIndex + 1).toLowerCase();
    }

    private boolean hasText(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private void applyWarning(ProductApply productApply) {
        boolean warning = priceWarningRule.shouldWarn(productApply.getPrice());

        productApply.setWarning(warning);
        productApply.setWarningReason(warning ? priceWarningRule.warningReason() : null);
        productApply.setWarningThreshold(priceWarningRule.getThreshold());
    }
}
