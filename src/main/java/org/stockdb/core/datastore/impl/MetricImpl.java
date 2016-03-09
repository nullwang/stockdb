package org.stockdb.core.datastore.impl;
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

import org.apache.commons.lang3.ObjectUtils;
import org.stockdb.core.datastore.Attribute;
import org.stockdb.core.datastore.Metric;

import java.util.*;

public class MetricImpl implements Metric {

    String name;
    Map<String,String> attrs = new HashMap();


    public MetricImpl() {
    }

    public MetricImpl(String name, Map<String, String> attrs) {
        this.name = name;
        this.attrs = attrs;
    }

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
        return new Attribute(attrName,attrs.get(attrName));
    }

    @Override
    public Collection<Attribute> getAttrs() {
        List<Attribute> attributes = new ArrayList<Attribute>();
        for(Map.Entry<String,String> entry: attrs.entrySet()){
            attributes.add(new Attribute(entry.getKey(),entry.getValue()));
        }
        return attributes;
    }

    @Override
    public boolean equals(Object o)
    {
        if( ! (o instanceof MetricImpl) )return false;

        MetricImpl metric = (MetricImpl) o;
        return ObjectUtils.equals(name,metric.name) &&
                ObjectUtils.equals(attrs,metric.attrs);
    }

    @Override
    public int hashCode()
    {
        if(name == null) return 0;
        return name.hashCode();
    }

    @Override
    public void setAttrValue(String attrName, String value) {
        attrs.put(attrName,value);
    }

    @Override
    public String getAttrValue(String attrName) {
        Attribute attribute = getAttr(attrName);
        if( attribute != null)
            return attribute.getValue();
        return null;
    }
}
