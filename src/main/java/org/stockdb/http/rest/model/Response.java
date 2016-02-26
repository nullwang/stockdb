package org.stockdb.http.rest.model;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Response {
    private int status = 0; // 0 - ok;

    private List<String> errorMessages = new ArrayList<String>();

    public Response(){
    }

    public Response(int status){
        this.status = status;
    }

    public void addErrors(String... messages){
        if( messages == null) return;
        errorMessages.addAll(Arrays.asList(messages));
    }
}
