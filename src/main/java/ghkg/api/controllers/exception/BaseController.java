package ghkg.api.controllers.exception;

import ghkg.api.dto.PageResponse;
import ghkg.api.mapper.PageMapper;
import org.springframework.data.domain.Page;

public abstract class BaseController {

    protected <T> PageResponse<T> toPageResponse(Page<T> page) {
        return PageMapper.toResponse(page);
    }
}