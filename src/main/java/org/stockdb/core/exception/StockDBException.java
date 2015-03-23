package org.stockdb.core.exception;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/13
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

public class StockDBException extends Exception{

    public StockDBException() {
    }

    public StockDBException(String message) {
        super(message);
    }

    public StockDBException(String message, Throwable cause) {
        super(message, cause);
    }

    public StockDBException(Throwable cause) {
        super(cause);
    }
}
