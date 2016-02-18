package org.stockdb.core.exception;
/*
 * @author nullwang@hotmail.com
 * created at 2016/2/18
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

public class TimeFormatException extends StockDBException {

    public TimeFormatException() {
    }

    public TimeFormatException(String message) {
        super(message);
    }

    public TimeFormatException(String formatStr, Object... objects) {
        super(formatStr, objects);
    }

    public TimeFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public TimeFormatException(Throwable cause) {
        super(cause);
    }
}
