/*
 * Copyright 2015-2016 Dark Phoenixs (Open-Source Organization).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.du.pool.tool;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import java.io.Closeable;
import java.io.Serializable;

public abstract class ConnectionPoolBase<T> implements Closeable, Serializable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 536428799879058482L;

    /**
     * internalPool
     */
    protected GenericObjectPool<T> internalPool;

    /**
     * <p>Title: ConnectionPoolBase</p>
     * <p>Description: 构造方法</p>
     */
    public ConnectionPoolBase() {
    }

    /**
     * <p>Title: ConnectionPoolBase</p>
     * <p>Description: 构造方法</p>
     *
     * @param poolConfig 池配置
     * @param factory    池对象工厂
     */
    public ConnectionPoolBase(final GenericObjectPoolConfig poolConfig,
                              PooledObjectFactory<T> factory) {
        this.initPool(poolConfig, factory);
    }

    /**
     * <p>Title: initPool</p>
     * <p>Description: 初始化对象池</p>
     *
     * @param poolConfig 池配置
     * @param factory    池对象工厂
     */
    protected void initPool(final GenericObjectPoolConfig poolConfig,
                            PooledObjectFactory<T> factory) {
        if (this.internalPool != null)
            this.destroy();

        this.internalPool = new GenericObjectPool<T>(factory, poolConfig);
    }

    /**
     * <p>Title: destroy</p>
     * <p>Description: 销毁对象池</p>
     */
    protected void destroy() {
        this.close();
    }

    /**
     * <p>Title: getResource</p>
     * <p>Description: 获得池对象</p>
     *
     * @return 池对象
     */
    protected T getResource() {
        try {
            return internalPool.borrowObject();
        } catch (Exception e) {
            throw new ConnectionException(
                    "Could not get a resource from the pool", e);
        }
    }

    /**
     * <p>Title: returnResource</p>
     * <p>Description: 返回池对象</p>
     *
     * @param resource 池对象
     */
    protected void returnResource(final T resource) {
        if (null != resource)
            try {
                internalPool.returnObject(resource);
            } catch (Exception e) {
                throw new ConnectionException(
                        "Could not return the resource to the pool", e);
            }
    }

    /**
     * <p>Title: invalidateResource</p>
     * <p>Description: 废弃池对象</p>
     *
     * @param resource 池对象
     */
    protected void invalidateResource(final T resource) {
        if (null != resource)
            try {
                internalPool.invalidateObject(resource);
            } catch (Exception e) {
                throw new ConnectionException(
                        "Could not invalidate the resource to the pool", e);
            }
    }

    /**
     * <p>Title: getNumActive</p>
     * <p>Description: 获得池激活数</p>
     *
     * @return 激活数
     */
    public int getNumActive() {
        if (isInactived()) {
            return -1;
        }

        return this.internalPool.getNumActive();
    }

    /**
     * <p>Title: getNumIdle</p>
     * <p>Description: 获得池空闲数</p>
     *
     * @return 空闲数
     */
    public int getNumIdle() {
        if (isInactived()) {
            return -1;
        }

        return this.internalPool.getNumIdle();
    }

    /**
     * <p>Title: getNumWaiters</p>
     * <p>Description: 获得池等待数</p>
     *
     * @return 等待数
     */
    public int getNumWaiters() {
        if (isInactived()) {
            return -1;
        }

        return this.internalPool.getNumWaiters();
    }

    /**
     * <p>Title: getMeanBorrowWaitTimeMillis</p>
     * <p>Description: 获得平均等待时间</p>
     *
     * @return 平均等待时间
     */
    public long getMeanBorrowWaitTimeMillis() {
        if (isInactived()) {
            return -1;
        }

        return this.internalPool.getMeanBorrowWaitTimeMillis();
    }

    /**
     * <p>Title: getMaxBorrowWaitTimeMillis</p>
     * <p>Description: 获得最大等待时间</p>
     *
     * @return 最大等待时间
     */
    public long getMaxBorrowWaitTimeMillis() {
        if (isInactived()) {
            return -1;
        }

        return this.internalPool.getMaxBorrowWaitTimeMillis();
    }

    /**
     * <p>Title: isClosed</p>
     * <p>Description: 池是否关闭</p>
     *
     * @return 是否关闭
     */
    public boolean isClosed() {
        try {
            return this.internalPool.isClosed();
        } catch (Exception e) {
            throw new ConnectionException(
                    "Could not check closed from the pool", e);
        }
    }

    /**
     * <p>Title: isInactived</p>
     * <p>Description: 池是否失效</p>
     *
     * @return 是否失效
     */
    private boolean isInactived() {
        try {
            return this.internalPool == null || this.internalPool.isClosed();
        } catch (Exception e) {
            throw new ConnectionException(
                    "Could not check inactived from the pool", e);
        }
    }

    /**
     * <p>Title: addObjects</p>
     * <p>Description: 添加池对象</p>
     *
     * @param count 池对象数量
     */
    protected void addObjects(final int count) {
        try {
            for (int i = 0; i < count; i++) {
                this.internalPool.addObject();
            }
        } catch (Exception e) {
            throw new ConnectionException("Error trying to add idle objects", e);
        }
    }

    /**
     * <p>Title: clear</p>
     * <p>Description: 清除对象池</p>
     */
    public void clear() {
        try {
            this.internalPool.clear();
        } catch (Exception e) {
            throw new ConnectionException("Could not clear the pool", e);
        }
    }

    /**
     * <p>Title: close</p>
     * <p>Description: 关闭对象池</p>
     */
    public void close() {
        try {
            this.internalPool.close();
        } catch (Exception e) {
            throw new ConnectionException("Could not destroy the pool", e);
        }
    }
}
