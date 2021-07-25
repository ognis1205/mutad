/*
 * Copyright 2021 Shingo OKAWA
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.ognis1205.mutad.spring.repository.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * @author Shingo OKAWA
 * @version 1.0.0
 */
public class OffsetLimit implements Pageable {
    /** offset within a page. */
    private int offset;

    /** page. */
    private int page;

    /** number of documents within a page. */
    private int size;

    /** Sort. */
    private Sort sort = Sort.unsorted();

    /**
     * Constructor.
     */
    protected OffsetLimit(int offset, int page, int size) {
        if (offset < 0) throw new IllegalArgumentException("Offset must not be less than zero!");
        if (page < 0) throw new IllegalArgumentException("Page index must not be less than zero!");
        if (size < 1) throw new IllegalArgumentException("Page size must not be less than one!");
        this.offset = offset;
        this.page = page;
        this.size = size;
    }

    /**
     * Factory.
     */
    public static OffsetLimit of(int offset, int page, int size) {
        return new OffsetLimit(offset, page, size);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPageNumber() {
        return page;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getPageSize() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getOffset() {
        return offset + page * size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Sort getSort() {
        return sort;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Pageable withPage(int page) {
        return of(offset, page, size);
    }

    /**
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#next()
     */
    public Pageable next() {
        return of(offset, page + 1, size);
    }

    /**
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#previousOrFirst()
     */
    public Pageable previousOrFirst() {
        return hasPrevious() ? of(offset, page - 1, size) : first();
    }

    /**
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#first()
     */
    public Pageable first() {
        return of(offset, 0, size);
    }

    /**
     * (non-Javadoc)
     * @see org.springframework.data.domain.Pageable#hasPrevious()
     */
    public boolean hasPrevious() {
        return page > 0;
    }
}
