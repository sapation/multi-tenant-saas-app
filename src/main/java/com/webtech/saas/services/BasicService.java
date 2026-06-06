package com.webtech.saas.services;

import com.webtech.saas.common.PageResponse;

import java.util.List;

public interface BasicService<Input, Output>{
    void create(final Input request);

    void update(final String Id, final Input request);

    PageResponse<Output> findAll(final int page, final int size);

    Output findById(final  String Id);

    void delete(final String id);
}
