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

package io.crate.statistics;

import io.crate.types.DataTypes;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class ColumnStatsTest {

    @Test
    public void test_column_stats_generation() {
        List<Integer> numbers = List.of(1, 1, 2, 4, 4, 4, 4, 4);
        ColumnStats columnStats = ColumnStats.fromSortedValues(numbers, DataTypes.INTEGER, 1, 400L);
        assertThat(columnStats.averageSizeInBytes(), is((double) DataTypes.INTEGER.fixedSize()));
        assertThat(columnStats.nullFraction(), is(0.125));
        assertThat(columnStats.approxDistinct(), is(3.0));
    }

    @Test
    public void test_average_size_in_bytes_is_calculated_for_variable_width_columns() {
        ColumnStats stats = ColumnStats.fromSortedValues(List.of("a", "b", "ccc", "dddddd"), DataTypes.STRING, 0, 100L);
        assertThat(stats.averageSizeInBytes(), is(50.0));
    }

    @Test
    public void test_column_stats_generation_from_null_values_only_has_null_fraction_1() {
        ColumnStats columnStats = ColumnStats.fromSortedValues(List.of(), DataTypes.INTEGER, 200, 1000L);
        assertThat(columnStats.nullFraction(), is(1.0));
    }
}