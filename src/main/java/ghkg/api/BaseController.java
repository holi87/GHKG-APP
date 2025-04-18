package ghkg.api;

import ghkg.dto.PageResponse;
import ghkg.mapper.PageMapper;
import org.springframework.data.domain.Page;

public abstract class BaseController {

    protected <T> PageResponse<T> toPageResponse(Page<T> page) {
        return PageMapper.toResponse(page);
    }
}