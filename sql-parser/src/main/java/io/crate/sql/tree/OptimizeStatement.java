/*
 * Licensed to Crate under one or more contributor license agreements.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.  Crate licenses this file
 * to you under the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.  You may
 * obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 * implied.  See the License for the specific language governing
 * permissions and limitations under the License.
 *
 * However, if you have executed another commercial license agreement
 * with Crate these terms will supersede the license and you may use the
 * software solely pursuant to the terms of the relevant commercial
 * agreement.
 */

package io.crate.sql.tree;

import com.google.common.base.MoreObjects;
import io.crate.common.collections.Lists2;

import java.util.List;
import java.util.function.Function;

public class OptimizeStatement<T> extends Statement {

    private final List<Table<T>> tables;
    private final GenericProperties<T> properties;

    public OptimizeStatement(List<Table<T>> tables, GenericProperties<T> properties) {
        this.tables = tables;
        this.properties = properties;
    }

    public List<Table<T>> tables() {
        return tables;
    }

    public GenericProperties<T> properties() {
        return properties;
    }

    public <U> OptimizeStatement<U> map(Function<? super T, ? extends U> mapper) {
        return new OptimizeStatement(
            Lists2.map(tables, x -> x.map(mapper)),
            properties.map(mapper)
        );
    }

    @Override
    public int hashCode() {
        int result = tables.hashCode();
        result = 31 * result + properties.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OptimizeStatement that = (OptimizeStatement) o;

        return tables.equals(that.tables) && properties.equals(that.properties);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
            .add("table", tables)
            .add("properties", properties)
            .toString();
    }

    @Override
    public <R, C> R accept(AstVisitor<R, C> visitor, C context) {
        return visitor.visitOptimizeStatement(this, context);
    }
}