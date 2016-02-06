package org.stockdb.core.http.rest.model;
/*
 * @author nullwang@hotmail.com
 * created at 2015/3/26
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

import org.stockdb.core.datastore.Attribute;
import org.stockdb.core.datastore.Metric;

import java.util.Map;

public class MetricImpl implements Metric {

    String name;
    Map<String,Attribute> attrs;


    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Attribute getAttr(String attrName) {
        return attrs.get(attrName);
    }

    @Override
    public void setAttrValue(String attrName, String value) {
        Attribute attribute = getAttr(attrName);
        if( attribute == null )
            attribute = new Attribute(attrName,value);
        else
            attribute.setValue(value);
        attrs.put(attrName,attribute);
    }

    @Override
    public String getAttrValue(String attrName) {
        Attribute attribute = getAttr(attrName);
        if( attribute != null)
            return attribute.getValue();
        return null;
    }
}
