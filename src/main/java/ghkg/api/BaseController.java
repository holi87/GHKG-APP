package ghkg.api;

import ghkg.dto.PageResponse;
import ghkg.infrastructure.mapper.PageMapper;
import org.springframework.data.domain.Page;

public abstract class BaseController {

    protected <T> PageResponse<T> toPageResponse(Page<T> page) {
        return PageMapper.toResponse(page);
    }
}