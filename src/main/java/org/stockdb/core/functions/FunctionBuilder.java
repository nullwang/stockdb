package org.stockdb.core.functions;
/*
 * @author nullwang@hotmail.com
 * created at 2016/2/14
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

import org.stockdb.core.exception.StockFunctionException;

import java.util.HashMap;
import java.util.Map;

public abstract class FunctionBuilder {

    static Map<String, Function> functionMap = new HashMap();

    public static Function build(String name) throws StockFunctionException {
        Function function = functionMap.get(name);
        if( function == null ) {
            if (DayFirstFunction.NAME.equals(name)) {
                function = new DayFirstFunction();
            } else if (DayLastFunction.NAME.equals(name)) {
                function = new DayLastFunction();
            } else if (DayMaxFunction.NAME.equals(name)) {
                function = new DayMaxFunction();
            } else if (DayMinFunction.NAME.equals(name)) {
                function = new DayMinFunction();
            }
            if ( function != null ) {
                functionMap.put(name,function);
                return function;
            }else {
                throw new StockFunctionException("function {0} not found", name);
            }
        }
        return function;
    }
}
