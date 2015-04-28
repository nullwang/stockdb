package org.stockdb.core.datastore;
/*
 * @author nullwang@hotmail.com
 * created at 2015/4/28
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

import redis.clients.jedis.*;

import java.io.Closeable;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class JedisWrapper implements JedisCommands,Closeable {

    JedisPool jedisPool ;
    JedisCluster jedisCluster;

    public JedisWrapper(JedisPool jedisPool, JedisCluster jedisCluster){
        this.jedisCluster = jedisCluster;
        this.jedisPool = jedisPool;
        if( jedisCluster == null && jedisPool == null)
            throw new IllegalArgumentException("Neither jedisPool or jedisCluster can not be null");
    }

    @Override
    public void close() throws IOException {
        if(jedisPool!= null) jedisPool.close();
        if( jedisCluster != null) jedisCluster.close();
    }

    @Override
    public String set(String key, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.set(key,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.set(key,value);
        }
    }

    private void closeJedis(Jedis jedis){
        if( jedis != null) jedis.close();
    }

    @Override
    public String set(String key, String value, String nxxx, String expx, long time) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.set(key,value,nxxx,expx,time);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.set(key,value,nxxx,expx,time);
        }
    }

    @Override
    public String get(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.get(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.get(key);
        }

    }

    @Override
    public Boolean exists(String key) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.exists(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.exists(key);
        }
    }

    @Override
    public Long persist(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.persist(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.persist(key);
        }
    }

    @Override
    public String type(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.type(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.type(key);
        }
    }

    @Override
    public Long expire(String key, int seconds) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.expire(key,seconds);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.expire(key,seconds);
        }
    }

    @Override
    public Long expireAt(String key, long unixTime) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.expireAt(key,unixTime);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.expireAt(key,unixTime);
        }
    }

    @Override
    public Long ttl(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.ttl(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.ttl(key);
        }
    }

    @Override
    public Boolean setbit(String key, long offset, boolean value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.setbit(key,offset,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.setbit(key,offset,value);
        }
    }

    @Override
    public Boolean setbit(String key, long offset, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.setbit(key,offset,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.setbit(key,offset,value);
        }
    }

    @Override
    public Boolean getbit(String key, long offset) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.getbit(key,offset);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.getbit(key,offset);
        }
    }

    @Override
    public Long setrange(String key, long offset, String value) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.setrange(key,offset,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.setrange(key,offset,value);
        }
    }

    @Override
    public String getrange(String key, long startOffset, long endOffset) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.getrange(key,startOffset,endOffset);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.getrange(key,startOffset,endOffset);
        }

    }

    @Override
    public String getSet(String key, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.getSet(key,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.getSet(key,value);
        }
    }

    @Override
    public Long setnx(String key, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.setnx(key,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.setnx(key,value);
        }
    }

    @Override
    public String setex(String key, int seconds, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.setex(key,seconds,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.setex(key,seconds,value);
        }
    }

    @Override
    public Long decrBy(String key, long integer) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.decrBy(key,integer);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.decrBy(key,integer);
        }
    }

    @Override
    public Long decr(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.decr(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.decr(key);
        }
    }

    @Override
    public Long incrBy(String key, long integer) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.incrBy(key,integer);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.incrBy(key,integer);
        }
    }

    @Override
    public Long incr(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.incr(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.incr(key);
        }
    }

    @Override
    public Long append(String key, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.append(key,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.append(key,value);
        }
    }

    @Override
    public String substr(String key, int start, int end) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.substr(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.substr(key,start,end);
        }
    }

    @Override
    public Long hset(String key, String field, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hset(key,field,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hset(key,field,value);
        }
    }

    @Override
    public String hget(String key, String field) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hget(key,field);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hget(key,field);
        }
    }

    @Override
    public Long hsetnx(String key, String field, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hsetnx(key,field,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hsetnx(key,field,value);
        }
    }

    @Override
    public String hmset(String key, Map<String, String> hash) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hmset(key,hash);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hmset(key,hash);
        }
    }

    @Override
    public List<String> hmget(String key, String... fields) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hmget(key,fields);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hmget(key,fields);
        }
    }

    @Override
    public Long hincrBy(String key, String field, long value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hincrBy(key,field,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hincrBy(key,field,value);
        }
    }

    @Override
    public Boolean hexists(String key, String field) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hexists(key,field);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hexists(key,field);
        }
    }

    @Override
    public Long hdel(String key, String... field) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hdel(key,field);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hdel(key,field);
        }
    }

    @Override
    public Long hlen(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hlen(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hlen(key);
        }
    }

    @Override
    public Set<String> hkeys(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hkeys(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hkeys(key);
        }
    }

    @Override
    public List<String> hvals(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hvals(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hvals(key);
        }
    }

    @Override
    public Map<String, String> hgetAll(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hgetAll(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.hgetAll(key);
        }
    }

    @Override
    public Long rpush(String key, String... string) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.rpush(key,string);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.rpush(key,string);
        }
    }

    @Override
    public Long lpush(String key, String... string) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.lpush(key,string);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.lpush(key,string);
        }
    }

    @Override
    public Long llen(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.llen(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.llen(key);
        }
    }

    @Override
    public List<String> lrange(String key, long start, long end) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.lrange(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.lrange(key,start,end);
        }
    }

    @Override
    public String ltrim(String key, long start, long end) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.ltrim(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.ltrim(key,start,end);
        }
    }

    @Override
    public String lindex(String key, long index) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.lindex(key,index);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.lindex(key,index);
        }
    }

    @Override
    public String lset(String key, long index, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.lset(key,index,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.lset(key,index,value);
        }
    }

    @Override
    public Long lrem(String key, long count, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.lrem(key,count,value);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.lrem(key,count,value);
        }
    }

    @Override
    public String lpop(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.lpop(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.lpop(key);
        }
    }

    @Override
    public String rpop(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.rpop(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.rpop(key);
        }
    }

    @Override
    public Long sadd(String key, String... member) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.sadd(key,member);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.sadd(key,member);
        }
    }

    @Override
    public Set<String> smembers(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.smembers(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.smembers(key);
        }
    }

    @Override
    public Long srem(String key, String... member) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.srem(key,member);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.srem(key,member);
        }
    }

    @Override
    public String spop(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.spop(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.spop(key);
        }
    }

    @Override
    public Long scard(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.scard(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.scard(key);
        }
    }

    @Override
    public Boolean sismember(String key, String member) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.sismember(key,member);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.sismember(key,member);
        }
    }

    @Override
    public String srandmember(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.srandmember(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.srandmember(key);
        }
    }

    @Override
    public List<String> srandmember(String key, int count) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.srandmember(key,count);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.srandmember(key,count);
        }
    }

    @Override
    public Long strlen(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.strlen(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.strlen(key);
        }
    }

    @Override
    public Long zadd(String key, double score, String member) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zadd(key,score,member);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zadd(key,score,member);
        }
    }

    @Override
    public Long zadd(String key, Map<String, Double> scoreMembers) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zadd(key,scoreMembers);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zadd(key,scoreMembers);
        }
    }

    @Override
    public Set<String> zrange(String key, long start, long end) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrange(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrange(key,start,end);
        }
    }

    @Override
    public Long zrem(String key, String... member) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrem(key,member);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrem(key,member);
        }
    }

    @Override
    public Double zincrby(String key, double score, String member) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zincrby(key,score,member);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zincrby(key,score,member);
        }
    }

    @Override
    public Long zrank(String key, String member) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrank(key,member);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrank(key,member);
        }
    }

    @Override
    public Long zrevrank(String key, String member) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrevrank(key,member);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrevrank(key,member);
        }
    }

    @Override
    public Set<String> zrevrange(String key, long start, long end) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrevrange(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrevrange(key,start,end);
        }
    }

    @Override
    public Set<Tuple> zrangeWithScores(String key, long start, long end) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeWithScores(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrangeWithScores(key,start,end);
        }
    }

    @Override
    public Set<Tuple> zrevrangeWithScores(String key, long start, long end) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrevrangeWithScores(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrevrangeWithScores(key,start,end);
        }
    }

    @Override
    public Long zcard(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zcard(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zcard(key);
        }
    }

    @Override
    public Double zscore(String key, String member) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zscore(key,member);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zscore(key,member);
        }
    }

    @Override
    public List<String> sort(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.sort(key);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.sort(key);
        }
    }

    @Override
    public List<String> sort(String key, SortingParams sortingParameters) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.sort(key,sortingParameters);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.sort(key,sortingParameters);
        }
    }

    @Override
    public Long zcount(String key, double min, double max) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zcount(key,min,max);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zcount(key,min,max);
        }
    }

    @Override
    public Long zcount(String key, String min, String max) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zcount(key,min,max);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zcount(key,min,max);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeByScore(key,min,max);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrangeByScore(key,min,max);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeByScore(key,min,max);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrangeByScore(key,min,max);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeByScore(key,min,max);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrangeByScore(key,min,max);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, double min, double max, int offset, int count) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeByScore(key,min,max,offset,count);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrangeByScore(key,min,max,offset,count);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrevrangeByScore(key,max,min);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrevrangeByScore(key,max,min);
        }
    }

    @Override
    public Set<String> zrangeByScore(String key, String min, String max, int offset, int count) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeByScore(key,min,max,offset,count);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrangeByScore(key,min,max,offset,count);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, double max, double min, int offset, int count) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrevrangeByScore(key,max,min,offset,count);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrevrangeByScore(key,max,min,offset,count);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
               return jedis.zrangeByScoreWithScores(key,min,max);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrangeByScoreWithScores(key,min,max);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrevrangeByScoreWithScores(key,max,min);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrevrangeByScoreWithScores(key,max,min);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, double min, double max, int offset, int count) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeByScoreWithScores(key,min,max,offset,count);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrangeByScoreWithScores(key,min,max,offset,count);
        }
    }

    @Override
    public Set<String> zrevrangeByScore(String key, String max, String min, int offset, int count) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrevrangeByScore(key,max,min,offset,count);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrevrangeByScore(key,max,min,offset,count);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max) {

        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeByScoreWithScores(key,min,max);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrangeByScoreWithScores(key,min,max);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrevrangeByScoreWithScores(key,max,min);
            }finally {
                closeJedis(jedis);
            }
        }else{
            return jedisCluster.zrevrangeByScoreWithScores(key,max,min);
        }
    }

    @Override
    public Set<Tuple> zrangeByScoreWithScores(String key, String min, String max, int offset, int count) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeByScoreWithScores(key,min,max,offset,count);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zrangeByScoreWithScores(key, min, max, offset, count);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, double max, double min, int offset, int count) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrevrangeByScoreWithScores(key,max,min,offset,count);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zrevrangeByScoreWithScores(key,max,min,offset,count);
        }
    }

    @Override
    public Set<Tuple> zrevrangeByScoreWithScores(String key, String max, String min, int offset, int count) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrevrangeByScoreWithScores(key,max,min,offset,count);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zrevrangeByScoreWithScores(key,max,min,offset,count);
        }
    }

    @Override
    public Long zremrangeByRank(String key, long start, long end) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zremrangeByRank(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zremrangeByRank(key,start,end);
        }
    }

    @Override
    public Long zremrangeByScore(String key, double start, double end) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zremrangeByScore(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zremrangeByScore(key,start,end);
        }
    }

    @Override
    public Long zremrangeByScore(String key, String start, String end) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zremrangeByScore(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zremrangeByScore(key,start,end);
        }
    }

    @Override
    public Long zlexcount(String key, String min, String max) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zlexcount(key,min,max);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zlexcount(key,min,max);
        }
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeByLex(key,min,max);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zrangeByLex(key,min,max);
        }
    }

    @Override
    public Set<String> zrangeByLex(String key, String min, String max, int offset, int count) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zrangeByLex(key,min,max,offset,count);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zrangeByLex(key,min,max,offset,count);
        }
    }

    @Override
    public Long zremrangeByLex(String key, String min, String max) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zremrangeByLex(key,min,max);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zremrangeByLex(key,min,max);
        }
    }

    @Override
    public Long linsert(String key, BinaryClient.LIST_POSITION where, String pivot, String value) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.linsert(key,where,pivot,value);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.linsert(key,where,pivot,value);
        }
    }

    @Override
    public Long lpushx(String key, String... string) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.lpushx(key,string);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.lpushx(key,string);
        }
    }

    @Override
    public Long rpushx(String key, String... string) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.rpushx(key,string);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.rpushx(key,string);
        }
    }

    @Override
    public List<String> blpop(String arg) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.blpop(arg);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.blpop(arg);
        }
    }

    @Override
    public List<String> blpop(int timeout, String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.blpop(timeout,key);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.blpop(timeout,key);
        }
    }

    @Override
    public List<String> brpop(String arg) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.brpop(arg);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.brpop(arg);
        }
    }

    @Override
    public List<String> brpop(int timeout, String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.brpop(timeout,key);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.brpop(timeout,key);
        }
    }

    @Override
    public Long del(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.del(key);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.del(key);
        }
    }

    @Override
    public String echo(String string) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.echo(string);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.echo(string);
        }
    }

    @Override
    public Long move(String key, int dbIndex) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.move(key,dbIndex);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.move(key,dbIndex);
        }
    }

    @Override
    public Long bitcount(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.bitcount(key);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.bitcount(key);
        }
    }

    @Override
    public Long bitcount(String key, long start, long end) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.bitcount(key,start,end);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.bitcount(key,start,end);
        }
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, int cursor) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hscan(key,cursor);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.hscan(key,cursor);
        }
    }

    @Override
    public ScanResult<String> sscan(String key, int cursor) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.sscan(key,cursor);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.sscan(key,cursor);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(String key, int cursor) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zscan(key,cursor);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zscan(key,cursor);
        }
    }

    @Override
    public ScanResult<Map.Entry<String, String>> hscan(String key, String cursor) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.hscan(key,cursor);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.hscan(key,cursor);
        }
    }

    @Override
    public ScanResult<String> sscan(String key, String cursor) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.sscan(key,cursor);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.sscan(key,cursor);
        }
    }

    @Override
    public ScanResult<Tuple> zscan(String key, String cursor) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.zscan(key,cursor);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.zscan(key,cursor);
        }
    }

    @Override
    public Long pfadd(String key, String... elements) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.pfadd(key,elements);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.pfadd(key,elements);
        }
    }

    @Override
    public long pfcount(String key) {
        if( jedisPool != null ){
            Jedis jedis = null;
            try{
                jedis = jedisPool.getResource();
                return jedis.pfcount(key);
            }finally {
                closeJedis(jedis);
            }
        }else {
            return jedisCluster.pfcount(key);
        }
    }
}
