package com.example.productapproval.service;

import com.example.productapproval.entity.ProductApply;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
public class CsvExportService {

    public void exportProductApplications(HttpServletResponse response, String filename, List<ProductApply> applications)
            throws IOException {
        String encodedFileName = URLEncoder.encode(filename, StandardCharsets.UTF_8).replace("+", "%20");
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("text/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

        StringBuilder csv = new StringBuilder();
        csv.append("申请ID,商家名称,商品分类,商品名称,商品价格,商品简介,备注,商品图片路径,审批状态,是否告警,告警原因,申请时间,审批时间,审批备注\r\n");
        for (ProductApply item : applications) {
            csv.append(csvValue(item.getId()))
                    .append(',').append(csvValue(item.getMerchantName()))
                    .append(',').append(csvValue(item.getCategoryName()))
                    .append(',').append(csvValue(item.getProductName()))
                    .append(',').append(csvValue(formatPrice(item.getPrice())))
                    .append(',').append(csvValue(item.getIntro()))
                    .append(',').append(csvValue(item.getRemark()))
                    .append(',').append(csvValue(item.getImageUrl()))
                    .append(',').append(csvValue(item.getStatus().name()))
                    .append(',').append(csvValue(Boolean.TRUE.equals(item.getWarning()) ? "是" : "否"))
                    .append(',').append(csvValue(item.getWarningReason()))
                    .append(',').append(csvValue(item.getCreatedAt()))
                    .append(',').append(csvValue(item.getApprovedAt()))
                    .append(',').append(csvValue(item.getApprovalRemark()))
                    .append("\r\n");
        }
        response.getOutputStream().write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});
        response.getOutputStream().write(csv.toString().getBytes(StandardCharsets.UTF_8));
    }

    private String csvValue(Object value) {
        if (value == null) {
            return "";
        }
        String text = value.toString().replace("\"", "\"\"");
        return "\"" + text + "\"";
    }

    private String formatPrice(BigDecimal price) {
        return price == null ? "" : price.toPlainString();
    }
}
