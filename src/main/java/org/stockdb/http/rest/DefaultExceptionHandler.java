package org.stockdb.http.rest;
/*
 * @author nullwang@hotmail.com
 * created at 2015/4/16
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

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.stockdb.core.exception.DataStoreException;
import org.stockdb.core.exception.StockDBException;
import org.stockdb.http.rest.model.Response;
import org.stockdb.util.Commons;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class DefaultExceptionHandler {

    @ExceptionHandler(StockDBException.class)
    public void handleStockDBException(StockDBException e,HttpServletRequest httpRequest,
                                       HttpServletResponse httpResponse) throws IOException {
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setCharacterEncoding("UTF8");
        Response response = new Response(2);
        response.addErrors(e.getMessage());
        httpResponse.getOutputStream().write(Commons.toJson(response).getBytes("UTF8"));
    }


    @ExceptionHandler(DataStoreException.class)
    public void handleDataStoreException(DataStoreException e,HttpServletRequest httpRequest,
                                         HttpServletResponse httpResponse) throws IOException {
        httpResponse.setContentType("application/json;charset=UTF-8");
        httpResponse.setCharacterEncoding("UTF8");
        Response response = new Response(1);
        response.addErrors(e.getMessage());
        httpResponse.getOutputStream().write(Commons.toJson(response).getBytes("UTF8"));
    }
}
