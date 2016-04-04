package org.stockdb.core.datastore.value;
/*
 * @author nullwang@hotmail.com
 * created at 2015/9/11
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

public class SeparatorProcess implements ValueProcess{

    @Override
    public String filter(String value) throws NumberFormatException {
        if( value == null) return null;
        StringBuilder sb = new StringBuilder();
        //remove all not digit character
        for(int i=0; i<value.length();i++){
            Character c = value.charAt(i);
            if( c >= '0' && c <= '9' || c =='.' ){
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
