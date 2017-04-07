/*
 * Copyright 2017 Kaiserpfalz EDV-Service, Roland T. Lichti
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.kaiserpfalzedv.paladinsinn.commons.api.paging;

import java.util.HashSet;

import de.kaiserpfalzedv.paladinsinn.commons.api.Builder;
import de.kaiserpfalzedv.paladinsinn.commons.api.BuilderValidationException;

/**
 * @author klenkes {@literal <rlichti@kaiserpfalz-edv.de>}
 * @version 1.0.0
 * @since 2017-04-07
 */
public class PageRequestBuilder implements Builder<PageRequest> {
    private static final int FIRST_PAGE = 1;
    public static final int DEFAULT_PAGE_SIZE = 50;

    private long page = FIRST_PAGE;
    private long size = DEFAULT_PAGE_SIZE;

    @Override
    public PageRequest build() throws BuilderValidationException {
        return new PageRequestImpl(page, size);
    }

    @Override
    public void validate() throws BuilderValidationException {
        HashSet<String> failures = new HashSet<>();

        if (page < FIRST_PAGE) {
            failures.add("Can't request a page with a number smaller than " + FIRST_PAGE);
        }

        if (size <= 0) {
            failures.add("Can't have a negative page size (you requested " + size + " elements on every page)!");
        }

        if (failures.size() >= 1) {
            throw new BuilderValidationException(PageRequestImpl.class, failures);
        }
    }

    public PageRequestBuilder withPage(final Page<?> page) {
        withPage(page.getCurrentPageNumber());
        withSize(page.getPageSize());
        return this;
    }

    public PageRequestBuilder nextPage(final Page<?> page) {
        withPage(page.getCurrentPageNumber() + 1);
        withSize(page.getPageSize());
        return this;
    }

    public PageRequestBuilder lastPage(final Page<?> page) {
        withPage(page.getTotalPages());
        withSize(page.getPageSize());
        return this;
    }

    public PageRequestBuilder previousPage(final Page<?> page) {
        withPage(page.getCurrentPageNumber() - 1);
        withSize(page.getPageSize());
        return this;
    }

    public PageRequestBuilder firstPage(final Page<?> page) {
        withPage(1);
        withSize(page.getPageSize());
        return this;
    }


    public PageRequestBuilder withPage(final long page) {
        this.page = page;
        return this;
    }

    public PageRequestBuilder withSize(final long size) {
        this.size = size;
        return this;
    }
}
