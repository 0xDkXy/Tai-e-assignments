/*
 * Tai-e: A Static Analysis Framework for Java
 *
 * Copyright (C) 2022 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2022 Yue Li <yueli@nju.edu.cn>
 *
 * This file is part of Tai-e.
 *
 * Tai-e is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation, either version 3
 * of the License, or (at your option) any later version.
 *
 * Tai-e is distributed in the hope that it will be useful,but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General
 * Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with Tai-e. If not, see <https://www.gnu.org/licenses/>.
 */

package pascal.taie.analysis.dataflow.analysis;

import pascal.taie.analysis.dataflow.fact.SetFact;
import pascal.taie.analysis.graph.cfg.CFG;
import pascal.taie.config.AnalysisConfig;
import pascal.taie.ir.exp.LValue;
import pascal.taie.ir.exp.RValue;
import pascal.taie.ir.exp.Var;
import pascal.taie.ir.stmt.Stmt;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of classic live variable analysis.
 */
public class LiveVariableAnalysis extends
        AbstractDataflowAnalysis<Stmt, SetFact<Var>> {

    public static final String ID = "livevar";

    public LiveVariableAnalysis(AnalysisConfig config) {
        super(config);
    }

    @Override
    public boolean isForward() {
        return false;
    }

    @Override
    public SetFact<Var> newBoundaryFact(CFG<Stmt> cfg) {
        // TODO - finish me
        SetFact<Var> in_exit = new SetFact<Var>();

        return in_exit;
    }

    @Override
    public SetFact<Var> newInitialFact() {
        // TODO - finish me
        SetFact<Var> in = new SetFact<Var>();
        return in;
    }

    @Override
    public void meetInto(SetFact<Var> fact, SetFact<Var> target) {
        // TODO - finish me
        target.union(fact);
    }

    @Override
    public boolean transferNode(Stmt stmt, SetFact<Var> in, SetFact<Var> out) {
        // TODO - finish me
        Optional<LValue> _def = stmt.getDef();
        SetFact<Var> temp_out = new SetFact<Var>();
        temp_out.set(out);
        try {
            LValue temp =  _def.get();
            Var def = (Var)temp;
            temp_out.remove(def);
        } catch (Exception e) {}
        List<RValue> _use_list = stmt.getUses();
        SetFact<Var> useVar = new SetFact<>();
        for (int i = 0; i < _use_list.size(); ++i) {
            try {
                Var item = (Var) _use_list.get(i);
                if (!useVar.contains(item)) {
                    useVar.add(item);
                }
            } catch (Exception e) {
                continue;
            }
        }
        try {
            useVar.union(temp_out);
        } catch (Exception e) {}
        if (in.equals(useVar)) {
            return false;
        } else {
            in.set(useVar);
            return true;
        }
    }
}
