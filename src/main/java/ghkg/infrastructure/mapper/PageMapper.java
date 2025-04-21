package ghkg.infrastructure.mapper;

import ghkg.dto.PageResponse;
import org.springframework.data.domain.Page;

public class PageMapper {
    public static <T> PageResponse<T> toResponse(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }
}