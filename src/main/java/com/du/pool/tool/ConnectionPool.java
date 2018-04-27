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

import java.io.Serializable;

public interface ConnectionPool<T> extends Serializable {

    /**
     * <p>Title: getConnection</p>
     * <p>Description: 获取连接</p>
     *
     * @return 连接
     */
    public abstract T getConnection();

    /**
     * <p>Title: returnConnection</p>
     * <p>Description: 返回连接</p>
     *
     * @param conn 连接
     */
    public void returnConnection(T conn);

    /**
     * <p>Title: invalidateConnection</p>
     * <p>Description: 废弃连接</p>
     *
     * @param conn 连接
     */
    public void invalidateConnection(T conn);
}
