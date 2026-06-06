package com.webtech.saas.services;

import java.util.List;

public interface BasicService<Input, Output>{
    void create(final Input request);

    void update(final String Id, final Input request);

    List<Output> findAll();

    Output findById(final  String Id);

    void delete(final String id);
}
