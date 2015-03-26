package org.stockdb.core.datastore;
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

import redis.clients.jedis.ScanResult;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ScanableAdapter<E> implements Collection<E> {

    Scanable scanable;
    ScanResult scanResult;
    String startTime;
    String endTime;
    String id;
    String metricName;
    int index;

    private ScanableAdapter(Scanable scanable,
                            String id, String metricName,
                            String startTime, String endTime) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.scanable = scanable;
        this.scanResult = scanable.scan(id, metricName, "0");
        index = 0;
    }

    private void scanNext() {
        this.scanResult = scanable.scan(id, metricName, scanResult.getStringCursor());
    }

    private E fetchNextValue() {
        //get next value matching the condition
        List list = scanResult.getResult();
        while (true) {
            if (index < list.size()) {
                Map.Entry entry = (Map.Entry) list.get(index);
                String key = String.valueOf(entry.getKey());
                if (key.compareTo(startTime) >= 0 && key.compareTo(endTime) <= 0) {
                    return (E) entry;
                }
                index++;
            } else {
                if( "0".equals(scanResult.getStringCursor())) break;
                scanNext();
                index =0;
            }
        }
        return null;
    }

    public static ScanableAdapter build(Scanable scanable,
                                        String id, String metricName,
                                        String startTime, String endTime) {
        return new ScanableAdapter(scanable, id, metricName, startTime, endTime);
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        return "0".equals(scanResult.getStringCursor());
    }

    @Override
    public boolean contains(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<E> iterator() {
        return new IteratorImpl<E>();
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean add(E e) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    private class IteratorImpl<E> implements Iterator<E> {

        Object t;

        @Override
        public boolean hasNext() {
          t = fetchNextValue();
          return t!=null;
        }

        @Override
        public E next() {
            if( t != null ) return (E) t;
            return (E) (t = fetchNextValue());
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
