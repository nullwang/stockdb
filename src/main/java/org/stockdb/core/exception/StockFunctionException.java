package org.stockdb.core.exception;
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

public class StockFunctionException extends StockDBException{
    String functionName;

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public StockFunctionException() {
    }

    public StockFunctionException(String message) {
        super(message);
    }

    public StockFunctionException(String formatStr, Object... objects) {
        super(formatStr, objects);
    }

    public StockFunctionException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockFunctionException(Throwable cause) {
        super(cause);
    }
}
