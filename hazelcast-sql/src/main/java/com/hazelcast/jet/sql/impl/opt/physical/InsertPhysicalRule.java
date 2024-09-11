/*
 * Copyright 2023 Hazelcast Inc.
 *
 * Licensed under the Hazelcast Community License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://hazelcast.com/hazelcast-community-license
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.jet.sql.impl.opt.physical;

import com.hazelcast.jet.sql.impl.opt.OptUtils;
import com.hazelcast.jet.sql.impl.opt.logical.InsertLogicalRel;
import org.apache.calcite.plan.RelOptRule;
import org.apache.calcite.rel.RelNode;
import org.apache.calcite.rel.convert.ConverterRule;

import static com.hazelcast.jet.sql.impl.opt.Conventions.LOGICAL;
import static com.hazelcast.jet.sql.impl.opt.Conventions.PHYSICAL;

final class InsertPhysicalRule extends ConverterRule {

    static final RelOptRule INSTANCE = new InsertPhysicalRule();

    private InsertPhysicalRule() {
        super(
                InsertLogicalRel.class, LOGICAL, PHYSICAL,
                InsertPhysicalRule.class.getSimpleName()
        );
    }

    @Override
    public RelNode convert(RelNode rel) {
        InsertLogicalRel logicalInsert = (InsertLogicalRel) rel;

        return new InsertPhysicalRel(
                logicalInsert.getCluster(),
                OptUtils.toPhysicalConvention(logicalInsert.getTraitSet()),
                logicalInsert.getTable(),
                logicalInsert.getCatalogReader(),
                OptUtils.toPhysicalInput(logicalInsert.getInput()),
                logicalInsert.isFlattened()
        );
    }
}
